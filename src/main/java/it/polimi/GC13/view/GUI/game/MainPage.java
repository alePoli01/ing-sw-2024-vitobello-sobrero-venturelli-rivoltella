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
    private TokenManager tokenManager;
    private Game game;




    public MainPage() throws HeadlessException {
        super("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Imposta il frame a schermo intero
        setResizable(false);

        panelContainer = new JPanel(new CardLayout());
        add(panelContainer);

        // Creazione delle due schermate

        //VERSIONE CORRETTA, DA INSERIRE DOPO AVER TESTATO
        /*JPanel panel1 = new JPanel(null);
        JPanel panel2 = new BackgroundPanel(new ImageIcon("src/main/utils/scoreboard.png").getImage(), false);
        panel2.setLayout(null);*/

        //VERSIONE DI PROVA: DA ELIMINARE QUANDO FINITO
        JPanel panel2 = new JPanel(null);
        JPanel panel1 = new BackgroundPanel(new ImageIcon("src/main/utils/scoreboard.png").getImage(), false);
        panel1.setLayout(null); //fino a qui


        panelContainer.add(panel1, PANEL1);
        panelContainer.add(panel2, PANEL2);

        //pagina 1: Game
        panel1.setBackground(new Color(237,230,188,255));

        //aggiunta del tasto per tornare alla pagina del gioco
        buttonToScoreBoard = new JButton("Go to scoreboard");
        buttonToScoreBoard.addActionListener(this);
        int widthb1 = 180;
        int heigthb1 = 28;
        buttonToScoreBoard.setBounds(10, 10, widthb1, heigthb1);

        panel1.add(buttonToScoreBoard);



        //pagina 2: Scoreboard

        //aggiunta del tasto per tornare alla pagina del gioco
        buttonToMainPage = new JButton("Go to game");
        buttonToMainPage.addActionListener(this);
        int widthb2 = 180;
        int heigthb2 = 28;
        buttonToMainPage.setBounds(10, 10, widthb2, heigthb2);
        panel2.add(buttonToMainPage);


        //inserimento dei token nella pagina

        //TODO: fare in modo che ad ogni giocatore venga assegnato un token

        //int numPlayer = game.numPlayer; //devo usare numPlayer o .getCurrNumPlayer() ?

        tokenManager = new TokenManager();
        for(int i=0; i</*game.numPlayer*/4; i++) {
            JLabel label = new JLabel();
            tokenManager.createToken(/*player loggato,*/ label);
            Image img = new ImageIcon(tokenManager.getTokenInGame().get(i).getDirectoryImage()).getImage();
            ImageIcon tokenIcon = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            //JLabel label = new JLabel(tokenIcon);
            tokenManager.getTokenInGame().get(i).getLabel().setIcon(tokenIcon);
            //label.setIcon(tokenIcon);
            tokenManager.getTokenInGame().get(i).getLabel().setBounds(tokenManager.getTokenInGame().get(i).getX(), tokenManager.getTokenInGame().get(i).getY(), tokenIcon.getIconWidth(), tokenIcon.getIconHeight());
            panel1.add(tokenManager.getTokenInGame().get(i).getLabel());


        }

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
        }
    }
}