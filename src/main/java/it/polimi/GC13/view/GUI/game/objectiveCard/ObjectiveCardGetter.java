package it.polimi.GC13.view.GUI.game.objectiveCard;


import it.polimi.GC13.view.GUI.Getter;

import java.net.URL;

public class ObjectiveCardGetter implements Getter {
    @Override
    public URL getURL(String resourceName) {
        return getClass().getResource(resourceName);
    }
}
