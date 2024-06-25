package it.polimi.GC13.view.TUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A thread-based reader that reads input from the console.
 * <p>
 *     This class continuously reads input from the console and stores the most
 *     recent non-blank input in a volatile variable. The input can be retrieved
 *     using the {@link #readInput()} method.
 * </p>
 */
public class Reader extends Thread {
    private volatile String input;
    private volatile boolean inputReady = false;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Retrieves the most recent input if it is ready.
     *
     * @return The most recent input if ready, or null if no new input is available
     */
    public synchronized String readInput() {
        if (this.inputReady) {
            this.inputReady = false;
            return this.input;
        } else {
            return null;
        }
    }

    /**
     * Continuously reads input from the console.
     */
    @Override
    public void run() {
        while (true) {
            try {
                String tmp = this.reader.readLine();
                if (!tmp.isBlank()) {
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
