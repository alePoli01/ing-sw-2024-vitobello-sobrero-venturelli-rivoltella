package it.polimi.GC13.view.TUI;

import junit.framework.TestCase;

public class ReaderTest extends TestCase {

    public void testReadInput() {
        Reader r = new Reader();
        int count = 0;
        r.start();

        while (count < 2) {
            try {
                System.out.println(r.readInput());
                count++;
                if (count == 2) {
                    throw new InterruptedException();
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }
}