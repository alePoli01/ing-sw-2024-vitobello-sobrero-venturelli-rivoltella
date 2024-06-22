package it.polimi.GC13.view.TUI;

import it.polimi.GC13.exception.GenericException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader extends Thread {
    private volatile String input;
    private volatile boolean inputReady = false;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final TUI owner;

    public Reader(TUI owner) {
        this.owner = owner;
    }

    public synchronized String readInput() throws InterruptedException, GenericException {
        while (!this.inputReady) {
            this.wait();
        }
        this.inputReady = false;
        if (!this.owner.getStatus()) {
            return this.input;
        } else {
            throw new GenericException(this.input);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String tmp = reader.readLine();
                if(!tmp.isBlank()){
                    synchronized (this) {
                        this.input = tmp;
                        this.inputReady = true;
                        this.notifyAll();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading");
            }
        }
    }
}
