package it.polimi.GC13.view.GUI.game.starterCard;

import it.polimi.GC13.view.GUI.Getter;

import java.net.URL;


/**
 * The {@code StarterCardGetter} class implements the {@link Getter} interface and provides
 * a method to retrieve a resource URL based on the resource name.
 * This class is typically used for loading resources related to starter cards
 * in the game GUI.
 */
public class StarterCardGetter implements Getter {

    /**
     * Retrieves the URL of the resource specified by the resource name.
     *
     * @param resourceName the name of the resource to be retrieved.
     * @return the URL of the resource, or null if the resource could not be found.
     */
    @Override
    public URL getURL(String resourceName) {
        return getClass().getResource(resourceName);
    }
}
