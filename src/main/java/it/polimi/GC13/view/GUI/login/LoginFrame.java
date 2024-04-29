package it.polimi.GC13.view.GUI.login;

import it.polimi.GC13.view.GUI.BackgroundPanel;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;


//Login page: The user can insert his nickname in a proper textfield and can submit with a start button. The button send the nickname to the Server.
// The game starts when the button is clicked

//TODO: verificare che ci sia altro da inviare oltre al nickname e aggiungerlo qui
public class LoginFrame extends JFrame {
    private JLabel text;
    private JTextField nicknameField;
    private JButton loginButton;


    public LoginFrame(){
        super("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //the program stops running when the user close the window
        setSize(750, 750);
        setResizable(false); // The user can't resize the frame
        setLocationRelativeTo(null); //the frame appears in the center of the screen

        BackgroundPanel backgroundPanel = new BackgroundPanel("src/main/utils/CodexLogo.jpg", true); //nuova aggiunta
        add(backgroundPanel);
        backgroundPanel.setLayout(null);

        //aggiunta testo al frame
        text = new JLabel("Inserisci nickname di gioco: ");
        text.setFont(new Font("Old English Text MT", Font.BOLD, 25));
        int width1 = 345;
        int heigth1 = 25;
        text.setBounds(getWidth()/2 - width1/2 +5, getHeight()/2 - heigth1/2 - 35, width1, heigth1);
        backgroundPanel.add(text);


        // Aggiungi il textfield al frame
        nicknameField = new JTextField();
        int width2 = 300;
        int heigth2 = 25;

        nicknameField.setBounds(getWidth()/2 - width2/2 -5, getHeight()/2 - heigth2/2 + 5, width2, heigth2);
        backgroundPanel.add(nicknameField); // Aggiungi il textfield al frame

        //customizza e aggiungi il bottone al frame
        loginButton = new JButton("Start");
        int width3 = 80;
        int heigth3 = 28;
        loginButton.setBounds(getWidth()/2 - width3/2 - 10, getHeight()/2 + 35, width3, heigth3);
        loginButton.setFont(new Font("Old English Text MT", Font.BOLD, 15)); //PlainGermanica
        loginButton.setBackground(new Color(177,163,28, 128));
        backgroundPanel.add(loginButton);

        setVisible(true);
    }
}
