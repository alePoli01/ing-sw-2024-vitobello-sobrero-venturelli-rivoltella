package it.polimi.GC13.app;

import it.polimi.GC13.view.GUI.game.MainFrameProva;

import javax.swing.*;

public class ClientAppTestGUI {
    public ClientAppTestGUI() {

        SwingUtilities.invokeLater(MainFrameProva::new);


        /*SwingUtilities.invokeLater(()->{
            List<TokenColor> tc = new ArrayList<>();
            List<TokenColor> tokenColorList = Arrays.asList(TokenColor.values());

            for (TokenColor tokenColor : tokenColorList) {
                if(!tokenColor.equals(TokenColor.BLUE))
                    tc.add(tokenColor);
            }
            new MainFrameProva(tc);
        });
        SwingUtilities.invokeLater(()->{
            List<Integer> ObjectiveList = Arrays.asList(87, 88);
            new MainFrameProva(ObjectiveList);
        });*/

        //SwingUtilities.invokeLater(ClasseDiProva::new);
    }
}
