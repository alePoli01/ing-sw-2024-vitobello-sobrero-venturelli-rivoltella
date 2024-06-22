package it.polimi.GC13.app;

import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.rmi.RMIServerInterface;
import it.polimi.GC13.network.ServerImpulse;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    private final ControllerDispatcher controllerDispatcher;
    private final LobbyController lobbyController;


    protected RMIServer(ControllerDispatcher controllerDispatcher, LobbyController lobbyController) throws RemoteException {
        this.controllerDispatcher = controllerDispatcher;
        this.lobbyController = lobbyController;
    }

    public void startServer(int port) throws RemoteException {
        try {
            //create registry on selected port
            System.out.println("\t\tcreating registry on port: " + port);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("server", this);//binds the RMIServer class on the registry under the name server
        } catch (Exception e) {
            System.err.println("Failed to bind server to RMI registry");
        }
        System.out.println("RMI Server is ready");
    }

    @Override
    public void createConnection(ClientInterface client) throws RemoteException {
        //setup Impulse
        System.out.println("\t\tCreating an Impulse generator");
        ServerImpulse serverImpulse = new ServerImpulse(client);
        System.out.println("\t\tStarting the Impulse generator Thread");
        new Thread(serverImpulse).start();
    }

    @Override
    public void registerMessageFromClient(MessagesFromClient message, ClientInterface client) throws RemoteException {
        message.methodToCall(this.lobbyController, this.controllerDispatcher.getClientControllerMap().get(client), client, this.controllerDispatcher.getClientPlayerMap().get(client));
    }
}
