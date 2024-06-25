package it.polimi.GC13.view.GUI.login;

import it.polimi.GC13.network.messages.fromclient.AddPlayerToGameMessage;
import it.polimi.GC13.network.messages.fromclient.CreateNewGameMessage;
import it.polimi.GC13.view.GUI.BackgroundImageSetter;
import it.polimi.GC13.view.GUI.FrameManager;
import it.polimi.GC13.view.GUI.WaitingLobby;
import it.polimi.GC13.view.View;

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

/**
 * Represents the graphical user interface for the login and lobby creation process.
 * <p>
 * Extends {@link JFrame} to manage GUI components.
 * <p>
 * Implements {@link WaitingLobby} interface for creating a lobby in which the players can wait the end
 * of the joining phase, and {@link ActionListener} to handle user actions such as button clicks.
 */
public class LoginFrame extends JFrame implements WaitingLobby, ActionListener {
    private final JTextField nicknameField;
    private JTextField gameNameField;
    private JComboBox<String> comboBox;
    private final JButton loginButton;
    private int progress;
    private int colorIndex = 0;
    private ArrayList<JRadioButton> radiobuttons;
    private FrameManager frameManager;
    private final JPanel panelContainer;
    private int playersNumber = -1;
    private JLabel waitingLabel;

    /**
     * Constructs a LoginFrame instance for starting a new game.
     *
     * @param frameManager The FrameManager instance managing the frames.
     */
    public LoginFrame(FrameManager frameManager) {
        setTitle("Login Page");
        setSize(750, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.frameManager = frameManager;

        JPanel backgroundPanel = new BackgroundImageSetter("src/main/resources/it/polimi/GC13/view/GUI/backgrounds/CodexLogoTitle.png");
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

    /**
     * Constructs a LoginFrame instance for joining an existing game.
     *
     * @param frameManager The FrameManager instance managing the frames.
     * @param gameNameWaitingPlayersMap Map containing game names and number of waiting players in the lobby.
     */
    public LoginFrame(FrameManager frameManager, Map<String, Integer> gameNameWaitingPlayersMap) {
        setTitle("Login Page");
        setSize(750, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel backgroundPanel = new BackgroundImageSetter("src/main/resources/it/polimi/GC13/view/GUI/backgrounds/CodexLogoTitle.png");
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
        loginButton.addActionListener(e -> {
            String nickname = nicknameField.getText();
            String gameName = map.get((String) comboBox.getSelectedItem());

            frameManager.getVirtualServer().sendMessageFromClient(new AddPlayerToGameMessage(nickname, gameName));
            frameManager.setNickname(nickname);
            if (e.getActionCommand().equals("Start")) {
                createLobby();
                getContentPane().revalidate();
                getContentPane().repaint();
            }
        });

        playersNumPanel.add(loginButton, gbcPlayersNumElements);
        panelContainer.add(playersNumPanel);

        setVisible(true);
    }

    /**
     * Enables the login button based on the filled fields and selected radio button.
     */
    private void enableLoginButton() {
        boolean fieldsFilled = !nicknameField.getText().isEmpty() && !gameNameField.getText().isEmpty();
        boolean radioButtonSelected = radiobuttons.stream().anyMatch(AbstractButton::isSelected);
        loginButton.setEnabled(fieldsFilled && radioButtonSelected);
    }

    /**
     * Enables the login button for joining an existing game based on the filled nickname field.
     */
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
        setBorderInsets(label2,30,0,0,0);
        panelContainer.add(label2, createGridBagConstraints(0,0));

        GridBagConstraints gbc = createGridBagConstraints(0,1);
        gbc.anchor = GridBagConstraints.LINE_START;
        waitingLabel = createTextLabelFont("", 20);
        setBorderInsets(waitingLabel,10,0,15,0);
        panelContainer.add(waitingLabel, gbc);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300,30));
        progressBar.setForeground(colors.get(colorIndex));
        colorIndex++;
        panelContainer.add(progressBar, createGridBagConstraints(0,2));

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

    /**
     * Creates a JLabel with the specified text and font size.
     *
     * @param content The text content of the label
     * @param dim The font size
     * @return JLabel instance with the specified text and font
     */
    private JLabel createTextLabelFont(String content, int dim) {
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return jLabel;
    }

    /**
     * Creates a JButton with the specified text and font size.
     *
     * @param text The text content of the button
     * @param dim The font size
     * @return JButton instance with the specified text and font
     */
    private JButton createButton(String text, int dim){
        JButton button = new JButton(text);
        button.setFont(new Font("Old English Text MT", Font.BOLD, dim)); //PlainGermanica
        return button;
    }

    /**
     * Creates GridBagConstraints with the specified grid coordinates.
     *
     * @param x The x-coordinate in the grid
     * @param y The y-coordinate in the grid
     * @return GridBagConstraints instance with the specified coordinates
     */
    private GridBagConstraints createGridBagConstraints(int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;

        return gbc;
    }

    /**
     * Sets insets for the border of a JComponent.
     *
     * @param jComponent The JComponent to set the border insets for
     * @param insetsTop The top inset value
     * @param insetsLeft The left inset value
     * @param insetsBottom The bottom inset value
     * @param insetsRight The right inset value
     */
    private void setBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        jComponent.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    /**
     * Sets up a radio button with the specified text, adds it to the panel and button group,
     * and attaches an ActionListener.
     *
     * @param arrayButton The ArrayList to store radio buttons
     * @param panel The JPanel to add the radio button to
     * @param buttonGroup The ButtonGroup to add the radio button to
     * @param text The text content of the radio button
     * @param textRadioListener The ActionListener for the radio button
     */
    private void setRadioButton(ArrayList<JRadioButton> arrayButton, JPanel panel, ButtonGroup buttonGroup, String text, ActionListener textRadioListener){
        JRadioButton radioButton = new JRadioButton(text);
        buttonGroup.add(radioButton);
        arrayButton.add(radioButton);
        radioButton.setOpaque(false);
        radioButton.addActionListener(textRadioListener);
        radioButton.addActionListener(this);
        panel.add(radioButton);
    }

    /**
     * Handles ActionListener events for buttons and radio buttons in the frame.
     *
     * @param e The ActionEvent triggered by user interaction
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String nickname = nicknameField.getText();
        String gameName = gameNameField.getText();
        Object source = e.getSource();
        //boolean flag = false;

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

    /**
     * Retrieves the waiting label used in the lobby view.
     *
     * @return The JLabel instance used to display waiting messages
     */
    public JLabel getWaitingLabel() {
        return waitingLabel;
    }
}
