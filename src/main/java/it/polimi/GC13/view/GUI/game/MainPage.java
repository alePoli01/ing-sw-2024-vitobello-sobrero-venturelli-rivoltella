package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.network.messages.fromclient.ChoosePrivateObjectiveCardMessage;
import it.polimi.GC13.network.messages.fromclient.PlaceCardMessage;
import it.polimi.GC13.network.messages.fromclient.PlaceStartCardMessage;
import it.polimi.GC13.network.messages.fromclient.TokenChoiceMessage;
import it.polimi.GC13.view.GUI.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MainPage extends JFrame implements ActionListener, CardManager, WaitingLobby {

    public record CardData(JLabel label, JCheckBox checkBox) {}

    private FrameManager frameManager;

    private String nickname;
    private TokenColor token;
    private final ArrayList<String> avatars = new ArrayList<>(Arrays.asList(FUNGI_JUDGE_DIR, ANIMAL_JUDGE_DIR, PLANT_JUDGE_DIR, INSECT_JUDGE_DIR));

    private final JPanel panelContainer;
    private JPanel choosePanel;
    private boolean flipToSend= false;
    private int serialTosend=-1;
    private List<TokenColor> tokenColorList;
    private JLayeredPane board;
    private JPanel tokenPanel;
    private JPanel namePanel;
    private JPanel checkBoxPanel;
    private JPanel commonPanel;
    private ButtonGroup buttonGroup;
    private Map<JLabel, JCheckBox> labelCheckBoxMap;
    private Map<Coordinates,JButton> buttonMap = new HashMap<>();
    private Map<Integer, CardData> serialNumerCheckBoxMap = new HashMap<>();
    //private Map <Integer,JCheckBox> mapOfCheckedBox = new HashMap<>();
    private JButton confirmButton;
    private JButton flipButton;
    private Coordinates cordToSend=null;

    private Timer timer;
    int progress = 0;
    int colorIndex = 0;
    int buttonplaced = 200;
    final static String PANEL1 = "Game";
    final static String PANEL2 = "Scoreboard";
    private int flag = -1;

    private JLabel turnLable;
    private JLabel turnLable2;
    /*private JLabel scoreLabel;
    private JLabel scoreLabel2;*/

    private TokenManager tokenManager;


    /*
        private ArrayList<JLabel> cardLabel;
        private TokenManager tokenManager;

    */

    public MainPage(List<TokenColor> tokenColorList) {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setSize(1700, 900); //for debugging
        setResizable(false);

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
        confirmButton.setBackground(new Color(232, 221, 183, 255));
        panelContainer.add(confirmButton, createGridBagConstraints(0, 3));

        confirmButton.addActionListener (e -> {
            String tokenColorChosen;
            for (JCheckBox checkBox : labelCheckBoxMap.values()) {
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
        buttonGroup = new ButtonGroup();
        choosePanel.add(checkBoxPanel, gbc2);

        namePanel = new JPanel(new FlowLayout()); //pannello dove inserire i nomi dei token
        namePanel.setOpaque(false);
        choosePanel.add(namePanel, createGridBagConstraints(0, 1));

        labelCheckBoxMap = new HashMap<>();

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
            jCheckBox.setForeground(panelContainer.getBackground());
            setBorderInsets(jCheckBox, 120, 120, 120, 120);
            buttonGroup.add(jCheckBox);
            jCheckBox.setOpaque(false);

            checkBoxPanel.add(jCheckBox);
            labelCheckBoxMap.put(tokenLabelText, jCheckBox);

            if(!this.tokenColorList.contains(tokenColor)){
                jCheckBox.setEnabled(false);
                tokenLabelText.setForeground(Color.GRAY);
                labelCheckBoxMap.remove(tokenLabelText, jCheckBox);
            } else {
                jCheckBox.addActionListener(e -> {
                    confirmButton.setEnabled(labelCheckBoxMap.values().stream().anyMatch(AbstractButton::isSelected));

                    labelCheckBoxMap.keySet().forEach(k -> k.setForeground(Color.BLACK));

                    labelCheckBoxMap.entrySet()
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

    private ImageIcon createResizedTokenImageIcon(String tokenImagePath, int dim) {
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
        setBorderInsets(startCardLabel, 0, 0, 90, 0);
        panelContainer.add(startCardLabel, createGridBagConstraints(0, 2));


        panelContainer.add(choosePanel, createGridBagConstraints(0,3));
        namePanel.removeAll();
        refresh();

        showStartCard(frameManager.getHand().getFirst());


        confirmButton.setText("Confirm");
        confirmButton.setBackground(new Color(232, 221, 183, 255));
        panelContainer.add(confirmButton, createGridBagConstraints(0, 4));
        confirmButton.addActionListener (e -> {
            boolean isFlipped;
            for (JCheckBox checkBox : labelCheckBoxMap.values()) {
                if (checkBox.isSelected()) {
                    isFlipped = !checkBox.getText().equals("Front");
                    if (e.getActionCommand().equals("Confirm")) {
                        //JOptionPane.showMessageDialog(this, "isFlipped: "+ isFlipped);
                        System.out.println("la carta è stata inviata "+isFlipped);
                        frameManager.getVirtualServer().sendMessageFromClient(new PlaceStartCardMessage(isFlipped));
                        createLobby();
                        getContentPane().revalidate();
                        getContentPane().repaint();
                    }
                    break;
                }
            }
        });
    }


    public void refresh(){
        tokenPanel.removeAll();
        checkBoxPanel.removeAll();
        labelCheckBoxMap.clear();

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

    @Override
    public void showStartCard(Integer numberCard) {
        Path startDir = identifyPathCard(numberCard);

        if(startDir != null){
            try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().contains(numberCard.toString()))) {
                paths.forEach(path -> {
                    Image img = new ImageIcon(String.valueOf(path)).getImage();
                    JLabel startCardLabelImage = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/3 , img.getHeight(null)/3, Image.SCALE_SMOOTH)));
                    setCompoundBorderInsets(startCardLabelImage, 0, 100, 0, 100, "ALL", Color.BLACK, 1);
                    tokenPanel.add(startCardLabelImage);

                    JLabel startCardLabelText;
                    JCheckBox jCheckBox;
                    if(String.valueOf(path).contains("front")){
                        startCardLabelText = createTextLabelFont("Front", 32);
                        jCheckBox = new JCheckBox("Front");
                    } else {
                        startCardLabelText = createTextLabelFont("Back", 32);
                        jCheckBox = new JCheckBox("Back");
                    }

                    setBorderInsets(startCardLabelText, 30, 230, 80, 230);
                    namePanel.add(startCardLabelText);

                    jCheckBox.setFocusPainted(false);
                    jCheckBox.setBorderPainted(false);
                    jCheckBox.setForeground(panelContainer.getBackground());
                    buttonGroup.add(jCheckBox);
                    setBorderInsets(jCheckBox, 100, 150, 100, 150);
                    jCheckBox.setOpaque(false);
                    jCheckBox.addActionListener(e -> {
                        confirmButton.setEnabled(labelCheckBoxMap.values().stream().anyMatch(AbstractButton::isSelected));

                        labelCheckBoxMap.keySet().forEach(k -> k.setForeground(Color.BLACK));

                        labelCheckBoxMap.entrySet()
                                .stream()
                                .filter(en -> en.getValue().equals(e.getSource()))
                                .findFirst()
                                .orElseThrow()
                                .getKey()
                                .setForeground(Color.RED);
                    });
                    checkBoxPanel.add(jCheckBox);
                    if(jCheckBox.getText().equals("true")){
                        JPanel emptyPanel = new JPanel();
                        emptyPanel.setOpaque(false);
                        setBorderInsets(emptyPanel, 10, 85, 10, 85);
                        checkBoxPanel.add(emptyPanel);
                    }
                    labelCheckBoxMap.put(startCardLabelText, jCheckBox);
                });

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void showCommonObjectiveCard(Integer numberCard) {
        Path startDir = identifyPathCard(numberCard);

        if(startDir != null){
            try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().contains(numberCard.toString())).filter(f -> f.getFileName().toString().contains("front"))) {
                paths.forEach(path -> {
                    Image img = new ImageIcon(String.valueOf(path)).getImage();
                    JLabel startCardLabelImage = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null) / 3, img.getHeight(null) / 3, Image.SCALE_SMOOTH)));
                    setCompoundBorderInsets(startCardLabelImage, 0, 100, 30, 100, "ALL", Color.BLACK, 1);
                    commonPanel.add(startCardLabelImage);
                });
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void showPrivateObjectiveCard(Integer numberCard) {
        Path startDir = identifyPathCard(numberCard);

        if(startDir != null){
            try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().contains(numberCard.toString())).filter(f -> f.getFileName().toString().contains("front"))) {
                paths.forEach(path -> {
                    Image img = new ImageIcon(String.valueOf(path)).getImage();
                    JLabel startCardLabelImage = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/3 , img.getHeight(null)/3, Image.SCALE_SMOOTH)));
                    setCompoundBorderInsets(startCardLabelImage, 0, 100, 20, 100, "ALL", Color.BLACK, 1);
                    tokenPanel.add(startCardLabelImage);

                    JLabel startCardLabelText;
                    JCheckBox jCheckBox;

                    if(numberCard>=87 && numberCard<=94){
                        startCardLabelText = createTextLabelFont("Pattern", 32);
                    }else if(numberCard<=98){
                        startCardLabelText = createTextLabelFont("Tris Reign", 32);
                    }else {
                        startCardLabelText = createTextLabelFont("Objects", 32);
                    }

                    jCheckBox = new JCheckBox(numberCard.toString());
                    setBorderInsets(startCardLabelText, 0, 230, 80, 230);
                    namePanel.add(startCardLabelText);

                    jCheckBox.setFocusPainted(false);
                    jCheckBox.setBorderPainted(false);
                    jCheckBox.setForeground(panelContainer.getBackground());
                    buttonGroup.add(jCheckBox);
                    setBorderInsets(jCheckBox, 0, 140, 60, 125);
                    jCheckBox.setOpaque(false);
                    checkBoxPanel.add(jCheckBox);
                    labelCheckBoxMap.put(startCardLabelText, jCheckBox);
                });

                ActionListener actionListener = e -> {
                    confirmButton.setEnabled(labelCheckBoxMap.values().stream().anyMatch(AbstractButton::isSelected));

                    labelCheckBoxMap.keySet().forEach(k -> k.setForeground(Color.BLACK));

                    labelCheckBoxMap.entrySet()
                            .stream()
                            .filter(en -> en.getValue().equals(e.getSource()))
                            .findFirst()
                            .orElseThrow()
                            .getKey()
                            .setForeground(Color.RED);
                };

                for(JCheckBox c : labelCheckBoxMap.values()) c.addActionListener(actionListener);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
            }
        }
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
        panelContainer.add(label2, createGridBagConstraints(0,1));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300,30));
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


    //public void setupObjectiveCard(List<Integer> serialCommonObjectiveCard, List<Integer> privateObjectiveCards){
    public void setupObjectiveCard(List<Integer> privateObjectiveCards){
        getContentPane().remove(panelContainer);
        JScrollPane scrollPane = new JScrollPane(panelContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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

        //for(Integer commonCard : serialCommonObjectiveCard) {
        for(Integer commonCard : frameManager.getSerialCommonObjectiveCard()) {
            showCommonObjectiveCard(commonCard);
        }

        JLabel titleLabel2 = createTextLabelFont("Choose your private objective card: ", 32);
        setBorderInsets(titleLabel2, 40, 0, 40, 0);
        panelContainer.add(titleLabel2, createGridBagConstraints(0, 4));

        panelContainer.add(choosePanel, createGridBagConstraints(0,5));
        namePanel.removeAll();
        refresh();

        for(int privateCard : privateObjectiveCards) {
            showPrivateObjectiveCard(privateCard);
        }

        confirmButton.setText("Let's Roll");
        confirmButton.setBackground(new Color(232, 221, 183, 255));
        JPanel panelButton = new JPanel();
        panelButton.setOpaque(false);
        panelButton.add(confirmButton, createGridBagConstraints(0, 5));
        setBorderInsets(panelButton, 0,0,26, 0);
        panelContainer.add(panelButton, createGridBagConstraints(0, 5));
        confirmButton.addActionListener (e -> {
            int choice;
            for (JCheckBox checkBox : labelCheckBoxMap.values()) {
                if (checkBox.isSelected()) {
                    choice = Integer.parseInt(checkBox.getText());
                    if (e.getActionCommand().equals("Let's Roll")) {
                        //JOptionPane.showMessageDialog(this, "choice: "+ choice);
                        frameManager.getVirtualServer().sendMessageFromClient(new ChoosePrivateObjectiveCardMessage(choice));
                        createLobby();
                        getContentPane().revalidate();
                        getContentPane().repaint();
                    }
                    break;
                }
            }
        });
    }




    public void createGamePanel() {
        panelContainer.setLayout(new CardLayout());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setOpaque(false);

        //da vedere sta parte
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setBackground(new Color(237,230,188,255));

        BackgroundPanel scoreboard = new BackgroundPanel("src/main/resources/it/polimi/GC13/view/GUI/backgrounds/scoreboard.png", false);
        scoreboard.setOpaque(false);
        panel2.add(scoreboard, BorderLayout.CENTER);

        panelContainer.add(panel1, PANEL1);
        panelContainer.add(panel2, PANEL2);


        //pagina 1: Game
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
        setBorderInsets(northPanel, 15, 0, 15, 0);

        JButton buttonToScoreBoard = createButton("Go to scoreboard", 20);
        buttonToScoreBoard.setBackground(new Color(232, 221, 183, 255));
        buttonToScoreBoard.addActionListener(this);
        setBoxComponentSize(buttonToScoreBoard, 180, 50);
        northPanel.add(buttonToScoreBoard);

        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(Box.createHorizontalGlue());

        JPanel turnNamePanel = new JPanel();
        turnNamePanel.setOpaque(false);
        turnNamePanel.setLayout(new BoxLayout(turnNamePanel, BoxLayout.Y_AXIS));
        JLabel label = createTextLabelFont(nickname, 32);
        turnNamePanel.add(label);
        turnLable = createTextLabelFont("", 20);
        turnNamePanel.add(turnLable);
        northPanel.add(turnNamePanel);

        JLabel tokenLabel = new JLabel(createPlayableTokenImageIcon(token, 50));
        northPanel.add(tokenLabel);

        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(Box.createHorizontalGlue());

        /*scoreLabel = createTextLabelFont("", 30);
        setBorderInsets(scoreLabel, 0,0,0,30);
        northPanel.add(scoreLabel);*/

        panel1.add(northPanel, BorderLayout.NORTH);


        //da verificarne il funzionamento
        board = new JLayeredPane();
        int boardheight = 11220;
        int boardwidth = 16830;
        board.setPreferredSize(new Dimension(boardwidth, boardheight)); //sarà la board di gioco, da capire con che Layout implementarla

        addScrollPane(board, panel1, 250, 285);

        //System.out.println("debug  "+frameManager.playersBoard.get(nickname).Board[50][50].getCardPointer().serialNumber);
        refreshBoard();


        panel1.setVisible(true);


        JPanel lateralPanelSX = new JPanel();

        lateralPanelSX.setLayout(new BoxLayout(lateralPanelSX, BoxLayout.Y_AXIS));
        setPlayerAvatar(lateralPanelSX);
        panel1.add(lateralPanelSX, BorderLayout.WEST);


        JPanel lateralPanelDX = new JPanel();

        String[] columnNames = {"Reigns/Objects", "counter"};
        Object[][] data = {
                {createResizedTokenImageIcon(FUNGI_LOGO_DIR, 30), 0},
                {createResizedTokenImageIcon(ANIMAL_LOGO_DIR, 30), 0},
                {createResizedTokenImageIcon(PLANT_LOGO_DIR, 30), 0},
                {createResizedTokenImageIcon(INSECT_LOGO_DIR, 30), 0},
                {createResizedTokenImageIcon(QUILL_LOGO_DIR, 30), 0},
                {createResizedTokenImageIcon(MANUSCRIPT_LOGO_DIR, 30), 0},
                {createResizedTokenImageIcon(INKWELL_LOGO_DIR,30), 0}
        };

        JTable table = createTable(columnNames, data);
        addScrollPane(table, lateralPanelDX, 250, 285);
        panel1.add(lateralPanelDX, BorderLayout.EAST);


        JPanel southPanel = new JPanel(new FlowLayout());
        setBorderInsets(southPanel, 1, 0, 1, 0);

        printHandGUI(false);

        namePanel =  new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));

        confirmButton = createButton("Confirm", 32);
        confirmButton.setBackground(new Color(232, 221, 183, 255));
        confirmButton.addActionListener(this);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(200,5));
        flipButton = createButton("Show Back", 32);
        flipButton.setBackground(new Color(232, 221, 183, 255));
        flipButton.addActionListener(this);

        namePanel.add(confirmButton);
        namePanel.add(emptyPanel);
        namePanel.add(flipButton);


        southPanel.add(namePanel, BorderLayout.SOUTH);
        southPanel.add(choosePanel, BorderLayout.SOUTH);



        /*JPanel deckPanel= new JPanel();
        deckPanel.setLayout(new BoxLayout(deckPanel, BoxLayout.Y_AXIS));

        JButton resourceDeck = createButton("Resource", 32);
        setBoxComponentSize(resourceDeck, 150, 40);
        deckPanel.add(resourceDeck);

        JPanel emptyPanel2 = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(200,5));
        deckPanel.add(emptyPanel2);

        JButton goldDeck = createButton("Gold", 32);
        setBoxComponentSize(goldDeck, 150, 40);
        deckPanel.add(goldDeck);


        southPanel.add(deckPanel);*/
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
        turnLable2 = createTextLabelFont("", 20);
        /*turnLable2.setForeground(new Color(45, 114, 27));
        turnNamePanel2.add(turnLable2);*/
        northPanel2.add(turnNamePanel2);

        JLabel tokenLabel2 = new JLabel(createPlayableTokenImageIcon(token, 50));
        northPanel2.add(tokenLabel2);

        northPanel2.add(Box.createHorizontalGlue());
        northPanel2.add(Box.createHorizontalGlue());

        /*scoreLabel2 = createTextLabelFont("Score: " + 0, 30);
        setBorderInsets(scoreLabel2, 0,0,0,30);
        northPanel2.add(scoreLabel2);*/

        panel2.add(northPanel2, BorderLayout.NORTH);

        /*tokenManager = new TokenManager();
        tokenManager.setOpaque(false);*/



        setVisible(true);



        //int numPlayer = game.numPlayer; //devo usare numPlayer o .getCurrNumPlayer() ?
/*
        tokenManager = new TokenManager();
        for(int i=0; i</*game.numPlayer*4; i++) {
            JLabel label = new JLabel();
            tokenManager.createToken(/*player loggato,* label);
            Image img = new ImageIcon(tokenManager.getTokenInGame().get(i).getDirectoryImage()).getImage();
            ImageIcon tokenIcon = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            //JLabel label = new JLabel(tokenIcon);
            tokenManager.getTokenInGame().get(i).getLabel().setIcon(tokenIcon);
            //label.setIcon(tokenIcon);
            tokenManager.getTokenInGame().get(i).getLabel().setBounds(tokenManager.getTokenInGame().get(i).getX(), tokenManager.getTokenInGame().get(i).getY(), tokenIcon.getIconWidth(), tokenIcon.getIconHeight());
            panel2.add(tokenManager.getTokenInGame().get(i).getLabel());
        }*/

        /*
        //PROVA MOVIMENTO TOKEN
        //movimento orizzontale
        tokenManager.moveHorizontal(tokenManager.getTokenInGame().get(0));

        //movimento verticale
        tokenManager.moveVertical(tokenManager.getTokenInGame().get(0));

        //movimento diagonale
        tokenManager.moveDiagonal(tokenManager.getTokenInGame().get(0));
        */

    }


    public void refreshBoard(){
        board.removeAll();
        int boardheight = 11220;
        int boardwidth = 16830;

        String available = frameManager.availablesCells.stream()
                .map(cell -> "(" + cell.getX() + ", " + cell.getY() + ")")
                .collect(Collectors.joining("\n"));
        System.out.println("Available cells in main page are:\n" + available + ".");
        //scorri le y
        for (int i = 10; i < 80; i++) {
            //scorri le x
            for (int j = 10; j < 80; j++) {
                if ((i + j) % 2 == 0) {
                    if (frameManager.playersBoard.get(nickname).Board[i][j] != null) {
                        addImageToLayeredPane(board, getDirectory(frameManager.playersBoard.get(nickname).Board[i][j].getCardPointer().serialNumber, frameManager.playersBoard.get(nickname).Board[i][j].isFlipped), boardwidth / 2 + ((j - 50) * 152),boardheight / 2 + ((i - 50) * 78) , frameManager.playersBoard.get(nickname).Board[i][j].weight);
                    } else {
                        for (Coordinates coordinates : frameManager.availablesCells) {

                            if (i == coordinates.getY() && j == coordinates.getX()) {
                                buttonplaced++;
                                addButtonToLayeredPane(board,boardwidth / 2 + ((j - 50) * 152) , boardheight / 2 + ((i - 50) * 78),buttonplaced);
                            }

                        }
                    }
                }
            }
        }
        buttonplaced=200;
        board.revalidate();
        board.repaint();
    }




    public void printHandGUI(boolean b){
        Map<Integer, ArrayList<String>> handImageIcon = showHand(frameManager.getHand());

        for(Map.Entry<Integer, ArrayList<String>> entry : handImageIcon.entrySet()){
            String flippedIcon;
            JCheckBox jCheckBox;
            if(!b){
                flippedIcon = entry.getValue().getLast();
                jCheckBox = new JCheckBox("false");
            } else {
                flippedIcon = entry.getValue().getFirst();
                jCheckBox = new JCheckBox("true");
            }
            Image img = new ImageIcon(flippedIcon).getImage();

            JLabel imageLabel = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/6 , img.getHeight(null)/6, Image.SCALE_SMOOTH)));
            setCompoundBorderInsets(imageLabel, 0, 30, 0, 30, "ALL", Color.BLACK, 1);
            tokenPanel.add(imageLabel);

            jCheckBox.setFocusPainted(false);
            jCheckBox.setBorderPainted(false);
            jCheckBox.setForeground(panelContainer.getBackground());
            buttonGroup.add(jCheckBox);
            setBorderInsets(jCheckBox, 60, 100, 60, 100);
            jCheckBox.setOpaque(false);
            jCheckBox.addActionListener(e -> {
                setButtonVisible();
                labelCheckBoxMap.keySet().forEach(k -> setCompoundBorderInsets(k, 0, 30, 0, 30, "ALL", Color.BLACK, 1));

                setCompoundBorderInsets(labelCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().equals(e.getSource()))
                        .findFirst()
                        .orElseThrow()
                        .getKey(), 0, 30, 0, 30, "ALL", new Color(255, 240, 1), 4);
            });
            checkBoxPanel.add(jCheckBox);
            checkBoxPanel.setOpaque(false);

            labelCheckBoxMap.put(imageLabel, jCheckBox);

            CardData record = new CardData(imageLabel, jCheckBox);
            serialNumerCheckBoxMap.put(entry.getKey(), record);

            choosePanel.add(tokenPanel, createGridBagConstraints(0,0));
            choosePanel.add(checkBoxPanel, createGridBagConstraints(0,0));

            choosePanel.revalidate();
            choosePanel.repaint();
        }
    }
    public void setButtonVisible(){
        for(JButton button:buttonMap.values()){
            button.setVisible(true);
        }
    }
    public void setButtonNOTVisible(){
        for(JButton button:buttonMap.values()){
            button.setVisible(false);
        }
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

    //da rivedere/fare merge con mio metodo
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
        } else if (serialnumber<=102){
            if(!isflipped){
                return null;
            }else{
                return null;
            }
        } else return null;
    }

    private static JTable createTable(String[] columnNames, Object[][] data) {
        ImageIconTableModel model = new ImageIconTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageIconRenderer());

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        table.setRowHeight(35);

        return table;
    }


    private void addScrollPane(JComponent component, JPanel panel, int dimW, int dimH){
        JScrollPane scrollPane = new JScrollPane(component);//aggiungi allo scrollpane la board
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
        } else if (e.getActionCommand().equals("Go to game")) {
            cardLayout.show(panelContainer, PANEL1);
        } else if (e.getActionCommand().equals("Show Back")) {
            flipButton.setText("Show Front");
            refresh();
            flipToSend=true;
            serialNumerCheckBoxMap.clear();

            printHandGUI(true);

        }  else if (e.getActionCommand().equals("Show Front")) {
            flipButton.setText("Show Back");
            refresh();

            flipToSend=false;
            serialNumerCheckBoxMap.clear();

            printHandGUI(false);

        } else if (e.getActionCommand().equals("Confirm")) {
            setButtonNOTVisible();
            refresh();
            try{
                serialTosend = serialNumerCheckBoxMap.entrySet()
                        .stream()
                        .filter(en -> en.getValue().checkBox.isSelected())
                        .findFirst()
                        .orElseThrow()
                        .getKey();
            }catch(NoSuchElementException ex){
                System.out.println("il serial to send non è stato aggiornato");}


            if(cordToSend==null || serialTosend == -1 ){
                System.out.println("errore seleziona la carta da inserire nella board");
            }else{
                System.out.println("INVIO CARTA    "+serialTosend+" "+flipToSend+" X:"+cordToSend.getX()+" y:"+cordToSend.getY());
                frameManager.getVirtualServer().sendMessageFromClient(new PlaceCardMessage(serialTosend,flipToSend, cordToSend.getX(), cordToSend.getY()));
            }
        }


        /* } else if (e.getActionCommand().equals("popup card")){
            PopupDialog popup = new PopupDialog(this);
            popup.setVisible(true);

            //testing
            /*Object[] options = {"Create Game", "Join Game"};
            int choice = JOptionPane.showOptionDialog(null, "There are existing games, choose: ", "Create Game / Join Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Tua mamma");
            } else if (choice == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null, "Quella troia");
            }*/
         else {
            for (JButton button : buttonMap.values()) {
                button.setIcon(null);
                button.setBorderPainted(true);
                if (e.getSource().equals(button)) {
                    try{
                        serialTosend = serialNumerCheckBoxMap.entrySet()
                                .stream()
                                .filter(en -> en.getValue().checkBox.isSelected())
                                .findFirst()
                                .orElseThrow()
                                .getKey();
                    }catch(NoSuchElementException ex){
                        System.out.println("il serial to send non è stato aggiornato");}

                    ImageIcon originalIcon = new ImageIcon(getDirectory(serialTosend,flipToSend));
                    Image scaledImage = originalIcon.getImage().getScaledInstance(originalIcon.getIconWidth() / 5, originalIcon.getIconHeight() / 5, Image.SCALE_SMOOTH); // Scala l'immagine
                    ImageIcon imageIcon = new ImageIcon(scaledImage);
                    button.setIcon(imageIcon);
                    button.setBorderPainted(false);
                    //button.setBorder(BorderFactory.createLineBorder(new Color(230, 64, 50, 255),4));
                    try {
                        cordToSend = buttonMap.entrySet()
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

    public void setFrameManager(FrameManager frameManager) {
        this.frameManager = frameManager;
    }

    public JLabel getTurnLable() {
        return turnLable;
    }



    public void setPlayerAvatar(JPanel lateralPanelSX){
        Random random = new Random();

        for (String player: frameManager.getPlayerPositions().keySet()) {
            int randomIndex = random.nextInt(avatars.size());
            String selectedAvatar = avatars.get(randomIndex);
            avatars.remove(randomIndex);
            JLabel label = new JLabel(createResizedTokenImageIcon(selectedAvatar, 100));

            JPanel playerAvatarPanel = new JPanel();
            playerAvatarPanel.setOpaque(false);
            JPanel playerNamePositionPanel = new JPanel();
            playerNamePositionPanel.setOpaque(false);
            playerNamePositionPanel.setLayout(new BoxLayout(playerNamePositionPanel, BoxLayout.Y_AXIS));
            JLabel playerNameLabel = createTextLabelFont(player, 16);
            playerNamePositionPanel.add(playerNameLabel);
            JLabel playerPositionLabel = createTextLabelFont(frameManager.getPlayerPositions().get(player).toString().toLowerCase(), 12);
            playerPositionLabel.setForeground(Color.GRAY);
            playerNamePositionPanel.add(playerPositionLabel);
            playerAvatarPanel.add(playerNamePositionPanel);
            setBorderInsets(playerAvatarPanel, 5, 0, 15, 0);
            playerAvatarPanel.add(label);
            lateralPanelSX.add(playerAvatarPanel);
        }
    }


    public Map<Integer, ArrayList<String>> showHand(List<Integer> hand) {
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
                /*for(String x: firstFilesInSubfolders.values()){
                    System.out.println(x);
                }*/

                ArrayList<String> cardIconList = new ArrayList<>(firstFilesInSubfolders.values());
                System.out.println("lista dei path della carta "+serialCard+": "+cardIconList);
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


    public Timer getTimer() {
        return timer;
    }


    public JPanel getTokenPanel() {
        return tokenPanel;
    }

    public JButton getFlipButton() {
        return flipButton;
    }

    public boolean isFlipToSend() {
        return flipToSend;
    }

    public void setFlipToSend(boolean flipToSend) {
        this.flipToSend = flipToSend;
    }

    public JPanel getNamePanel() {
        return namePanel;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Map<Integer, CardData> getSerialNumerCheckBoxMap() {
        return serialNumerCheckBoxMap;
    }

    public JLabel getTurnLable2() {
        return turnLable2;
    }
}