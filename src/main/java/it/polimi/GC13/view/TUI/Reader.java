package it.polimi.GC13.view.TUI;

import it.polimi.GC13.exception.GenericException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader extends Thread {
    private volatile String input;
    private volatile boolean inputReady = false;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public synchronized String readInput(){
        if(inputReady) {
            inputReady = false;
            return this.input;
        }else{
            return null;
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
