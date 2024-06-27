package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.view.GUI.Getter;

import java.net.URL;

public class ResourceGetter implements Getter {

    @Override
    public URL getURL(String resource) {
        return getClass().getResource(resource);
    }
}
