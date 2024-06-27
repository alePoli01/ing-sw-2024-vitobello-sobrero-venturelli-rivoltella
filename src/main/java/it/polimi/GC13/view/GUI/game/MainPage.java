package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.ChatMessage;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.network.messages.fromclient.*;
import it.polimi.GC13.view.GUI.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the main graphical user interface (GUI) page for the game "Codex Naturalis".
 * <p></p>
 * Extends {@link JFrame} and implements {@link ActionListener}, {@link CardManager}, {@link WaitingLobby} interfaces.
 * <p>
 *     <br>
 *     This page handles the <b>setup phase</b> <i>(<u>token selection</u>, <u>starter card selection</u>, <u>objective card selection</u>)</i>
 *     and the <b>mid-game phase</b>, and various game interactions.
 * </p>
 */
public class MainPage extends JFrame implements ActionListener, CardManager, WaitingLobby {

    //record(s)
    /**
     *A record that encapsulates a JLabel and a corresponding JCheckBox.
     *
     * @param label The JLabel containing the ImageIcon of a card.
     * @param checkBox The JCheckBox associated with the JLabel.
     */
    public record CardData(JLabel label, JCheckBox checkBox) {}

    //Manager of interaction between server and game
    private FrameManager frameManager;

    //setting user parameters
    private String nickname;
    private TokenColor token;
    private List<TokenColor> tokenColorList;

    //panels and tables in the game
    private final JPanel panelContainer;
    private JPanel southPanel;
    private final JPanel choosePanel;
    private JPanel imagePanel;
    private JPanel namePanel;
    private JPanel checkBoxPanel;
    private JPanel commonObjCardPanel;
    private final JTable resourceTable = new JTable(); //table in the Game page
    private JTable scoreTable; //table in the ScoreBoard page

    //Game board attributes
    private JLayeredPane board;
    private final Map<Coordinates, JButton> boardButtonMap = new HashMap<>();
    public String boardToDisplay;

    //parameters to send to the server
    private boolean flipToSend = false;
    private int serialToSend = -1;
    private Coordinates cordToSend = null;


    //HashMap and ButtonGroup
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final ButtonGroup avatarButtonGroup = new ButtonGroup(); //for the users' avatar
    private final Map<String, String> playersAvatarMap = new HashMap<>();


    //hand attributes
    private final Map<JLabel, JCheckBox> handLabelCheckBoxMap = new HashMap<>();;
    private final Map<Integer, CardData> handSerialNumberCheckBoxMap = new HashMap<>();
    private final Map<JLabel, JCheckBox> playerCheckBoxMap = new HashMap<>();

    //decks attributes
    private final ButtonGroup buttonGroupDecks = new ButtonGroup();
    private final Map<JLabel, JCheckBox> decksLabelCheckBoxMap = new HashMap<>();
    private final Map<Integer, CardData> drawableSerialNumberCheckBoxMap = new HashMap<>();

    //buttons
    private JButton confirmButton;
    private JButton flipButton;
    private JButton decksButton;

    //attributes for the waiting Lobby
    private Timer timer;
    int progress = 0;
    int colorIndex = 0;

    //GamePanel and ScoreBoardPanel
    final static String PANEL1 = "Game";
    final static String PANEL2 = "Scoreboard";

    //Flags
    private boolean checkingHandWhileDrawing = false;
    private boolean boardButtonFlag = true;
    private boolean unableExceptionFlag = false;

    //Labels
    private JLabel waitingLabel;
    private JLabel turnLabel;
    private JLabel turnLabel2;
    private JLabel scoreLabel;
    private JLabel scoreLabel2;
    private JLabel numAttemptLabel;
    private JLabel resourcePlayerLabel;

    //management of the movements of tokens on the ScoreBoard
    private TokenManager tokenManager;

    //chat
    private JComboBox<JLabel> combobox;
    private JEditorPane chatArea;
    private JTextField messageField;
    private JLabel notifyLabel;
    private final Map<String, ArrayList<Boolean>> newMessageMap = new HashMap<>();


    /**
     * Constructs a new {@code MainPage} with the specified list of token colors.
     *
     * @param tokenColorList The list of token colors available for selection
     */
    public MainPage(List<TokenColor> tokenColorList) {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        try {
            Image icon = ImageIO.read(new File("src/main/resources/it/polimi/GC13/view/GUI/backgrounds/logo.png"));
            int newWidth = 1000;
            int newHeight = 1000;
            setIconImage(icon.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
        }

        panelContainer = new JPanel(new GridBagLayout());
        add(panelContainer);

        JLabel setupLabel = createTextLabelFont("setup phase [1/3] ", 28);
        setBorderInsets(setupLabel, 0, 0, 60, 0);
        panelContainer.add(setupLabel, createGridBagConstraints(0, 0));


        JLabel tokenLabel = createTextLabelFont("Choose your token: ", 64);
        setBorderInsets(tokenLabel, 0, 0, 90, 0);
        panelContainer.add(tokenLabel, createGridBagConstraints(0, 1));


        choosePanel = new JPanel(new GridBagLayout());
        choosePanel.setOpaque(false);
        panelContainer.add(choosePanel, createGridBagConstraints(0,2));

        showTokenChoose(tokenColorList);

        confirmButton = createButton("Select", 32);
        confirmButton.setEnabled(false);
        //confirmButton.setBackground(new Color(232, 221, 183, 255));
        panelContainer.add(confirmButton, createGridBagConstraints(0, 3));

        confirmButton.addActionListener (e -> {
            String tokenColorChosen;
            for (JCheckBox checkBox : handLabelCheckBoxMap.values()) {
                if (checkBox.isSelected()) {
                    tokenColorChosen = checkBox.getText();
                    if (e.getActionCommand().equals("Select")) {
                        frameManager.getVirtualServer().sendMessageFromClient(new TokenChoiceMessage(TokenColor.valueOf(tokenColorChosen.toUpperCase())));
                        confirmButton.setEnabled(false);
                    }
                    break;
                }
            }
        });
        setVisible(true);
    }

    /**
     * Displays the token selection options based on the provided list of token colors.
     *
     * @param tokenColorList The list of token colors available for selection
     */
    public void showTokenChoose(List<TokenColor> tokenColorList){
        GridBagConstraints gbc2 = createGridBagConstraints(0, 0);
        imagePanel = new JPanel(new FlowLayout()); //pannello dove inserire le icone dei token
        imagePanel.setOpaque(false);
        choosePanel.add(imagePanel, gbc2);

        checkBoxPanel = new JPanel(new FlowLayout());
        checkBoxPanel.setOpaque(false);
        choosePanel.add(checkBoxPanel, gbc2);

        namePanel = new JPanel(new FlowLayout()); //pannello dove inserire i nomi dei token
        namePanel.setOpaque(false);
        choosePanel.add(namePanel, createGridBagConstraints(0, 1));

        setTokenColorList(tokenColorList);
    }

    /**
     * Sets the list of token colors available for selection, and print the token Images on the GUI.
     * <p>
     *     If the token is already taken, a grey token is printed.
     * </p>
     *
     * @param tokenColorList The list of token colors available for selection
     */
    public void setTokenColorList(List<TokenColor> tokenColorList) {
        this.tokenColorList = tokenColorList;

        Arrays.stream(TokenColor.values()).forEach( tokenColor -> {
            JLabel tokenLabelImage = new JLabel((this.tokenColorList.contains(tokenColor)) ? createPlayableTokenImageIcon(tokenColor, 300) : createGreyTokenImageIcon(300));
            imagePanel.add(tokenLabelImage);

            JLabel tokenLabelText = createTextLabelFont(tokenColor.toString().toLowerCase(), 32);
            setBorderInsets(tokenLabelText, 30, 108, 80, 125);
            namePanel.add(tokenLabelText);

            JCheckBox jCheckBox = new JCheckBox(tokenColor.toString().toLowerCase());
            jCheckBox.setFocusPainted(false);
            jCheckBox.setBorderPainted(false);
            setBorderInsets(jCheckBox, 120, 120, 120, 120);
            buttonGroup.add(jCheckBox);
            jCheckBox.setOpaque(false);

            checkBoxPanel.add(jCheckBox);
            handLabelCheckBoxMap.put(tokenLabelText, jCheckBox);

            if(!this.tokenColorList.contains(tokenColor)){
                jCheckBox.setEnabled(false);
                tokenLabelText.setForeground(Color.GRAY);
                handLabelCheckBoxMap.remove(tokenLabelText, jCheckBox);
            } else {
                jCheckBox.addActionListener(e -> {
                    confirmButton.setEnabled(handLabelCheckBoxMap.values().stream().anyMatch(AbstractButton::isSelected));

                    handLabelCheckBoxMap.keySet().forEach(k -> k.setForeground(Color.BLACK));

                    handLabelCheckBoxMap.entrySet()
                            .stream()
                            .filter(en -> en.getValue().equals(e.getSource()))
                            .findFirst()
                            .orElseThrow()
                            .getKey()
                            .setForeground(Color.RED);
                });
            }
        });
    }

    /**
     * Gets the filename of the token image based on the token color.
     *
     * @param tokenColor The token color
     * @return The filename of the token image
     */
    private String getTokenFileName(TokenColor tokenColor) {
        return tokenColor.toString().toLowerCase() + TOKEN_FILE_SUFFIX;
    }

    /**
     * Creates a resized ImageIcon from a token image path.
     *
     * @param tokenImagePath The path to the token image
     * @param dim The desired dimension (width or height) of the resized image
     * @return The resized ImageIcon
     */
    private static ImageIcon createResizedTokenImageIcon(String tokenImagePath, int dim) {
        return new ImageIcon(new ImageIcon(tokenImagePath).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
    }

    /**
     * Creates a playable token image icon based on the provided TokenColor and dimension.
     *
     * @param tokenColor The TokenColor enum representing the color of the token
     * @param dim The desired dimension (width or height) of the token image icon
     * @return The ImageIcon representing the playable token
     */
    private ImageIcon createPlayableTokenImageIcon(TokenColor tokenColor, int dim) {
        return createResizedTokenImageIcon(P_TOKEN_DIR + getTokenFileName(tokenColor), dim);
    }

    /**
     * Creates a grey token image icon with the specified dimension.
     *
     * @param dim The desired dimension (width or height) of the grey token image icon
     * @return The ImageIcon representing the grey token
     */
    private ImageIcon createGreyTokenImageIcon(int dim) {
        return createResizedTokenImageIcon(TOKEN_DIR + GREY_TOKEN_FILE_NAME + TOKEN_FILE_SUFFIX, dim);
    }

    /**
     * Creates a black token image icon with the specified dimension.
     *
     * @param dim The desired dimension (width or height) of the black token image icon
     * @return The ImageIcon representing the black token
     */
    private ImageIcon createBlackTokenImageIcon(int dim) {
        return createResizedTokenImageIcon(TOKEN_DIR + BLACK_TOKEN_FILE_NAME + TOKEN_FILE_SUFFIX, dim);
    }

    /**
     * Handle the starter card selection during the game setup phase.
     * This method includes displaying options for selecting starter cards and handling user interactions.
     */
    public void startCardSetup(){
        JLabel setupLabel = createTextLabelFont("setup phase [2/3] ", 28);
        setBorderInsets(setupLabel, 0, 0, 60, 0);
        panelContainer.add(setupLabel, createGridBagConstraints(0, 0));

        JLabel titleLabel = createTextLabelFont("Select Starter Card: ", 64);
        setBorderInsets(titleLabel, 0, 0, 60, 0);
        panelContainer.add(titleLabel, createGridBagConstraints(0, 1));

        JLabel startCardLabel = createTextLabelFont("Choose which side you would like to place your start card: ", 32);
        setBorderInsets(startCardLabel, 0, 0, 50, 0);
        panelContainer.add(startCardLabel, createGridBagConstraints(0, 2));


        panelContainer.add(choosePanel, createGridBagConstraints(0,3));
        namePanel.removeAll();
        refresh();

        showStarterCardAndPrivateObjectiveCard(frameManager.getHand());

        confirmButton.setText("Confirm");
        //confirmButton.setBackground(new Color(232, 221, 183, 255));
        panelContainer.add(confirmButton, createGridBagConstraints(0, 4));
        confirmButton.addActionListener (e -> {
            boolean isFlipped;
            if(handLabelCheckBoxMap.values().stream().noneMatch(c->c.getText().equals("error"))) {
                for(JCheckBox checkBox : handLabelCheckBoxMap.values()) {
                    if(checkBox.isSelected()) {
                        isFlipped = checkBox.getText().equals("true");
                        if(e.getActionCommand().equals("Confirm")) {
                            frameManager.getVirtualServer().sendMessageFromClient(new PlaceStartCardMessage(isFlipped));
                            confirmButton.setEnabled(false);
                            createLobby();
                            getContentPane().revalidate();
                            getContentPane().repaint();
                        }
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,"Invalid card", "ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Refreshes the display by clearing the Components and the structures (tokenPanel, checkBoxPanel, handLabelCheckBoxMap and ButtonGroup).
     */
    public void refresh(){
        imagePanel.removeAll();
        checkBoxPanel.removeAll();
        handLabelCheckBoxMap.clear();

        Enumeration<AbstractButton> checkBoxes = buttonGroup.getElements();
        while (checkBoxes.hasMoreElements()) {
            AbstractButton button = checkBoxes.nextElement();
            if (button instanceof JCheckBox) {
                buttonGroup.remove(button);
            }
        }
    }

    /**
     * Identifies the path for a specific card number based on predefined directories.
     *
     * @param numberCard The number of the card to identify the path for
     * @return The Path object representing the directory of the card image files, or null if not found
     */
    public Path identifyPathCard(int numberCard){
        Path startDir;
        try {
            if (numberCard > 0) {
                if (numberCard <= 40)
                    startDir = Paths.get(RESOURCE_DIR);
                else if (numberCard <= 80)
                    startDir = Paths.get(GOLD_DIR);
                else if (numberCard <= 86)
                    startDir = Paths.get(STARTER_DIR);
                else if((numberCard <= 102))
                    startDir = Paths.get(OBJECTIVE_DIR);
                else {
                    JOptionPane.showMessageDialog(this,"Invalid card", "ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            } else {
                JOptionPane.showMessageDialog(this,"Invalid card", "ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (InvalidPathException e) {
            JOptionPane.showMessageDialog(this,"Invalid card", "ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return startDir;
    }

    /**
     * Method used to display the starter cards and private objective card selection options on the GUI.
     *
     * @param numberCards A list of integers representing the card numbers
     */
    @Override
    public void showStarterCardAndPrivateObjectiveCard(List<Integer> numberCards) {
        AtomicBoolean b = new AtomicBoolean(true);
        numberCards.forEach( numberCard -> {
            Path startDir = identifyPathCard(numberCard);
            if(startDir != null){
                try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().contains(numberCard.toString())).filter(numberCard > 86 ? f -> f.getFileName().toString().contains("front") : Path::isAbsolute)) {
                    paths.forEach(path -> {
                        Image img = new ImageIcon(String.valueOf(path)).getImage();
                        JLabel startCardLabelImage = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/3 , img.getHeight(null)/3, Image.SCALE_SMOOTH)));
                        setCompoundBorderInsets(startCardLabelImage, 0, 100, 0, 100, "ALL", Color.BLACK, 1);
                        imagePanel.add(startCardLabelImage);

                        JLabel startCardLabelText;
                        JCheckBox jCheckBox;

                        if (numberCard > 86) { //objective
                            if(numberCard<=94){
                                startCardLabelText = createTextLabelFont("Pattern", 32);
                            }else if(numberCard<=98){
                                startCardLabelText = createTextLabelFont("Tris Reign", 32);
                            }else {
                                startCardLabelText = createTextLabelFont("Objects", 32);
                            }
                            jCheckBox = new JCheckBox(numberCard.toString());
                        } else { //starter
                            if(String.valueOf(path).contains("front")){
                                startCardLabelText = createTextLabelFont("Front", 32);
                                jCheckBox = new JCheckBox("false");
                            } else {
                                startCardLabelText = createTextLabelFont("Back", 32);
                                jCheckBox = new JCheckBox("true");
                            }
                        }

                        setBorderInsets(startCardLabelText, 30, 230, 80, 230);
                        namePanel.add(startCardLabelText);

                        jCheckBox.setFocusPainted(false);
                        jCheckBox.setBorderPainted(false);
                        buttonGroup.add(jCheckBox);
                        setBorderInsets(jCheckBox, 100, 150, 100, 150);
                        jCheckBox.setOpaque(false);
                        jCheckBox.addActionListener(e -> {
                            confirmButton.setEnabled(handLabelCheckBoxMap.values().stream().anyMatch(AbstractButton::isSelected));

                            handLabelCheckBoxMap.keySet().forEach(k -> k.setForeground(Color.BLACK));

                            handLabelCheckBoxMap.entrySet()
                                    .stream()
                                    .filter(en -> en.getValue().equals(e.getSource()))
                                    .findFirst()
                                    .orElseThrow()
                                    .getKey()
                                    .setForeground(Color.RED);
                        });
                        checkBoxPanel.add(jCheckBox);

                        if(jCheckBox.getText().equals("true") || b.get()){
                            JPanel emptyPanel = new JPanel();
                            emptyPanel.setOpaque(false);
                            setBorderInsets(emptyPanel, 1, 85, 1, 85);
                            checkBoxPanel.add(emptyPanel);
                            b.set(false);
                        }
                        handLabelCheckBoxMap.put(startCardLabelText, jCheckBox);
                    });

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
                }
            } else { //gestione errore path
                for(int i = 0; i < 2; i++){
                    Image img = new ImageIcon(ERROR_CARD).getImage();
                    JLabel startCardLabelImage = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/15, img.getHeight(null)/15, Image.SCALE_SMOOTH)));
                    setCompoundBorderInsets(startCardLabelImage, 0, 100, 0, 100, "ALL", Color.BLACK, 1);
                    imagePanel.add(startCardLabelImage);

                    JLabel startCardLabelText = createTextLabelFont("Error", 32);
                    JCheckBox jCheckBox = new JCheckBox("error");

                    setBorderInsets(startCardLabelText, 15, 230, 40, 230);
                    namePanel.add(startCardLabelText);

                    jCheckBox.setFocusPainted(false);
                    jCheckBox.setBorderPainted(false);
                    buttonGroup.add(jCheckBox);
                    setBorderInsets(jCheckBox, 100, 150, 100, 150);
                    jCheckBox.setOpaque(false);
                    jCheckBox.addActionListener(e -> {
                        confirmButton.setEnabled(handLabelCheckBoxMap.values().stream().anyMatch(AbstractButton::isSelected));

                        handLabelCheckBoxMap.keySet().forEach(k -> k.setForeground(Color.BLACK));

                        handLabelCheckBoxMap.entrySet()
                                .stream()
                                .filter(en -> en.getValue().equals(e.getSource()))
                                .findFirst()
                                .orElseThrow()
                                .getKey()
                                .setForeground(Color.RED);
                    });
                    checkBoxPanel.add(jCheckBox);

                    if(jCheckBox.getText().equals("error") || b.get()){
                        JPanel emptyPanel = new JPanel();
                        emptyPanel.setOpaque(false);
                        setBorderInsets(emptyPanel, 1, 85, 1, 85);
                        checkBoxPanel.add(emptyPanel);
                        b.set(false);
                    }
                    handLabelCheckBoxMap.put(startCardLabelText, jCheckBox);
                }
            }
        });
    }

    /**
     * Displays the common objective cards on a specified panel with a given divider for scaling.
     *
     * @param commonObjectiveCards A list of integers representing the card numbers
     * @param panel The JPanel where the common objective cards will be displayed
     * @param divider The divider used for scaling the card images
     */
    public void showCommonObjectiveCard(List<Integer> commonObjectiveCards, JPanel panel, int divider) {
        commonObjectiveCards.forEach( numberCard -> {
            Path startDir = identifyPathCard(numberCard);

            if(startDir != null){
                try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().contains(numberCard.toString())).filter(f -> f.getFileName().toString().contains("front"))) {
                    paths.forEach(path -> {
                        Image img = new ImageIcon(String.valueOf(path)).getImage();
                        JLabel startCardLabelImage = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null) / divider, img.getHeight(null) / divider, Image.SCALE_SMOOTH)));
                        setCompoundBorderInsets(startCardLabelImage, 0, 100, 30, 100, "ALL", Color.BLACK, 1);
                        panel.add(startCardLabelImage);
                    });
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,"Invalid card", "ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
                for(int i = 0; i<2; i++){
                    Image img = new ImageIcon(ERROR_CARD).getImage();
                    JLabel startCardLabelImage = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null) / divider, img.getHeight(null) / divider, Image.SCALE_SMOOTH)));
                    setCompoundBorderInsets(startCardLabelImage, 0, 100, 30, 100, "ALL", Color.BLACK, 1);
                    panel.add(startCardLabelImage);
                }
            }
        });
    }


    @Override
    public void createLobby(){
        panelContainer.removeAll();
        ArrayList<Color> colors = new ArrayList<>();

        panelContainer.setLayout(new GridBagLayout());
        //panelContainer.setBackground(new Color(237,230,188,255));

        for(int i=0; i<4; i++) {
            colors.add(new Color(107, 189, 192));
            colors.add(new Color(233, 73, 23));
            colors.add(new Color(113, 192, 124));
            colors.add(new Color(171, 63, 148));
        }

        JLabel label2 = createTextLabelFont("Waiting for the players: ", 60);
        setBorderInsets(label2,30,0,20,0);
        panelContainer.add(label2, createGridBagConstraints(0,0));

        waitingLabel = createTextLabelFont("", 30);
        setBorderInsets(waitingLabel,10,0,15,0);
        panelContainer.add(waitingLabel, createGridBagConstraints(0,1));

        JProgressBar progressBar = new JProgressBar(0, 100);
        setBoxComponentSize(progressBar,600,30);
        progressBar.setForeground(colors.get(colorIndex));
        colorIndex++;
        panelContainer.add(progressBar, createGridBagConstraints(0,2));

         timer = new Timer(100, e -> {
             progress += 5;
             if (progress > 100) {
                 progress = 0;
                 progressBar.setForeground(colors.get(colorIndex));
                 colorIndex = (colorIndex + 1) % colors.size();
             }
             progressBar.setValue(progress);
         });
        timer.start();
    }

    /**
     * Sets up the GUI for selecting objective cards during the game setup phase.
     * This method includes displaying common objective cards and allowing the player to choose their private objective card.
     *
     * @param privateObjectiveCards A list of integers representing the private objective card options
     */
    public void setupObjectiveCard(List<Integer> privateObjectiveCards){
        JLabel setupLabel = createTextLabelFont("setup phase [3/3] ", 28);
        setBorderInsets(setupLabel, 20, 0, 20, 0);
        panelContainer.add(setupLabel, createGridBagConstraints(0, 0));

        JLabel titleLabel1 = createTextLabelFont("Setup Objective Card: ", 64);
        setBorderInsets(titleLabel1, 0, 0, 30, 0);
        panelContainer.add(titleLabel1, createGridBagConstraints(0, 1));

        JLabel startCardLabel = createTextLabelFont("These are the Common Objective Cards: ", 32);
        setBorderInsets(startCardLabel, 0, 0, 40, 0);
        panelContainer.add(startCardLabel, createGridBagConstraints(0, 2));

        commonObjCardPanel = new JPanel(new FlowLayout());
        commonObjCardPanel.setOpaque(false);
        panelContainer.add(commonObjCardPanel, createGridBagConstraints(0, 3));

        showCommonObjectiveCard(frameManager.getSerialCommonObjectiveCard(), commonObjCardPanel, 3);

        JLabel titleLabel2 = createTextLabelFont("Choose your private objective card: ", 32);
        setBorderInsets(titleLabel2, 0, 0, 30, 0);
        panelContainer.add(titleLabel2, createGridBagConstraints(0, 4));

        panelContainer.add(choosePanel, createGridBagConstraints(0,5));
        namePanel.removeAll();
        refresh();

        showStarterCardAndPrivateObjectiveCard(privateObjectiveCards);

        confirmButton.setText("Play");
        //confirmButton.setBackground(new Color(232, 221, 183, 255));
        JPanel panelButton = new JPanel();
        panelButton.setOpaque(false);
        panelButton.add(confirmButton);
        setBorderInsets(panelButton, 210,10,0, 10); // <---
        panelContainer.add(panelButton, createGridBagConstraints(0, 5));
        confirmButton.addActionListener (e -> {
            int choice;
            if(handLabelCheckBoxMap.values().stream().noneMatch(c->c.getText().equals("error"))) {
                for(JCheckBox checkBox : handLabelCheckBoxMap.values()) {
                    if(checkBox.isSelected()) {
                        choice = Integer.parseInt(checkBox.getText());
                        if(e.getActionCommand().equals("Play")) {
                            frameManager.getVirtualServer().sendMessageFromClient(new ChoosePrivateObjectiveCardMessage(choice));
                            createLobby();
                            getContentPane().revalidate();
                            getContentPane().repaint();
                        }
                        break;
                    }
                }
            } //TODO: da gestire il caso di errore
        });
    }



    /**
     * Creates and sets up the game panel for the game.
     * <p>
     *     This method initializes two main panels using CardLayout:
     *     <ul>
     *         <li><i>Panel 1</i>: Contains the main game components such as the game board, player information,
     *         common and private objective cards, resource table, and control buttons.</li>
     *         <li><i>Panel 2</i>: Contains the scoreboard, score table and the chat functionality.</li>
     *     </ul>
     * </p>
     * <p>
     *     <b>Panel 1 Details:</b>
     *     <ul>
     *         <li><i>North Panel</i>: Contains buttons, player turn information, token display, and score.</li>
     *         <li><i>Board</i>: JLayeredPane displaying the game board with a scrollable view.</li>
     *         <li><i>West Panel</i>: Displays player avatar and additional player-specific information.</li>
     *         <li><i>East Panel</i>: Displays common and private objective cards, player resource table,
     *         and control buttons for decks.</li>
     *         <li> <i>South Panel</i>: Contains confirmation buttons and additional controls related to card flipping.</li>
     *     </ul>
     * </p>
     * <p>
     *     <b>Panel 2 Details:</b>
     *     <ul>
     *         <li><i>North Panel</i>: Contains buttons to navigate back to the game, player turn information,
     *         token display, and score.</li>
     *         <li><i>Token Manager</i>: Manages tokens in the game, displaying them according to player positions.</li>
     *         <li>Right (East) Panel: Displays a table of player positions and their respective tokens.</li>
     *         <li><i>Chat Panel</i>: Allows selection of chat recipients, displays chat history, and provides
     *         input field and send button for sending messages.</li>
     *     </ul>
     *</p>
     * Both panels include necessary event listeners and methods for handling user interactions.
     */
    public void createGamePanel() {
        panelContainer.setLayout(new CardLayout());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setOpaque(false);

        JPanel panel2 = new JPanel(new BorderLayout());
        //panel2.setBackground(new Color(237,230,188,255));

        panelContainer.add(panel1, PANEL1);
        panelContainer.add(panel2, PANEL2);


        //pagina 1: Game
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
        setBorderInsets(northPanel, 15, 0, 15, 0);

        JButton buttonToScoreBoard = createButton("Go to scoreboard", 20);
        //buttonToScoreBoard.setBackground(new Color(232, 221, 183, 255));
        buttonToScoreBoard.addActionListener(this);
        setBoxComponentSize(buttonToScoreBoard, 180, 50);
        northPanel.add(buttonToScoreBoard);

        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(Box.createHorizontalGlue());

        JPanel turnNamePanel = new JPanel();
        turnNamePanel.setOpaque(false);
        turnNamePanel.setLayout(new BoxLayout(turnNamePanel, BoxLayout.Y_AXIS));
        JLabel labelNickname = createTextLabelFont(nickname, 32);
        turnNamePanel.add(labelNickname);
        turnLabel = createTextLabelFont("", 20);
        turnNamePanel.add(turnLabel);
        northPanel.add(turnNamePanel);

        JLabel tokenLabel = new JLabel(createPlayableTokenImageIcon(token, 50));
        northPanel.add(tokenLabel);

        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(Box.createHorizontalGlue());

        scoreLabel = createTextLabelFont("", 30);
        setBorderInsets(scoreLabel, 0,0,0,30);
        northPanel.add(scoreLabel);

        panel1.add(northPanel, BorderLayout.NORTH);


        board = new JLayeredPane();
        int boardHeight = 11220;
        int boardWidth = 16830;
        board.setPreferredSize(new Dimension(boardWidth, boardHeight));

        addScrollPane(board, panel1, 250, 285);

        //System.out.println("debug  "+frameManager.playersBoard.get(nickname).Board[50][50].getCardPointer().serialNumber);
        refreshBoard();

        panel1.setVisible(true);


        JPanel westPanel = new JPanel(new GridBagLayout());
        setBoxComponentSize(westPanel, 150, westPanel.getHeight());

        setPlayerAvatar(westPanel);
        panel1.add(westPanel, BorderLayout.WEST);


        JPanel eastPanel = new JPanel(new GridBagLayout());
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        setBoxComponentSize(eastPanel, 200,eastPanel.getHeight());

        commonObjCardPanel.removeAll();
        commonObjCardPanel.setLayout(new BoxLayout(commonObjCardPanel, BoxLayout.Y_AXIS));
        JLabel labelCommonObjectiveCards = createTextLabelFont("Common Objective Cards", 16);
        setBorderInsets(labelCommonObjectiveCards, 0,0,10,0);
        commonObjCardPanel.add(labelCommonObjectiveCards);

        showCommonObjectiveCard(frameManager.getSerialCommonObjectiveCard(), commonObjCardPanel, 10);

        JLabel labelPrivateObjectiveCards = createTextLabelFont("Private Objective Cards", 16);
        setBorderInsets(labelPrivateObjectiveCards, 0,0,10,0);
        commonObjCardPanel.add(labelPrivateObjectiveCards);

        List<Integer> privateObjectiveCard = List.of(frameManager.getSerialPrivateObjectiveCard());

        showCommonObjectiveCard(privateObjectiveCard, commonObjCardPanel, 10);

        eastPanel.add(commonObjCardPanel, createGridBagConstraints(0,0));

        String name = String.format("<span style = 'color: rgb(%d, %d, %d);'>" + this.nickname + "</span>", 55, 133, 9);
        resourcePlayerLabel = createTextLabelFont("<html><div style='white-space: nowrap;'>Table player: " + name + "</div></html>", 16);
        eastPanel.add(resourcePlayerLabel, createGridBagConstraints(0,1));

        JPanel tableResourcePanel = new JPanel();
        addScrollPane(resourceTable, tableResourcePanel, 200, ((resourceTable.getRowCount() + 1) * resourceTable.getRowHeight()) + 5);
        eastPanel.add(tableResourcePanel, createGridBagConstraints(0,2));

        decksButton = createButton("Show Decks", 27);
        decksButton.addActionListener(this);
        eastPanel.add(decksButton, createGridBagConstraints(0,3));

        panel1.add(eastPanel, BorderLayout.EAST);


        southPanel = new JPanel(new GridBagLayout());
        setBorderInsets(southPanel, 10, 0, 10, 0);

        namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));

        confirmButton = createButton("Confirm", 28);
        confirmButton.setEnabled(false);
        //confirmButton.setBackground(new Color(232, 221, 183, 255));
        confirmButton.addActionListener(this);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(10,5));
        flipButton = createButton("Show Back", 28);
        //flipButton.setBackground(new Color(232, 221, 183, 255));
        flipButton.addActionListener(this);

        namePanel.add(confirmButton);
        namePanel.add(emptyPanel);
        namePanel.add(flipButton);

        GridBagConstraints gbc = createGridBagConstraints(0,0);
        gbc.anchor = GridBagConstraints.WEST;

        southPanel.add(namePanel, gbc);
        southPanel.add(choosePanel, createGridBagConstraints(1,0));

        panel1.add(southPanel, BorderLayout.SOUTH);


        //PAGINA 2: SCOREBOARD
        JPanel northPanel2 = new JPanel();
        northPanel2.setLayout(new BoxLayout(northPanel2, BoxLayout.LINE_AXIS));
        setBorderInsets(northPanel2, 15, 0, 15, 0);

        JButton buttonToMainPage = createButton("Go to game", 20);
        setBoxComponentSize(buttonToMainPage, 180,50);
        buttonToMainPage.addActionListener(this);
        northPanel2.add(buttonToMainPage);

        northPanel2.add(Box.createHorizontalGlue());
        northPanel2.add(Box.createHorizontalGlue());

        JPanel turnNamePanel2 = new JPanel();
        turnNamePanel2.setOpaque(false);
        turnNamePanel2.setLayout(new BoxLayout(turnNamePanel2, BoxLayout.Y_AXIS));
        JLabel labelNickname2 = createTextLabelFont(nickname, 32);
        turnNamePanel2.add(labelNickname2);
        turnLabel2 = createTextLabelFont("", 20);
        turnNamePanel2.add(turnLabel2);
        northPanel2.add(turnNamePanel2);

        JLabel tokenLabel2 = new JLabel(createPlayableTokenImageIcon(token, 50));
        northPanel2.add(tokenLabel2);

        northPanel2.add(Box.createHorizontalGlue());
        northPanel2.add(Box.createHorizontalGlue());

        scoreLabel2 = createTextLabelFont("", 30);
        setBorderInsets(scoreLabel2, 0,0,0,30);
        northPanel2.add(scoreLabel2);

        panel2.add(northPanel2, BorderLayout.NORTH);


        tokenManager = new TokenManager();
        tokenManager.setOpaque(false);
        tokenManager.setTokenInGame(frameManager.getTokenInGame());
        tokenManager.initializeDataPlayer();

        panel2.add(tokenManager);


        ArrayList<String> listTokenInGame = new ArrayList<>();
        EnumMap<Position, String> positionPlayerMap = frameManager.getPlayerPositions().entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Position::getIntPosition)))
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey,
                        (e1, e2) -> e1,
                        () -> new EnumMap<>(Position.class)));


        for(Map.Entry<Position, String> entry : positionPlayerMap.entrySet()){
            listTokenInGame.add(P_TOKEN_DIR + getTokenFileName(frameManager.getTokenInGame().get(entry.getValue())));
        }


        JPanel eastPanel2 = new JPanel();
        eastPanel2.setLayout(new BoxLayout(eastPanel2, BoxLayout.Y_AXIS));
        setBoxComponentSize(eastPanel2, 380, eastPanel2.getHeight());

        JPanel tablePanel = new JPanel();
        scoreTable = new JTable();
        createTable(scoreTable, new String[]{"Token", "player"}, positionPlayerMap, listTokenInGame, frameManager.getTokenInGame(), true, 35);

        addScrollPane(scoreTable, tablePanel, 300, (scoreTable.getRowCount() + 1) * scoreTable.getRowHeight() + 5);
        eastPanel2.add(tablePanel);
        panel2.add(eastPanel2, BorderLayout.EAST);


        JPanel chatPanel = new JPanel(new GridBagLayout());
        setBorderInsets(chatPanel, 0,5,0,5);

        combobox = new JComboBox<>();

        combobox.addItem(new JLabel("global"));
        ArrayList<Boolean> emptyChatGlobal = new ArrayList<>();
        emptyChatGlobal.add(false);
        newMessageMap.put("global", emptyChatGlobal);

        for(String s : positionPlayerMap.values()){
            if(!s.equals(nickname)) {
                combobox.addItem(new JLabel(s));
                ArrayList<Boolean> emptyChatPlayer = new ArrayList<>();
                emptyChatPlayer.add(false);
                newMessageMap.put(s, emptyChatPlayer);
            }
        }

        combobox.setRenderer(new CustomComboBoxRenderer(newMessageMap));

        combobox.addActionListener(e -> {
            JLabel selectedLabel = (JLabel)combobox.getSelectedItem();
            if(selectedLabel != null) {
                String selectedPlayer = selectedLabel.getText();
                updateChatArea(selectedPlayer);
            }
        });

        GridBagConstraints gbcCombobox = createGridBagConstraints(0,0);
        gbcCombobox.fill = GridBagConstraints.HORIZONTAL;

        chatPanel.add(combobox, gbcCombobox);

        notifyLabel = new JLabel();
        chatPanel.add(notifyLabel, createGridBagConstraints(1,0));


        chatArea = new JEditorPane();
        chatArea.setContentType("text/html");
        chatArea.setEditorKit(new HTMLEditorKit());
        chatArea.setEditable(false);

        JScrollPane scrollPaneChat = new JScrollPane(chatArea);
        setBoxComponentSize(scrollPaneChat, 300, 400);

        GridBagConstraints gbcChat = createGridBagConstraints(0,1) ;
        gbcChat.gridwidth = 2;
        gbcChat.gridheight = 1;
        gbcChat.fill = GridBagConstraints.HORIZONTAL;
        gbcChat.weightx = 1.0;
        gbcChat.insets = new Insets(5, 5, 5, 5);

        chatPanel.add(scrollPaneChat,gbcChat);


        messageField = new JTextField(20);
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        JScrollPane scrollPaneMessage = new JScrollPane(messageField);
        scrollPaneMessage.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneMessage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneMessage.setPreferredSize(new Dimension(300, 30));

        chatPanel.add(scrollPaneMessage, createGridBagConstraints(0,2));


        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(e -> sendMessage());

        chatPanel.add(sendButton, createGridBagConstraints(1,2));
        eastPanel2.add(chatPanel);


        setVisible(true);
    }

    /**
     * Updates the chat area with messages exchanged with the selected player, and print the unread messages from this one.
     *
     * @param selectedPlayer The player whose chat messages are to be displayed.
     */
    public void updateChatArea(String selectedPlayer) {
        if (selectedPlayer != null) {
            while(newMessageMap.get(selectedPlayer).stream().anyMatch(b -> b)) {
                newMessageMap.get(selectedPlayer).remove(newMessageMap.get(selectedPlayer).getFirst());
                frameManager.setNewMessage(frameManager.getNewMessage() - 1);
                if (frameManager.getNewMessage() <= 0) {
                    frameManager.setNewMessage(0);
                }
            }

            if(newMessageMap.get(selectedPlayer).isEmpty()){
                newMessageMap.get(selectedPlayer).add(false);
            }

            changeComboBoxColor(selectedPlayer);
            changeNotifyLabelImage();

            List<ChatMessage> messages = frameManager.getChat().get(selectedPlayer);
            if (messages != null) {
                StringBuilder content = new StringBuilder("<html>");
                for (ChatMessage msg : messages) {
                    content.append("<b>").append(msg.sender()).append("</b>").append(": ").append(msg.content()).append(";<br>");
                }
                content.append("</html>");
                chatArea.setText(content.toString());
            }else{
                chatArea.setText("");
            }
        }
    }

    /**
     * Sends a message from the current player to the selected player in the JComboBox.
     */
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            JLabel selectedLabel = (JLabel)combobox.getSelectedItem();
            if (selectedLabel != null) {
                String selectedPlayer = selectedLabel.getText();
                frameManager.getVirtualServer().sendMessageFromClient(new NewMessage(this.nickname, selectedPlayer, message));
                messageField.setText("");
            }
        }
    }

    /**
     * Changes the color of the selected player's label in the JComboBox based on
     * whether there are unread messages for that player.
     *
     * @param selectedPlayer The player whose combo box label color is to be changed.
     */
    public void changeComboBoxColor(String selectedPlayer){
        for (int i = 0; i < combobox.getItemCount(); i++) {
            if (combobox.getItemAt(i).getText().equals(selectedPlayer)) {
                if (newMessageMap.get(selectedPlayer).contains(true)) {
                    combobox.getItemAt(i).setForeground(Color.RED); //NON FUNZIONA
                } else {
                    combobox.getItemAt(i).setForeground(UIManager.getColor("ComboBox.foreground"));
                }
            }
        }
    }

    /**
     * Updates the notification label based on the number of unread messages.
     */
    public void changeNotifyLabelImage(){
        if (frameManager.getNewMessage() > 0) {
            if(frameManager.getNewMessage() == 1) {
                notifyLabel.setIcon(createResizedTokenImageIcon(ONE_MESSAGE, 35));
            } else if(frameManager.getNewMessage() == 2){
                notifyLabel.setIcon(createResizedTokenImageIcon(TWO_MESSAGE, 35));
            } else if(frameManager.getNewMessage() == 3){
                notifyLabel.setIcon(createResizedTokenImageIcon(THREE_MESSAGE, 35));
            } else {
                notifyLabel.setIcon(createResizedTokenImageIcon(MANY_MESSAGE, 35));
            }
        } else {
            notifyLabel.setIcon(new ImageIcon(""));
        }
    }

    /**
     * Refreshes the game board display with updated card positions and states.
     */
    public void refreshBoard(){
        board.removeAll();
        boardButtonMap.clear();

        int boardHeight = 11220;
        int boardWidth = 16830;

        //scorri le y
        for (int i = 10; i < 80; i++) {
            //scorri le x
            for (int j = 10; j < 80; j++) {
                if ((i + j) % 2 == 0) {
                    if (frameManager.getPlayersBoard().get(boardToDisplay).Board[i][j] != null) {
                        addImageToLayeredPane(board, getDirectory(frameManager.getPlayersBoard().get(boardToDisplay).Board[i][j].getCardPointer().serialNumber, frameManager.getPlayersBoard().get(boardToDisplay).Board[i][j].isFlipped), boardWidth / 2 + ((j - 50) * 152),boardHeight / 2 + ((i - 50) * 78) , frameManager.getPlayersBoard().get(boardToDisplay).Board[i][j].weight);
                    } else {
                        if (boardToDisplay.equals(nickname)) {
                            for (Coordinates coordinates : frameManager.availableCells) {

                                if (i == coordinates.getY() && j == coordinates.getX()) {
                                    addButtonToLayeredPane(board,boardWidth / 2 + ((j - 50) * 152) , boardHeight / 2 + ((i - 50) * 78));
                                }

                            }
                        }
                    }
                }
            }
        }
        board.revalidate();
        board.repaint();
    }

    /**
     * Displays the hand or decks cards on the GUI.
     * Sorts and displays cards based on the provided map of card serial numbers and their visibility states.
     *
     * @param cardsMap A map of card serial numbers and their visibility states (flipped/unflipped).
     * @param imagePanel The panel where card images are displayed.
     * @param checkBoxPanel The panel where selection checkboxes for cards are displayed.
     * @param container The main container panel where all GUI components are added.
     * @param buttonGroup The button group for checkboxes to ensure single selection behavior.
     * @param labelCheckBoxMap A map linking card image labels to their corresponding selection checkboxes.
     * @param serialNumberCheckBoxMap A map linking card serial numbers to their corresponding CardData objects.
     * @param x The x-coordinate position for GUI layout.
     * @param y The y-coordinate position for GUI layout.
     */
    public void printHandOrDecksOnGUI(Map<Integer, Boolean> cardsMap, JPanel imagePanel, JPanel checkBoxPanel, JPanel container, ButtonGroup buttonGroup, Map<JLabel, JCheckBox> labelCheckBoxMap, Map<Integer, CardData> serialNumberCheckBoxMap, int x, int y){
        Map<Integer, Boolean> sortedCardsMap = cardsMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        Map<Integer, ArrayList<String>> cards = showHandOrDecks(sortedCardsMap.keySet().stream().toList());

        for(Map.Entry<Integer, Boolean> entry : sortedCardsMap.entrySet()){
            String flippedIcon;
            JCheckBox jCheckBox;
            if(!entry.getValue()){
                try {
                    flippedIcon = cards.get(entry.getKey())
                            .stream()
                            .filter(s->s.contains("front"))
                            .findFirst()
                            .orElseThrow();
                } catch(NoSuchElementException noSuchElementException){
                    flippedIcon = CardManager.ERROR_CARD;
                }
            } else {
                try {
                    flippedIcon = cards.get(entry.getKey())
                            .stream()
                            .filter(path -> path.contains("back"))
                            .findFirst()
                            .orElseThrow();
                } catch(NoSuchElementException noSuchElementException) {
                    flippedIcon = CardManager.ERROR_CARD;
                }
            }

            jCheckBox = new JCheckBox(entry.getValue().toString());
            Image img = new ImageIcon(flippedIcon).getImage();

            JLabel imageLabel = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/6 , img.getHeight(null)/6, Image.SCALE_SMOOTH)));
            setCompoundBorderInsets(imageLabel, 0, 30, 0, 30, "ALL", Color.BLACK, 1);
            imagePanel.add(imageLabel);

            jCheckBox.setFocusPainted(false);
            jCheckBox.setBorderPainted(false);
            jCheckBox.setForeground(panelContainer.getBackground());
            buttonGroup.add(jCheckBox);
            setBorderInsets(jCheckBox, 60, 100, 60, 100);
            jCheckBox.setOpaque(false);


            jCheckBox.addItemListener(e -> {
                boolean anySelected = labelCheckBoxMap.values().stream().anyMatch(JCheckBox::isSelected);
                confirmButton.setEnabled(anySelected);
            });

            jCheckBox.addActionListener(e -> {

                cordToSend=null;
                confirmButton.setEnabled(true);
                for (JButton button : boardButtonMap.values()) {
                    button.setIcon(null);
                    button.setBorderPainted(true);
                }
                setButtonVisible();

                labelCheckBoxMap.keySet().forEach(k -> setCompoundBorderInsets(k, 0, 30, 0, 30, "ALL", Color.BLACK, 1));

                setButtonVisible();
                setCompoundBorderInsets(labelCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().equals(e.getSource()))
                        .findFirst()
                        .orElseThrow()
                        .getKey(), 0, 30, 0, 30, "ALL", new Color(255, 240, 1), 4);
            });


            jCheckBox.setEnabled(frameManager.isMyTurn());

            checkBoxPanel.add(jCheckBox);
            checkBoxPanel.setOpaque(false);

            labelCheckBoxMap.put(imageLabel, jCheckBox);

            CardData record = new CardData(imageLabel, jCheckBox);
            serialNumberCheckBoxMap.put(entry.getKey(), record);

            container.add(imagePanel, createGridBagConstraints(x,y));
            container.add(checkBoxPanel, createGridBagConstraints(x,y));

            container.revalidate();
            container.repaint();
        }
    }

    /**
     * Sets all buttons associated with the game board to be visible.
     */
    public void setButtonVisible(){
        if(boardButtonFlag) {
            for(JButton button: boardButtonMap.values()){
                button.setVisible(true);
            }
        }
    }

    /**
     * Sets all buttons associated with the game board to be not visible.
     */
    public void setButtonNOTVisible(){
        for(JButton button: boardButtonMap.values()){
            button.setVisible(false);
        }
    }

    /**
     * Adds a button to the specified layered pane at the given coordinates.
     *
     * @param layeredPane The layered pane to which the button is added.
     * @param x The x-coordinate position of the button.
     * @param y The y-coordinate position of the button.
     */
    private void addButtonToLayeredPane(JLayeredPane layeredPane, int x, int y) {
        JButton button = new JButton();
        button.addActionListener(this);

        Coordinates corner = new Coordinates(((x-(16830/2))/152)+50,((y-(11220/2))/78)+50);
        boardButtonMap.put(corner,button);

        setBoxComponentSize(button, 192, 132);

        button.setVisible(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(30, 30, 30, 255),4));
        button.setBounds(x,y,198,132);//card sizes

        layeredPane.add(button,JLayeredPane.PALETTE_LAYER);
    }

    /**
     * Adds an image to the specified layered pane at the given coordinates and layer.
     *
     * @param layeredPane The layered pane to which the image label is added.
     * @param imagePath   The path to the image file.
     * @param x           The x-coordinate position of the image label.
     * @param y           The y-coordinate position of the image label.
     * @param layer       The layer at which the image is displayed within the layered pane.
     */
    public static void addImageToLayeredPane(JLayeredPane layeredPane, String imagePath, int x, int y, int layer) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(originalIcon.getIconWidth() / 5, originalIcon.getIconHeight() / 5, Image.SCALE_SMOOTH); // Scala l'immagine
        ImageIcon imageIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        imageLabel.setBounds(x, y, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        layeredPane.add(imageLabel, Integer.valueOf(layer));
    }

    /**
     * Retrieves the directory path of a card image based on its serial number and flipped status.
     *
     * @param serialNumber The serial number of the card.
     * @param isFlipped Whether the card is flipped (back side) or not (front side).
     * @return The directory path of the card image.
     */
    public String getDirectory(int serialNumber,boolean isFlipped){
        if (serialNumber>0) {
            if(serialNumber<=40){
                if(!isFlipped){
                    return RESOURCE_DIR+"/resource_card_front/resource_card_front_"+serialNumber+".png";
                }else{
                    return RESOURCE_DIR+"/resource_card_back/resource_card_back_"+serialNumber+".png";
                }
            } else if(serialNumber<=80){
                if(!isFlipped){
                    return GOLD_DIR+"/gold_card_front/gold_card_front_"+serialNumber+".png";
                }else{
                    return GOLD_DIR+"/gold_card_back/gold_card_back_"+serialNumber+".png";
                }
            } else if(serialNumber<=86){
                if(!isFlipped){
                    return STARTER_DIR+"/starter_card_front/starter_card_front_"+serialNumber+".png";
                }else{
                    return STARTER_DIR+"/starter_card_back/starter_card_back_"+serialNumber+".png";
                }
            } else return null;
        } else return null;
    }

    /**
     * Remaps and retrieves the directory path of a resource's logo image based on the provided resource type.
     *
     * @param resource The resource type for which the logo directory path is retrieved.
     * @return The directory path of the resource's logo image.
     */
    public String remappingResources(Resource resource){
        switch (resource){
            case FUNGI -> {
                return FUNGI_LOGO_DIR;
            }
            case ANIMAL -> {
                return ANIMAL_LOGO_DIR;
            }
            case PLANT -> {
                return PLANT_LOGO_DIR;
            }
            case INSECT -> {
                return INSECT_LOGO_DIR;
            }
            case QUILL -> {
                return QUILL_LOGO_DIR;
            }
            case MANUSCRIPT -> {
                return MANUSCRIPT_LOGO_DIR;
            }
            case INKWELL -> {
                return INKWELL_LOGO_DIR;
            }
            default -> {
                return ERROR_FISH;
            }
        }
    }

    /**
     * Creates and configures a JTable with specified columns, data, and rendering options.
     *
     * @param <K>             The type of keys in the EnumMap.
     * @param <V>             The type of values in the EnumMap.
     * @param table           The JTable to be configured.
     * @param columnsNames    Array of column names for the table.
     * @param mapInInput      The EnumMap containing the data to be displayed in the table.
     * @param logosPath       List of paths to logos used in the table.
     * @param conversionMap   Mapping of values to token colors for rendering icons in the table.
     * @param b               Flag indicating if additional columns should be added.
     * @param dim             Dimension of the table rows and header.
     */
    public <K extends Enum<K>, V> void createTable(JTable table, String[] columnsNames, EnumMap<K, V> mapInInput, ArrayList<String> logosPath, Map<V, TokenColor> conversionMap, boolean b, int dim) {
        ImageIconTableModel<K, V> model = new ImageIconTableModel<>(columnsNames, mapInInput, logosPath, conversionMap, dim);

        int k = 0;
        if(b){
            ArrayList<Object> newColumnDataLeft = new ArrayList<>();

            for(int i = 0; i< frameManager.getPlayersScore().size(); i++){
                newColumnDataLeft.add(false);
            }

            ArrayList<Object> newColumnDataRight = new ArrayList<>();

            for (int row = 0; row < model.getRowCount(); row++) {
                String cellValue = (String) model.getValueAt(row, 1);

                for(String playerName : frameManager.getPlayersScore().keySet()) {
                    if (playerName.equals(cellValue)) {
                        newColumnDataRight.add(frameManager.getPlayersScore().get(playerName));
                    }
                }
            }

            model.addColumnOnLeft(" ", newColumnDataLeft);
            model.addColumnOnRight("Score", newColumnDataRight);
            k = 1;
        }

        table.setModel(model);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 0 || i == k) {
                table.getColumnModel().getColumn(i).setCellRenderer(new ImageIconRenderer());
            } else {
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(){
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        c.setFont(new Font("Old English Text MT", Font.PLAIN, 14));
                        setHorizontalAlignment(SwingConstants.CENTER);
                        return c;
                    }
                };
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), dim+5));

        table.setCellSelectionEnabled(false);

        table.setRowHeight(dim+3);
    }

    /**
     * Updates the JTable with new data, reflecting changes in collected resources.
     *
     * @param table             The JTable to be updated.
     * @param collectedResources The EnumMap containing the collected resources and their counts.
     * @param logosPath         List of paths to logos used in the table.
     */
    public void updateResourceTable(JTable table, EnumMap<Resource, Integer> collectedResources, ArrayList<String> logosPath){
        for (Resource resource : collectedResources.keySet()) {
            table.setValueAt(collectedResources.get(resource), logosPath.indexOf(remappingResources(resource)), 1);
        }
    }

    /**
     * Updates the crown image in a table to indicate the player with the highest score.
     * Sets the crown icon next to the player with the maximum score in the table.
     */
    public void updateCrownImageInTable(){
        //posso anche mettere la corona a tutti i giocatori a parimerito, se essi sono meno del numero totale dei giocatori

        String maxScorePlayer = frameManager.getPlayersScore().entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).stream().findFirst().orElseThrow();

        for (int row = 0; row < scoreTable.getModel().getRowCount(); row++) {
            String cellValue = (String) scoreTable.getModel().getValueAt(row, 2);
            if (maxScorePlayer.equals(cellValue)) {
                scoreTable.setValueAt(true, row , 0);
            } else {
                scoreTable.setValueAt(false, row , 0);
            }
        }
    }

    /**
     * Updates the score column in a table based on the current game state.
     * Retrieves and displays the scores of players in the score column of the table.
     */
    public void updateScoreColumn(){
        for (int row = 0; row < scoreTable.getModel().getRowCount(); row++) {
            String cellValue = (String) scoreTable.getModel().getValueAt(row, 2);

            for(String playerName : frameManager.getPlayersScore().keySet()) {
                if (playerName.equals(cellValue)) {
                    scoreTable.setValueAt(frameManager.getPlayersScore().get(playerName), row, 3);
                }
            }
        }
    }

    /**
     * Adds a scroll pane to a component within a panel, configuring its dimensions and behavior.
     * Ensures the scroll pane displays scrollbars as needed and centers the view within the component.
     *
     * @param component The component to add to the scroll pane.
     * @param panel The panel where the scroll pane is added.
     * @param dimW The width dimension of the scroll pane.
     * @param dimH The height dimension of the scroll pane.
     */
    private void addScrollPane(JComponent component, JPanel panel, int dimW, int dimH){
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setPreferredSize(new Dimension(dimW, dimH));
        //aggiungi allo scrollpane la board
        if(component==board){
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            DragScrollListener dragScrollListener = new DragScrollListener(scrollPane);
            board.addMouseListener(dragScrollListener);
            board.addMouseMotionListener(dragScrollListener);
        }
        //aggiungi al panel1 lo scrollpane
        panel.add(scrollPane);
        SwingUtilities.invokeLater(() -> {
            JViewport viewport = scrollPane.getViewport();
            Dimension viewportSize = viewport.getSize();
            Dimension layeredPaneSize = viewport.getViewSize();

            int offsetX = (layeredPaneSize.width - viewportSize.width) / 2;
            int offsetY = (layeredPaneSize.height - viewportSize.height) / 2;

            viewport.setViewPosition(new Point(offsetX, offsetY));
        });
    }

    /**
     * Creates a styled button with specific text and dimensions for use in the application.
     *
     * @param text The text label to display on the button.
     * @param dim The font size dimension for the button text.
     * @return A JButton instance with the specified text and styling.
     */
    private JButton createButton(String text, int dim){
        JButton button = new JButton(text);
        button.setFont(new Font("Old English Text MT", Font.BOLD, dim)); //PlainGermanica
        return button;
    }

    /**
     * Creates and configures GridBag constraints for layout components within a GridBagLayout.
     *
     * @param x The x-coordinate grid position for the component.
     * @param y The y-coordinate grid position for the component.
     * @return Configured GridBagConstraints object for component layout.
     */
    private GridBagConstraints createGridBagConstraints(int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;

        return gbc;
    }

    /**
     * Sets the size dimensions (width and height) of a Swing component.
     *
     * @param component The Swing component to set the size for.
     * @param w The width dimension to set for the component.
     * @param h The height dimension to set for the component.
     */
    private void setBoxComponentSize(JComponent component, int w, int h){
        component.setPreferredSize(new Dimension(w, h));
        component.setMaximumSize(new Dimension(w, h));
        component.setMinimumSize(new Dimension(w, h));
    }

    /**
     * Creates a text label with a specific font and text content for use in the application.
     *
     * @param content The text content to display in the label.
     * @param dim The font size dimension for the label text.
     * @return A JLabel instance with the specified text and font styling.
     */
    private JLabel createTextLabelFont(String content, int dim) {
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return jLabel;
    }

    /**
     * Sets the border insets (top, left, bottom, right) for a Swing component.
     *
     * @param jComponent The Swing component to set the border insets for.
     * @param insetsTop The top inset dimension.
     * @param insetsLeft The left inset dimension.
     * @param insetsBottom The bottom inset dimension.
     * @param insetsRight The right inset dimension.
     */
    private void setBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        jComponent.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    /**
     * Sets compound border insets (top, left, bottom, right) for a Swing component,
     * with customizable border thickness and color.
     *
     * @param jComponent The Swing component to set the compound border insets for.
     * @param insetsTop The top inset dimension.
     * @param insetsLeft The left inset dimension.
     * @param insetsBottom The bottom inset dimension.
     * @param insetsRight The right inset dimension.
     * @param inset The specific border side to apply the compound border.
     * @param color The color of the compound border.
     * @param thickness The thickness of the compound border.
     */
    private void setCompoundBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight, String inset, Color color, int thickness) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        Border b = BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
        switch (inset) {
            case "TOP" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)));
            case "BOTTOM" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)));
            case "ALL" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createLineBorder(color, thickness)));
        }
    }


    /**
     * Handles action events triggered by buttons in the GUI.
     * Performs specific actions based on the action command of the event.
     *
     * @param e The action event generated by a button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cardLayout = (CardLayout) panelContainer.getLayout();
        if (e.getActionCommand().equals("Go to scoreboard")) {
            cordToSend=null;
            confirmButton.setEnabled(false);
            cardLayout.show(panelContainer, PANEL2);
            setButtonNOTVisible();
        } else if (e.getActionCommand().equals("Go to game")) {
            cardLayout.show(panelContainer, PANEL1);
            handLabelCheckBoxMap.keySet().forEach(k -> setCompoundBorderInsets(k, 0, 30, 0, 30, "ALL", Color.BLACK, 1));
            setButtonNOTVisible();
        } else if (e.getActionCommand().equals("Show Back")) {
            cordToSend=null;
            setButtonNOTVisible();

            for (JButton button : boardButtonMap.values()) {
                button.setIcon(null);
                button.setBorderPainted(true);
            }

            confirmButton.setEnabled(false);
            flipButton.setText("Show Front");
            refresh();
            handSerialNumberCheckBoxMap.clear();
            flipToSend = true;

            Map<Integer, Boolean> printMap = new HashMap<>();
            for (Integer integer : frameManager.getHand()) printMap.put(integer, true);

            printHandOrDecksOnGUI(printMap, imagePanel, checkBoxPanel, choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap, 0,0);

        }  else if (e.getActionCommand().equals("Show Front") || e.getActionCommand().equals("Show Hand")) {
            cordToSend=null;
            setButtonNOTVisible();

            for (JButton button : boardButtonMap.values()) {
                button.setIcon(null);
                button.setBorderPainted(true);
            }

            confirmButton.setEnabled(false);
            flipButton.setText("Show Back");
            if (e.getActionCommand().equals("Show Hand")) {
                decksButton.setText("Show Decks");
            }

            refresh();
            refreshDecksPanels();
            handSerialNumberCheckBoxMap.clear();

            Map<Integer, Boolean> printMap = new HashMap<>();
            for (Integer integer : frameManager.getHand()) printMap.put(integer, false);

            printHandOrDecksOnGUI(printMap, imagePanel, checkBoxPanel, choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap, 0,0);

            if(checkingHandWhileDrawing) {
                handLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(false));
            } else {
                flipButton.setEnabled(true);
                flipToSend = false;
            }

        } else if (e.getActionCommand().equals("Show Decks")) {
            setButtonNOTVisible();
            cordToSend=null;
            confirmButton.setEnabled(false);
            flipButton.setEnabled(false);
            decksButton.setText("Show Hand");

            refresh();
            refreshDecksPanels();
            drawableSerialNumberCheckBoxMap.clear();

            printHandOrDecksOnGUI(frameManager.getResourceCardsAvailable(), imagePanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 0, 0);
            printHandOrDecksOnGUI(frameManager.getGoldCardsAvailable(), imagePanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 1, 0);


            if (checkingHandWhileDrawing) {
                decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(true));
            } else {
                decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(false));
            }

        } else if (e.getActionCommand().equals("Confirm")) {
            try {
                serialToSend = handSerialNumberCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().checkBox.isSelected())
                        .findFirst()
                        .orElseThrow()
                        .getKey();


                flipToSend = handSerialNumberCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().checkBox.isSelected())
                        .findFirst()
                        .orElseThrow()
                        .getValue()
                        .checkBox()
                        .getText().equals("true");

                if(cordToSend==null || serialToSend == -1 ){
                    JOptionPane.showMessageDialog(this, "Error. How to insert a card: \n 1) click on a card in your deck\n 2) choose a place to insert the card in the board\n 3) click on the 'Confirm' button","ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
                } else{
                    frameManager.getVirtualServer().sendMessageFromClient(new PlaceCardMessage(serialToSend,flipToSend, cordToSend.getX(), cordToSend.getY()));
                    checkingHandWhileDrawing = true;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid card","ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("Draw")){
            setButtonNOTVisible();
            boardButtonFlag = true;
            checkingHandWhileDrawing = false;
            try {
                serialToSend = drawableSerialNumberCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().checkBox.isSelected())
                        .findFirst()
                        .orElseThrow()
                        .getKey();

                frameManager.getVirtualServer().sendMessageFromClient(new DrawCardFromDeckMessage(serialToSend));
                frameManager.setTurnPlayed(frameManager.getTurnPlayed()+1);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid card","ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
            }

            confirmButton.setText("Confirm");
            confirmButton.setEnabled(false);
            flipButton.setEnabled(true);
            flipButton.setText("Show Back");
            decksButton.setText("Show Decks");

            refresh();
            refreshDecksPanels();
            handSerialNumberCheckBoxMap.clear();

        } else {
            for (JButton button : boardButtonMap.values()) {
                button.setIcon(null);
                button.setBorderPainted(true);
                if (e.getSource().equals(button)) {
                    try{
                        serialToSend = handSerialNumberCheckBoxMap.entrySet()
                                .stream()
                                .filter(en -> en.getValue().checkBox.isSelected())
                                .findFirst()
                                .orElseThrow()
                                .getKey();
                    }catch(NoSuchElementException ex){
                        JOptionPane.showMessageDialog(this, "serialToSend not updated.");
                    }

                    ImageIcon originalIcon = new ImageIcon(getDirectory(serialToSend,flipToSend));
                    Image scaledImage = originalIcon.getImage().getScaledInstance(originalIcon.getIconWidth() / 5, originalIcon.getIconHeight() / 5, Image.SCALE_SMOOTH); // Scala l'immagine
                    ImageIcon imageIcon = new ImageIcon(scaledImage);
                    button.setIcon(imageIcon);
                    button.setBorderPainted(false);
                    try {
                        cordToSend = boardButtonMap.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().equals(button))
                                .findFirst()
                                .orElseThrow().getKey();
                        System.out.println(cordToSend.getX() + cordToSend.getY());
                    } catch (NoSuchElementException ex) {
                        JOptionPane.showMessageDialog(this, "fatal error, try again.");
                    }
                }
            }
        }
    }


    /**
     * Executes actions to perform after a card is placed in the game.
     * This method updates UI components and clears data for drawing cards.
     */
    public void afterCardPlaced(){
        setButtonNOTVisible();
        confirmButton.setEnabled(false);
        flipButton.setEnabled(false);
        decksButton.setText("Show Hand");
        confirmButton.setText("Draw");
        boardButtonFlag = false;

        refresh();
        refreshDecksPanels();
        drawableSerialNumberCheckBoxMap.clear();
        handLabelCheckBoxMap.clear();

        printHandOrDecksOnGUI(frameManager.getResourceCardsAvailable(), imagePanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 0, 0);
        printHandOrDecksOnGUI(frameManager.getGoldCardsAvailable(), imagePanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 1, 0);


        decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(true));

        if (checkingHandWhileDrawing) {
            decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(true));
        } else {
            decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(false));
        }

        southPanel.revalidate();
        southPanel.repaint();
    }


    /**
     * Executes actions to handle exceptions detected during the position of the card on the board.
     * This method resets UI components and prepares for further actions.
     */
    public void exceptionDetected(){
        setButtonNOTVisible();

        for (JButton button : boardButtonMap.values()) {
            button.setIcon(null);
            button.setBorderPainted(true);
        }

        confirmButton.setEnabled(false);
        flipButton.setText("Show Back");
        decksButton.setText("Show Decks");

        refresh();
        refreshDecksPanels();
        handSerialNumberCheckBoxMap.clear();

        Map<Integer, Boolean> printMap = new HashMap<>();
        for (Integer integer : frameManager.getHand()) printMap.put(integer, false);

        printHandOrDecksOnGUI(printMap, imagePanel, checkBoxPanel, choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap, 0,0);

        if(checkingHandWhileDrawing) {
            handLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(false));
        } else {
            flipButton.setEnabled(true);
            flipToSend = false;
        }
    }

    /**
     * Refreshes the decks panels in the GUI layout, clearing existing checkboxes.
     */
    private void refreshDecksPanels(){
        decksLabelCheckBoxMap.clear();

        Enumeration<AbstractButton> checkBoxes = buttonGroupDecks.getElements();
        while (checkBoxes.hasMoreElements()) {
            AbstractButton button = checkBoxes.nextElement();
            if (button instanceof JCheckBox) {
                buttonGroupDecks.remove(button);
            }
        }

        southPanel.revalidate();
        southPanel.repaint();
    }

    /**
     * Sets player avatars in the west panel based on player positions.
     *
     * @param westPanel The panel where player avatars are displayed.
     */
    public void setPlayerAvatar(JPanel westPanel){
        Random random = new Random();
        int y2 = 0;

        Map<Position, String> positionPlayerMap = frameManager.getPlayerPositions().entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Position::getIntPosition)))
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey,
                        (e1, e2) -> e1,
                        () -> new EnumMap<>(Position.class)));

        for (Map.Entry<Position, String> entry : positionPlayerMap.entrySet()) {
            int randomIndex = random.nextInt(avatarsLogo.size());
            String selectedAvatar = avatarsLogo.get(randomIndex);
            playersAvatarMap.put(entry.getValue(), selectedAvatar);
            avatarsLogo.remove(randomIndex);
            int y1 = 0;

            JPanel playerAvatarPanel = new JPanel(new GridBagLayout());
            playerAvatarPanel.setOpaque(false);
            JLabel playerNameLabel = createTextLabelFont(entry.getValue(), 16);
            GridBagConstraints gbc = createGridBagConstraints(0,y1);
            gbc.anchor = GridBagConstraints.CENTER;
            playerAvatarPanel.add(playerNameLabel, gbc);
            gbc.gridy++;
            JLabel playerPositionLabel = createTextLabelFont(entry.getKey().toString().toLowerCase(), 12);
            setBorderInsets(playerPositionLabel, 2, 0,5, 0);
            playerPositionLabel.setForeground(Color.GRAY);
            playerAvatarPanel.add(playerPositionLabel, gbc);
            gbc.gridy++;
            setBorderInsets(playerAvatarPanel, 15, 0, 15, 0);

            if (entry.getKey() == Position.FIRST) {
                JLabel blackToken = new JLabel(createBlackTokenImageIcon(45));
                blackToken.setOpaque(false);
                GridBagConstraints gbcBlackToken = createGridBagConstraints(gbc.gridx, gbc.gridy);
                gbcBlackToken.insets = new Insets(0, 0, 0, 10);
                gbcBlackToken.anchor = GridBagConstraints.SOUTHEAST;
                playerAvatarPanel.add(blackToken, gbcBlackToken);
            }

            JLabel avatar = new JLabel(createResizedTokenImageIcon(selectedAvatar, 100));
            playerAvatarPanel.add(avatar, gbc);

            JCheckBox jCheckBox = new JCheckBox();
            jCheckBox.setFocusPainted(false);
            jCheckBox.setBorderPainted(false);
            avatarButtonGroup.add(jCheckBox);
            setBorderInsets(jCheckBox, 30, 30, 30, 30);
            jCheckBox.setOpaque(false);
            playerCheckBoxMap.put(playerNameLabel, jCheckBox);

            playerAvatarPanel.add(jCheckBox, gbc);
            gbc.gridy++;

            jCheckBox.addActionListener(e -> {
                playerCheckBoxMap.keySet().forEach(k -> k.setForeground(Color.BLACK));

                JLabel avatarLabel = playerCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().equals(e.getSource()))
                        .findFirst()
                        .orElseThrow()
                        .getKey();

                String name;
                if(avatarLabel.getText().equals(nickname)){
                    avatarLabel.setForeground(new Color(55, 133, 9));
                    name = String.format("<span style = 'color: rgb(%d, %d, %d);'>" + avatarLabel.getText() + "</span>", 55, 133, 9);
                }else {
                    avatarLabel.setForeground(Color.RED);
                    name = String.format("<span style = 'color: red'>" + avatarLabel.getText() + "</span>");
                }

                boardToDisplay = playerCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().equals(e.getSource()))
                        .findFirst()
                        .orElseThrow()
                        .getKey()
                        .getText();

                resourcePlayerLabel.setText("<html><div style='white-space: nowrap;'>Table player: " + name + "</div></html>");
                updateResourceTable(resourceTable, frameManager.getPlayersCollectedResources().get(boardToDisplay), logos);

                refreshBoard();
            });

            westPanel.add(playerAvatarPanel, createGridBagConstraints(0,y2));
            y2++;
        }
    }

    /**
     * Displays the hand or decks of cards in the GUI, showing icons for each card.
     *
     * @param hand The list of card IDs in the hand or decks.
     * @return A mapping of card serial numbers to lists of paths to their corresponding icons.
     */
    public Map<Integer, ArrayList<String>> showHandOrDecks(List<Integer> hand) {
        Map<Integer, ArrayList<String>> handIconsPaths = new LinkedHashMap<>();

        hand.forEach(serialCard -> {
            Path startDir = identifyPathCard(serialCard);
            try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().contains(serialCard.toString()))) {
                List<Path> sortedPaths = paths.sorted(Comparator.comparingInt(this::extractNumberFromPath)).toList();

                Map<Path, String> firstFilesInSubfolders = new HashMap<>();
                for (Path path : sortedPaths) {
                    Path parentDir = path.getParent();
                    if (!firstFilesInSubfolders.containsKey(parentDir)) {
                        firstFilesInSubfolders.put(parentDir, path.toString());
                    }
                }
                ArrayList<String> cardIconList = new ArrayList<>(firstFilesInSubfolders.values());
                handIconsPaths.put(serialCard, cardIconList);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
            }
        });
        return handIconsPaths;
    }

    /**
     * Extracts a numeric value from a file path for sorting purposes.
     *
     * @param path The path from which to extract the number.
     * @return The extracted numeric value from the path.
     */
    private int extractNumberFromPath(Path path) {
        String fileName = path.getFileName().toString();
        String numberStr = fileName.replaceAll("\\D+", ""); // Rimuovi tutti i caratteri non numerici
        try {
            return Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Sets the nickname of the player on the GUI.
     *
     * @param nickname The nickname of the player.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        boardToDisplay=nickname;
    }

    /**
     * Sets the token color of the player on the GUI.
     *
     * @param token The token color of the player.
     */
    public void setToken(TokenColor token) {
        this.token = token;
    }

    /**
     * Retrieves the nickname of the player from the GUI.
     *
     * @return The nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * Retrieves the token color of the player from the GUI.
     *
     * @return The token color of the player.
     */
    public TokenColor getToken() {
        return token;
    }

    /**
     * Retrieves the main panel container of the GUI.
     *
     * @return The main panel container of the GUI.
     */
    public JPanel getPanelContainer() {
        return panelContainer;
    }

    /**
     * Retrieves the panel for choosing options on the GUI.
     *
     * @return The panel for choosing options on the GUI.
     */
    public JPanel getChoosePanel() {
        return choosePanel;
    }

    /**
     * Retrieves the panel for displaying names on the GUI.
     *
     * @return The panel for displaying names on the GUI.
     */
    public JPanel getNamePanel() {
        return namePanel;
    }

    /**
     * Retrieves the panel for displaying images on the GUI.
     *
     * @return The panel for displaying tokens on the GUI.
     */
    public JPanel getImagePanel() {
        return imagePanel;
    }

    /**
     * Retrieves the panel for displaying checkboxes on the GUI.
     *
     * @return The panel for displaying checkboxes on the GUI.
     */
    public JPanel getCheckBoxPanel() {
        return checkBoxPanel;
    }

    /**
     * Retrieves the button group used in the GUI.
     *
     * @return The button group used in the GUI.
     */
    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    /**
     * Retrieves the mapping of label-checkbox pairs for hand labels on the GUI.
     *
     * @return The mapping of label-checkbox pairs for hand labels on the GUI.
     */
    public Map<JLabel, JCheckBox> getHandLabelCheckBoxMap() {
        return handLabelCheckBoxMap;
    }

    /**
     * Sets the frame manager instance for managing game frames.
     *
     * @param frameManager The frame manager instance to set.
     */
    public void setFrameManager(FrameManager frameManager) {
        this.frameManager = frameManager;
    }

    /**
     * Retrieves the label displaying the current turn on the GUI.
     *
     * @return The label displaying the current turn on the GUI.
     */
    public JLabel getTurnLabel() {
        return turnLabel;
    }

    /**
     * Retrieves an alternative label displaying the current turn on the GUI.
     *
     * @return The alternative label displaying the current turn on the GUI.
     */
    public JLabel getTurnLabel2() {
        return turnLabel2;
    }

    /**
     * Retrieves the label displaying the current score on the GUI.
     *
     * @return The label displaying the current score on the GUI.
     */
    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    /**
     * Retrieves an alternative label displaying the current score on the GUI.
     *
     * @return The alternative label displaying the current score on the GUI.
     */
    public JLabel getScoreLabel2() {
        return scoreLabel2;
    }

    /**
     * Retrieves the resource table displayed on the GUI.
     *
     * @return The resource table displayed on the GUI.
     */
    public JTable getResourceTable() {
        return resourceTable;
    }

    /**
     * Retrieves the timer used in the GUI.
     *
     * @return The timer used in the GUI.
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * Retrieves the flip button used in the GUI.
     *
     * @return The flip button used in the GUI.
     */
    public JButton getFlipButton() {
        return flipButton;
    }

    /**
     * Sets the flag indicating whether a flip action is to be sent from the GUI.
     *
     * @param flipToSend The flag indicating whether a flip action is to be sent.
     */
    public void setFlipToSend(boolean flipToSend) {
        this.flipToSend = flipToSend;
    }

    /**
     * Retrieves the mapping of serial numbers to checkbox components for hand labels on the GUI.
     *
     * @return The mapping of serial numbers to checkbox components for hand labels on the GUI.
     */
    public Map<Integer, CardData> getHandSerialNumberCheckBoxMap() {
        return handSerialNumberCheckBoxMap;
    }

    /**
     * Retrieves the token manager instance associated with the GUI.
     *
     * @return The token manager instance associated with the GUI.
     */
    public TokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Indicates if an exception flag is set on the GUI.
     *
     * @return {@code true} if the exception flag is set; otherwise, {@code false}.
     */
    public boolean isUnableExceptionFlag() {
        return unableExceptionFlag;
    }

    /**
     * Sets the flag indicating whether the GUI is in a state where exceptions are disabled.
     *
     * @param unableExceptionFlag The flag indicating whether exceptions are disabled.
     */
    public void setUnableExceptionFlag(boolean unableExceptionFlag) {
        this.unableExceptionFlag = unableExceptionFlag;
    }

    /**
     * Indicates if the GUI is currently checking the player's hand while drawing.
     *
     * @return {@code true} if the GUI is checking the hand while drawing; otherwise, {@code false}.
     */
    public boolean isCheckingHandWhileDrawing() {
        return checkingHandWhileDrawing;
    }

    /**
     * Sets the flag indicating whether the GUI is checking the hand while drawing.
     *
     * @param checkingHandWhileDrawing The flag indicating whether the GUI is checking the hand while drawing.
     */
    public void setCheckingHandWhileDrawing(boolean checkingHandWhileDrawing) {
        this.checkingHandWhileDrawing = checkingHandWhileDrawing;
    }


    /**
     * Retrieves the combobox used in the GUI for displaying labels.
     *
     * @return The combobox used in the GUI for displaying labels.
     */
    public JComboBox<JLabel> getCombobox() {
        return combobox;
    }

    /**
     * Retrieves the label indicating waiting status on the GUI.
     *
     * @return The label indicating waiting status on the GUI.
     */
    public JLabel getWaitingLabel() {
        return waitingLabel;
    }

    /**
     * Retrieves the mapping of player names to avatar images on the GUI.
     *
     * @return The mapping of player names to avatar images on the GUI.
     */
    public Map<String, String> getPlayersAvatarMap() {
        return playersAvatarMap;
    }

    /**
     * Retrieves the mapping of new messages on the GUI.
     *
     * @return The mapping of new messages on the GUI.
     */
    public Map<String, ArrayList<Boolean>> getNewMessageMap() {
        return newMessageMap;
    }

    /**
     * Retrieves the label indicating the number of attempts on the GUI.
     *
     * @return The label indicating the number of attempts on the GUI.
     */
    public JLabel getNumAttemptLabel() {
        return numAttemptLabel;
    }

    /**
     * Replaces the current panel container with a waiting page during reconnection attempts.
     */
    public void reconnectionWaitingPage() {
        getContentPane().remove(panelContainer);

        System.out.println("Prova");
        numAttemptLabel = createTextLabelFont("", 30);
        setBorderInsets(numAttemptLabel, 0,20,0,0);
        getContentPane().add(numAttemptLabel, BorderLayout.NORTH);


        JPanel panel = new JPanel(new GridBagLayout());
        JLabel reconnectionLabel = createTextLabelFont("Internet Connection Lost, trying to reconnect...", 64);
        setBorderInsets(reconnectionLabel, 0, 0, 20, 0);
        panel.add(reconnectionLabel, createGridBagConstraints(0, 0));

        ArrayList<Color> colors = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            colors.add(new Color(107, 189, 192));
            colors.add(new Color(233, 73, 23));
            colors.add(new Color(113, 192, 124));
            colors.add(new Color(171, 63, 148));
        }

        colorIndex = 0;
        JProgressBar progressBar = new JProgressBar(0, 100);
        setBoxComponentSize(progressBar, 1200, 30);
        progressBar.setForeground(colors.get(colorIndex));
        colorIndex++;
        panel.add(progressBar, createGridBagConstraints(0, 1));

        timer = new Timer(100, e -> {
            progress += 5;
            if (progress > 100) {
                progress = 0;
                progressBar.setForeground(colors.get(colorIndex));
                colorIndex = (colorIndex + 1) % colors.size();
            }
            progressBar.setValue(progress);
        });
        timer.start();

        getContentPane().add(panel, BorderLayout.CENTER);
    }
}