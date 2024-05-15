package it.polimi.GC13.view.GUI.login;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.AddPlayerToGameMessage;
import it.polimi.GC13.network.messages.fromclient.CreateNewGameMessage;
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


public class LoginFrame extends JFrame implements WaitingLobby, ActionListener {
    private JTextField nicknameField;
    private JTextField gameNameField;
    private JList<String> existingGameList;

    private JButton loginButton;

    final static String PANEL1 = "Panel 1";
    final static String PANEL2 = "Panel 2";
    private JPanel waitingLobby;
    int progress;
    int colorIndex = 0;

    //PARTE NUOVA
    ArrayList<JRadioButton> radiobuttons;
    private FrameManager frameManager;
    private JPanel cards;
    Object source;
    boolean flag;
    String nickname;
    String gameName;
    int playersNumber = -1;


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


        cards = new JPanel(new CardLayout());
        //JPanel cards = new JPanel(new CardLayout());
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

        //PARTE NUOVA
        this.frameManager = frameManager;
        ButtonGroup buttonGroup = new ButtonGroup();
        JPanel radioButtonPanel = new JPanel(new FlowLayout());
        radioButtonPanel.setOpaque(false);
        backgroundPanel.add(radioButtonPanel);
        radiobuttons = new ArrayList<>();

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


        for(int i=0; i<3; i++){
            setRadioButton(radiobuttons, radioButtonPanel, buttonGroup, String.valueOf(i+2), textRadioListener);
        }

        setBoundsComponent(radioButtonPanel, 200, 30, 100, -10);

        loginButton = createButton("Start", 20);
        loginButton.setEnabled(false);
        setBoundsComponent(loginButton, 85, 33, 100, 40);
        loginButton.setBackground(new Color(177,163,28));

        loginButton.addActionListener(this);

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

        existingGameList.setBorder(BorderFactory.createEtchedBorder());




        //TODO: FARE UN JCOMBOBOX
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
                frameManager.getVirtualServer().sendMessageFromClient(new AddPlayerToGameMessage(nickname, gameName));
                frameManager.setNickname(nickname);
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
        //boolean radioButtonSelected = radioButton1.isSelected() || radioButton2.isSelected() || radioButton3.isSelected();

        //PARTE NUOVA
        boolean radioButtonSelected = radiobuttons.stream().anyMatch(AbstractButton::isSelected);

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


    private void setRadioButton(ArrayList<JRadioButton> arrayButton, JPanel panel, ButtonGroup buttonGroup, String text, ActionListener textRadioListener){
        JRadioButton radioButton = new JRadioButton(text);
        buttonGroup.add(radioButton);
        arrayButton.add(radioButton);
        radioButton.setOpaque(false);
        radioButton.addActionListener(textRadioListener);
        radioButton.addActionListener(this);
        panel.add(radioButton);
    }


    //PARTE NUOVA
    @Override
    public void actionPerformed(ActionEvent e) {
        nickname = nicknameField.getText();
        gameName = gameNameField.getText();
        source = e.getSource();
        flag = false;

        if(source instanceof JRadioButton){
            playersNumber = Integer.parseInt(((JRadioButton) source).getText());
            //System.out.println(playersNumber);
            //flag = true;
        } else if(source instanceof JButton){
            CardLayout cardLayout = (CardLayout) cards.getLayout();
            //System.out.println(nickname);
            //System.out.println(gameName);
            //System.out.println(playersNumber);
            frameManager.getVirtualServer().sendMessageFromClient(new CreateNewGameMessage(nickname, playersNumber, gameName));
            frameManager.setNickname(nickname);
            waitingLobby = createWaitingLobby();
            cards.add(waitingLobby, PANEL2);
            cardLayout.show(cards, PANEL2);
        }
    }
}
