package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record OnGameWinnerMessage(Set<String> winner, Map<String, List<ObjectiveAchieved>> objectiveAchievedMap) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.gameOver(this.winner(), this.objectiveAchievedMap());
    }
}
