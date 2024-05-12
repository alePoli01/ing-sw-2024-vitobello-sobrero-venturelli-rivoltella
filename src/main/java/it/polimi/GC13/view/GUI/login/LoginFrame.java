package it.polimi.GC13.view.GUI.login;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.view.GUI.BackgroundPanel;
import it.polimi.GC13.view.GUI.FrameManager;
import it.polimi.GC13.view.GUI.WaitingLobby;

import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LoginFrame extends JFrame implements WaitingLobby {
    private JTextField nicknameField;
    private JTextField gameNameField;
    private JList<String> existingGameList;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JButton loginButton;


    final static String PANEL1 = "Panel 1";
    final static String PANEL2 = "Panel 2";
    private JPanel waitingLobby;
    int progress;
    int colorIndex = 0;


/*JOINING PHASE
       1)Not existing --> game LoginFrame()
       2)Existing game:
            2.a) Create new game --> LoginFrame()
            2.b) Join existing game --> LoginFrame(Map<String, Integer> gameNameWaitingPlayersMap)
       */


    public LoginFrame(FrameManager frameManager) {
        setTitle("Login Page");
        setSize(750, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel cards = new JPanel(new CardLayout());
        add(cards);

        BackgroundPanel backgroundPanel = new BackgroundPanel("src/main/utils/CodexLogo.jpg", true);
        backgroundPanel.setLayout(null);
        cards.add(backgroundPanel, PANEL1);

        JLabel nicknameLabel = createTextLabelFont("Insert your nickname: ", 20);
        setBoundsComponent(nicknameLabel, 345, 25, 5, -70);
        backgroundPanel.add(nicknameLabel);

        nicknameField = new JTextField();
        setBoundsComponent(nicknameField, 200, 25, -65, -40);
        backgroundPanel.add(nicknameField);


        //Insertion of the game name
        JLabel gameNameLabel = createTextLabelFont("Insert game name: ", 20);
        setBoundsComponent(gameNameLabel, 345, 25, 5, 5);
        backgroundPanel.add(gameNameLabel);

        gameNameField = new JTextField();
        setBoundsComponent(gameNameField, 180, 25, -75, +35);
        backgroundPanel.add(gameNameField);

        //Definition of the number of players
        JLabel numPlayerLabel = createTextLabelFont("Players: ", 20);
        setBoundsComponent(numPlayerLabel, 345, 25, 230, -40);
        backgroundPanel.add(numPlayerLabel);


        radioButton1 = new JRadioButton("2");
        radioButton2 = new JRadioButton("3");
        radioButton3 = new JRadioButton("4");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        buttonGroup.add(radioButton3);

        //TODO: inserire i radiobuttons in un pannello e impostare FlowLayout
        setBoundsComponent(radioButton1, 100, 30, 100, -10);
        setBoundsComponent(radioButton2, 100, 30, 140, -10);
        setBoundsComponent(radioButton3, 100, 30, 180, -10);

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
        loginButton = createButton("Start", 20);
        loginButton.setEnabled(false);
        setBoundsComponent(loginButton, 85, 33, 100, 40);
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
                frameManager.setNickname(nickname);
                //dispose();

                CardLayout cardLayout = (CardLayout) cards.getLayout();
                if (e.getActionCommand().equals("Start")) {
                    waitingLobby = createWaitingLobby();
                    cards.add(waitingLobby, PANEL2);
                    cardLayout.show(cards, PANEL2);
                }
            }
        });

        backgroundPanel.add(loginButton);
        setVisible(true);
    }


    public LoginFrame(FrameManager frameManager, Map<String, Integer> gameNameWaitingPlayersMap) {
        setTitle("Login Page");
        setSize(750, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel cards = new JPanel(new CardLayout());
        add(cards);

        BackgroundPanel backgroundPanel = new BackgroundPanel("src/main/utils/CodexLogo.jpg", true);
        cards.add(backgroundPanel, PANEL1);
        backgroundPanel.setLayout(null);

        //insertion of the player nickname
        JLabel nicknameLabel = createTextLabelFont("Insert your nickname: ", 20);
        setBoundsComponent(nicknameLabel, 345, 25, 5, -70);
        backgroundPanel.add(nicknameLabel);

        nicknameField = new JTextField();
        setBoundsComponent(nicknameField, 200, 25, -65, -40);
        backgroundPanel.add(nicknameField);


        //Selection of the existing game
        JLabel gameNameLabel = createTextLabelFont("Choose an existing game: ", 20);
        setBoundsComponent(gameNameLabel, 345, 25, 5, -5);
        backgroundPanel.add(gameNameLabel);



        DefaultListModel<String> listModel = new DefaultListModel<>();
        existingGameList = new JList<>(listModel);
        Map<String, String> map = new HashMap<>();

        for(String s : gameNameWaitingPlayersMap.keySet()){
            String concat = s + " (" + gameNameWaitingPlayersMap.get(s).toString() + " players waiting)";
            map.put(concat, s);
            listModel.addElement(concat);
        }
        existingGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //existingGameList.setPreferredSize(new Dimension(170, 55));

        existingGameList.setBorder(BorderFactory.createEtchedBorder());


        //TODO: FAR FUNZIONARE LO SCROLLPANE
        //JScrollPane scrollPane = addScrollPane(existingGameList);
        //setBoundsComponent(scrollPane, 100, 25, -115, +30);
        setBoundsComponent(existingGameList, 100, 25, -115, +30);

        backgroundPanel.add(existingGameList);


        /*int width5 = 100;
        int heigth5 = 25;
        scrollPane.setPreferredSize(new Dimension(width5, heigth5));
        scrollPane.setBounds((getWidth() - width5)/2 - 115, (getHeight() - heigth5)/2 + 30, width5, heigth5);*/
        //backgroundPanel.add(scrollPane);


        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableLoginButton2();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableLoginButton2();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableLoginButton2();
            }
        };

        nicknameField.getDocument().addDocumentListener(documentListener);



        loginButton = createButton("Start", 20);
        setBoundsComponent(loginButton, 85, 33, 100, 40);
        loginButton.setBackground(new Color(177, 163, 28));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText();
                //String gameName = existingGameList.getSelectedValue();
                String gameName = map.get(existingGameList.getSelectedValue());
                frameManager.getVirtualServer().addPlayerToGame(nickname, gameName);
                CardLayout cardLayout = (CardLayout) cards.getLayout();
                if (e.getActionCommand().equals("Start")) {
                    waitingLobby = createWaitingLobby();
                    cards.add(waitingLobby, PANEL2);
                    cardLayout.show(cards, PANEL2);
                }
            }
        });
        backgroundPanel.add(loginButton);
        setVisible(true);
    }




  /*  private static JScrollPane addScrollPane(JComponent component){
        JScrollPane scrollPane = new JScrollPane(component);
        //scrollPane.setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(600, 600));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }*/


    private void enableLoginButton() {
        boolean fieldsFilled = !nicknameField.getText().isEmpty() && !gameNameField.getText().isEmpty();
        boolean radioButtonSelected = radioButton1.isSelected() || radioButton2.isSelected() || radioButton3.isSelected();
        loginButton.setEnabled(fieldsFilled && radioButtonSelected);
    }


    //TODO: DA SISTEMARE: bisogna disattivare il bottone se non si seleziona nessun game nella lista
    private void enableLoginButton2() {
        boolean fieldsFilled = !nicknameField.getText().isEmpty();
        loginButton.setEnabled(fieldsFilled);
    }

    @Override
    public JPanel createWaitingLobby(){
        JPanel waitingLobby = new JPanel();
        ArrayList<Color> colors = new ArrayList<>();

        waitingLobby.setLayout(new GridBagLayout());
        waitingLobby.setBackground(new Color(237,230,188,255));

        for(int i=0; i<4; i++) {
            colors.add(new Color(107, 189, 192, 255));
            colors.add(new Color(233, 73, 23, 255));
            colors.add(new Color(113, 192, 124, 255));
            colors.add(new Color(171, 63, 148, 255));
        }



        JLabel label = createTextLabelFont("Welcome to waiting lobby", 35);
        setBorderInsets(label,0,0,70,0);
        waitingLobby.add(label, createGridBagConstraints(0,0));


        JLabel label2 = createTextLabelFont("Waiting for the players: ", 20);
        setBorderInsets(label,0,0,50,0);
        waitingLobby.add(label2, createGridBagConstraints(0,1));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300,30));
        progressBar.setForeground(colors.get(colorIndex));
        colorIndex++;
        waitingLobby.add(progressBar, createGridBagConstraints(0,2));

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 5;
                if (progress > 100) {
                    progress = 0;
                    progressBar.setForeground(colors.get(colorIndex));
                    colorIndex = (colorIndex + 1) % colors.size();
                }
                progressBar.setValue(progress);
            }
        });
        timer.start();

        return waitingLobby;
    }


    private JLabel createTextLabelFont(String content, int dim) {
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return jLabel;
    }

    private void setBoundsComponent(JComponent component, int width, int heigth, int translationX, int translationY){
        component.setBounds((getWidth() - width) / 2 + translationX, (getHeight() - heigth) / 2 + translationY, width, heigth);
    }

    private JButton createButton(String text, int dim){
        JButton button = new JButton(text);
        button.setFont(new Font("Old English Text MT", Font.BOLD, dim)); //PlainGermanica
        return button;
    }

    private GridBagConstraints createGridBagConstraints(int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;

        return gbc;
    }

    private void setBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        jComponent.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }


}
