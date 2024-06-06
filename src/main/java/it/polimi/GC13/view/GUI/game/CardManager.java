package it.polimi.GC13.view.GUI.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface CardManager {
      String TOKEN_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/token/";
      String P_TOKEN_DIR = TOKEN_DIR + "playableToken/";
      String TOKEN_FILE_SUFFIX = "_token.png";
      String GREY_TOKEN_FILE_NAME = "grey";

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

      String CROWN = "src/main/resources/it/polimi/GC13/view/GUI/crown.png";
      String ERROR_IMAGE = "src/main/resources/it/polimi/GC13/view/GUI/backgrounds/dead-fish.png";

     ArrayList<String> logos = new ArrayList<>(Arrays.asList(FUNGI_LOGO_DIR, ANIMAL_LOGO_DIR, PLANT_LOGO_DIR, INSECT_LOGO_DIR, QUILL_LOGO_DIR, MANUSCRIPT_LOGO_DIR, INKWELL_LOGO_DIR));



     void showStarterCardAndPrivateObjectiveCard(List<Integer> hand) throws IOException; //permette di associare alla carta con tale serialNumber i corrispettivi front e back

}
