package it.polimi.GC13.view.TUI;

import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.model.Deck;
import it.polimi.GC13.model.ObjectiveCard;
import it.polimi.GC13.model.PlayableCard;
import it.polimi.GC13.network.messages.fromserver.ObjectiveAchieved;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * class {@code Printer} is used to print messages from TUI.
 */
public class Printer {
    private static final Deck visualDeck = new Deck();

    /**
     * Shows the game's history.
     *
     * @param gamesLog A list of strings representing the game's history.
     */
    public void showHistory(List<String> gamesLog) {
        System.out.println("\n--- HISTORY ---");
        gamesLog.forEach(log -> System.out.println(log + ";"));
    }

    /**
     * Shows the drawable cards.
     *
     * @param serialToDraw A map where keys are card serial numbers and values indicate in which side they are showed.
     *                     ('true' = back, 'false' = front)
     */
    public void showDrawableCards(Map<Integer, Boolean> serialToDraw) {
        AtomicInteger lineCounter = new AtomicInteger(0);
        Map<PlayableCard, Boolean> cardsToDraw = new HashMap<>();
        serialToDraw.forEach((key, value) -> cardsToDraw.put(visualDeck.getCard(key), value));

        for (lineCounter.set(0); lineCounter.get() < 6; lineCounter.incrementAndGet()) {
            cardsToDraw.forEach((key, value) -> {
                key.linePrinter(0, lineCounter.get(), value);
                System.out.print(" ░ ");
            });
            System.out.println();
        }
        cardsToDraw.keySet().forEach(key -> System.out.print("        [" + key.serialNumber + "]          "));
        System.out.println();
    }

    /**
     * Prints the objective cards to the console.
     *
     * @param message A message to be displayed before the objective cards.
     * @param serialObjectiveCards A list of serial numbers of the objective cards.
     */
    public void showObjectiveCard(String message, List<Integer> serialObjectiveCards) {
        System.out.println("\n" + message);
        List<ObjectiveCard> objectiveCards = new LinkedList<>();
        serialObjectiveCards.forEach(cardNumber -> objectiveCards.add(visualDeck.getObjectiveCard(cardNumber)));
        AtomicInteger lineCounter = new AtomicInteger(0);

        for (lineCounter.set(0); lineCounter.get() < 6; lineCounter.incrementAndGet()) {
            objectiveCards.forEach(card -> {
                card.printLineObjectiveCard(lineCounter.get());
                System.out.print(" ░ ");
            });
            System.out.println();
        }
        objectiveCards.forEach(card -> System.out.print("        [" + card.serialNumber + "]          "));
        System.out.println();
    }

    /**
     * Prints the player's hand to the console.
     *
     * @param hand A list of serial numbers of the playable cards in the player's hand.
     */
    public void showHand(List<Integer> hand) {
        // PRINTS START CARD
        if (hand.size() == 1) {
            System.out.println("Start card serial: [" + hand.getFirst() + "]\n");
            PlayableCard startCard = visualDeck.getCard(hand.getFirst());
            for (int i = 0; i < 6; i++) {
                startCard.linePrinter(0, i, false);
                System.out.print(" ░ ");
                startCard.linePrinter(0, i, true);
                System.out.println();
            }
            System.out.println("       FRONT                 BACK");
        } else {
            AtomicInteger lineCounter = new AtomicInteger(0);
            LinkedList<PlayableCard> cardsOnHand = new LinkedList<>();

            hand.forEach(serialCard -> cardsOnHand.add(visualDeck.getCard(serialCard)));

            for (lineCounter.set(0); lineCounter.get() < 6; lineCounter.incrementAndGet()) {
                cardsOnHand
                        .forEach(card -> {
                            card.linePrinter(0, lineCounter.get(), false);
                            System.out.print(" ░ ");
                        } );
                System.out.println();
            }
            cardsOnHand.forEach(card -> System.out.print("        [" + card.serialNumber + "]          "));
            System.out.println();
        }
    }

    /**
     * Prints the players' score on TUI.
     *
     * @param playersScore players' score map; prints all players score present in the map
     *
     */
    public void showPlayersScore(Map<String, Integer> playersScore) {
        System.out.println("\n--- PLAYERS SCORE ---");
        playersScore.forEach((key, value) -> System.out.println(key + "'s current score is " + value));
    }

    /**
     * Prints the winner message to the console.
     */
    public void winnerString() {
        System.out.println("""
                
                
                ██╗   ██╗ ██████╗ ██╗   ██╗    ██╗    ██╗ ██████╗ ███╗   ██╗
                ╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║    ██║██╔═══██╗████╗  ██║
                 ╚████╔╝ ██║   ██║██║   ██║    ██║ █╗ ██║██║   ██║██╔██╗ ██║
                  ╚██╔╝  ██║   ██║██║   ██║    ██║███╗██║██║   ██║██║╚██╗██║
                   ██║   ╚██████╔╝╚██████╔╝    ╚███╔███╔╝╚██████╔╝██║ ╚████║
                   ╚═╝    ╚═════╝  ╚═════╝      ╚══╝╚══╝  ╚═════╝ ╚═╝  ╚═══╝
                """);
    }

    /**
     * Prints the loser message to the console.
     */
    public void loserString() {
        System.out.println("""
                
                
                ██╗   ██╗ ██████╗ ██╗   ██╗    ██╗      ██████╗ ███████╗████████╗
                ╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║     ██╔═══██╗██╔════╝╚══██╔══╝
                 ╚████╔╝ ██║   ██║██║   ██║    ██║     ██║   ██║███████╗   ██║
                  ╚██╔╝  ██║   ██║██║   ██║    ██║     ██║   ██║╚════██║   ██║
                   ██║   ╚██████╔╝╚██████╔╝    ███████╗╚██████╔╝███████║   ██║
                   ╚═╝    ╚═════╝  ╚═════╝     ╚══════╝ ╚═════╝ ╚══════╝   ╚═╝
                """);
    }

    /**
     * Prints the intro message to the console.
     */
    public void intro(){
        System.out.println("""
                
                
                 ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗
                ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝
                ██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗
                ██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║
                ╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║
                 ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝
                """);
    }

    /**
     * Prints the collected resources to the console.
     *
     * @param collectedResource An EnumMap containing the collected resources and their quantities.
     */
    public void collectedResource(EnumMap<Resource, Integer> collectedResource) {
        System.out.println("Collected resource are: ");
        System.out.println( collectedResource.entrySet().stream().toList()+"\n");
    }

    /**
     * Displays the points scored by each player for each objective achieved and their final score.
     *
     * @param playerNickname player's score
     * @param objectiveAchieved objective achieved
     * @param finalScore player's final score
     */
    public void objectivesAchieved(String playerNickname, List<ObjectiveAchieved> objectiveAchieved, int finalScore) {
        System.out.println("\n\nPLAYER: " + playerNickname + "\nFinal score: " + finalScore + "\n");
        objectiveAchieved
            .forEach(objAchieved -> {
                ObjectiveCard toPrint = visualDeck.getObjectiveCard(objAchieved.serialCard());
                AtomicInteger lineCounter = new AtomicInteger(0);

                for (lineCounter.set(0); lineCounter.get() < 6; lineCounter.incrementAndGet()) {
                    toPrint.printLineObjectiveCard(lineCounter.get());
                    System.out.print(" ░ ");
                    if (lineCounter.get() == 3) {
                        System.out.println("   points given: " + objAchieved.pointsAchieved());
                    } else {
                        System.out.println();
                    }
                }
            });
        System.out.print("\n----------------------------");
    }
}
