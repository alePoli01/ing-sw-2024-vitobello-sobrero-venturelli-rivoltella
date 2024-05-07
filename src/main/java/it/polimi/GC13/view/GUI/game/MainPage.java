package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.view.GUI.BackgroundPanel;
import it.polimi.GC13.view.GUI.FrameManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainPage extends JFrame implements ActionListener {
    private JPanel panelContainer;
    final static String PANEL0 = "TokenPage";
    final static String PANEL1 = "Game";
    final static String PANEL2 = "Scoreboard";
    private JButton buttonToMainPage;
    private JButton buttonToScoreBoard;
    private JPanel board;
    private TokenManager tokenManager;
    private Game game;




    public MainPage(FrameManager frameManager) {
        setTitle("Codex Naturalis");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        panelContainer = new JPanel(new CardLayout());
        getContentPane().add(panelContainer);

        // Creazione delle due schermate

        //JPanel panel0 = new JPanel(new BorderLayout());
        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new BorderLayout());
        BackgroundPanel scoreboard = new BackgroundPanel("src/main/utils/scoreboard.png", false);
        scoreboard.setOpaque(false);
        panel2.add(scoreboard, BorderLayout.CENTER);

        //panel0.setBackground(new Color(237,230,188,255));
        panel1.setBackground(new Color(237,230,188,255));
        panel2.setBackground(new Color(237,230,188,255));

        //panelContainer.add(panel0, PANEL0);
        panelContainer.add(panel1, PANEL1);
        panelContainer.add(panel2, PANEL2);

        //pagina 1: Game
        JPanel buttonScorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        //buttonScorePanel.setOpaque(false);
        buttonToScoreBoard = new JButton("Go to scoreboard");
        buttonToScoreBoard.setFont(new Font("Old English Text MT", Font.BOLD, 16)); //PlainGermanica
        buttonToScoreBoard.addActionListener(this);
        buttonToScoreBoard.setPreferredSize(new Dimension(150, 40)); //TUTTI I 40 ERANO PRIMA 25
        buttonScorePanel.add(buttonToScoreBoard);
        JLabel label = new JLabel("Nickname giocatore");
        label.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        buttonScorePanel.add(label);
        panel1.add(buttonScorePanel, BorderLayout.NORTH);


        //i colori servono solo per visualizzare quanto occupanp i pannelli
        JPanel lateralPanelSX = new JPanel();
        lateralPanelSX.setBackground(Color.GREEN);
        lateralPanelSX.setPreferredSize(new Dimension(200, 0));
        panel1.add(lateralPanelSX, BorderLayout.WEST);


        JPanel lateralPanelDX = new JPanel();
        //lateralPanelDX.setBackground(Color.RED);

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
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(300, 243));

        lateralPanelDX.add(tableScrollPane);
        panel1.add(lateralPanelDX, BorderLayout.EAST);




        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton resourceDeck = new JButton("Risorsa");
        resourceDeck.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        resourceDeck.setPreferredSize(new Dimension(150, 40));
        JButton goldDeck = new JButton("Oro");
        goldDeck.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        goldDeck.setPreferredSize(new Dimension(150, 40));

        //prova creazione popup
        JButton popupButton = new JButton("popup card");
        popupButton.setFont(new Font("Old English Text MT", Font.BOLD, 16)); //PlainGermanica
        popupButton.setPreferredSize(new Dimension(150, 40));
        popupButton.addActionListener(this);
        southPanel.add(popupButton);
        southPanel.add(resourceDeck);
        southPanel.add(goldDeck);
        panel1.add(southPanel, BorderLayout.SOUTH);







        //da verificarne il funzionamento
        board = new JPanel(); //sarà la board di gioco, da capire con che Layout implementarla
        board.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(board);

        //da verificare
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel1.add(scrollPane, BorderLayout.CENTER);








        //pagina 2: Scoreboard
        JPanel buttonGamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonGamePanel.setOpaque(false);
        buttonToMainPage = new JButton("Go to game");
        buttonToMainPage.setFont(new Font("Old English Text MT", Font.BOLD, 20)); //PlainGermanica
        buttonToMainPage.addActionListener(this);
        buttonToMainPage.setPreferredSize(new Dimension(180, 25));
        buttonGamePanel.add(buttonToMainPage);
        panel2.add(buttonGamePanel, BorderLayout.NORTH);






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

























        setVisible(true);
    }


    //comportamento dei due tasti
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cardLayout = (CardLayout) panelContainer.getLayout();
        if (e.getActionCommand().equals("Go to scoreboard")) {
            cardLayout.show(panelContainer, PANEL2);
        } else if (e.getActionCommand().equals("Go to game")) {
            cardLayout.show(panelContainer, PANEL1);
        } else if (e.getActionCommand().equals("popup card")){
            PopupDialog popup = new PopupDialog(this);
            popup.setVisible(true);

            //testing
            /*Object[] options = { "Create Game", "Join Game" };
            int choice = JOptionPane.showOptionDialog(null,"There are existing games, choose: ","Create Game / Join Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options, options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null,"Tua mamma");
            } else if(choice == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null,"Quella troia");
            }*/

        }
    }
}