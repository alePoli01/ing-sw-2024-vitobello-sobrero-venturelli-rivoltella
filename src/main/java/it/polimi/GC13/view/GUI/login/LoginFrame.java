package it.polimi.GC13.view.GUI.login;

import it.polimi.GC13.network.messages.fromclient.AddPlayerToGameMessage;
import it.polimi.GC13.network.messages.fromclient.CreateNewGameMessage;
import it.polimi.GC13.view.GUI.BackgroundImageSetter;
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
    //private JList<String> existingGameList;
    private JComboBox<String> comboBox;
    private JButton loginButton;
    int progress;
    int colorIndex = 0;
    ArrayList<JRadioButton> radiobuttons;
    private FrameManager frameManager;
    private JPanel panelContainer;
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

        this.frameManager = frameManager;

        JPanel backgroundPanel = new BackgroundImageSetter("src/main/resources/it/polimi/GC13/view/GUI/backgrounds/CodexBackgroundTitle.jpg");
        getContentPane().add(backgroundPanel);
        panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.LINE_AXIS));
        panelContainer.setOpaque(false);
        panelContainer.setPreferredSize(new Dimension(340, 710));
        backgroundPanel.add(panelContainer);


        JPanel credentialPanel = new JPanel(new GridBagLayout());
        credentialPanel.setOpaque(false);
        GridBagConstraints gbcElements = createGridBagConstraints(0,0);
        gbcElements.anchor = GridBagConstraints.LINE_START;
        gbcElements.insets =  new Insets(5, 0, 0, 0);

        JLabel nicknameLabel = createTextLabelFont("Insert your nickname: ", 20);
        credentialPanel.add(nicknameLabel, gbcElements);

        gbcElements.gridy = 1;
        nicknameField = new JTextField();
        nicknameField.setPreferredSize(new Dimension(200, 25));
        credentialPanel.add(nicknameField, gbcElements);


        gbcElements.gridy = 2;
        gbcElements.insets =  new Insets(20, 0, 0, 0);
        JLabel gameNameLabel = createTextLabelFont("Insert game name: ", 20);
        credentialPanel.add(gameNameLabel, gbcElements);
        gbcElements.gridy = 3;
        gbcElements.insets =  new Insets(5, 0, 0, 0);
        gameNameField = new JTextField();
        gameNameField.setPreferredSize(new Dimension(200, 25));
        credentialPanel.add(gameNameField, gbcElements);

        panelContainer.add(credentialPanel);


        JPanel playersNumPanel = new JPanel(new GridBagLayout());
        playersNumPanel.setOpaque(false);
        GridBagConstraints gbcPlayersNumElements = createGridBagConstraints(0,0);
        gbcPlayersNumElements.insets =  new Insets(0, 0, 10, 0);
        gbcElements.anchor = GridBagConstraints.LINE_START;
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        playersNumPanel.add(emptyPanel, gbcPlayersNumElements);

        gbcPlayersNumElements.gridy = 1;
        gbcPlayersNumElements.insets =  new Insets(0, 0, 0, 0);
        JLabel numPlayerLabel = createTextLabelFont("Players: ", 20);
        playersNumPanel.add(numPlayerLabel, gbcPlayersNumElements);

        gbcPlayersNumElements.gridy = 2;
        ButtonGroup buttonGroup = new ButtonGroup();
        JPanel radioButtonPanel = new JPanel(new FlowLayout());
        setBorderInsets(radioButtonPanel,0, 20, 0, 0);
        radioButtonPanel.setOpaque(false);
        playersNumPanel.add(radioButtonPanel, gbcPlayersNumElements);
        radiobuttons = new ArrayList<>();


        gbcPlayersNumElements.gridy = 3;
        gbcPlayersNumElements.insets =  new Insets(10, 0, 0, 0);
        loginButton = createButton("Start", 20);
        loginButton.setEnabled(false);
        loginButton.setBackground(new Color(177,163,28));
        loginButton.addActionListener(this);
        playersNumPanel.add(loginButton, gbcPlayersNumElements);

        panelContainer.add(playersNumPanel);

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

        setVisible(true);
    }


    public LoginFrame(FrameManager frameManager, Map<String, Integer> gameNameWaitingPlayersMap) {
        setTitle("Login Page");
        setSize(750, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel backgroundPanel = new BackgroundImageSetter("src/main/resources/it/polimi/GC13/view/GUI/backgrounds/CodexBackgroundTitle.jpg");
        getContentPane().add(backgroundPanel);
        panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.LINE_AXIS));
        panelContainer.setOpaque(false);
        panelContainer.setPreferredSize(new Dimension(340, 710));
        backgroundPanel.add(panelContainer);


        JPanel credentialPanel = new JPanel(new GridBagLayout());
        credentialPanel.setOpaque(false);
        GridBagConstraints gbcElements = createGridBagConstraints(0,0);
        gbcElements.anchor = GridBagConstraints.LINE_START;
        gbcElements.insets =  new Insets(5, 0, 0, 0);

        JLabel nicknameLabel = createTextLabelFont("Insert your nickname: ", 20);
        credentialPanel.add(nicknameLabel, gbcElements);
        gbcElements.gridy = 1;
        nicknameField = new JTextField();
        nicknameField.setPreferredSize(new Dimension(200, 25));
        credentialPanel.add(nicknameField, gbcElements);



        gbcElements.gridy = 2;
        gbcElements.insets =  new Insets(15, 0, 0, 0);
        JLabel gameNameLabel = createTextLabelFont("Choose an existing game: ", 20);
        credentialPanel.add(gameNameLabel, gbcElements);

        gbcElements.gridy = 3;
        gbcElements.insets =  new Insets(5, 0, 0, 0);


        /*DefaultListModel<String> listModel = new DefaultListModel<>();
        existingGameList = new JList<>(listModel);
        Map<String, String> map = new HashMap<>();

        for(String s : gameNameWaitingPlayersMap.keySet()){
            String concat = s + " (" + gameNameWaitingPlayersMap.get(s).toString() + " players waiting)";
            map.put(concat, s);
            listModel.addElement(concat);
        }
        existingGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        existingGameList.setBorder(BorderFactory.createEtchedBorder());


        credentialPanel.add(existingGameList, gbcElements);

        panelContainer.add(credentialPanel);*/



        comboBox = new JComboBox<>();

        Map<String, String> map = new HashMap<>();
        for(String s : gameNameWaitingPlayersMap.keySet()){
            String concat = s + " (" + gameNameWaitingPlayersMap.get(s).toString() + " players waiting)";
            map.put(concat, s);
            comboBox.addItem(concat);
        }

        credentialPanel.add(comboBox, gbcElements);
        panelContainer.add(credentialPanel);


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


        JPanel playersNumPanel = new JPanel(new GridBagLayout());
        playersNumPanel.setOpaque(false);
        GridBagConstraints gbcPlayersNumElements = createGridBagConstraints(0,0);
        gbcPlayersNumElements.insets =  new Insets(10, 0, 0, 0);
        loginButton = createButton("Start", 20);
        loginButton.setEnabled(false);
        loginButton.setBackground(new Color(177,163,28));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText();
                //String gameName = map.get(existingGameList.getSelectedValue());

                String gameName = map.get((String) comboBox.getSelectedItem());


                frameManager.getVirtualServer().sendMessageFromClient(new AddPlayerToGameMessage(nickname, gameName));
                frameManager.setNickname(nickname);
                if (e.getActionCommand().equals("Start")) {
                    createLobby();
                    getContentPane().revalidate();
                    getContentPane().repaint();
                }
            }
        });

        playersNumPanel.add(loginButton, gbcPlayersNumElements);
        panelContainer.add(playersNumPanel);

        setVisible(true);
    }


    private void enableLoginButton() {
        boolean fieldsFilled = !nicknameField.getText().isEmpty() && !gameNameField.getText().isEmpty();
        boolean radioButtonSelected = radiobuttons.stream().anyMatch(AbstractButton::isSelected);
        loginButton.setEnabled(fieldsFilled && radioButtonSelected);
    }


    private void enableLoginButton2() {
        boolean fieldsFilled = !nicknameField.getText().isEmpty();
        loginButton.setEnabled(fieldsFilled);
    }

    @Override
    public void createLobby(){
        panelContainer.removeAll();
        ArrayList<Color> colors = new ArrayList<>();

        panelContainer.setLayout(new GridBagLayout());
        panelContainer.setBackground(new Color(237,230,188,255));

        for(int i=0; i<4; i++) {
            colors.add(new Color(107, 189, 192));
            colors.add(new Color(233, 73, 23));
            colors.add(new Color(113, 192, 124));
            colors.add(new Color(171, 63, 148));
        }

        JLabel label2 = createTextLabelFont("Waiting for the players: ", 30);
        setBorderInsets(label2,30,0,30,0);
        panelContainer.add(label2, createGridBagConstraints(0,0));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300,30));
        progressBar.setForeground(colors.get(colorIndex));
        colorIndex++;
        panelContainer.add(progressBar, createGridBagConstraints(0,1));

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
    }


    private JLabel createTextLabelFont(String content, int dim) {
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return jLabel;
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
        } else if(source instanceof JButton){
            frameManager.getVirtualServer().sendMessageFromClient(new CreateNewGameMessage(nickname, playersNumber, gameName));
            frameManager.setNickname(nickname);

            createLobby();
            getContentPane().revalidate();
            getContentPane().repaint();
        }
    }
}
