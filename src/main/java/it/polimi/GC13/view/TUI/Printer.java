package it.polimi.GC13.view.TUI;

import it.polimi.GC13.model.Deck;
import it.polimi.GC13.model.PlayableCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
    CLASS USED TO PRINT MESSAGES FROM TUI
 */
public class Printer {
    private static final Deck visualDeck = new Deck();
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
        METHOD USED TO SHOW GAME'S HISTORY
    */
    public void showHistory(List<String> gamesLog) {
        System.out.println("\n--- HISTORY ---");
        gamesLog.forEach(log -> System.out.println(log + ";"));
    }

    /**
        METHOD USED TO SHOW DRAWABLE CARDS
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
    }

    /**
        METHOD USED TO PRINT OBJECTIVE CARD
     */
    public synchronized void showObjectiveCard(String message, List<Integer> privateObjectiveCards) {
        System.out.println("\n" + message);

        visualDeck.getObjectiveDeck()
                .stream()
                .filter(card -> privateObjectiveCards.contains(card.serialNumber))
                .forEach(card -> {
                    card.printObjectiveCard();
                    System.out.println("\t    [" + card.serialNumber + "]");
                });
    }

    /**
        METHOD USED TO PRINT PLAYABLE CARDS
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
            System.out.print("       FRONT                 BACK");
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
            hand.forEach(serialCard -> System.out.print("        [" + serialCard + "]          "));
        }
    }

    /**
     *
     * @param playersScore players' score map; prints all players score present in the map
     *
     */
    public void showPlayersScore(Map<String, Integer> playersScore) {
        System.out.println("\n--- PLAYERS SCORE ---");
        playersScore.forEach((key, value) -> System.out.println(key + "'s current score is " + value));
    }

    public void seeChat(Map<String, List<String>> chat) {
        if (!chat.isEmpty()) {
            try {
                String playerChosen;
                do {
                    System.out.println("Choose player to see the chat: [" + (String.join("], [", chat.keySet()) + "]"));
                    System.out.print("Player: ");
                    playerChosen = reader.readLine();
                    while (!chat.containsKey(playerChosen)) {
                        System.out.print("Chat with " + playerChosen + " doesn't exist.\nEnter an existing player chat: ");
                        playerChosen = reader.readLine();
                    }
                } while (!(chat.containsKey(playerChosen) || playerChosen.equals("global")));
                synchronized (chat.get(playerChosen)) {
                    System.out.println("CHAT WITH " + playerChosen.toUpperCase() + "\n" + chat.get(playerChosen)
                            .stream()
                            .map(message -> message + ";\n")
                            .collect(Collectors.joining()));
                }
            } catch (IOException e) {
                System.err.println("Error parsing the name");
            }
        } else {
            System.out.println("No message exists.");
        }
    }

    public void winnerString() {
        System.out.println("""
                ██╗   ██╗ ██████╗ ██╗   ██╗    ██╗    ██╗ ██████╗ ███╗   ██╗
                ╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║    ██║██╔═══██╗████╗  ██║
                 ╚████╔╝ ██║   ██║██║   ██║    ██║ █╗ ██║██║   ██║██╔██╗ ██║
                  ╚██╔╝  ██║   ██║██║   ██║    ██║███╗██║██║   ██║██║╚██╗██║
                   ██║   ╚██████╔╝╚██████╔╝    ╚███╔███╔╝╚██████╔╝██║ ╚████║
                   ╚═╝    ╚═════╝  ╚═════╝      ╚══╝╚══╝  ╚═════╝ ╚═╝  ╚═══╝
                                                                           \s
                """);
    }

    public void loserString() {
        System.out.println("""
                
                ██╗   ██╗ ██████╗ ██╗   ██╗    ██╗      ██████╗ ███████╗████████╗
                ╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║     ██╔═══██╗██╔════╝╚══██╔══╝
                 ╚████╔╝ ██║   ██║██║   ██║    ██║     ██║   ██║███████╗   ██║  \s
                  ╚██╔╝  ██║   ██║██║   ██║    ██║     ██║   ██║╚════██║   ██║  \s
                   ██║   ╚██████╔╝╚██████╔╝    ███████╗╚██████╔╝███████║   ██║  \s
                   ╚═╝    ╚═════╝  ╚═════╝     ╚══════╝ ╚═════╝ ╚══════╝   ╚═╝  \s
                                                                                \s
                """);
    }

}
