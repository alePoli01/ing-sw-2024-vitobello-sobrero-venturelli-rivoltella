package it.polimi.GC13.view.GUI.game.goldCard;

import it.polimi.GC13.view.GUI.Getter;

import java.net.URL;

public class GoldCardGetter implements Getter {

    @Override
    public URL getURL(String resourceName) {
        return getClass().getResource(resourceName);
    }
}
