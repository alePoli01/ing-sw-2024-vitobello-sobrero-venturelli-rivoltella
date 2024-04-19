package it.polimi.GC13.view.TUI;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.view.View;
import java.util.Scanner;

public class TUI implements View {
    ServerInterface virtualServer;

    public TUI(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
        this.display();
    }

    @Override
    public void display() {
        System.out.println("Choose nickname:");
        Scanner in = new Scanner(System.in);
        do {
            try {
                virtualServer.addPlayerToGame(in.next());
            } catch (NicknameAlreadyTakenException e) {
                System.out.println(e.getMessage());
                in = new Scanner(System.in);
            }
        } while(true);
    }
}
