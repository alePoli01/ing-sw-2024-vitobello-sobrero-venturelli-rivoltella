package it.polimi.GC13.network.socket;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.LostConnectionToServerInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.*;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.*;
import it.polimi.GC13.view.View;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ClientDispatcher implements ClientDispatcherInterface, LostConnectionToServerInterface {
    View view;
    /*
    TODO: ho iniziato a scrivere la parte di ricezione messaggi dal server ma credo serva implementare degli observer da qualche parte sulla TUI
     */
    public ClientDispatcher() {
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void dispatch(String message) {

    }

    @Override
    public void dispatch(OnCheckForExistingGameMessage onCheckForExistingGameMessage) {
        try {
            view.joiningPhase(onCheckForExistingGameMessage.gameNameWaitingPlayersMap());
        } catch(IOException e) {
            System.out.println("Error dispatching game: " + e.getMessage());
        }
    }

    @Override
    public void dispatch(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage) {
        List<TokenColor> tokenColorList = Arrays.asList(TokenColor.values());
        view.tokenSetupPhase(onPlayerAddedToGameMessage.connectedPlayers(), tokenColorList, onPlayerAddedToGameMessage.numPlayersNeeded());
    }

    // EXCEPTION HANDLER
    @Override
    public void dispatch(OnInputExceptionMessage onInputExceptionMessage) {
        view.exceptionHandler(onInputExceptionMessage.getPlayerNickname(), onInputExceptionMessage);
    }

    @Override
    public void dispatch(OnTokenChoiceMessage onTokenChoiceMessage) {
        try {
            view.startCardSetupPhase(onTokenChoiceMessage.playerNickname(), onTokenChoiceMessage.tokenColor());
        } catch (IOException e) {
            System.out.println("Error choosing the token color: " + e.getMessage());
        }
    }

    @Override
    public void dispatch(OnPlaceStartCardMessage onPlaceStartCardMessage) {
        view.displayPositionedCard(onPlaceStartCardMessage.playerNickname(), onPlaceStartCardMessage.serialNumberCard(), onPlaceStartCardMessage.isFlipped());
    }

    @Override
    public void dispatch(OnDealCardMessage onDealCardMessage) {
        view.handUpdate(onDealCardMessage.availableCards());
    }

    @Override
    public void dispatch(OnDealPrivateObjectiveCardsMessage onDealPrivateObjectiveCardsMessage) {
        view.chosePrivateObjectiveCard(onDealPrivateObjectiveCardsMessage.playerNickname(), onDealPrivateObjectiveCardsMessage.privateObjectiveCards());
    }

    @Override
    public void dispatch(OnDealCommonObjectiveCardMessage onDealCommonObjectiveCardMessage) {
        view.setSerialCommonObjectiveCard(onDealCommonObjectiveCardMessage.privateObjectiveCards());
    }

    @Override
    public void connectionLost(ServerInterface server) {
        System.out.println("****ERROR CONNECTION TO SERVER LOST****");
        System.out.println("da qua bisogna capire come ricollegarsi");
    }
}
