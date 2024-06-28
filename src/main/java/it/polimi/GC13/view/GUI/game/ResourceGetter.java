package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.view.GUI.Getter;

import java.net.URL;

/**
 * The {@code ResourceGetter} class implements the {@link Getter} interface.
 * It provides methods to retrieve resource URLs based on resource names.
 */
public class ResourceGetter implements Getter {

    /**
     * Retrieves the URL of the specified resource.
     *
     * @param resource The name or path of the resource
     * @return The URL object representing the resource location,
     *         or {@code null} if the resource is not found
     */
    @Override
    public URL getURL(String resource) {
        return getClass().getResource(resource);
    }
}
