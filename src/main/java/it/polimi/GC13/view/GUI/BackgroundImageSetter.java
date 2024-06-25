package it.polimi.GC13.view.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A JPanel subclass used to display a background image.
 */
public class BackgroundImageSetter extends JPanel {
    private BufferedImage backgroundImage;

    /**
     * Constructs a BackgroundImageSetter instance with the specified background image file.
     *
     * @param backgroundImage The path to the background image file
     */
    public BackgroundImageSetter(String backgroundImage) {
        try {
            this.backgroundImage = ImageIO.read(new File(backgroundImage));
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
