package it.polimi.GC13.view.GUI.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interface for storing all the image path and for managing various card-related functionalities in the game GUI.
 */
public interface CardManager {
      // fixed
      String RESOURCE_DIR = "resource_card";
      String GOLD_DIR = "gold_card";
      String STARTER_DIR = "starter_card";

      // fixed
      String FUNGI_LOGO_DIR = "fungi_reign_logo.png";
      String ANIMAL_LOGO_DIR = "animal_reign_logo.png";
      String PLANT_LOGO_DIR = "plant_reign_logo.png";
      String INSECT_LOGO_DIR = "insect_reign_logo.png";
      String QUILL_LOGO_DIR = "quill.png";
      String MANUSCRIPT_LOGO_DIR = "manuscript.png";
      String INKWELL_LOGO_DIR = "inkwell.png";

      // fixed
      String FUNGI_JUDGE_DIR = "fungi_judge.png";
      String ANIMAL_JUDGE_DIR = "animal_judge.png";
      String PLANT_JUDGE_DIR = "plant_judge.png";
      String INSECT_JUDGE_DIR = "insect_judge.png";

      // fixed
      String FUNGI_GRAVESTONE = "gravestone_fungi.png";
      String ANIMAL_GRAVESTONE = "gravestone_animal.png";
      String PLANT_GRAVESTONE = "gravestone_plant.png";
      String INSECT_GRAVESTONE = "gravestone_insect.png";

      // fixed
      String MONK1 = "monk1.png";
      String MONK2 = "monk2.png";
      String MONK3 = "monk3.png";
      String MONK4 = "monk4.png";

      // fixed
      String CROWN = "crown.png";
      String ERROR_FISH = "dead-fish.png";
      String ADVERTISEMENT_MONK = "advertisement.png";
      String ERROR_MONK = "error_monk.png";
      String QUESTION_MONK = "question_monk.png";

      // fixed
      String ONE_MESSAGE = "notify_1message.png";
      String TWO_MESSAGE = "notify_2messages.png";
      String THREE_MESSAGE = "notify_3messages.png";
      String MANY_MESSAGE = "notify_many_messages.png";


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
