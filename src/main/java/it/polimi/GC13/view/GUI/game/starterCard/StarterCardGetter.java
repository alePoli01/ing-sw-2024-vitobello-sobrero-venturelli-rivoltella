package it.polimi.GC13.view.GUI.game.starterCard;

import it.polimi.GC13.view.GUI.Getter;

import java.net.URL;

public class StarterCardGetter implements Getter {

    @Override
    public URL getURL(String resourceName) {
        return getClass().getResource(resourceName);
    }
}
