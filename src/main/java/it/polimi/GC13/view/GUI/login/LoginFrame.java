package it.polimi.GC13.view.GUI.login;

import it.polimi.GC13.view.GUI.BackgroundPanel;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//Login page: The user can insert his nickname in a proper textfield and can submit with a start button. The button send the nickname to the Server.
// The game starts when the button is clicked

//TODO: verificare che ci sia altro da inviare oltre al nickname e aggiungerlo qui
public class LoginFrame extends JFrame{
    private JTextField nicknameField;
    private JTextField gameNameField;
    private JComboBox<Integer> numberOfPlayerBox;
    private JComboBox<String> existingGameList;
    private JButton loginButton;


    public LoginFrame(/*boolean b*/){
        super("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //the program stops running when the user close the window
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 750);
        setResizable(false); // The user can't resize the frame
        setLocationRelativeTo(null); //the frame appears in the center of the screen

       /*JOINING PHASE
       1)Not existing game ==> login player name + game name + number of player needed to start
       2)Existing game:
            2.a) Create new game: login player name + game name + number of player needed to start
            2.b) Join existing game: login player name + choose existing game
       */

        BackgroundPanel backgroundPanel = new BackgroundPanel("src/main/utils/CodexLogo.jpg", true); //nuova aggiunta
        add(backgroundPanel);
        backgroundPanel.setLayout(null); //da provare GroupLayout

        //aggiunta testo al frame
        JLabel nicknameLabel = new JLabel("Insert your nickname: ");
        nicknameLabel.setFont(new Font("Old English Text MT", Font.BOLD, 21));
        int width1 = 345;
        int heigth1 = 25;
        nicknameLabel.setBounds(getWidth()/2 - width1/2 +5, getHeight()/2 - heigth1/2 - 70, width1, heigth1);
        backgroundPanel.add(nicknameLabel);


        // Aggiungi il textfield al frame
        nicknameField = new JTextField();
        int width2 = 200;
        int heigth2 = 25;

        nicknameField.setBounds(getWidth()/2 - width2/2 -65, getHeight()/2 - heigth2/2 - 40, width2, heigth2);
        backgroundPanel.add(nicknameField); // Aggiungi il textfield al frame

        //if b: new game --> game name + number of player needed to start
        //if !b: existing game --> choose existing game

        //TODO: gestire il posizionamento dei componenti con GroupLayout
        /*if(b){
            JLabel gameNameLabel = new JLabel("Insert game name: ");
            gameNameLabel.setFont(new Font("Old English Text MT", Font.BOLD, 20));
            int width3 = 345;
            int heigth3 = 25;
            gameNameLabel.setBounds(getWidth()/2 - width3/2 +5, getHeight()/2 - heigth3/2 - 5, width3, heigth3);
            backgroundPanel.add(gameNameLabel);


            gameNameField = new JTextField();
            int width4 = 180;
            int heigth4 = 25;
            gameNameField.setBounds(getWidth()/2 - width4/2 -75, getHeight()/2 - heigth4/2 + 25, width4, heigth4);
            backgroundPanel.add(gameNameField); // Aggiungi il textfield al frame


            int[] options = {1, 2, 3};
            numberOfPlayerBox = new JComboBox<>(options);
            games.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                     JComboBox<Integer> source = (JComboBox<Integer>) e.getSource();
                     Integer selectedOption = (Integer) source.getSelectedItem();
                     //da continuare
                }
            });

            int width5= 100;
            int heigth5 = 25;
            games.setPreferredSize(new Dimension(width5, heigth5));
            games.setToolTipText("Seleziona un'opzione"); // Imposta un tooltip
            games.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            games.setBounds(getWidth()/2 - width5/2 +110, getHeight()/2 - heigth5/2 - 30, width5, heigth5);
            backgroundPanel.add(games);
        }else{

        }*/


        //TEST LOGIN DI CREAZIONE: da tenere solo la parte commentata, questa parte è da togliere

        JLabel gameNameLabel = new JLabel("Insert game name: ");
        gameNameLabel.setFont(new Font("Old English Text MT", Font.BOLD, 20));
        int width3 = 345;
        int heigth3 = 25;
        gameNameLabel.setBounds(getWidth()/2 - width3/2 +5, getHeight()/2 - heigth3/2 - 5, width3, heigth3);
        backgroundPanel.add(gameNameLabel);


        gameNameField = new JTextField();
        int width4 = 180;
        int heigth4 = 25;
        gameNameField.setBounds(getWidth()/2 - width4/2 -75, getHeight()/2 - heigth4/2 + 25, width4, heigth4);
        backgroundPanel.add(gameNameField); // Aggiungi il textfield al frame




        Integer[] options = {1, 2, 3};
        numberOfPlayerBox = new JComboBox<>(options);
        numberOfPlayerBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<Integer> source = (JComboBox<Integer>) e.getSource();
                Integer selectedOption = (Integer) source.getSelectedItem();
                //da continuare
            }
        });

        int width5= 100;
        int heigth5 = 25;
        numberOfPlayerBox.setPreferredSize(new Dimension(width5, heigth5));
        numberOfPlayerBox.setToolTipText("Seleziona un'opzione"); // Imposta un tooltip
        numberOfPlayerBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        numberOfPlayerBox.setBounds(getWidth()/2 - width5/2 +110, getHeight()/2 - heigth5/2 - 30, width5, heigth5);
        backgroundPanel.add(numberOfPlayerBox);


        //customizza e aggiungi il bottone al frame
        loginButton = new JButton("Start");
        int width6 = 80;
        int heigth6 = 28;
        loginButton.setBounds(getWidth()/2 - width6/2 - 10, getHeight()/2 + 50, width6, heigth6);
        loginButton.setFont(new Font("Old English Text MT", Font.BOLD, 15)); //PlainGermanica
        loginButton.setBackground(new Color(177,163,28, 128));
        backgroundPanel.add(loginButton);

        setVisible(true);
    }
}
