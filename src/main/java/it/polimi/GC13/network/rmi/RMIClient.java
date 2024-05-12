package it.polimi.GC13.network.rmi;

import it.polimi.GC13.network.socket.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.view.View;

import java.io.Serializable;

public class RMIClient implements Serializable {

    private final View view;
    public  RMIClient (View view){
        this.view = view;
    }
    public void sendMessage (MessagesFromServer message){
        message.methodToCall(view);
    }
}
