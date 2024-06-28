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

    /**
     * The most recent non-blank input read from the console.
     */
    private volatile String input;

    /**
     * Indicates whether new input is ready to be retrieved.
     */
    private volatile boolean inputReady = false;

    /**
     * The buffered reader used to read input from the console.
     */
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
     * Continuously reads input from the console and updates the stored input.
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
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading");
            }
        }
    }
}
