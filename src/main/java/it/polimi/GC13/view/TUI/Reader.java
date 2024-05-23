package it.polimi.GC13.view.TUI;

import java.io.*;

public class Reader extends Thread {
    private String input;
    private boolean inputReady = false;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public synchronized String readInput() throws InterruptedException {
        while (!this.inputReady) {
            this.wait();
        }
        this.inputReady = false;
        return this.input;
    }

    public void interruptThread() {
        this.interrupt();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String tmp = reader.readLine();
                synchronized (this) {
                    this.input = tmp;
                    this.inputReady = true;
                    this.notifyAll();
                }
            } catch (IOException e) {
                System.out.println("Error reading");
            }
        }
    }
}
