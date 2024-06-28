package it.polimi.GC13.view.GUI.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interface for managing various card-related functionalities and storing image paths
 * in the game GUI. This interface provides constants for resource directories and
 * image file names, as well as methods to display specific cards.
 */
public interface CardManager {

      /**
       * Directory path for resource cards.
       */
      String RESOURCE_DIR = "resource_card";

      /**
       * Directory path for gold cards.
       */
      String GOLD_DIR = "gold_card";

      /**
       * Directory path for starter cards.
       */
      String STARTER_DIR = "starter_card";


      /**
       * Path for the fungi reign logo image.
       */
      String FUNGI_LOGO_DIR = "fungi_reign_logo.png";

      /**
       * Path for the animal reign logo image.
       */
      String ANIMAL_LOGO_DIR = "animal_reign_logo.png";

      /**
       * Path for the plant reign logo image.
       */
      String PLANT_LOGO_DIR = "plant_reign_logo.png";

      /**
       * Path for the insect reign logo image.
       */
      String INSECT_LOGO_DIR = "insect_reign_logo.png";

      /**
       * Path for the quill logo image.
       */
      String QUILL_LOGO_DIR = "quill.png";

      /**
       * Path for the manuscript logo image.
       */
      String MANUSCRIPT_LOGO_DIR = "manuscript.png";

      /**
       * Path for the inkwell logo image.
       */
      String INKWELL_LOGO_DIR = "inkwell.png";


      /**
       * Path for the fungi judge image.
       */
      String FUNGI_JUDGE_DIR = "fungi_judge.png";

      /**
       * Path for the animal judge image.
       */
      String ANIMAL_JUDGE_DIR = "animal_judge.png";

      /**
       * Path for the plant judge image.
       */
      String PLANT_JUDGE_DIR = "plant_judge.png";

      /**
       * Path for the insect judge image.
       */
      String INSECT_JUDGE_DIR = "insect_judge.png";


      /**
       * Path for the fungi gravestone image.
       */
      String FUNGI_GRAVESTONE = "gravestone_fungi.png";

      /**
       * Path for the animal gravestone image.
       */
      String ANIMAL_GRAVESTONE = "gravestone_animal.png";

      /**
       * Path for the plant gravestone image.
       */
      String PLANT_GRAVESTONE = "gravestone_plant.png";

      /**
       * Path for the insect gravestone image.
       */
      String INSECT_GRAVESTONE = "gravestone_insect.png";


      /**
       * Path for the first monk image.
       */
      String MONK1 = "monk1.png";

      /**
       * Path for the second monk image.
       */
      String MONK2 = "monk2.png";

      /**
       * Path for the third monk image.
       */
      String MONK3 = "monk3.png";

      /**
       * Path for the fourth monk image.
       */
      String MONK4 = "monk4.png";



      /**
       * Path for the crown image.
       */
      String CROWN = "crown.png";

      /**
       * Path for the advertisement monk image.
       */
      String ADVERTISEMENT_MONK = "advertisement.png";

      /**
       * Path for the error monk image.
       */
      String ERROR_MONK = "error_monk.png";

      /**
       * Path for the question monk image.
       */
      String QUESTION_MONK = "question_monk.png";



      /**
       * Path for the single message notification image.
       */
      String ONE_MESSAGE = "notify_1message.png";

      /**
       * Path for the two messages notification image.
       */
      String TWO_MESSAGE = "notify_2messages.png";

      /**
       * Path for the three messages notification image.
       */
      String THREE_MESSAGE = "notify_3messages.png";

      /**
       * Path for the multiple messages notification image.
       */
      String MANY_MESSAGE = "notify_many_messages.png";


      /**
       * List of resource logo image paths.
       */
     ArrayList<String> logos = new ArrayList<>(Arrays.asList(FUNGI_LOGO_DIR, ANIMAL_LOGO_DIR, PLANT_LOGO_DIR, INSECT_LOGO_DIR, QUILL_LOGO_DIR, MANUSCRIPT_LOGO_DIR, INKWELL_LOGO_DIR));

      /**
       * List of player avatar image paths.
       */
     ArrayList<String> avatarsLogo = new ArrayList<>(Arrays.asList(FUNGI_JUDGE_DIR, ANIMAL_JUDGE_DIR, PLANT_JUDGE_DIR, INSECT_JUDGE_DIR));

      /**
       * List of monk image paths for winners.
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
