package it.polimi.GC13.view.TUI;

import junit.framework.TestCase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReaderTest extends TestCase {

    public void testReadInput() throws InterruptedException {
        Reader r = new Reader();
        r.start();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            try {
                r.readInput();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        r.wakeUpMainThread();
        System.out.println("Done");
    }
}