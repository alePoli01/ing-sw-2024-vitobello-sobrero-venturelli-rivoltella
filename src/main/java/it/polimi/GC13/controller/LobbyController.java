package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.LostConnectionToClientInterface;
import it.polimi.GC13.network.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnPlayerNotReconnectedException;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnGameNameAlreadyTakenMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnNickNameAlreadyTakenMessage;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyController implements LostConnectionToClientInterface , Serializable {
    private final Map<String, Game> joinableGameMap = new ConcurrentHashMap<>();
    private final Map<String, Game> startedGameMap = new ConcurrentHashMap<>();
    private transient final Map<Game, Controller> gameControllerMap = new ConcurrentHashMap<>(); //contains all ongoing games controller
    private ControllerDispatcher controllerDispatcher;
    private final List<ClientInterface> disconnectedClients = new ArrayList<>();//list of all the disconnected clients

    public void setControllerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    public Map<String, Game> getJoinableGameMap() {
        return this.joinableGameMap;
    }

    public Map<String, Game> getStartedGameMap() {
        return this.startedGameMap;
    }

    public void checkForExistingGame(ClientInterface client) throws RemoteException {
        // upon check just provide the answer
        if (disconnectedClients.contains(client)) {
            System.out.println("Disconnected client recognised");
        }
        //upon check just provide the answer
        System.out.println("--Received: checkForExistingGame");
        Map<String, Integer> gameNameWaitingPlayersMap = new ConcurrentHashMap<>();
        this.joinableGameMap.forEach((gameName, game) -> gameNameWaitingPlayersMap.put(gameName, game.getCurrNumPlayer()));
        client.sendMessageFromServer(new OnCheckForExistingGameMessage(gameNameWaitingPlayersMap));
    }

    /*
        METHOD TO ADD EACH PLAYER TO THE GAME
     */
    public synchronized void addPlayerToGame(ClientInterface client, String playerNickname, String gameName) throws RemoteException {
        System.out.println("--Received: PlayerJoiningMessage: [player:" + playerNickname+"]");
        try {
            // creates the player
            Player player = new Player(playerNickname);
            Game workingGame = joinableGameMap.get(gameName);
            // updates controller playerClientMap
            this.gameControllerMap.get(workingGame).getPlayerClientMap().put(player, client);
            // updates controller clientPlayerMap
            this.gameControllerMap.get(workingGame).getClientPlayerMap().put(client, player);
            // set the game for the player
            player.setGame(workingGame);
            // adds the player to the model
            this.gameControllerMap.get(workingGame).addPlayerToExistingGame(player, workingGame, client);
            // updates controller dispatcher client <-> player Map
            this.controllerDispatcher.getClientPlayerMap().put(client, player);
            // updates Controller Dispatcher's ClientGameMap adding <client, gamePhase>
            this.controllerDispatcher.getClientControllerMap().put(client, this.gameControllerMap.get(workingGame));

        } catch (GenericException e) {
            client.sendMessageFromServer(new OnNickNameAlreadyTakenMessage(playerNickname));
            System.err.println(e.getMessage());
        }
    }

    /*
        METHOD TO CREATE A NEW GAME
     */
    public synchronized void createNewGame(ClientInterface client, String playerNickname, int playersNumber, String gameName) throws RemoteException {
        Game workingGame;
        if (this.startedGameMap.containsKey(gameName) | this.joinableGameMap.containsKey(gameName)) {
            client.sendMessageFromServer(new OnGameNameAlreadyTakenMessage(playerNickname, gameName));
        } else {
            workingGame = new Game(playersNumber, gameName);
            // updates useful maps
            this.gameControllerMap.put(workingGame, new Controller(workingGame, this, this.controllerDispatcher));
            this.joinableGameMap.put(gameName, workingGame);
            this.addPlayerToGame(client, playerNickname, gameName);
        }
    }
    public synchronized void reconnectPlayerToGame(ClientInterface client, String gameName,String playerName)throws RemoteException{
        System.out.println("--Received: reconnectPlayerToGame");
        if(startedGameMap.containsKey(gameName) && startedGameMap.get(gameName).getPlayerList().stream().anyMatch(player -> player.getNickname().equals(playerName))){
            System.out.println("Game and Player name was found, reconnecting client");
            //TODO: RECONNECT CLIENT
        }else if(!(startedGameMap.containsKey(gameName))){
            System.out.println("gameName incorrect");
            client.sendMessageFromServer(new OnPlayerNotReconnectedException(playerName,0));
        }else{
            System.out.println("playerName incorrect");
            client.sendMessageFromServer(new OnPlayerNotReconnectedException(playerName,0));
        }

    }
    @Override
    public void connectionLost(ClientInterface client) {
        //TODO: ignorare questo metodo
        //when connection is lost put client in waitingToReconnectList
        /*
        SERVER SIDE
            1. make the usernames unique server-wise and use them to reconnect players
                1.1 save both username,gameName(will be used like a password)
                    and the client(socket/rmi, will be deleted upon reconnection)
            2. when a player tries to reconnect the list won't be empty-> give the player an option
                    ie.:[3] Reconnect using your nickname
            3. if 3 is chosen (client-side) the server will receive the reconnection
                message, link the new socket or rmiClient

        CLIENT SIDE
        CASE:CLIENT CRASHED
            1. show the options join/create/[reconnect] then ask the user for his nickname
            2. if the nick is correct reconnect the player
        CASE:CLIENT LOST INTERNET CONNECTION
            1. initiate automatic poke (or just checkForExistingGame?) messages (with exponential backoff?)
            2. the protocol is the same as the crash except the nickname will be sent automatically
         */
        System.out.println("\t\u001B[33mla connessione non funziona, metto il client come disconnected\u001B[0m");
        disconnectedClients.add(client);
        for (ClientInterface disconnectedClient : this.disconnectedClients) {
            System.out.println(disconnectedClient);
        }
        System.out.println("\tda qua bisogna capire come ricollegare il client");
    }
}
