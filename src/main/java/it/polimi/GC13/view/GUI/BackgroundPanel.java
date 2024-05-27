package it.polimi.GC13.view.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//Class for the Login Page background
public class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;
    private boolean bool;


    public BackgroundPanel(String backgroundImage, boolean bool) {
        try {
            this.backgroundImage = ImageIO.read(new File(backgroundImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bool = bool;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Set the background image

        //nuova aggiunta
        if (bool) {
            if (backgroundImage != null) {
                //Set the background image full screen
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        } else {
            //Resize the image and set it in the center of the screen

            // Calcola l'altezza dello schermo in pixel
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
            int screenHeight = screenSize.height - screenInsets.bottom - 50;
            int screenWidth = screenSize.width;


            if (backgroundImage != null) {
                // Calcola il fattore di ridimensionamento in base all'altezza dello schermo
                double scaleFactor = (double) screenHeight / backgroundImage.getHeight();

                // Ridimensiona l'immagine in base al fattore di ridimensionamento
                double newWidth = backgroundImage.getWidth() * scaleFactor;
                double newHeight = backgroundImage.getHeight() * scaleFactor;

                // Calcola le coordinate per centrare l'immagine
                int x = (int)(screenWidth - newWidth) / 2;
                int y = (int)(screenHeight - newHeight) / 2 + 20;

                //g2d.drawImage(backgroundImage, x, y, (int)newWidth, (int)newHeight, this);
                g.drawImage(backgroundImage, x, y, (int)newWidth, (int)newHeight, this);
            }
        }
    }
}