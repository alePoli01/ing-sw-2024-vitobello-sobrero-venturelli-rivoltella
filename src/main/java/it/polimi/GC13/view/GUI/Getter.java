package it.polimi.GC13.view.GUI;

import java.net.URL;

/**
 * The {@code Getter} interface defines a method for retrieving the URL of a resource
 * based on its name. Implementations of this interface can be used to load
 * various types of resources in the game GUI.
 */
public interface Getter {

    /**
     * Retrieves the URL of the resource specified by the resource name.
     *
     * @param resourceName the name of the resource to be retrieved.
     * @return the URL of the resource, or null if the resource could not be found.
     */
    URL getURL(String resourceName);
}
