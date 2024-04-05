package it.polimi.GC13.app;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.rmi.RMIClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.rmi.NotBoundException;

public class ClientApp {
    public static void main( String[] args ) throws IOException {
        System.out.println("Hello from Client");
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        int choice = 0;

        do {
            System.out.println("Chose the connection \n[1] RMI\n[2] SOCKET");
            try {
                choice = Integer.parseInt(reader.readLine());
                if (choice == 1) {
                    System.out.println("Chose a nickname");
                    ClientInterface client = new RMIClient(reader.readLine());
                    try {
                        ((RMIClient) client).startRMIConnection();
                        System.out.println("You chose RMI!");
                    } catch (NotBoundException e) {
                        System.out.println("Binding with server failed");
                    }
                } else if (choice == 2) {
                    System.out.println("Chose a nickname");
                    // socket
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid value!");
            }
        } while (choice == 1 || choice == 2);



    }
}
