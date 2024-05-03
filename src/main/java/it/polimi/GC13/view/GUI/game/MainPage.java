package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.view.GUI.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


//qui andrà la grafica della partita
public class MainPage extends JFrame implements ActionListener {
    private JPanel panelContainer;
    final static String PANEL1 = "Game";
    final static String PANEL2 = "Scoreboard";
    private JButton buttonToMainPage;
    private JButton buttonToScoreBoard;
    private JPanel board;
    private TokenManager tokenManager;
    private Game game;




    public MainPage() throws HeadlessException {
        super("Codex Naturalis");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Imposta il frame a schermo intero
        setResizable(false);

        panelContainer = new JPanel(new CardLayout());
        getContentPane().add(panelContainer);

        // Creazione delle due schermate

        //VERSIONE CORRETTA, DA INSERIRE DOPO AVER TESTATO
        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new BorderLayout());
        BackgroundPanel scoreboard = new BackgroundPanel("src/main/utils/scoreboard.png", false);
        scoreboard.setOpaque(false);
        panel2.add(scoreboard, BorderLayout.CENTER);

        panel1.setBackground(new Color(237,230,188,255));
        panel2.setBackground(new Color(237,230,188,255));
        panelContainer.add(panel1, PANEL1);
        panelContainer.add(panel2, PANEL2);

        //pagina 1: Game
        JPanel buttonScorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Imposta il layout del pannello
        //buttonScorePanel.setOpaque(false);
        buttonToScoreBoard = new JButton("Go to scoreboard");
        buttonToScoreBoard.addActionListener(this);
        buttonToScoreBoard.setPreferredSize(new Dimension(150, 40)); //TUTTI I 40 ERANO PRIMA 25
        buttonScorePanel.add(buttonToScoreBoard);
        JLabel label = new JLabel("Nickname giocatore");
        buttonScorePanel.add(label);
        panel1.add(buttonScorePanel, BorderLayout.NORTH);


        //i colori servono solo per visualizzare quanto occupanp i pannelli
        JPanel lateralPanelSX = new JPanel();
        lateralPanelSX.setBackground(Color.GREEN);
        lateralPanelSX.setPreferredSize(new Dimension(200, 0));
        panel1.add(lateralPanelSX, BorderLayout.WEST);

        JPanel lateralPanelDX = new JPanel();
        lateralPanelDX.setBackground(Color.RED);
        lateralPanelDX.setPreferredSize(new Dimension(200, 0));
        panel1.add(lateralPanelDX, BorderLayout.EAST);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton resourceDeck = new JButton("Risorsa");
        resourceDeck.setPreferredSize(new Dimension(150, 40));
        JButton goldDeck = new JButton("Oro");
        goldDeck.setPreferredSize(new Dimension(150, 40));

        //prova creazione popup
        JButton popupButton = new JButton("popup card");
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
        panel1.add(scrollPane, BorderLayout.CENTER);








        //pagina 2: Scoreboard
        JPanel buttonGamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonGamePanel.setOpaque(false);
        buttonToMainPage = new JButton("Go to game");
        buttonToMainPage.addActionListener(this);
        buttonToMainPage.setPreferredSize(new Dimension(180, 25));
        buttonGamePanel.add(buttonToMainPage);
        panel2.add(buttonGamePanel, BorderLayout.NORTH);






        /*
        JPanel lateralPanelSX = new JPanel();
        lateralPanelSX.setBackground(Color.GREEN);
        lateralPanelSX.setPreferredSize(new Dimension(200, 0));
        panel1.add(lateralPanelSX, BorderLayout.WEST);

        JPanel lateralPanelDX = new JPanel();
        lateralPanelDX.setBackground(Color.RED);
        lateralPanelDX.setPreferredSize(new Dimension(200, 0));
        panel1.add(lateralPanelDX, BorderLayout.EAST);
        */













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