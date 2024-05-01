package it.polimi.GC13.network.socket.messages.fromserver.exceptions;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

public class OnNickNameAlreadyTakenMessage implements OnInputExceptionMessage {
    private final String playerNickname;
    private final String errorMessage;

    public OnNickNameAlreadyTakenMessage(String nickname) {
        this.playerNickname = nickname;
        this.errorMessage = "Nickname " + nickname + " is already taken";
    }

    @Override
    public void methodToRecall(View TUI) {
        TUI.checkForExistingGame();
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
        /*StringJoiner joiner = new StringJoiner(" / ", "[ ", " ]");

        System.out.println("Nickname already chosen.\nPlayers in game:");
        for(Player player : playerList) {
            joiner.add(player.getNickname());
        }
        return joiner.toString();*/
    }

    @Override
    public String getPlayerNickname() {
        return this.playerNickname;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }

    @Override
    public void notifyClient(ClientInterface client) {
        client.sendMessage(this);
    }
}
