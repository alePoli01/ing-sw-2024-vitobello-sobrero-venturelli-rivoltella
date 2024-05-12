package it.polimi.GC13.network.rmi;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIClientAdapter extends UnicastRemoteObject implements ServerInterface {
    private ExecutorService executorService;
    public RMIServerInterface serverStub;
    private final ClientInterface rmiClientImpl;

    public RMIClientAdapter(ClientInterface rmiClientImpl) throws RemoteException {
        super();
        this.executorService = Executors.newCachedThreadPool();
        this.rmiClientImpl = rmiClientImpl;
    }

    /*
    clientAapter: implements ServerInterface, view uses the adapter to ignore the remote exception (view's code shouldn't change regardless of connection type)

    RMIServer: implements RMIServerInterface, identical to ServerInterface but requires the client
    */
    public ServerInterface startRMIConnection(String hostName, int port) throws RemoteException {
        System.out.println("Starting RMI connection to " + hostName + ":" + port + " ...");
        System.out.println("\t\tgetting registry");
        Registry registry = LocateRegistry.getRegistry(hostName, port);
        try {
            System.out.println("\t\tgetting server stub");
            this.serverStub = (RMIServerInterface) registry.lookup("server");
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Registry Lookup for server stub failed.");
            System.exit(-1);
        }
        return this;
    }

    @Override
    public void createNewGame(String playerNickname, int numOfPlayers, String gameName) {
        executorService.submit(() -> {
            try {
                serverStub.createNewGame(playerNickname, numOfPlayers, gameName, rmiClientImpl);
            } catch (RemoteException e) {
                System.err.println("Remote exception occurred.");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void addPlayerToGame(String playerNickname, String gameName) {
        executorService.submit(() -> {
            try {
                serverStub.addPlayerToGame(playerNickname, gameName, rmiClientImpl);
            } catch (RemoteException e) {
                System.err.println("Remote exception occurred.");
                e.printStackTrace();
            }
        });

    }

    @Override
    public void checkForExistingGame() {
        executorService.submit(() -> {
            try {
                serverStub.checkForExistingGame(rmiClientImpl);
            } catch (RemoteException e) {
                System.err.println("Remote exception occurred.");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void chooseToken(TokenColor tokenColor) {

        executorService.submit(() -> {
            try {
                serverStub.chooseToken(tokenColor, rmiClientImpl);
            } catch (RemoteException e) {
                System.err.println("Remote exception occurred.");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void placeStartCard(boolean isFlipped) {

        executorService.submit(() -> {
            try {
                serverStub.placeStartCard(isFlipped, rmiClientImpl);
            } catch (RemoteException e) {
                System.err.println("Remote exception occurred.");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void placeCard(int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {

        executorService.submit(() -> {
            try {
                serverStub.placeCard(cardToPlaceHandIndex, isFlipped, X, Y, rmiClientImpl);
            } catch (RemoteException e) {
                System.err.println("Remote exception occurred.");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void writeMessage(String sender, String receiver, String message) {
        executorService.submit(() -> {
            try {
                serverStub.writeMessage(sender, receiver, message, rmiClientImpl);
            } catch (RemoteException e) {
                System.err.println("Remote exception occurred.");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void drawCard(int serialCardToDraw) {
        executorService.submit(() -> {
            try {
                serverStub.drawCard(serialCardToDraw, rmiClientImpl);
            } catch (RemoteException e) {
                System.err.println("Remote exception occurred.");
                e.printStackTrace();
            }
        });

    }

    @Override
    public void choosePrivateObjectiveCard(int serialPrivateObjectiveCard) {
        executorService.submit(() -> {
            try {
                serverStub.choosePrivateObjectiveCard(serialPrivateObjectiveCard, rmiClientImpl);
            } catch (RemoteException e) {
                System.err.println("Remote exception occurred.");
                e.printStackTrace();
            }
        });

    }

}
