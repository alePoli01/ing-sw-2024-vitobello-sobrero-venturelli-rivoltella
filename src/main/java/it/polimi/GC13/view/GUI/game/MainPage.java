package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.TokenColor;
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
import java.util.stream.Stream;


public class MainPage extends JFrame implements ActionListener, CardManager, WaitingLobby {

    private record CardData(JLabel label, JCheckBox checkBox) {}

    private FrameManager frameManager;

    private String nickname;
    private TokenColor token;
    private ArrayList<String> avatars = new ArrayList<>(Arrays.asList(FUNGI_JUDGE_DIR, ANIMAL_JUDGE_DIR, PLANT_JUDGE_DIR, INSECT_JUDGE_DIR));

    private final JPanel panelContainer;
    private JPanel choosePanel;

    private List<TokenColor> tokenColorList;
    private JPanel board;
    JPanel tokenPanel;
    JPanel namePanel;
    JPanel checkBoxPanel;
    JPanel commonPanel;
    ButtonGroup buttonGroup;
    Map<JLabel, JCheckBox> labelCheckBoxMap;

    Map<Integer, CardData> serialNumerCheckBoxMap = new HashMap<>();

    private JButton confirmButton;
    private JButton flipButton;

    int progress = 0;
    int colorIndex = 0;

    final static String PANEL1 = "Game";
    final static String PANEL2 = "Scoreboard";

    private JLabel turnLable;
    Map<Integer, ArrayList<String>> handImageIcon;
    //private Map<String, Position> playerPositions;

/*
    private ArrayList<JLabel> cardLabel;
    private TokenManager tokenManager;

*/

    public MainPage(List<TokenColor> tokenColorList) {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1700, 900); //for debugging
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
            setBorderInsets(tokenLabelText, 30, 124, 80, 120);
            namePanel.add(tokenLabelText);

            JCheckBox jCheckBox = new JCheckBox(tokenColor.toString().toLowerCase());
            jCheckBox.setFocusPainted(false);
            jCheckBox.setBorderPainted(false);
            jCheckBox.setForeground(panelContainer.getBackground());
            buttonGroup.add(jCheckBox);
            setBorderInsets(jCheckBox, 0, 140, 70, 125);
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
            labelCheckBoxMap.put(tokenLabelText, jCheckBox);
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

        JLabel titleLabel = createTextLabelFont("SETUP START CARD: ", 64);
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
        panelContainer.add(confirmButton, createGridBagConstraints(0, 4));
        confirmButton.addActionListener (e -> {
            boolean isFlipped;
            for (JCheckBox checkBox : labelCheckBoxMap.values()) {
                if (checkBox.isSelected()) {
                    isFlipped = !checkBox.getText().equals("Front");
                    if (e.getActionCommand().equals("Confirm")) {
                        //JOptionPane.showMessageDialog(this, "isFlipped: "+ isFlipped);
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


    private void refresh(){
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
                        jCheckBox = new JCheckBox("false");
                    } else {
                        startCardLabelText = createTextLabelFont("Back", 32);
                        jCheckBox = new JCheckBox("true");
                    }

                    setBorderInsets(startCardLabelText, 30, 230, 80, 230);
                    namePanel.add(startCardLabelText);

                    jCheckBox.setFocusPainted(false);
                    jCheckBox.setBorderPainted(false);
                    jCheckBox.setForeground(panelContainer.getBackground());
                    buttonGroup.add(jCheckBox);
                    setBorderInsets(jCheckBox, 0, 140, 70, 125);
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


    //public void setupObjectiveCard(List<Integer> serialCommonObjectiveCard, List<Integer> privateObjectiveCards){
    public void setupObjectiveCard(List<Integer> privateObjectiveCards){
        getContentPane().remove(panelContainer);
        JScrollPane scrollPane = new JScrollPane(panelContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setOpaque(false);
        getContentPane().add(scrollPane);


        JLabel setupLabel = createTextLabelFont("setup phase [3/3] ", 28); //20
        setBorderInsets(setupLabel, 20, 0, 40, 0);
        panelContainer.add(setupLabel, createGridBagConstraints(0, 0));

        JLabel titleLabel1 = createTextLabelFont("SETUP OBJECTIVE CARD: ", 64); //50
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
        JPanel panelButton = new JPanel();
        panelButton.setOpaque(false);
        panelButton.add(confirmButton, createGridBagConstraints(0, 6));
        setBorderInsets(panelButton, 0,10,30, 10);
        panelContainer.add(panelButton, createGridBagConstraints(0, 6));
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
        namePanel.removeAll();
        refresh();

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
        turnLable = createTextLabelFont("", 14);
        turnNamePanel.add(turnLable);
        northPanel.add(turnNamePanel);

        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(Box.createHorizontalGlue());

        JLabel rightLabel = new JLabel(createPlayableTokenImageIcon(token, 50));
        northPanel.add(rightLabel);

        panel1.add(northPanel, BorderLayout.NORTH);


        //da verificarne il funzionamento
        board = new JPanel(new GridLayout(100, 100, 70, 100)); //sarà la board di gioco, da capire con che Layout implementarla
        board.setOpaque(false);
        addScrollPane(board, panel1); //DA VERIFICARE SE ME LO METTE IN BORDERLAYOUT.CENTER


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
        addScrollPane(table, lateralPanelDX);
        panel1.add(lateralPanelDX, BorderLayout.EAST);


        JPanel southPanel = new JPanel(new FlowLayout());
        setBorderInsets(southPanel, 10, 0, 10, 0);




        handImageIcon = showHand(frameManager.getHand());

        for(Map.Entry<Integer, ArrayList<String>> entry : handImageIcon.entrySet()){
            String frontIcon = entry.getValue().getLast();
            Image img = new ImageIcon(frontIcon).getImage();
            JLabel imageLabel = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/6 , img.getHeight(null)/6, Image.SCALE_SMOOTH)));
            setCompoundBorderInsets(imageLabel, 0, 30, 0, 30, "ALL", Color.BLACK, 1);
            tokenPanel.add(imageLabel);

            JCheckBox jCheckBox = new JCheckBox("false");
            jCheckBox.setFocusPainted(false);
            jCheckBox.setBorderPainted(false);
            jCheckBox.setForeground(panelContainer.getBackground());
            buttonGroup.add(jCheckBox);
            setBorderInsets(jCheckBox, 60, 100, 60, 100);
            jCheckBox.setOpaque(false);
            jCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    labelCheckBoxMap.keySet().forEach(k -> setCompoundBorderInsets(k, 0, 30, 0, 30, "ALL", Color.BLACK, 1));

                    setCompoundBorderInsets(labelCheckBoxMap.entrySet()
                            .stream()
                            .filter(en -> en.getValue().equals(e.getSource()))
                            .findFirst()
                            .orElseThrow()
                            .getKey(), 0, 30, 0, 30, "ALL", new Color(255, 240, 1), 4);
                }
            });
            checkBoxPanel.add(jCheckBox);
            checkBoxPanel.setOpaque(false);
            labelCheckBoxMap.put(imageLabel, jCheckBox);

            CardData record = new CardData(imageLabel, jCheckBox);
            serialNumerCheckBoxMap.put(entry.getKey(), record);
        }

        choosePanel.add(tokenPanel, createGridBagConstraints(0,0));
        choosePanel.add(checkBoxPanel, createGridBagConstraints(0,0));


        namePanel =  new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));

        confirmButton = createButton("Confirm", 32);
        confirmButton.addActionListener(this);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(200,5));
        flipButton = createButton("Show Back", 32);
        flipButton.addActionListener(this);

        namePanel.add(confirmButton);
        namePanel.add(emptyPanel);
        namePanel.add(flipButton);

        choosePanel.add(namePanel, createGridBagConstraints(1,0));

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





        //goldDeck.setPreferredSize(new Dimension(150, 40));



        //PAGINA 2: SCOREBOARD
        JPanel buttonGamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        //buttonGamePanel.setOpaque(false);
        JButton buttonToMainPage = createButton("Go to game", 20);
        setBoxComponentSize(buttonToMainPage, 180,40);
        buttonToMainPage.addActionListener(this);
        buttonGamePanel.add(buttonToMainPage);
        panel2.add(buttonGamePanel, BorderLayout.NORTH);


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


    private static void addScrollPane(JComponent component, JPanel panel){
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setPreferredSize(new Dimension(250, 285));
        panel.add(scrollPane);
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


            for(Map.Entry<Integer, ArrayList<String>> entry : handImageIcon.entrySet()){
                String backIcon = entry.getValue().getFirst();
                Image img = new ImageIcon(backIcon).getImage();
                JLabel imageLabel = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/6 , img.getHeight(null)/6, Image.SCALE_SMOOTH)));
                setCompoundBorderInsets(imageLabel, 0, 30, 0, 30, "ALL", Color.BLACK, 1);
                tokenPanel.add(imageLabel);

                JCheckBox jCheckBox;
                jCheckBox = new JCheckBox("true");
                jCheckBox.setFocusPainted(false);
                jCheckBox.setBorderPainted(false);
                jCheckBox.setForeground(panelContainer.getBackground());
                buttonGroup.add(jCheckBox);
                setBorderInsets(jCheckBox, 60, 100, 60, 100);
                jCheckBox.setOpaque(false);
                jCheckBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        labelCheckBoxMap.keySet().forEach(k -> setCompoundBorderInsets(k, 0, 30, 0, 30, "ALL", Color.BLACK, 1));

                        setCompoundBorderInsets(labelCheckBoxMap.entrySet()
                                .stream()
                                .filter(en -> en.getValue().equals(e.getSource()))
                                .findFirst()
                                .orElseThrow()
                                .getKey(), 0, 30, 0, 30, "ALL", new Color(255, 240, 1), 4);
                    }
                });                checkBoxPanel.add(jCheckBox);
                checkBoxPanel.setOpaque(false);
                labelCheckBoxMap.put(imageLabel, jCheckBox);


                /*CardData record = new CardData(imageLabel, jCheckBox);
                serialNumerCheckBoxMap.put(entry.getKey(), record);*/

            }

        }  else if (e.getActionCommand().equals("Show Front")) {
            flipButton.setText("Show Back");
            refresh();

            for(Map.Entry<Integer, ArrayList<String>> entry : handImageIcon.entrySet()) {
                String frontIcon = entry.getValue().getLast();
                Image img = new ImageIcon(frontIcon).getImage();
                JLabel imageLabel = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null) / 6, img.getHeight(null) / 6, Image.SCALE_SMOOTH)));
                setCompoundBorderInsets(imageLabel, 0, 30, 0, 30, "ALL", Color.BLACK, 1);
                tokenPanel.add(imageLabel);

                JCheckBox jCheckBox;
                jCheckBox = new JCheckBox("false");
                jCheckBox.setFocusPainted(false);
                jCheckBox.setBorderPainted(false);
                jCheckBox.setForeground(panelContainer.getBackground());
                buttonGroup.add(jCheckBox);
                setBorderInsets(jCheckBox, 60, 100, 60, 100);
                jCheckBox.setOpaque(false);
                jCheckBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        labelCheckBoxMap.keySet().forEach(k -> setCompoundBorderInsets(k, 0, 30, 0, 30, "ALL", Color.BLACK, 1));

                        setCompoundBorderInsets(labelCheckBoxMap.entrySet()
                                .stream()
                                .filter(en -> en.getValue().equals(e.getSource()))
                                .findFirst()
                                .orElseThrow()
                                .getKey(), 0, 30, 0, 30, "ALL", new Color(255, 240, 1), 4);
                    }
                });                checkBoxPanel.add(jCheckBox);
                checkBoxPanel.setOpaque(false);
                labelCheckBoxMap.put(imageLabel, jCheckBox);



            }
        } else if (e.getActionCommand().equals("Confirm")) {
            //inserisce l'immagine sul campo
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
            try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f->f.getFileName().toString().contains(serialCard.toString()))){
                ArrayList<String> cardIconList = new ArrayList<>();
                paths.forEach(path-> {
                    cardIconList.add(String.valueOf(path));
                });
                handIconsPaths.put(serialCard, cardIconList);
            } catch (IOException e){
                JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
            }
        });
        return handIconsPaths;
    }





}