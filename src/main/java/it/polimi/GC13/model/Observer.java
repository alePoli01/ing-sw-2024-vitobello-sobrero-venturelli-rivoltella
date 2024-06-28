package it.polimi.GC13.model;

import it.polimi.GC13.app.DiskManager;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code Observer} class manages client listeners and notifies them with messages from the server.
 * It
 */
public class Observer {

    /**
     * List of client listeners registered with this Observer to receive notifications.
     */
    private final List<ClientInterface> listenerList = new ArrayList<>();

    /**
     * DiskManager responsible for managing game state persistence and disk operations.
     */
    private final DiskManager diskManager;

    /**
     * The name of the game being observed by this Observer instance.
     */
    private final String gameName;


    /**
     * Constructs an {@code Observer} instance with the specified {@link DiskManager} and {@link Game}.
     *
     * @param diskManager The {@link DiskManager} responsible for managing game state persistence.
     * @param game        The {@link Game} instance to observe.
     */
    public Observer(DiskManager diskManager, Game game) {
        this.diskManager = diskManager;
        this.diskManager.setGameManaged(game);
        this.gameName = game.getGameName();
    }


    /**
     * Retrieves the number of listeners currently registered with this Observer.
     *
     * @return The number of listeners.
     */
    public int getListenerSize() {
        return this.listenerList.size();
    }


    /**
     * Adds a client listener to the list of listeners.
     *
     * @param listener The client listener to add.
     */
    public void addListener(ClientInterface listener) {
        this.listenerList.add(listener);
        System.out.println("[Game: "+ gameName +"][Listeners size: " + this.listenerList.size()+"]");
    }

    /**
     * Removes a client listener from the list of listeners.
     *
     * @param listener The client listener to remove.
     */
    public void removeListener(ClientInterface listener) {
        this.listenerList.remove(listener);
    }


    /**
     * Notifies all registered clients with the specified message.
     *
     * @param message The message to notify clients with.
     */
    public void notifyClients(MessagesFromServer message) {
        this.listenerList.forEach(client -> {
                try {
                    message.notifyClient(client);
                } catch (RemoteException e) {
                    System.err.println("RMI: Error notifying client: " + client);
                }
        });
        this.diskManager.writeOnDisk();
    }

    /**
     * Retrieves the DiskManager associated with this Observer.
     *
     * @return The DiskManager instance.
     */
    public DiskManager getDiskManager() {
        return this.diskManager;
    }
}
