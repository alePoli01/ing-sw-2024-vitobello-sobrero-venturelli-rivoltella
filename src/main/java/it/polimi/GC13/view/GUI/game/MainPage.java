package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.view.GUI.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

//qui andrà la grafica della partita
public class MainPage extends JFrame implements ActionListener {
    private JPanel panelContainer;
    final static String PANEL1 = "Game";
    final static String PANEL2 = "Scoreboard";
    private JButton buttonToMainPage;
    private JButton buttonToScoreBoard;
    private ArrayList<TokenGUI> tokenPlayerList;
    private String[] imageFile = {"src/main/utils/token/yellow_token.png", "src/main/utils/token/blue_token.png", "src/main/utils/token/red_token.png", "src/main/utils/token/green_token.png"};
    ;






    public MainPage() throws HeadlessException {
        super("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Imposta il frame a schermo intero
        setResizable(false);

        panelContainer = new JPanel(new CardLayout());
        add(panelContainer);

        // Creazione delle due schermate
        JPanel panel1 = new JPanel(null);
        JPanel panel2 = new BackgroundPanel(new ImageIcon("src/main/utils/scoreboard.png").getImage(), false);
        panel2.setLayout(null);

        //pagina 1: Game

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

        panelContainer.add(panel1, PANEL1);
        panelContainer.add(panel2, PANEL2);





        //inserimento dei token nella pagina

        tokenPlayerList = new ArrayList<>();
        //TODO: mettere la condizione che per ogni giocatore viene assegnato un token

        while(imageFile.length>0) {
            Random random = new Random();
            int randomIndex = random.nextInt(imageFile.length);
            TokenGUI token = new TokenGUI(new ImageIcon(imageFile[randomIndex]).getImage());
            tokenPlayerList.add(token);

        }










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