package it.polimi.GC13.app;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.rmi.RMIClient;
import it.polimi.GC13.view.login.LoginFrame;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.rmi.NotBoundException;

public class  ClientApp {
    public static void main( String[] args ) throws IOException {
        System.out.println("Hello from Client");
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        int choice = 0;

        do {
            System.out.println("Chose the connection \n[1] RMI\n[2] SOCKET \nYour choice: ");
            try {
                choice = Integer.parseInt(reader.readLine());

                if (choice == 1) {
                    //System.out.println("Chose a nickname");
                    ClientInterface client = new RMIClient(reader.readLine());
                    try {
                        ((RMIClient) client).startRMIConnection();
                        System.out.println("You chose RMI!");
                    } catch (NotBoundException e) {
                        System.out.println("Binding with server failed");
                    } catch (PlayerNotAddedException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (choice == 2) {
                    //System.out.println("Chose a nickname");
                    System.out.println("You chose Socket!");
                    // socket




                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid value!");
            }
        } while (choice == 1 || choice == 2);





        do {
            System.out.println("Chose the interface \n[1] TUI\n[2] GUI \nYour choice: ");
            try {
                choice = Integer.parseInt(reader.readLine());
                if (choice == 1) {
                    //System.out.println("Chose a nickname");
                    //TUI interface configuration

                } else if (choice == 2) {
                    //SwingUtilities.invokeLater(LoginFrame::new); //non so se si inserisce qui

                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid value!");
            }
        } while (choice == 1 || choice == 2);

        //SwingUtilities.invokeLater(MainPage::new); //serve per far partire il frame della GUI

    }
}
