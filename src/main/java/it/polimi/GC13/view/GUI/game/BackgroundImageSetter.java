package it.polimi.GC13.view.GUI.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * A JPanel subclass used to display a background image.
 */
public class BackgroundImageSetter extends JPanel {

    /**
     * The background image to be displayed on the panel.
     */
    private BufferedImage backgroundImage;

    /**
     * Constructs a {@code BackgroundImageSetter} instance with the specified background image file.
     *
     * @param resource The path to the background image file as a URL
     */
    public BackgroundImageSetter(URL resource) {
        try {
            if (resource != null) {
                this.backgroundImage = ImageIO.read(resource);
            } else {
                System.err.println("Resource not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overrides the paintComponent method to paint the background image onto the panel.
     *
     * @param g The Graphics object used for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
