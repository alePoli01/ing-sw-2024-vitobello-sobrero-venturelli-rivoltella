package it.polimi.GC13.view.GUI;

import javax.swing.*;
import java.awt.*;

//Class for the Login Page background
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private boolean bool;

    public BackgroundPanel(Image backgroundImage, boolean bool) {
        this.backgroundImage = backgroundImage;
        this.bool = bool;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Set the background image
        if(bool)
            //Set the background image full screen
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        else {
            //Resize the image and set it in the center of the screen
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(237,230,188,255));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            double width = backgroundImage.getWidth(this);
            double height = backgroundImage.getHeight(this);

            double i = 1;
            do {
                width = width/i;
                height = height/i;
                i += 0.1;
            } while(height > getHeight());


            double x = (getWidth() - width) / 2;
            double y = (getHeight() - height) / 2;

            g2d.drawImage(backgroundImage, (int) x, (int) y, (int) width, (int) height, this);
            //g2d.dispose();
        }
    }

}