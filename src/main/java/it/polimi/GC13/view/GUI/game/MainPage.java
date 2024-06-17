package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.network.messages.fromclient.*;
import it.polimi.GC13.view.GUI.*;

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


public class MainPage extends JFrame implements ActionListener, CardManager, WaitingLobby {

    //record(s)
    public record CardData(JLabel label, JCheckBox checkBox) {}

    //Manager of interaction between server and game
    private FrameManager frameManager;

    //setting user parameters
    private String nickname;
    private TokenColor token;
    private List<TokenColor> tokenColorList;

    //panels and tables in the game
    private final JPanel panelContainer;
    private final JPanel choosePanel;
    private JPanel southPanel;
    private JPanel tokenPanel;
    private JPanel namePanel;
    private JPanel checkBoxPanel;
    private JPanel commonPanel;
    private final JTable resourceTable = new JTable(); //table in the Game page
    private JTable positionTable; //table in the ScoreBoard page

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

    //management of the movements of tokens on the ScoreBoard
    private TokenManager tokenManager;

    //chat
    private JComboBox<String> combobox;
    private JEditorPane chatArea;
    private JTextField messageField;


    public MainPage(List<TokenColor> tokenColorList) {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        panelContainer = new JPanel(new GridBagLayout());
        panelContainer.setBackground(new Color(237,230,188,255));
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
        //confirmButton.setBackground(new Color(232, 221, 183, 255));
        panelContainer.add(confirmButton, createGridBagConstraints(0, 3));

        confirmButton.addActionListener (e -> {
            String tokenColorChosen;
            for (JCheckBox checkBox : handLabelCheckBoxMap.values()) {
                if (checkBox.isSelected()) {
                    tokenColorChosen = checkBox.getText();
                    if (e.getActionCommand().equals("Select")) {
                        frameManager.getVirtualServer().sendMessageFromClient(new TokenChoiceMessage(TokenColor.valueOf(tokenColorChosen.toUpperCase())));
                    }
                    break;
                }
            }
        });
        setVisible(true);
    }


    public void showTokenChoose(List<TokenColor> tokenColorList){
        GridBagConstraints gbc2 = createGridBagConstraints(0, 0);
        tokenPanel = new JPanel(new FlowLayout()); //pannello dove inserire le icone dei token
        tokenPanel.setOpaque(false);
        choosePanel.add(tokenPanel, gbc2);

        checkBoxPanel = new JPanel(new FlowLayout());
        checkBoxPanel.setOpaque(false);
        choosePanel.add(checkBoxPanel, gbc2);

        namePanel = new JPanel(new FlowLayout()); //pannello dove inserire i nomi dei token
        namePanel.setOpaque(false);
        choosePanel.add(namePanel, createGridBagConstraints(0, 1));

        setTokenColorList(tokenColorList);
    }


    public void setTokenColorList(List<TokenColor> tokenColorList) {
        this.tokenColorList = tokenColorList;

        Arrays.stream(TokenColor.values()).forEach( tokenColor -> {
            JLabel tokenLabelImage = new JLabel((this.tokenColorList.contains(tokenColor)) ? createPlayableTokenImageIcon(tokenColor, 300) : createGreyTokenImageIcon(300));
            tokenPanel.add(tokenLabelImage);

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


    private String getTokenFileName(TokenColor tokenColor) {
        return tokenColor.toString().toLowerCase() + TOKEN_FILE_SUFFIX;
    }

    private static ImageIcon createResizedTokenImageIcon(String tokenImagePath, int dim) {
        return new ImageIcon(new ImageIcon(tokenImagePath).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
    }

    private ImageIcon createPlayableTokenImageIcon(TokenColor tokenColor, int dim) {
        return createResizedTokenImageIcon(P_TOKEN_DIR + getTokenFileName(tokenColor), dim);
    }

    private ImageIcon createGreyTokenImageIcon(int dim) {
        return createResizedTokenImageIcon(TOKEN_DIR + GREY_TOKEN_FILE_NAME + TOKEN_FILE_SUFFIX, dim);
    }



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
                            createLobby();
                            getContentPane().revalidate();
                            getContentPane().repaint();
                        }
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,"Invalid card", "ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
                //TODO: da capire cosa fare per gestire l'errore
            }
        });
    }


    public void refresh(){
        tokenPanel.removeAll();
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
                        tokenPanel.add(startCardLabelImage);

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
                    tokenPanel.add(startCardLabelImage);

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
        panelContainer.setBackground(new Color(237,230,188,255));

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

        commonPanel = new JPanel(new FlowLayout());
        commonPanel.setOpaque(false);
        panelContainer.add(commonPanel, createGridBagConstraints(0, 3));

        showCommonObjectiveCard(frameManager.getSerialCommonObjectiveCard(), commonPanel, 3);

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




    public void createGamePanel() {
        panelContainer.setLayout(new CardLayout());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setOpaque(false);

        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setBackground(new Color(237,230,188,255));

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


        JPanel lateralPanelSX = new JPanel(new GridBagLayout());
        setBoxComponentSize(lateralPanelSX, 150, lateralPanelSX.getHeight());

        setPlayerAvatar(lateralPanelSX);
        panel1.add(lateralPanelSX, BorderLayout.WEST);


        JPanel lateralPanelDX = new JPanel();
        lateralPanelDX.setLayout(new BoxLayout(lateralPanelDX, BoxLayout.Y_AXIS));
        setBoxComponentSize(lateralPanelDX, 200,lateralPanelDX.getHeight());

        commonPanel.removeAll();
        commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.Y_AXIS));
        JLabel labelCommonObjectiveCards = createTextLabelFont("Common Objective Cards", 16);
        setBorderInsets(labelCommonObjectiveCards, 0,0,10,0);
        commonPanel.add(labelCommonObjectiveCards);

        showCommonObjectiveCard(frameManager.getSerialCommonObjectiveCard(), commonPanel, 10);

        JLabel labelPrivateObjectiveCards = createTextLabelFont("Private Objective Cards", 16);
        setBorderInsets(labelPrivateObjectiveCards, 0,0,10,0);
        commonPanel.add(labelPrivateObjectiveCards);

        List<Integer> privateObjectiveCard = List.of(frameManager.getSerialPrivateObjectiveCard());

        showCommonObjectiveCard(privateObjectiveCard, commonPanel, 10);

        lateralPanelDX.add(commonPanel);


        JPanel tableResourcePanel = new JPanel();
        addScrollPane(resourceTable, tableResourcePanel, 200, ((resourceTable.getRowCount() + 1) * resourceTable.getRowHeight()) + 5);
        lateralPanelDX.add(tableResourcePanel);


        decksButton = createButton("Show Decks", 27);
        decksButton.addActionListener(this);
        lateralPanelDX.add(decksButton);

        panel1.add(lateralPanelDX, BorderLayout.EAST);


        southPanel = new JPanel(new GridBagLayout());
        setBorderInsets(southPanel, 1, 0, 1, 0);


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


        JPanel lateralPanelDX2 = new JPanel();
        lateralPanelDX2.setLayout(new BoxLayout(lateralPanelDX2, BoxLayout.Y_AXIS));
        setBoxComponentSize(lateralPanelDX2, 380, lateralPanelDX2.getHeight());

        JPanel tablePanel = new JPanel();
        positionTable = new JTable();
        createTable(positionTable, new String[]{"Token", "player"}, positionPlayerMap, listTokenInGame, frameManager.getTokenInGame(), true, 35);

        addScrollPane(positionTable, tablePanel, 300, (positionTable.getRowCount() + 1) * positionTable.getRowHeight() + 5);
        lateralPanelDX2.add(tablePanel);
        panel2.add(lateralPanelDX2, BorderLayout.EAST);


        JPanel chatPanel = new JPanel(new GridBagLayout());
        setBorderInsets(chatPanel, 0,5,0,5);

        combobox = new JComboBox<>();

        combobox.addItem("global");
        for(String s : positionPlayerMap.values()){
            if(!s.equals(nickname)) {
                combobox.addItem(s);
            }
        }

        combobox.addActionListener(e -> updateChatArea((String)combobox.getSelectedItem()));

        GridBagConstraints gbcCombobox = createGridBagConstraints(0,0);
        gbcCombobox.fill = GridBagConstraints.HORIZONTAL;

        chatPanel.add(combobox, gbcCombobox);

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
        lateralPanelDX2.add(chatPanel);


        setVisible(true);
    }


    public void updateChatArea(String selectedPlayer) {
        if (selectedPlayer != null) {
            List<ChatMessage> messages = frameManager.getChat().get(selectedPlayer);
            if(messages != null) {
                StringBuilder content = new StringBuilder("<html>");
                for(ChatMessage msg : messages) {
                    content.append("<b>").append(msg.sender()).append("</b>").append(": ").append(msg.content()).append(";<br>");
                }
                content.append("</html>");
                chatArea.setText(content.toString());
            } else {
                chatArea.setText("");
            }
        }
    }


    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            String selectedPlayer = (String)combobox.getSelectedItem();
            if (selectedPlayer != null) {
                frameManager.getVirtualServer().sendMessageFromClient(new NewMessage(this.nickname, selectedPlayer, message));
                messageField.setText("");
            }
        }
    }


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

    public void printHandOrDecksOnGUI(List<Integer> list, List<Boolean> b, JPanel imagePanel, JPanel checkBoxPanel, JPanel container, ButtonGroup buttonGroup, Map<JLabel, JCheckBox> labelCheckBoxMap, Map<Integer, CardData> serialNumberCheckBoxMap, int x, int y){
        Map<Integer, ArrayList<String>> cards = showHandOrDecks(list);
        Map<Integer, Integer> mappingIndexCardsMap = new HashMap<>();

        //remapping the map cards
        int newIndex = 0;
        for(int i : cards.keySet()){
            mappingIndexCardsMap.put(newIndex, i);
            newIndex++;
        }

        for(int i=0; i<cards.size(); i++){
            String flippedIcon;
            JCheckBox jCheckBox;
            if(!b.get(i)){
                try{
                    flippedIcon = cards.get(mappingIndexCardsMap.get(i))
                            .stream()
                            .filter(s->s.contains("front"))
                            .findFirst()
                            .orElseThrow();
                }catch(NoSuchElementException noSuchElementException){
                    flippedIcon = CardManager.ERROR_IMAGE;
                }
            } else {
                try{
                    flippedIcon = cards.get(mappingIndexCardsMap.get(i))
                            .stream()
                            .filter(path -> path.contains("back"))
                            .findFirst()
                            .orElseThrow();
                }catch(NoSuchElementException noSuchElementException){
                    flippedIcon = CardManager.ERROR_IMAGE;
                }
            }

            jCheckBox = new JCheckBox(b.get(i).toString());
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
            serialNumberCheckBoxMap.put(mappingIndexCardsMap.get(i), record);

            container.add(imagePanel, createGridBagConstraints(x,y));
            container.add(checkBoxPanel, createGridBagConstraints(x,y));

            container.revalidate();
            container.repaint();
        }
    }

    public void setButtonVisible(){
        if(boardButtonFlag) {
            for(JButton button: boardButtonMap.values()){
                button.setVisible(true);
            }
        }
    }

    public void setButtonNOTVisible(){
        for(JButton button: boardButtonMap.values()){
            button.setVisible(false);
        }
    }

    private void addButtonToLayeredPane(JLayeredPane layeredPane, int x, int y) {
        JButton button = new JButton();
        button.addActionListener(this);

        Coordinates corner = new Coordinates(((x-(16830/2))/152)+50,((y-(11220/2))/78)+50);
        boardButtonMap.put(corner,button);

        setBoxComponentSize(button, 192, 132);

        button.setVisible(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(135, 142, 150, 255),4));
        button.setBounds(x,y,198,132);//card sizes

        layeredPane.add(button,JLayeredPane.PALETTE_LAYER);
    }

    public static void addImageToLayeredPane(JLayeredPane layeredPane, String imagePath, int x, int y, int layer) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(originalIcon.getIconWidth() / 5, originalIcon.getIconHeight() / 5, Image.SCALE_SMOOTH); // Scala l'immagine
        ImageIcon imageIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        imageLabel.setBounds(x, y, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        layeredPane.add(imageLabel, Integer.valueOf(layer));
    }

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
                return ERROR_IMAGE;
            }
        }
    }

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
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), dim+5));

        table.setRowHeight(dim+5);
    }

    public void updateResourceTable(JTable table, EnumMap<Resource, Integer> collectedResources, ArrayList<String> logosPath){
        for (Resource resource : collectedResources.keySet()) {
            table.setValueAt(collectedResources.get(resource), logosPath.indexOf(remappingResources(resource)), 1);
        }
    }

    public void updateCrownImageInTable(){
        //posso anche mettere la corona a tutti i giocatori a parimerito, se essi sono meno del numero totale dei giocatori

        String maxScorePlayer = frameManager.getPlayersScore().entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).stream().findFirst().orElseThrow();

        for (int row = 0; row < positionTable.getModel().getRowCount(); row++) {
            String cellValue = (String) positionTable.getModel().getValueAt(row, 2);
            if (maxScorePlayer.equals(cellValue)) {
                positionTable.setValueAt(true, row , 0);
            } else {
                positionTable.setValueAt(false, row , 0);
            }
        }
    }


    public void updateScoreColumn(){
        for (int row = 0; row < positionTable.getModel().getRowCount(); row++) {
            String cellValue = (String) positionTable.getModel().getValueAt(row, 2);

            for(String playerName : frameManager.getPlayersScore().keySet()) {
                if (playerName.equals(cellValue)) {
                    positionTable.setValueAt(frameManager.getPlayersScore().get(playerName), row, 3);
                }
            }
        }
    }


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

    private void setBoxComponentSize(JComponent component, int w, int h){
        component.setPreferredSize(new Dimension(w, h));
        component.setMaximumSize(new Dimension(w, h));
        component.setMinimumSize(new Dimension(w, h));
    }

    private JLabel createTextLabelFont(String content, int dim) {
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return jLabel;
    }

    private void setBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        jComponent.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    private void setCompoundBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight, String inset, Color color, int thickness) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        Border b = BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
        switch (inset) {
            case "TOP" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)));
            case "BOTTOM" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)));
            case "ALL" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createLineBorder(color, thickness)));
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cardLayout = (CardLayout) panelContainer.getLayout();
        if (e.getActionCommand().equals("Go to scoreboard")) {
            cardLayout.show(panelContainer, PANEL2);
            setButtonNOTVisible();
        } else if (e.getActionCommand().equals("Go to game")) {
            cardLayout.show(panelContainer, PANEL1);
            setButtonNOTVisible();
        } else if (e.getActionCommand().equals("Show Back")) {
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

            ArrayList<Boolean> b = new ArrayList<>();
            for(int i=0; i<frameManager.getHand().size(); i++)
                b.add(true);

            printHandOrDecksOnGUI(frameManager.getHand(), b, tokenPanel, checkBoxPanel, choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap, 0,0);

        }  else if (e.getActionCommand().equals("Show Front") || e.getActionCommand().equals("Show Hand")) {
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

            ArrayList<Boolean> b = new ArrayList<>();
            for(int i = 0; i < frameManager.getHand().size(); i++)
                b.add(false);

            printHandOrDecksOnGUI(frameManager.getHand(), b, tokenPanel, checkBoxPanel, choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap, 0,0);

            if(checkingHandWhileDrawing) {
                handLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(false));
            } else {
                flipButton.setEnabled(true);
                flipToSend = false;
            }

        } else if (e.getActionCommand().equals("Show Decks")) {
            setButtonNOTVisible();
            confirmButton.setEnabled(false);
            flipButton.setEnabled(false);
            decksButton.setText("Show Hand");

            refresh();
            refreshDecksPanels();
            drawableSerialNumberCheckBoxMap.clear();

            printHandOrDecksOnGUI(frameManager.getResourceCardsAvailable().keySet().stream().toList(), frameManager.getResourceCardsAvailable().values().stream().toList(), tokenPanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 0, 0);
            printHandOrDecksOnGUI(frameManager.getGoldCardsAvailable().keySet().stream().toList(), frameManager.getGoldCardsAvailable().values().stream().toList(), tokenPanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 1, 0);


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
                    JOptionPane.showMessageDialog(this, "Error: select the card to play ","ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
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
                        System.out.println("il serial to send non è stato aggiornato");
                    }

                    ImageIcon originalIcon = new ImageIcon(getDirectory(serialToSend,flipToSend));
                    Image scaledImage = originalIcon.getImage().getScaledInstance(originalIcon.getIconWidth() / 5, originalIcon.getIconHeight() / 5, Image.SCALE_SMOOTH); // Scala l'immagine
                    ImageIcon imageIcon = new ImageIcon(scaledImage);
                    button.setIcon(imageIcon);
                    button.setBorderPainted(false);
                    //button.setBorder(BorderFactory.createLineBorder(new Color(230, 64, 50, 255),4));
                    try {
                        cordToSend = boardButtonMap.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().equals(button))
                                .findFirst()
                                .orElseThrow().getKey();
                        System.out.println(cordToSend.getX() + cordToSend.getY());
                    } catch (NoSuchElementException ex) {
                        System.out.println("fatal error, non sappiamo programmare, mi spiace che hai scelto il nostro gioco ");
                    }
                }
            }
        }
    }


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

        printHandOrDecksOnGUI(frameManager.getResourceCardsAvailable().keySet().stream().toList(), frameManager.getResourceCardsAvailable().values().stream().toList(), tokenPanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 0, 0);
        printHandOrDecksOnGUI(frameManager.getGoldCardsAvailable().keySet().stream().toList(), frameManager.getGoldCardsAvailable().values().stream().toList(), tokenPanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 1, 0);


        decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(true));

        if (checkingHandWhileDrawing) {
            decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(true));
        } else {
            decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(false));
        }

        southPanel.revalidate();
        southPanel.repaint();
    }


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

        ArrayList<Boolean> b = new ArrayList<>();
        for(int i=0; i<frameManager.getHand().size(); i++)
            b.add(false);

        printHandOrDecksOnGUI(frameManager.getHand(), b, tokenPanel, checkBoxPanel, choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap, 0,0);

        if(checkingHandWhileDrawing) {
            handLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(false));
        } else {
            flipButton.setEnabled(true);
            flipToSend = false;
        }
    }


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

    public void setPlayerAvatar(JPanel lateralPanelSX){
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
            JLabel avatar = new JLabel(createResizedTokenImageIcon(selectedAvatar, 100));
            playerAvatarPanel.add(avatar, gbc);

            JCheckBox jCheckBox = new JCheckBox();
            jCheckBox.setFocusPainted(false);
            jCheckBox.setBorderPainted(false);
            jCheckBox.setForeground(new Color(0,0,0,0));
            avatarButtonGroup.add(jCheckBox);
            setBorderInsets(jCheckBox, 30, 30, 30, 30);
            jCheckBox.setOpaque(false);
            playerCheckBoxMap.put(playerNameLabel, jCheckBox);

            playerAvatarPanel.add(jCheckBox, gbc);
            gbc.gridy++;

            jCheckBox.addActionListener(e -> {
                playerCheckBoxMap.keySet().forEach(k -> k.setForeground(Color.BLACK));

                playerCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().equals(e.getSource()))
                        .findFirst()
                        .orElseThrow()
                        .getKey()
                        .setForeground(Color.RED);

                boardToDisplay = playerCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().equals(e.getSource()))
                        .findFirst()
                        .orElseThrow()
                        .getKey()
                        .getText();


                refreshBoard();
            });

            lateralPanelSX.add(playerAvatarPanel, createGridBagConstraints(0,y2));
            y2++;
        }
    }


    public Map<Integer, ArrayList<String>> showHandOrDecks(List<Integer> hand) {
        Map<Integer, ArrayList<String>> handIconsPaths = new HashMap<>();

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


    private int extractNumberFromPath(Path path) {
        String fileName = path.getFileName().toString();
        String numberStr = fileName.replaceAll("\\D+", ""); // Rimuovi tutti i caratteri non numerici
        try {
            return Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        boardToDisplay=nickname;
    }

    public void setToken(TokenColor token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public TokenColor getToken() {
        return token;
    }

    public JPanel getPanelContainer() {
        return panelContainer;
    }

    public JPanel getChoosePanel() {
        return choosePanel;
    }

    public JPanel getNamePanel() {
        return namePanel;
    }

    public JPanel getTokenPanel() {
        return tokenPanel;
    }

    public JPanel getCheckBoxPanel() {
        return checkBoxPanel;
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public Map<JLabel, JCheckBox> getHandLabelCheckBoxMap() {
        return handLabelCheckBoxMap;
    }

    public void setFrameManager(FrameManager frameManager) {
        this.frameManager = frameManager;
    }

    public JLabel getTurnLabel() {
        return turnLabel;
    }

    public JLabel getTurnLabel2() {
        return turnLabel2;
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public JLabel getScoreLabel2() {
        return scoreLabel2;
    }

    public JTable getResourceTable() {
        return resourceTable;
    }

    public Timer getTimer() {
        return timer;
    }

    public JButton getFlipButton() {
        return flipButton;
    }

    public void setFlipToSend(boolean flipToSend) {
        this.flipToSend = flipToSend;
    }

    public Map<Integer, CardData> getHandSerialNumberCheckBoxMap() {
        return handSerialNumberCheckBoxMap;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public boolean isUnableExceptionFlag() {
        return unableExceptionFlag;
    }

    public void setUnableExceptionFlag(boolean unableExceptionFlag) {
        this.unableExceptionFlag = unableExceptionFlag;
    }

    public boolean isCheckingHandWhileDrawing() {
        return checkingHandWhileDrawing;
    }

    public void setCheckingHandWhileDrawing(boolean checkingHandWhileDrawing) {
        this.checkingHandWhileDrawing = checkingHandWhileDrawing;
    }

    public JTable getPositionTable() {
        return positionTable;
    }

    public JComboBox<String> getCombobox() {
        return combobox;
    }

    public JLabel getWaitingLabel() {
        return waitingLabel;
    }
}