package it.polimi.GC13.view.GUI.game;

import java.io.IOException;

public interface CardManager {
     final static String TOKEN_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/token/";
     final static String P_TOKEN_DIR = TOKEN_DIR + "playableToken/";
     final static String TOKEN_FILE_SUFFIX = "_token.png";
     final static String GREY_TOKEN_FILE_NAME = "grey";

     static final String RESOURCE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/cards/resource_card/";
     static final String GOLD_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/cards/gold_card/";
     static final String STARTER_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/cards/starter_card/";
     static final String OBJECTIVE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/cards/objective_card/";

     final static String FUNGI_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/fungi_reign_logo.png";
     final static String ANIMAL_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/animal_reign_logo.png";
     final static String PLANT_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/plant_reign_logo.png";
     final static String INSECT_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/insect_reign_logo.png";
     final static String QUILL_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/quill.png";
     final static String MANUSCRIPT_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/manuscript.png";
     final static String INKWELL_LOGO_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/logos/inkwell.png";

     final static String FUNGI_JUDGE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/playersAvatar/fungi_judge.png";
     final static String ANIMAL_JUDGE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/playersAvatar/animal_judge.png";
     final static String PLANT_JUDGE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/playersAvatar/plant_judge.png";
     final static String INSECT_JUDGE_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/playersAvatar/insect_judge.png";



    void showStartCard(Integer hand) throws IOException; //permette di associare alla carta con tale serialNumber i corrispettivi front e back

}
