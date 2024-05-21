package it.polimi.GC13.model;

import it.polimi.GC13.enums.GameState;

import java.io.*;

public class DiskManager {
    private Game gameManaged;

    public DiskManager(Game game) {
        this.gameManaged = game;
    }

    public void writeOnDisk() {
        if (this.gameManaged.getGameState() == GameState.MID || this.gameManaged.getGameState() == GameState.END) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(this.gameManaged.getGameName() + ".ser");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(gameManaged);
                objectOutputStream.close();
                fileOutputStream.close();
                System.out.println("Serialization completed");
            } catch (IOException e) {
                System.out.println("Error writing file");
            }
        }
    }

    public void readFromDisk(String gameName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(gameName + ".ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.gameManaged = (Game) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Game doesn't exist or it wasn't saved");
            System.out.println(e.getMessage());
        }
        System.out.println("Reading from disk completed");
    }
}
