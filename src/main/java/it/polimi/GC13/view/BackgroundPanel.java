package it.polimi.GC13.view;

import javax.swing.*;
import java.awt.*;

//Class for the Login Page background
public class BackgroundPanel extends JPanel {

    private Image backgroundImage;
    private String text="";
    private boolean bool;

    public BackgroundPanel(Image backgroundImage, boolean bool) {
        this.backgroundImage = backgroundImage;
        this.bool = bool;

    }

    public BackgroundPanel(Image backgroundImage, String text, boolean bool){
        this.backgroundImage = backgroundImage;
        this.text = text;
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

        //Write a text in the center of the screen (used in the login page)
        if(!text.isEmpty()){
            FontMetrics fontMetrics = g.getFontMetrics();

            // define teh coordinates of the text based on the width and the height of the frame
            int x = getWidth() / 2 - fontMetrics.stringWidth(text) + 10; // Centro orizzontale del pannello meno metà larghezza del testo
            int y = getHeight() / 2 - 16; // centro verticale del pannello, alzato di 26


            //set the logo font and print the text on the screen
            g.setFont(new Font("PlainGermanica", Font.BOLD, 21));
            g.drawString(text, x, y); // Posizione del testo
        }
    }

}












    /*
      private Image backgroundImage;
    private String text;


    public BackgroundPanel() {
        backgroundImage = new ImageIcon("src/main/utils/CodexLogo.jpg").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Set the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        //get the lenght of the text
        text = "Inserisci il tuo nickname di gioco:";
        FontMetrics fontMetrics = g.getFontMetrics();


        // define teh coordinates of the text based on the width and the height of the frame
        int x = getWidth() / 2 - fontMetrics.stringWidth(text) +10; // Centro orizzontale del pannello meno metà larghezza del testo
        int y = getHeight() / 2 - 16; // centro verticale del pannello, alzato di 26


        //set the logo font and print the text on the screen
        g.setFont(new Font("PlainGermanica", Font.BOLD, 21));
        g.drawString(text, x, y); // Posizione del testo
    }

}*/
