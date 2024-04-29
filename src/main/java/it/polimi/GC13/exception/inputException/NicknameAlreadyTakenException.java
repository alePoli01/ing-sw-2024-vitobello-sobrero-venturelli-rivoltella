package it.polimi.GC13.exception.inputException;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class NicknameAlreadyTakenException extends Exception implements InputException {
    private final List<Player> playerList;
    private final Map<Game, Integer> waitingPlayersMap;
    private final Map<String, Game> joinableGameMap;

    public NicknameAlreadyTakenException(List<Player> playerList, Map<Game, Integer> waitingPlayersMap, Map<String, Game> joinableGameMap) {
        this.playerList = playerList;
        this.waitingPlayersMap = waitingPlayersMap;
        this.joinableGameMap = joinableGameMap;
    }

    @Override
    public String getMessage(){
        StringJoiner joiner = new StringJoiner(" / ", "[ ", " ]");

        System.out.println("Nickname already chosen.\nPlayers in game:");
        for(Player player : playerList){
            joiner.add(player.getNickname());
        }
        return joiner.toString();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    @Override
    public void methodToRecall(View TUI) throws IOException {
        TUI.joinExistingGame(waitingPlayersMap, joinableGameMap);
    }
}
