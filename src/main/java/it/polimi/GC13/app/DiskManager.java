package it.polimi.GC13.app;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.model.Game;

import java.io.*;

/**
 * Class that represents a disk manager. It is responsible for writing to and reading from the disk
 * to allow the continuation of a game in case the server loses connection with clients.
 */
public class DiskManager implements Serializable {
    /**
     * game managed by the DiskManager
     */
    private Game gameManaged;

    /**
     * Writes the current game state to disk if the game is in the MID state.
     * The game is serialized to a file with the name of the game.
     */
    public void writeOnDisk() {
        if (this.gameManaged.getGameState().equals(GameState.MID)) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(this.gameManaged.getGameName() + ".ser");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(gameManaged);
                objectOutputStream.close();
                fileOutputStream.close();
                System.out.println("\u001B[32mSerialization completed\u001B[0m");
            } catch (IOException e) {
                System.err.println("Error writing file");
            }
        }
    }

    /**
     * Reads a game state from disk.
     * The game is deserialized from a file with the specified game name.
     *
     * @param gameName the name of the game file to read
     * @return the deserialized Game object, or null if an error occurs
     */
    public Game readFromDisk(String gameName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(gameName + ".ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.gameManaged = (Game) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            return null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("Game doesn't exist or it wasn't saved");
            System.err.println(e.getMessage());
            return null;
        }
        System.out.println("Reading from disk completed");
        return this.gameManaged;
    }

    /**
     * Sets the game to be managed by this DiskManager.
     *
     * @param gameManaged the Game object to be managed
     */
    public void setGameManaged(Game gameManaged) {
        this.gameManaged = gameManaged;
    }
}
