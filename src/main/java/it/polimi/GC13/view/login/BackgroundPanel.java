package it.polimi.GC13.view.login;

import javax.swing.*;
import java.awt.*;

//Class for the Login Page background
public class BackgroundPanel extends JPanel {
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

        //TODO: da migliorare le coordinate nel caso si cambiasse la dimensione del frame

        // define teh coordinates of the text based on the width and the height of the frame
        int x = getWidth() / 2 - fontMetrics.stringWidth(text) +10; // Centro orizzontale del pannello meno metà larghezza del testo
        int y = getHeight() / 2 - 26; // centro verticale del pannello, alzato di 26


        //set the logo font and print the text on the screen
        g.setFont(new Font("Old English Text MT", Font.BOLD, getWidth()/31));
        g.drawString(text, x, y); // Posizione del testo
    }

}
