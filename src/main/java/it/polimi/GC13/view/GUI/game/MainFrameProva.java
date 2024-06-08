package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.view.GUI.ImageIconRenderer;
import it.polimi.GC13.view.GUI.ImageIconTableModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class MainFrameProva extends JFrame implements ActionListener, CardManager {

    public record CardData(JLabel label, JCheckBox checkBox) {}

//    public record AvatarData(String playerName, String avatarPath) {}
//    private final EnumMap<Position, AvatarData> positionToPlayerData = new EnumMap<>(Position.class);

    //ArrayList<String> avatarsImagePaths = new ArrayList<>(Arrays.asList(FUNGI_JUDGE_DIR, ANIMAL_JUDGE_DIR, PLANT_JUDGE_DIR, INSECT_JUDGE_DIR));


    //private FrameManager frameManager;

    private String nickname = "Niccolo";
    private TokenColor token = TokenColor.BLUE;
    private List<Integer> hand = List.of(1, 2, 3); //DA NON METTERE
    //private final ArrayList<String> avatars = new ArrayList<>(Arrays.asList(FUNGI_JUDGE_DIR, ANIMAL_JUDGE_DIR, PLANT_JUDGE_DIR, INSECT_JUDGE_DIR));
    private boolean myTurn = true;

    private JPanel panelContainer;
    private JPanel choosePanel;
    private boolean flipToSend= false;
    private int serialTosend=-1;
    private JTable resourceTable = new JTable();
    private List<TokenColor> tokenColorList;
    private JLayeredPane board;
    private JPanel tokenPanel;
    private JPanel namePanel;
    private JPanel checkBoxPanel;
    private JPanel commonPanel;
    private JLabel labelPrivateObjectiveCards;
    private JPanel southPanel;

    private ButtonGroup buttonGroup = new ButtonGroup();

    //per le carte nella mano
    private Map<JLabel, JCheckBox> handLabelCheckBoxMap = new HashMap<>();
    private Map<Integer, CardData> handSerialNumberCheckBoxMap = new HashMap<>();

    //per le carte da pescare
    private JPanel decksPanel;
    private final ButtonGroup buttonGroupDecks = new ButtonGroup();
    private Map<JLabel, JCheckBox> decksLabelCheckBoxMap = new HashMap<>();
    private Map<Integer, CardData> drawableSerialNumberCheckBoxMap = new HashMap<>();

    private JPanel resourceImagePanel;
    private JPanel resourceCheckBoxPanel;

    private JPanel goldImagePanel;
    private JPanel goldCheckBoxPanel;

    private Map<Coordinates,JButton> buttonMap = new HashMap<>();

    private  JButton confirmButton;
    private  JButton flipButton;
    private JButton decksButton;
    private JButton handButton;

    private Coordinates cordToSend = null;

    //attributi waitingLobby

    final static String PANEL1 = "Game";
    final static String PANEL2 = "Scoreboard";
    private int flag = 1;
    private boolean boardButtonFlag = false;
    private boolean checkingHandWhileDrawing = false;

    private JLabel turnLable;
    private JLabel turnLable2;
    private JLabel scoreLabel;
    private JLabel scoreLabel2;


    private Map<String, Position> playerPosition = new HashMap<>(); //DA NON METTERE
    Map<String, TokenColor> tokenInGame = new HashMap<>();

    private TokenManager tokenManager;


    private final Map<Integer, Boolean> goldCardsAvailable = new HashMap<>(); //DA NON METTERE
    private final Map<Integer, Boolean> resourceCardsAvailable = new HashMap<>(); //DA NON METTERE
    private final Map<String, Integer> playersScore = new HashMap<>(); //DA NON METTERE

    private List<Integer> serialCommonObjectiveCard  = List.of(88, 99);; //DA NON METTERE
    private int serialPrivateObjectiveCard = 89; //DA NON METTERE


    //TOKEN SETUP
    /*public MainFrameProva() {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setSize(1700, 900); //for debugging
        setResizable(false);

        List<TokenColor> tokenColorList = List.of(TokenColor.values());

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
        panelContainer.add(confirmButton, createGridBagConstraints(0, 3));

        confirmButton.addActionListener (e -> {
            String tokenColorChosen;
            for (JCheckBox checkBox : handLabelCheckBoxMap.values()) {
                if (checkBox.isSelected()) {
                    tokenColorChosen = checkBox.getText();
                    if (e.getActionCommand().equals("Select")) {
                        JOptionPane.showMessageDialog(this, "token choose: " + tokenColorChosen);

                    }
                    break;
                }
            }
        });
        setVisible(true);
    }*/

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



    //START CARD SETUP
    /*public MainFrameProva() {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setSize(1700, 900); //for debugging
        setResizable(false);

        panelContainer = new JPanel(new GridBagLayout());
        panelContainer.setBackground(new Color(237,230,188,255));
        add(panelContainer);

        JLabel setupLabel = createTextLabelFont("setup phase [2/3] ", 28);
        setBorderInsets(setupLabel, 0, 0, 60, 0);
        panelContainer.add(setupLabel, createGridBagConstraints(0, 0));

        JLabel titleLabel = createTextLabelFont("SETUP START CARD: ", 64);
        setBorderInsets(titleLabel, 0, 0, 60, 0);
        panelContainer.add(titleLabel, createGridBagConstraints(0, 1));

        JLabel startCardLabel = createTextLabelFont("Choose which side you would like to place your start card: ", 32);
        setBorderInsets(startCardLabel, 0, 0, 90, 0);
        panelContainer.add(startCardLabel, createGridBagConstraints(0, 2));


        choosePanel = new JPanel(new GridBagLayout());
        choosePanel.setOpaque(false);
        panelContainer.add(choosePanel, createGridBagConstraints(0,3));

        GridBagConstraints gbc2 = createGridBagConstraints(0, 0);
        tokenPanel = new JPanel(new FlowLayout()); //pannello dove inserire le icone dei token
        tokenPanel.setOpaque(false);
        choosePanel.add(tokenPanel, gbc2);


        checkBoxPanel = new JPanel(new FlowLayout());
        checkBoxPanel.setOpaque(false);
        buttonGroup = new ButtonGroup();
        choosePanel.add(checkBoxPanel, gbc2);

        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //pannello dove inserire i nomi dei token
        namePanel.setOpaque(false);
        choosePanel.add(namePanel, createGridBagConstraints(0, 1));

        showStarterCardAndPrivateObjectiveCard(hand);


        confirmButton = createButton("Confirm", 32);
        panelContainer.add(confirmButton, createGridBagConstraints(0, 4));
        confirmButton.addActionListener (e -> {
            boolean isFlipped;
            for (JCheckBox checkBox : handLabelCheckBoxMap.values()) {
                if (checkBox.isSelected()) {
                    isFlipped = checkBox.getText().equals("true");
                    if (e.getActionCommand().equals("Confirm")) {
                        JOptionPane.showMessageDialog(this, "isFlipped: "+ isFlipped);
                    }
                    break;
                }
            }
        });

        setVisible(true);
    }*/

    private void refresh(){
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
        if (numberCard > 0 && numberCard <= 40)
            startDir = Paths.get(RESOURCE_DIR);
        else if (numberCard <= 80)
            startDir = Paths.get(GOLD_DIR);
        else if (numberCard <= 86)
            startDir = Paths.get(STARTER_DIR);
        else if (numberCard <= 102)
            startDir = Paths.get(OBJECTIVE_DIR);
        else {
            JOptionPane.showMessageDialog(this, "ErrorMsg: ", "Invalid card", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return startDir;
    }

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
            }
        });
    }



    //OBJECTIVE CARD
    /*public MainFrameProva() {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1700, 900); //for debugging
        setResizable(false);


        List<Integer> privateObjectiveCards = List.of(87, 88);

        panelContainer = new JPanel(new GridBagLayout());
        panelContainer.setBackground(new Color(237,230,188,255));

        JScrollPane scrollPane = new JScrollPane(panelContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setOpaque(false);
        getContentPane().add(scrollPane);


        JLabel setupLabel = createTextLabelFont("setup phase [3/3] ", 28); //20
        setBorderInsets(setupLabel, 20, 0, 40, 0);
        panelContainer.add(setupLabel, createGridBagConstraints(0, 0));

        JLabel titleLabel1 = createTextLabelFont("Setup Objective Card: ", 64); //50
        setBorderInsets(titleLabel1, 0, 0, 60, 0);
        panelContainer.add(titleLabel1, createGridBagConstraints(0, 1));

        JLabel startCardLabel = createTextLabelFont("These are the Common Objective Cards: ", 32); //25
        setBorderInsets(startCardLabel, 0, 0, 40, 0);
        panelContainer.add(startCardLabel, createGridBagConstraints(0, 2));

        commonPanel = new JPanel(new FlowLayout());
        commonPanel.setOpaque(false);
        panelContainer.add(commonPanel, createGridBagConstraints(0, 3));


        List<Integer> serialCommonObjectiveCards = List.of(99, 92);

        showCommonObjectiveCard( serialCommonObjectiveCards, commonPanel, 3);


        JLabel titleLabel2 = createTextLabelFont("Choose your private objective card: ", 32);
        setBorderInsets(titleLabel2, 40, 0, 40, 0);
        panelContainer.add(titleLabel2, createGridBagConstraints(0, 4));


        choosePanel = new JPanel(new GridBagLayout());
        choosePanel.setOpaque(false);

        GridBagConstraints gbc2 = createGridBagConstraints(0, 0);
        tokenPanel = new JPanel(new FlowLayout()); //pannello dove inserire le icone dei token
        tokenPanel.setOpaque(false);
        choosePanel.add(tokenPanel, gbc2);


        checkBoxPanel = new JPanel(new FlowLayout());
        checkBoxPanel.setOpaque(false);
        buttonGroup = new ButtonGroup();
        choosePanel.add(checkBoxPanel, gbc2);

        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //pannello dove inserire i nomi dei token
        namePanel.setOpaque(false);
        choosePanel.add(namePanel, createGridBagConstraints(0, 1));

        panelContainer.add(choosePanel, createGridBagConstraints(0,5));


        showStarterCardAndPrivateObjectiveCard( privateObjectiveCards);

        confirmButton = createButton("Let's Roll", 32);
        JPanel panelButton = new JPanel();
        panelButton.setOpaque(false);
        panelButton.add(confirmButton, createGridBagConstraints(0, 6));
        setBorderInsets(panelButton, 0,10,30, 10);
        panelContainer.add(panelButton, createGridBagConstraints(0, 6));
        confirmButton.addActionListener (e -> {
            int choice;
            for (JCheckBox checkBox : handLabelCheckBoxMap.values()) {
                if (checkBox.isSelected()) {
                    choice = Integer.parseInt(checkBox.getText());
                    if (e.getActionCommand().equals("Let's Roll")) {
                        JOptionPane.showMessageDialog(this, "choice: "+ choice);
                    }
                    break;
                }
            }
        });

        setVisible(true);
    }*/

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
            }
        });
    }




//TODO: ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //GAME
    public MainFrameProva() {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setSize(1700, 900); //for debugging
        setResizable(false);



        for (int i = 4; i < 7; i++) {
            if (i != 6)
                resourceCardsAvailable.put(i, false);
            else
                resourceCardsAvailable.put(i, true);
        }


        for (int i = 41; i < 44; i++) {
            if (i != 43)
                goldCardsAvailable.put(i, false);
            else
                goldCardsAvailable.put(i, true);
        }

        EnumMap<Position, String> positionPlayerMap = new EnumMap<>(Position.class);
        ArrayList<String> listTokenInGame = new ArrayList<>();

        Random random = new Random();

        for(int i=0; i<4; i++){
            playerPosition.put("Player" + (i + 1), Arrays.stream(Position.values()).toList().get(i));
            tokenInGame.put("Player" + (i + 1), Arrays.stream(TokenColor.values()).toList().get(i));
            int randomIndex = random.nextInt(16);
            playersScore.put("Player" + (i + 1), randomIndex);
            //System.out.println("Player" + (i + 1) + " : " + tokenInGame.get("Player" + (i + 1)) + " score: " + playersScore.get("Player" + (i + 1)));
        }


        for(String player : playerPosition.keySet()){
            positionPlayerMap.put(playerPosition.get(player), player);
            listTokenInGame.add(P_TOKEN_DIR + getTokenFileName(tokenInGame.get(player)));
        }



        panelContainer = new JPanel(new CardLayout());
        add(panelContainer);
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setOpaque(false);

        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setBackground(new Color(237, 230, 188, 255));

        panelContainer.add(panel1, PANEL1);
        panelContainer.add(panel2, PANEL2);

        //pagina 1: Game
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
        setBorderInsets(northPanel, 15, 0, 15, 0);

        JButton buttonToScoreBoard = createButton("Go to scoreboard", 20);
        buttonToScoreBoard.addActionListener(this);
        setBoxComponentSize(buttonToScoreBoard, 180, 50);
        northPanel.add(buttonToScoreBoard);

        northPanel.add(Box.createHorizontalGlue());

        JButton popup = createButton("popup", 27);
        northPanel.add(popup);

        popup.addActionListener(e -> {
            OnSetLastTurnDialog dialog = new OnSetLastTurnDialog(this, "Nico2", Position.FIRST);
            dialog.setVisible(true);
        });


        northPanel.add(Box.createHorizontalGlue());

        JPanel turnNamePanel = new JPanel();
        turnNamePanel.setOpaque(false);
        turnNamePanel.setLayout(new BoxLayout(turnNamePanel, BoxLayout.Y_AXIS));
        JLabel labelNickname = createTextLabelFont(nickname, 32);
        turnNamePanel.add(labelNickname);

        if(myTurn) {
            turnLable = createTextLabelFont("It's your turn", 20);
            turnLable.setForeground(new Color(45, 114, 27)); //DA NON AGGIUNGERE
        }else{
            turnLable = createTextLabelFont("It's not your turn", 20);
            turnLable.setForeground(new Color(152, 31, 31)); //DA NON AGGIUNGERE
        }
        turnNamePanel.add(turnLable);
        northPanel.add(turnNamePanel);

        JLabel tokenLabel = new JLabel(createPlayableTokenImageIcon(token, 50));
        northPanel.add(tokenLabel);

        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(Box.createHorizontalGlue());

        scoreLabel = createTextLabelFont("Score: " + 0, 30);
        setBorderInsets(scoreLabel, 0, 0, 0, 30);
        northPanel.add(scoreLabel);

        panel1.add(northPanel, BorderLayout.NORTH);


        board = new JLayeredPane();
        int boardheight = 11220;
        int boardwidth = 16830;
        board.setPreferredSize(new Dimension(boardwidth, boardheight)); //sarà la board di gioco, da capire con che Layout implementarla

        addScrollPane(board, panel1, 250, 285);

        //System.out.println("debug  "+frameManager.playersBoard.get(nickname).Board[50][50].getCardPointer().serialNumber);
        refreshboard();


        panel1.setVisible(true);

        JPanel lateralPanelSX = new JPanel(new GridBagLayout());
        lateralPanelSX.setMaximumSize(new Dimension(150, lateralPanelSX.getHeight()));
        lateralPanelSX.setPreferredSize(new Dimension(150, lateralPanelSX.getHeight()));


        setPlayerAvatar(lateralPanelSX);
        panel1.add(lateralPanelSX, BorderLayout.WEST);


        JPanel lateralPanelDX = new JPanel();
        lateralPanelDX.setLayout(new BoxLayout(lateralPanelDX, BoxLayout.Y_AXIS));
        lateralPanelDX.setPreferredSize(new Dimension(200, lateralPanelDX.getHeight()));


        //stampa delle carte obiettivo comuni


        commonPanel = new JPanel();
        commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.Y_AXIS));
        JLabel labelCommonObjectiveCards = createTextLabelFont("Common Objective Cards", 16);
        setBorderInsets(labelCommonObjectiveCards, 0,0,10,0);
        commonPanel.add(labelCommonObjectiveCards);

        showCommonObjectiveCard(serialCommonObjectiveCard, commonPanel, 10);

        labelPrivateObjectiveCards = createTextLabelFont("Private Objective Cards", 16);
        setBorderInsets(labelPrivateObjectiveCards, 0,0,10,0);
        commonPanel.add(labelPrivateObjectiveCards);

        List<Integer> privateObjectiveCard = List.of(serialPrivateObjectiveCard);

        showCommonObjectiveCard(privateObjectiveCard, commonPanel, 10);

        lateralPanelDX.add(commonPanel);

        JPanel tabelPanel = new JPanel();
        addScrollPane(resourceTable, tabelPanel, 200, 245);
        lateralPanelDX.add(tabelPanel);


        EnumMap<Resource, Integer> collectedResources = new EnumMap<>(Resource.class);


        for(Resource r: Resource.values()) {
            collectedResources.put(r, 0);
        }

        collectedResources.remove(Resource.NULL);
        collectedResources.remove(Resource.EMPTY);

        createTable(resourceTable, new String[]{"Resources", "Amount"}, collectedResources, logos, null, false);

        Random random2 = new Random();

        for(Resource r: Resource.values()) {
            int randomIndex = random2.nextInt(30);
            System.out.println("Resource: " + r + " amount: "+ randomIndex);
            collectedResources.put(r, randomIndex);
        }

        collectedResources.remove(Resource.NULL);
        collectedResources.remove(Resource.EMPTY);

        updateResourceTable(resourceTable, collectedResources, logos);


        handButton = createButton("Show Hand", 28);
        handButton.addActionListener(this);
        handButton.setEnabled(false);
        lateralPanelDX.add(handButton);


        decksButton = createButton("Show Decks", 27);
        decksButton.addActionListener(this);
        lateralPanelDX.add(decksButton);

        panel1.add(lateralPanelDX, BorderLayout.EAST);


        southPanel = new JPanel(new GridBagLayout());
        setBorderInsets(southPanel, 10, 0, 10, 0);

        choosePanel = new JPanel(new GridBagLayout());
        tokenPanel = new JPanel(new FlowLayout());
        checkBoxPanel = new JPanel(new FlowLayout());


        ArrayList<Boolean> b = new ArrayList<>();
        for(int i=0; i<hand.size(); i++)
            b.add(false);

        printHandOrDecksOnGUI(hand, b, tokenPanel, checkBoxPanel,choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap,0,0);

        namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));

        confirmButton = createButton("Confirm", 28);
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(this);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(10, 5));
        flipButton = createButton("Show Back", 28);
        flipButton.addActionListener(this);

        namePanel.add(confirmButton);
        namePanel.add(emptyPanel);
        namePanel.add(flipButton);



        GridBagConstraints gbc = createGridBagConstraints(0,0);
        gbc.anchor = GridBagConstraints.WEST;

        southPanel.add(namePanel, gbc);
        southPanel.add(choosePanel, createGridBagConstraints(1,0));


        decksPanel = new JPanel(new GridBagLayout());

        decksPanel = new JPanel(); //al posto di choosePanel
        resourceImagePanel = new JPanel(new FlowLayout()); //al posto di tokenPanel
        resourceCheckBoxPanel = new JPanel(new FlowLayout());

        decksPanel = new JPanel(new GridBagLayout()); //invocare qui metodo
        goldImagePanel = new JPanel(new FlowLayout()); //al posto di tokenPanel
        goldCheckBoxPanel = new JPanel(new FlowLayout());


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
        turnLable2 = createTextLabelFont("It's your turn", 20);
        turnLable2.setForeground(new Color(45, 114, 27));
        turnNamePanel2.add(turnLable2);
        northPanel2.add(turnNamePanel2);

        JLabel tokenLabel2 = new JLabel(createPlayableTokenImageIcon(token, 50));
        northPanel2.add(tokenLabel2);

        northPanel2.add(Box.createHorizontalGlue());
        northPanel2.add(Box.createHorizontalGlue());

        scoreLabel2 = createTextLabelFont("Score: " + 0, 30);
        setBorderInsets(scoreLabel2, 0,0,0,30);
        northPanel2.add(scoreLabel2);

        panel2.add(northPanel2, BorderLayout.NORTH);


        tokenManager = new TokenManager();
        tokenManager.setOpaque(false);

        tokenManager.setTokenInGame(tokenInGame);
        tokenManager.initializeDataPlayer();

        for(String player : playersScore.keySet()){
            tokenManager.updatePlayerScore(player, playersScore.get(player));
        }







        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);

        JTable table = new JTable();
        createTable(table, new String[]{"Token", "player"}, positionPlayerMap, listTokenInGame, tokenInGame, true);
        addScrollPane(table, tablePanel, 200, 180);
        panel2.add(tablePanel, BorderLayout.EAST);



        panel2.add(tokenManager);
        



        setVisible(true);
    }



    //TODO: ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    public void refreshboard(){
         /*board.removeAll();
        int boardheight = 11220;
        int boardwidth = 16830;

        for (int i = 10; i < 80; i++) {
            for (int j = 10; j < 80; j++) {
                if ((i + j) % 2 == 0) {
                    if (frameManager.playersBoard.get(nickname).Board[i][j] != null) {
                        addImageToLayeredPane(board, getDirectory(frameManager.playersBoard.get(nickname).Board[i][j].getCardPointer().serialNumber, frameManager.playersBoard.get(nickname).Board[i][j].isFlipped), boardwidth / 2 + ((i - 50) * 152), boardheight / 2 + ((j - 50) * 78), frameManager.playersBoard.get(nickname).Board[i][j].weight);
                    } else {
                        for (Coordinates coordinates : frameManager.availablesCells) {
                            if (i == coordinates.getX() && j == coordinates.getY()) {
                                addButtonToLayeredPane(board, boardwidth / 2 + ((i - 50) * 152), boardheight / 2 + ((j - 50) * 78), -1);
                            }

                        }
                    }
                }
            }
        }
        board.revalidate();
        board.repaint();*/
    }

    public String getDirectory(int serialnumber,boolean isflipped){
        if(serialnumber>0 && serialnumber<=40){
            if(!isflipped){
                return RESOURCE_DIR+"/resource_card_front/resource_card_front_"+serialnumber+".png";
            }else{
                return RESOURCE_DIR+"/resource_card_back/resource_card_back_"+serialnumber+".png";
            }
        } else if(serialnumber<=80){
            if(!isflipped){
                return GOLD_DIR+"/gold_card_front/gold_card_front_"+serialnumber+".png";
            }else{
                return GOLD_DIR+"/gold_card_back/gold_card_back_"+serialnumber+".png";
            }
        } else if(serialnumber<=86){
            if(!isflipped){
                return STARTER_DIR+"/starter_card_front/starter_card_front_"+serialnumber+".png";
            }else{
                return STARTER_DIR+"/starter_card_back/starter_card_back_"+serialnumber+".png";
            }
        } else return null;
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

    private void addButtonToLayeredPane(JLayeredPane layeredPane, int x, int y,int weight) {
        JButton button=new JButton();
        button.addActionListener(this);

        Coordinates corner = new Coordinates(((x-(16830/2))/152)+50,((y-(11220/2))/78)+50);
        buttonMap.put(corner,button);

        setBoxComponentSize(button, 192, 132);

        button.setVisible(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(Color.yellow,4));
        button.setBounds(x,y,198,132);//card sizes

        layeredPane.add(button,weight);
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
            return Integer.MAX_VALUE; // Ritorna un valore alto se non ci sono numeri nel nome del file
        }
    }


    private static JTable createTable(String[] columnNames, Object[][] data) {
        //ImageIconTableModel model = new ImageIconTableModel(data, columnNames);
        JTable table = new JTable(new DefaultTableModel());
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageIconRenderer());

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        table.setRowHeight(30);

        return table;
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

    public <K extends Enum<K>, V> void createTable(JTable table, String[] columnsNames, EnumMap<K, V> mapInInput, ArrayList<String> logosPath, Map<V, TokenColor> conversionMap, boolean b) {
        ImageIconTableModel<K, V> model = new ImageIconTableModel<>(columnsNames, mapInInput, logosPath, conversionMap);

        int k = 0;
        if(b){
            ArrayList<Object> newColumnDataLeft = new ArrayList<>();
            //String maxScorePlayer = playersScore.entrySet().stream().max((p1, p2) -> Integer.compare(p1.getValue(), p2.getValue())).map(n -> n.getKey()).stream().findFirst().orElseThrow();
            String maxScorePlayer = playersScore.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).stream().findFirst().orElseThrow();

            for(String player : playersScore.keySet()){
                if(player.equals(maxScorePlayer)){
                    newColumnDataLeft.add(true);
                } else{
                    newColumnDataLeft.add(false);
                }
            }

            model.addColumnOnLeft(" ", newColumnDataLeft);
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
        header.setPreferredSize(new Dimension(header.getWidth(), 35));


        table.setRowHeight(35);
    }

    public void updateResourceTable(JTable table, EnumMap<Resource, Integer> collectedResources, ArrayList<String> logosPath){
        for (Resource resource : collectedResources.keySet()) {
            table.setValueAt(collectedResources.get(resource), logosPath.indexOf(remappingResources(resource)), 1);
        }
    }


    //Da cambiare la posizione della corona quando cambia il giocatore con più punti
//    public void updateCrownImageInTable(JTable table, Map<String, Integer> playerScore, ArrayList<String> logosPath){
//        for (Resource resource : collectedResources.keySet()) {
//            table.setValueAt(collectedResources.get(resource), logosPath.indexOf(remappingResources(resource)), 1);
//        }
//    }


    private void addScrollPane(JComponent component, JPanel panel, int dimW, int dimH){
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setPreferredSize(new Dimension(dimW, dimH));
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
        button.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return button;
    }

    private GridBagConstraints createGridBagConstraints(int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;

        return gbc;
    }

    // TODO: da utilizzare per lateralPanelDX e lateralPanelSX
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
        } else if (e.getActionCommand().equals("Go to game")) {
            cardLayout.show(panelContainer, PANEL1);
        } else if (e.getActionCommand().equals("Show Back")) {
            confirmButton.setEnabled(false);
            flipButton.setText("Show Front");
            refresh();
            handSerialNumberCheckBoxMap.clear();
            flipToSend = true;


            ArrayList<Boolean> b = new ArrayList<>();
            for(int i=0; i<hand.size(); i++)
                b.add(true);

            printHandOrDecksOnGUI(hand, b, tokenPanel, checkBoxPanel, choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap, 0,0);

        }else if (e.getActionCommand().equals("Show Front") || e.getActionCommand().equals("Show Hand")) {
            confirmButton.setEnabled(false);
            flipButton.setText("Show Back");
            decksButton.setEnabled(true);
            handButton.setEnabled(false);

            refresh();
            refreshDecksPanels();
            handSerialNumberCheckBoxMap.clear();

            ArrayList<Boolean> b = new ArrayList<>();
            for(int i=0; i<hand.size(); i++)
                b.add(false);

            printHandOrDecksOnGUI(hand, b, tokenPanel, checkBoxPanel, choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap, 0,0);

            if(checkingHandWhileDrawing) {
                handLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(false));
            } else {
                flipButton.setEnabled(true);
                flipToSend = false;
            }

            southPanel.remove(decksPanel);
            southPanel.add(choosePanel, createGridBagConstraints(1,0));

        } else if (e.getActionCommand().equals("Confirm") || e.getActionCommand().equals("Show Decks")){
            confirmButton.setEnabled(false);
            flipButton.setEnabled(false);
            decksButton.setEnabled(false);
            handButton.setEnabled(true);

            refresh();
            refreshDecksPanels();
            drawableSerialNumberCheckBoxMap.clear();

            printHandOrDecksOnGUI(resourceCardsAvailable.keySet().stream().toList(),resourceCardsAvailable.values().stream().toList(), tokenPanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 0,0);
            printHandOrDecksOnGUI(goldCardsAvailable.keySet().stream().toList(),goldCardsAvailable.values().stream().toList(), tokenPanel, checkBoxPanel, choosePanel, buttonGroupDecks, decksLabelCheckBoxMap, drawableSerialNumberCheckBoxMap, 1,0);

//            southPanel.remove(choosePanel);
//            southPanel.add(decksPanel, createGridBagConstraints(1,0));

            if (e.getActionCommand().equals("Show Decks")) {
                if (checkingHandWhileDrawing) {
                    decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(true));
                } else {
                    decksLabelCheckBoxMap.values().forEach(checkBox -> checkBox.setEnabled(false));
                }
            } else if (e.getActionCommand().equals("Confirm")) {
                boardButtonFlag = false;
                confirmButton.setText("Draw");


                serialTosend = handSerialNumberCheckBoxMap.entrySet()
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


                System.out.println(serialTosend);
                System.out.println(flipToSend);

                hand = hand.stream().filter(c -> c!=serialTosend).toList();


                checkingHandWhileDrawing = true;
            }
        } else if (e.getActionCommand().equals("Draw")){
            boardButtonFlag = true;
            checkingHandWhileDrawing = false;
            try {
                serialTosend = drawableSerialNumberCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().checkBox.isSelected())
                        .findFirst()
                        .orElseThrow()
                        .getKey();

                //invio al server della carta da pescare
                System.out.println(serialTosend);
                List<Integer> temp = new ArrayList<>(hand);
                temp.add(serialTosend);

                hand = temp;


                confirmButton.setText("Confirm");
                confirmButton.setEnabled(false);
                flipButton.setEnabled(true);
                flipButton.setText("Show Back");
                decksButton.setEnabled(true);

                refresh();
                refreshDecksPanels();
                handSerialNumberCheckBoxMap.clear();

                ArrayList<Boolean> b = new ArrayList<>();
                for(int i=0; i<hand.size(); i++)
                    b.add(false);

                printHandOrDecksOnGUI(hand, b, tokenPanel, checkBoxPanel, choosePanel, buttonGroup, handLabelCheckBoxMap, handSerialNumberCheckBoxMap, 0,0);

                southPanel.remove(decksPanel);
                southPanel.add(choosePanel, createGridBagConstraints(1,0));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid card","ErrorMsg: ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshDecksPanels(){
        resourceImagePanel.removeAll();
        goldImagePanel.removeAll();
        resourceCheckBoxPanel.removeAll();
        goldCheckBoxPanel.removeAll();
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



    public void setNickname(String nickname) {
        this.nickname = nickname;
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


    public JLabel getTurnLable() {
        return turnLable;
    }



    public Map<String, Position> getPlayerPosition() { //non presente in MainPage
        return playerPosition;
    }

    public void setPlayerPosition(Map<String, Position> playerPosition) { //non presente in MainPage
        this.playerPosition = playerPosition;
    }


    public void setPlayerAvatar(JPanel lateralPanelSX){
        Random random = new Random();
        int y2 = 0;

        for (String player: playerPosition.keySet()) {
            int randomIndex = random.nextInt(avatarsLogo.size());
            String selectedAvatar = avatarsLogo.get(randomIndex);
            avatarsLogo.remove(randomIndex);
            int y1 = 0;

            JPanel playerAvatarPanel = new JPanel(new GridBagLayout());
            playerAvatarPanel.setOpaque(false);
            JLabel playerNameLabel = createTextLabelFont(player, 16);
            GridBagConstraints gbc = createGridBagConstraints(0,y1);
            gbc.anchor = GridBagConstraints.CENTER;
            playerAvatarPanel.add(playerNameLabel, gbc);
            gbc.gridy++;
            JLabel playerPositionLabel = createTextLabelFont(playerPosition.get(player).toString().toLowerCase(), 12);
            setBorderInsets(playerPositionLabel, 2, 0,5, 0);
            playerPositionLabel.setForeground(Color.GRAY);
            playerAvatarPanel.add(playerPositionLabel, gbc);
            gbc.gridy++;
            setBorderInsets(playerAvatarPanel, 15, 0, 15, 0);
            JLabel avatar = new JLabel(createResizedTokenImageIcon(selectedAvatar, 100));
            playerAvatarPanel.add(avatar, gbc);
            gbc.gridy++;


//            AvatarData avatarData = new AvatarData(player, selectedAvatar);
//            positionToPlayerData.put(playerPosition.get(player), avatarData);


            lateralPanelSX.add(playerAvatarPanel, createGridBagConstraints(0,y2));
            y2++;
        }
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
            if(myTurn) {
                jCheckBox.addItemListener(e -> {
                    boolean anySelected = labelCheckBoxMap.values().stream().anyMatch(JCheckBox::isSelected);
                    confirmButton.setEnabled(anySelected);
                });

                jCheckBox.addActionListener(e -> {
                    //setButtonVisible();
                    labelCheckBoxMap.keySet().forEach(k -> setCompoundBorderInsets(k, 0, 30, 0, 30, "ALL", Color.BLACK, 1));

                    setCompoundBorderInsets(labelCheckBoxMap.entrySet()
                            .stream()
                            .filter(en -> en.getValue().equals(e.getSource()))
                            .findFirst()
                            .orElseThrow()
                            .getKey(), 0, 30, 0, 30, "ALL", new Color(255, 240, 1), 4);
                });
            }

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
}
