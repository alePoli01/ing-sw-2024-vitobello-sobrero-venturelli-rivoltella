package it.polimi.GC13.view.GUI.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interface for storing all the image path and for managing various card-related functionalities in the game GUI.
 */
public interface CardManager {
      String TOKEN_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/token/";
      String P_TOKEN_DIR = TOKEN_DIR + "playableToken/";
      String TOKEN_FILE_SUFFIX = "_token.png";
      String GREY_TOKEN_FILE_NAME = "grey";
      String BLACK_TOKEN_FILE_NAME = "black";

      String RESOURCE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/cards/resource_card/";
      String GOLD_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/cards/gold_card/";
      String STARTER_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/cards/starter_card/";
      String OBJECTIVE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/cards/objective_card/";

      String FUNGI_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/fungi_reign_logo.png";
      String ANIMAL_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/animal_reign_logo.png";
      String PLANT_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/plant_reign_logo.png";
      String INSECT_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/insect_reign_logo.png";
      String QUILL_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/quill.png";
      String MANUSCRIPT_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/manuscript.png";
      String INKWELL_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/inkwell.png";

      String FUNGI_JUDGE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/playersAvatar/fungi_judge.png";
      String ANIMAL_JUDGE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/playersAvatar/animal_judge.png";
      String PLANT_JUDGE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/playersAvatar/plant_judge.png";
      String INSECT_JUDGE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/playersAvatar/insect_judge.png";

      String FUNGI_GRAVESTONE = "src/main/resources/it/polimi/GC13/view/GUI/game/graveyard/gravestone_fungi.png";
      String ANIMAL_GRAVESTONE = "src/main/resources/it/polimi/GC13/view/GUI/game/graveyard/gravestone_animal.png";
      String PLANT_GRAVESTONE = "src/main/resources/it/polimi/GC13/view/GUI/game/graveyard/gravestone_plant.png";
      String INSECT_GRAVESTONE = "src/main/resources/it/polimi/GC13/view/GUI/game/graveyard/gravestone_insect.png";

      String MONK1 = "src/main/resources/it/polimi/GC13/view/GUI/game/monks/monk1.png";
      String MONK2 = "src/main/resources/it/polimi/GC13/view/GUI/game/monks/monk2.png";
      String MONK3 = "src/main/resources/it/polimi/GC13/view/GUI/game/monks/monk3.png";
      String MONK4 = "src/main/resources/it/polimi/GC13/view/GUI/game/monks/monk4.png";

      String CROWN = "src/main/resources/it/polimi/GC13/view/GUI/crown.png";
      String ERROR_FISH = "src/main/resources/it/polimi/GC13/view/GUI/backgrounds/dead-fish.png";
      String ERROR_CARD = "src/main/resources/it/polimi/GC13/view/GUI/game/cards/Error_Card.png";
      String ADVERTISEMENT_MONK = "src/main/resources/it/polimi/GC13/view/GUI/backgrounds/icons/advertisement.png";
      String ERROR_MONK = "src/main/resources/it/polimi/GC13/view/GUI/backgrounds/icons/error_monk.png";
      String QUESTION_MONK = "src/main/resources/it/polimi/GC13/view/GUI/backgrounds/icons/question_monk.png";

      String ONE_MESSAGE = "src/main/resources/it/polimi/GC13/view/GUI/backgrounds/icons/notify_1message.png";
      String TWO_MESSAGE = "src/main/resources/it/polimi/GC13/view/GUI/backgrounds/icons/notify_2messages.png";
      String THREE_MESSAGE = "src/main/resources/it/polimi/GC13/view/GUI/backgrounds/icons/notify_3messages.png";
      String MANY_MESSAGE = "src/main/resources/it/polimi/GC13/view/GUI/backgrounds/icons/notify_many_messages.png";


      /**
       * ArrayList of Resources' image path
       */
     ArrayList<String> logos = new ArrayList<>(Arrays.asList(FUNGI_LOGO_DIR, ANIMAL_LOGO_DIR, PLANT_LOGO_DIR, INSECT_LOGO_DIR, QUILL_LOGO_DIR, MANUSCRIPT_LOGO_DIR, INKWELL_LOGO_DIR));

      /**
       * ArrayList of players avatar image path
       */
     ArrayList<String> avatarsLogo = new ArrayList<>(Arrays.asList(FUNGI_JUDGE_DIR, ANIMAL_JUDGE_DIR, PLANT_JUDGE_DIR, INSECT_JUDGE_DIR));

      /**
       * ArrayList of image path given to the winners of the game
       */
     ArrayList<String> monks = new ArrayList<>(Arrays.asList(MONK1, MONK2, MONK3, MONK4));


      /**
       * Displays the starter card and private objective card for the given hand.
       *
       * @param hand The list of card IDs representing the player's hand.
       * @throws IOException If an I/O error occurs while displaying the cards.
       */
      void showStarterCardAndPrivateObjectiveCard(List<Integer> hand) throws IOException;

}
