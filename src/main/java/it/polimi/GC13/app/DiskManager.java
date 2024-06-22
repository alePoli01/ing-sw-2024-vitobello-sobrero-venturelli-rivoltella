package it.polimi.GC13.app;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.model.Game;

import java.io.*;

public class DiskManager implements Serializable {
    private Game gameManaged;

    public void writeOnDisk() {
        if (this.gameManaged.getGameState().equals(GameState.MID)) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/it/polimi/GC13/app/" + this.gameManaged.getGameName() + ".ser");
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

    public Game readFromDisk(String gameName) {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/java/it/polimi/GC13/app/" + gameName + ".ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.gameManaged = (Game) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Game doesn't exist or it wasn't saved");
            System.err.println(e.getMessage());
        }
        System.out.println("Reading from disk completed");
        return this.gameManaged;
    }

    public void setGameManaged(Game gameManaged) {
        this.gameManaged = gameManaged;
    }
}
