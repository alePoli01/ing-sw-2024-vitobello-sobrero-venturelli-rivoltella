package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.messages.fromclient.PlaceStartCardMessage;
import it.polimi.GC13.network.messages.fromclient.TokenChoiceMessage;
import it.polimi.GC13.view.GUI.BackgroundPanel;
import it.polimi.GC13.view.GUI.FrameManager;

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
import java.util.stream.Stream;


public class MainPage extends JFrame implements ActionListener, CardManager {
    private final static String TOKEN_DIR = "src/main/utils/token/";
    private final static String P_TOKEN_DIR = TOKEN_DIR + "playableToken/";
    private final static String TOKEN_FILE_SUFFIX = "_token.png";
    private final static String GREY_TOKEN_FILE_NAME = "grey";

    private static final String RESOURCE_DIR = "src/main/utils/resource_card/";
    private static final String GOLD_DIR = "src/main/utils/gold_card/";
    private static final String STARTER_DIR = "src/main/utils/starter_card/";
    private static final String OBJECTIVE_DIR = "src/main/utils/objective_card/";

    private final JPanel panelContainer;
    private JPanel choosePanel;
    final static String PANEL1 = "Game";
    final static String PANEL2 = "Scoreboard";

    private FrameManager frameManager;

    private String nickname;
    private TokenColor token;
    private List<Integer> hand;


    JLabel starterCardFrontLabel;
    JLabel starterCardBackLabel;

    private List<TokenColor> tokenColorList;
    private JPanel board;
    JPanel tokenPanel;
    JPanel namePanel;
    JPanel checkBoxPanel;
    ButtonGroup buttonGroup;
    Map<JLabel, JCheckBox> tokenLabelCheckBox;
    private final JButton confirmButton;

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

        JLabel setupLabel = createTextLabelFont("setup phase [1/2] ", 32);
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
            for (JCheckBox checkBox : tokenLabelCheckBox.values()) {
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

        tokenLabelCheckBox = new HashMap<>();

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
            checkBoxPanel.add(jCheckBox);
            tokenLabelCheckBox.put(tokenLabelText, jCheckBox);
        });

        ActionListener actionListener = e -> {
            confirmButton.setEnabled(tokenLabelCheckBox.values().stream().anyMatch(AbstractButton::isSelected));

            tokenLabelCheckBox.keySet().forEach(k -> k.setForeground(Color.BLACK));

            tokenLabelCheckBox.entrySet()
                    .stream()
                    .filter(en -> en.getValue().equals(e.getSource()))
                    .findFirst()
                    .orElseThrow()
                    .getKey()
                    .setForeground(Color.RED);
        };

        for(JCheckBox c : tokenLabelCheckBox.values()) c.addActionListener(actionListener);
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
        JLabel setupLabel = createTextLabelFont("setup phase [2/2] ", 32);
        setBorderInsets(setupLabel, 0, 0, 60, 0);
        panelContainer.add(setupLabel, createGridBagConstraints(0, 0));

        JLabel titleLabel = createTextLabelFont("SETUP START CARD: ", 64);
        setBorderInsets(titleLabel, 0, 0, 60, 0);
        panelContainer.add(titleLabel, createGridBagConstraints(0, 1));

        JLabel startCardLabel = createTextLabelFont("Choose which side you would like to place your start card: ", 28);
        setBorderInsets(startCardLabel, 0, 0, 90, 0);
        panelContainer.add(startCardLabel, createGridBagConstraints(0, 2));


        panelContainer.add(choosePanel, createGridBagConstraints(0,3));
        refresh();

        //System.out.println(hand.getFirst());
        //JOptionPane.showMessageDialog(this, hand.getFirst() !=null? "hand: "+ hand.getFirst() : "Culo"); //non va

        if(hand!=null) showCard(hand.getFirst());
        else System.out.println("culo");


        panelContainer.add(confirmButton, createGridBagConstraints(0, 4));
        confirmButton.addActionListener (e -> {
            boolean isFlipped;
            for (JCheckBox checkBox : tokenLabelCheckBox.values()) {
                if (checkBox.isSelected()) {
                    isFlipped = !checkBox.getText().equals("Front");
                    if (e.getActionCommand().equals("Select")) {
                        //JOptionPane.showMessageDialog(null, "isFlipped: "+ isFlipped);
                        frameManager.getVirtualServer().sendMessageFromClient(new PlaceStartCardMessage(isFlipped));
                    }
                    break;
                }
            }
        });
    }



    private void refresh(){
        tokenPanel.removeAll();
        checkBoxPanel.removeAll();
        namePanel.removeAll();
        tokenLabelCheckBox.clear();

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

    // metodo per mostrare una singola carta
    @Override
    public void showCard(Integer numberCard) {
        Path startDir = identifyPathCard(numberCard);

        System.out.println(startDir);

        if(startDir != null){
            try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().contains(numberCard.toString()))) {
                paths.forEach(path -> {
                    System.out.println(path);
                    Image img = new ImageIcon(String.valueOf(path)).getImage();
                    JLabel startCardLabelImage = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/3 , img.getHeight(null)/3, Image.SCALE_SMOOTH)));
                    setCompoundBorderInsets(startCardLabelImage, 0, 100, 0, 100, "ALL");
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

                    setBorderInsets(startCardLabelText, 30, 124, 80, 120);
                    namePanel.add(startCardLabelText);

                    jCheckBox.setFocusPainted(false);
                    jCheckBox.setBorderPainted(false);
                    jCheckBox.setForeground(panelContainer.getBackground());
                    buttonGroup.add(jCheckBox);
                    setBorderInsets(jCheckBox, 0, 140, 70, 125);
                    jCheckBox.setOpaque(false);
                    checkBoxPanel.add(jCheckBox);
                    tokenLabelCheckBox.put(startCardLabelText, jCheckBox);
                });

                ActionListener actionListener = e -> {
                    confirmButton.setEnabled(tokenLabelCheckBox.values().stream().anyMatch(AbstractButton::isSelected));

                    tokenLabelCheckBox.keySet().forEach(k -> k.setForeground(Color.BLACK));

                    tokenLabelCheckBox.entrySet()
                            .stream()
                            .filter(en -> en.getValue().equals(e.getSource()))
                            .findFirst()
                            .orElseThrow()
                            .getKey()
                            .setForeground(Color.RED);
                };

                for(JCheckBox c : tokenLabelCheckBox.values()) c.addActionListener(actionListener);

                //   List<Path> fileList = Files.walk(startDir).filter(Files::isRegularFile).filter(f->f.getFileName().toString().contains(numberCard.toString())).toList();
                //   fileList.forEach(path->cardIconList.add(new ImageIcon(String.valueOf(path))));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
            }
        }
    }






    public void createGamePanel() {
        panelContainer.setLayout(new CardLayout());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setVisible(false);

        //da vedere sta parte
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setBackground(new Color(237,230,188,255));

        BackgroundPanel scoreboard = new BackgroundPanel("src/main/utils/scoreboard.png", false);
        scoreboard.setOpaque(false);
        panel2.add(scoreboard, BorderLayout.CENTER);

        panelContainer.add(panel1, PANEL1);
        panelContainer.add(panel2, PANEL2);


        //pagina 1: Game
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));

        JButton buttonToScoreBoard = createButton("Go to scoreboard", 20);
        buttonToScoreBoard.addActionListener(this);
        setBoxComponentSize(buttonToScoreBoard, 180, 50);
        northPanel.add(buttonToScoreBoard);

        northPanel.add(Box.createHorizontalGlue());

        //JLabel label = createTextLabelFont("Nickname giocatore", 16);
        JLabel label = createTextLabelFont(nickname, 16);
        northPanel.add(label);

        northPanel.add(Box.createHorizontalGlue());

        JLabel rightLabel = new JLabel(createPlayableTokenImageIcon(token, 50));
        northPanel.add(rightLabel);

        panel1.add(northPanel, BorderLayout.NORTH);


        //da verificarne il funzionamento
        board = new JPanel(new GridLayout(100, 100, 70, 100)); //sarà la board di gioco, da capire con che Layout implementarla
        board.setOpaque(false);
        addScrollPane(board, panel1); //DA VERIFICARE SE ME LO METTE IN BORDERLAYOUT.CENTER


        JPanel lateralPanelDX = new JPanel();
        String[] columnNames = {"Reigns/Objects", "counter"};
        Object[][] data = {
                {"Fungi", 0},
                {"Animal", 0},
                {"Plant", 0},
                {"Insect", 0},
                {"Quill", 0},
                {"Manuscript", 0},
                {"Inkwell", 0}
        };

        JTable table = createTable(columnNames, data);
        addScrollPane(table, lateralPanelDX);
        panel1.add(lateralPanelDX, BorderLayout.EAST);


        JPanel southPanel = new JPanel();
        setBorderInsets(southPanel, 10, 0, 10, 0);
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));

        southPanel.add(Box.createHorizontalGlue());

        //QUI ANDRÀ LA STARTCARD IN MANO --> HAND

        ImageIcon imgI1 = new ImageIcon("src/main/utils/starter_card/starter_card_front/starter_card_back_83.png");
        Image img = imgI1.getImage().getScaledInstance(imgI1.getIconWidth()/6,imgI1.getIconHeight()/6,Image.SCALE_SMOOTH);
        JLabel startCardLabel = new JLabel(new ImageIcon(img));
        southPanel.add(startCardLabel, BorderLayout.SOUTH);

        southPanel.add(Box.createHorizontalGlue());
        panel1.add(southPanel, BorderLayout.SOUTH);

        //DA RIVEDERE/COMPLETARE
        //Mappa serialnumber->ArrayList<Front, Back> ??

     /*   starterCardFrontLabel = new JLabel();
        starterCardBackLabel = new JLabel();
        ArrayList<ImageIcon> iconStarter;
        iconStarter = showCard(hand.getFirst());

        starterCardBackLabel.setIcon(iconStarter.getFirst()); //back
        starterCardFrontLabel.setIcon(iconStarter.getLast()); //front

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);

        starterCardFrontLabel.setBorder(border);
        starterCardBackLabel.setBorder(border);
        southPanel.add(starterCardFrontLabel);
        southPanel.add(starterCardBackLabel);

        starterCardFrontLabel.addMouseListener(new ClickListener());
        starterCardBackLabel.addMouseListener(new ClickListener());

        JButton confirm = createButton("Confirm", 20); //tasto per posizionare la carta al centro, una volta cliccato la carta si piazza in automatico
        setBoxComponentSize(confirm, 120, 30);
        confirm.addActionListener(this);
        southPanel.add(confirm);

        southPanel.add(Box.createHorizontalGlue());
        panel1.add(southPanel, BorderLayout.SOUTH); */




        JButton resourceDeck = createButton("Risorsa", 16);
        setBoxComponentSize(resourceDeck, 150, 40);
        //resourceDeck.setPreferredSize(new Dimension(150, 40));

        JButton goldDeck = createButton("Oro", 16);
        setBoxComponentSize(goldDeck, 150, 40);
        //goldDeck.setPreferredSize(new Dimension(150, 40));

     /*   //prova creazione popup
        JButton popupButton = createButton("popup card", 16);
        setBoxComponentSize(popupButton, 150, 40);
        //popupButton.setPreferredSize(new Dimension(150, 40));
        popupButton.addActionListener(this);
        southPanel.add(popupButton);
        southPanel.add(resourceDeck);
        southPanel.add(goldDeck); */



        //PAGINA 2: SCOREBOARD
        JPanel buttonGamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        //buttonGamePanel.setOpaque(false);
        JButton buttonToMainPage = createButton("Go to game", 20);
        setBoxComponentSize(buttonToMainPage, 180,40);
        //buttonToMainPage.setPreferredSize(new Dimension(180, 40));
        buttonToMainPage.addActionListener(this);
        buttonGamePanel.add(buttonToMainPage);
        panel2.add(buttonGamePanel, BorderLayout.NORTH);


        JPanel playersPanel = new JPanel();

        String[] column = {"Order", "Nickname", "Token", "Score"};
        Object[][] dataPlayers = {
                {0, 0, "A", 0},
                {0, 0, "B", 0},
                {0, 0,  "C", 0},
                {0, 0,  "D", 0}
        };
        JTable tablePlayer = createTable(column, dataPlayers);
        addScrollPane(tablePlayer, playersPanel);
        panel2.add(playersPanel, BorderLayout.WEST);


        //inserimento dei token nella pagina

        //TODO: fare in modo che ad ogni giocatore venga assegnato un token

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
        DefaultTableModel model = new DefaultTableModel(data, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        table.setRowHeight(30);

        return table;
    }


    private static void addScrollPane(JComponent component, JPanel panel){
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setPreferredSize(new Dimension(300, 243));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(component);
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

    private void setCompoundBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight, String inset) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        Border b = BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
        switch (inset) {
            case "TOP" ->
                    jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)));
            case "BOTTOM" ->
                    jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)));
            case "ALL" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createLineBorder(Color.black)));
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cardLayout = (CardLayout) panelContainer.getLayout();
        if (e.getActionCommand().equals("Go to scoreboard")) {
            cardLayout.show(panelContainer, PANEL2);
        } else if (e.getActionCommand().equals("Go to game")) {
            cardLayout.show(panelContainer, PANEL1);
        } else if (e.getActionCommand().equals("Confirm")) {
            //premendo confirm la carta viene posizionata in centro allo schermo

        }

        /* } else if (e.getActionCommand().equals("popup card")){
            PopupDialog popup = new PopupDialog(this);
            popup.setVisible(true);

            //testing
            Object[] options = { "Create Game", "Join Game" };
            int choice = JOptionPane.showOptionDialog(null,"There are existing games, choose: ","Create Game / Join Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options, options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null,"Tua mamma");
            } else if(choice == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null,"Quella troia");
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

    public void setHand(List<Integer> hand) {
        this.hand = hand;
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




    //metodo per mostrare la mano durante la partita (non lo uso per le carte starter)
/*    public Map<Integer, ArrayList<ImageIcon>> showHand(List<Integer> hand) {
        Map<Integer, ArrayList<ImageIcon>> handIcons = new HashMap<>();

        hand.forEach(serialCard -> {
            Path startDir;
            if(serialCard <= 40)
                startDir = Paths.get(RESOURCE_DIR);
            else if (serialCard <= 80)
                startDir = Paths.get(GOLD_DIR);
            else if (serialCard > 86 && serialCard <= 102)
                startDir = Paths.get(OBJECTIVE_DIR);
            else {
                JOptionPane.showMessageDialog(this, "ErrorMsg: ", "Invalid card", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f->f.getFileName().toString().contains(serialCard.toString()))){
                ArrayList<ImageIcon> cardIconList = new ArrayList<>();
                paths.forEach(path-> cardIconList.add(new ImageIcon(String.valueOf(path))));
                handIcons.put(serialCard, cardIconList);

                //handIcons.put(serialCard, new ImageIcon(String.valueOf(path)))
                //   List<Path> fileList = Files.walk(startDir).filter(Files::isRegularFile).filter(f->f.getFileName().toString().contains(serialCard.toString())).toList();
                //   fileList.forEach(path->cardIconList.add(new ImageIcon(String.valueOf(path))));
            } catch (IOException e){
                JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
            }
        });


        return handIcons;
    }
    */


}