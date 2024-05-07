package it.polimi.GC13.view.GUI.login;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.view.GUI.BackgroundPanel;
import it.polimi.GC13.view.GUI.FrameManager;

import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;


//TODO: da testare
public class LoginFrame extends JFrame {
    private JTextField nicknameField;
    private JTextField gameNameField;
    private JList<String> existingGameList;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JButton loginButton;


/*JOINING PHASE
       1)Not existing --> game LoginFrame()
       2)Existing game:
            2.a) Create new game --> LoginFrame()
            2.b) Join existing game --> LoginFrame(Map<String, Integer> gameNameWaitingPlayersMap)
       */


    public LoginFrame(FrameManager frameManager) {
        setTitle("Login Page");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 750);
        setResizable(false);
        setLocationRelativeTo(null);


        BackgroundPanel backgroundPanel = new BackgroundPanel("src/main/utils/CodexLogo.jpg", true);
        add(backgroundPanel);
        backgroundPanel.setLayout(null);

        JLabel nicknameLabel = new JLabel("Insert your nickname: ");
        nicknameLabel.setFont(new Font("Old English Text MT", Font.BOLD, 20));
        int width1 = 345;
        int heigth1 = 25;
        nicknameLabel.setBounds((getWidth() - width1)/2 +5, (getHeight() - heigth1)/2 - 70, width1, heigth1);
        backgroundPanel.add(nicknameLabel);

        nicknameField = new JTextField();
        int width2 = 200;
        int heigth2 = 25;
        nicknameField.setBounds((getWidth() - width2)/2 -65, (getHeight() - heigth2)/2 - 40, width2, heigth2);
        backgroundPanel.add(nicknameField);


        //TODO: gestire il posizionamento dei componenti con GroupLayout

        //Insertion of the game name
        JLabel gameNameLabel = new JLabel("Insert game name: ");
        gameNameLabel.setFont(new Font("Old English Text MT", Font.BOLD, 20));
        int width3 = 345;
        int heigth3 = 25;
        gameNameLabel.setBounds((getWidth() - width3)/2 +5, (getHeight() - heigth3)/2 +5, width3, heigth3);
        backgroundPanel.add(gameNameLabel);

        gameNameField = new JTextField();
        int width4 = 180;
        int heigth4 = 25;
        gameNameField.setBounds((getWidth() - width4)/2 -75, (getHeight() - heigth4)/2 + 35, width4, heigth4);
        backgroundPanel.add(gameNameField);

        //Definition of the number of players
        JLabel numPlayerLabel = new JLabel("Players: ");
        numPlayerLabel.setFont(new Font("Old English Text MT", Font.BOLD, 20));
        int width = 345;
        int heigth = 25;
        numPlayerLabel.setBounds((getWidth() - width)/2 + 230, (getHeight() - heigth)/2 - 40, width, heigth);
        backgroundPanel.add(numPlayerLabel);


        radioButton1 = new JRadioButton("2");
        radioButton2 = new JRadioButton("3");
        radioButton3 = new JRadioButton("4");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        buttonGroup.add(radioButton3);

        //TODO: inserire i radiobuttons in un pannello e impostare FlowLayout
        int radioButtonWidth = 50;
        int radioButtonHeight = 30;
        int startX = (getWidth() - (2 * radioButtonWidth)) / 2;
        int startY = (getHeight() - radioButtonHeight) / 2;

        radioButton1.setBounds(startX + 100, startY-10, radioButtonWidth, radioButtonHeight);
        radioButton2.setBounds(startX + 140, startY-10, radioButtonWidth, radioButtonHeight);
        radioButton3.setBounds(startX + 180, startY-10, radioButtonWidth, radioButtonHeight);

        radioButton1.setOpaque(false);
        radioButton2.setOpaque(false);
        radioButton3.setOpaque(false);

        backgroundPanel.add(radioButton1);
        backgroundPanel.add(radioButton2);
        backgroundPanel.add(radioButton3);


        ActionListener textRadioListener = e -> enableLoginButton();

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableLoginButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableLoginButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableLoginButton();
            }
        };

        nicknameField.getDocument().addDocumentListener(documentListener);
        gameNameField.getDocument().addDocumentListener(documentListener);
        radioButton1.addActionListener(textRadioListener);
        radioButton2.addActionListener(textRadioListener);
        radioButton3.addActionListener(textRadioListener);


        //Start button
        loginButton = new JButton("Start");
        loginButton.setEnabled(false);
        int width6 = 85;
        int heigth6 = 33;
        loginButton.setBounds((getWidth() - width6)/2 + 100, getHeight()/2 +20, width6, heigth6);
        loginButton.setFont(new Font("Old English Text MT", Font.BOLD, 20)); //PlainGermanica
        loginButton.setBackground(new Color(177,163,28));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText();
                String gameName = gameNameField.getText();
                int playersNumber = -1;

                if (radioButton1.isSelected()) {
                    playersNumber = 2;
                } else if (radioButton2.isSelected()) {
                    playersNumber = 3;
                } else if (radioButton3.isSelected()){
                    playersNumber = 4;
                }
                //JOptionPane.showMessageDialog(null, "player: "+ nickname + "game: "+ gameName+ "num: "+ playersNumber);
                frameManager.getVirtualServer().createNewGame(nickname, playersNumber, gameName);
                dispose();
            }
        });

        backgroundPanel.add(loginButton);
        setVisible(true);
    }


    public LoginFrame(FrameManager frameManager, Map<String, Integer> gameNameWaitingPlayersMap) {
        setTitle("Login Page");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 750);
        setResizable(false);
        setLocationRelativeTo(null);


        /*Object[] options = {"Create Game", "Join Game"};
        int choice = JOptionPane.showOptionDialog(null, "There are existing games, choose: ", "Create Game / Join Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

        if (choice == JOptionPane.YES_OPTION) {
            //player wants to create a new game
            createNewGame();

            //da capire come gestire eventuali errori
            //JOptionPane.showMessageDialog(null, "Error while creating the game.", "Creation Failed", JOptionPane.WARNING_MESSAGE);

        } else {
            //player can and wants to join an existing game
            joinExistingGame(gameNameWaitingPlayersMap);

            //da capire come gestire eventuali errori
            //JOptionPane.showMessageDialog(null, "Error while joining game.", "Joining Failed", JOptionPane.WARNING_MESSAGE);
        }*/

        BackgroundPanel backgroundPanel = new BackgroundPanel("src/main/utils/CodexLogo.jpg", true);
        add(backgroundPanel);
        backgroundPanel.setLayout(null);

        //insertion of the player nickname
        JLabel nicknameLabel = new JLabel("Insert your nickname: ");
        nicknameLabel.setFont(new Font("Old English Text MT", Font.BOLD, 20));
        int width1 = 345;
        int heigth1 = 25;
        nicknameLabel.setBounds((getWidth() - width1) / 2 + 5, (getHeight() - heigth1) / 2 - 70, width1, heigth1);
        backgroundPanel.add(nicknameLabel);

        nicknameField = new JTextField();
        int width2 = 200;
        int heigth2 = 25;
        nicknameField.setBounds((getWidth() - width2) / 2 - 65, (getHeight() - heigth2) / 2 - 40, width2, heigth2);
        backgroundPanel.add(nicknameField);


        //Selection of the existing game
        JLabel gameNameLabel = new JLabel("Choose an existing game: ");
        gameNameLabel.setFont(new Font("Old English Text MT", Font.BOLD, 20));
        int width3 = 345;
        int heigth3 = 25;
        gameNameLabel.setBounds((getWidth() - width3)/2 + 5, (getHeight() - heigth3)/2 - 5, width3, heigth3);
        backgroundPanel.add(gameNameLabel);


        //da testare
        DefaultListModel<String> listModel = new DefaultListModel<>();
        existingGameList = new JList<>(listModel);

        for(String s : gameNameWaitingPlayersMap.keySet()){
            String concat = s + "(" + gameNameWaitingPlayersMap.get(s).toString() + " players waiting)";
            listModel.addElement(concat);
        }
        existingGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(existingGameList);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        int width5 = 100;
        int heigth5 = 25;
        scrollPane.setPreferredSize(new Dimension(width5, heigth5));
        scrollPane.setBounds((getWidth() - width5)/2 - 115, (getHeight() - heigth5)/2 + 30, width5, heigth5);
        backgroundPanel.add(scrollPane);



        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableLoginButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableLoginButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableLoginButton();
            }
        };

        nicknameField.getDocument().addDocumentListener(documentListener);



        loginButton = new JButton("Start");
        int width6 = 85;
        int heigth6 = 33;
        loginButton.setBounds((getWidth() - width6) / 2 + 100, getHeight() / 2 + 20, width6, heigth6);
        loginButton.setFont(new Font("Old English Text MT", Font.BOLD, 20)); //PlainGermanica
        loginButton.setBackground(new Color(177, 163, 28));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText();
                String gameName = existingGameList.getSelectedValue();
                frameManager.getVirtualServer().addPlayerToGame(nickname, gameName);
                dispose();
            }
        });
        backgroundPanel.add(loginButton);
        setVisible(true);
    }


    private void enableLoginButton() {
        boolean fieldsFilled = !nicknameField.getText().isEmpty() && !gameNameField.getText().isEmpty();
        boolean radioButtonSelected = radioButton1.isSelected() || radioButton2.isSelected() || radioButton3.isSelected();
        //System.out.println(fieldsFilled && radioButtonSelected);
        loginButton.setEnabled(fieldsFilled && radioButtonSelected);
    }

}
