package it.polimi.GC13.view.login;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;


//Login page: The user can insert his nickname in a proper textfield and can submit with a start button. The button send the nickname to the Server.
// The game starts when the button is clicked

//TODO: verificare che ci sia altro da inviare oltre al nickname e aggiungerlo qui
public class LoginFrame extends JFrame {
    private JTextField nicknameField;
    private JButton loginButton;
    //private ColorFrame colorFrame;


    public LoginFrame(){
        super("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //the program stops running when the user close the window
        setSize(750, 750);
        setResizable(false); // The user can't resize the frame
        setLocationRelativeTo(null); //the frame appears in the center of the screen


        //Set the background image
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        add(backgroundPanel);
        backgroundPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; //.REMAINDER Specifies that this component is the last component in its column or row

        // Create a panel for login components
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        backgroundPanel.add(loginPanel, gbc);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;

        // Add some vertical space between the textfield and the login button
        gbc2.gridy++;
        loginPanel.add(Box.createVerticalStrut(20), gbc2);
        gbc2.insets = new Insets(70, 10, 0, 10); // Padding top only

        // Add the nickname field to the login panel
        nicknameField = new JTextField(30);
        loginPanel.add(nicknameField, gbc2);


        // Add the login button to the login panel
        //TODO: modificare l'icona del bottone
        gbc2.gridy++;
        //ImageIcon icon = new ImageIcon("src/main/utils/button.png");

        /*
        // Ridimensiona l'immagine
        Image originalImage = icon.getImage();
        Image resizedImage = originalImage.getScaledInstance(150, 60, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        */

        loginButton = new JButton("Start!");
        /*
        loginButton.setIcon(resizedIcon); // Imposta l'immagine come sfondo del bottone
        //loginButton.setBorderPainted(false); // Rimuovi il bordo del bottone
        loginButton.setFocusPainted(false); // Rimuovi il feedback di focus
        loginButton.setContentAreaFilled(false); // Rimuovi il riempimento del contenuto del bottone
        */
        gbc2.insets = new Insets(20, 10, 0, 10); // Padding top only
        loginPanel.add(loginButton, gbc2);


        setVisible(true);


    }
}
