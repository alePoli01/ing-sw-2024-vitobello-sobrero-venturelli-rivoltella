package it.polimi.GC13.view.game;

import it.polimi.GC13.view.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//qui andrà la grafica della partita
public class MainPage extends JFrame implements ActionListener {
    private JPanel panelContainer;
    final static String PANEL1 = "Game";
    final static String PANEL2 = "Scoreboard";
    private JButton buttonToMainPage;
    private JButton buttonToScoreBoard;

    public MainPage() throws HeadlessException {
        super("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Imposta il frame a schermo intero
        setResizable(false);

        panelContainer = new JPanel(new CardLayout());
        add(panelContainer);

        // Creazione delle due schermate
        JPanel panel1 = new JPanel();
        JPanel panel2 = new BackgroundPanel(new ImageIcon("src/main/utils/scoreboard.png").getImage(), false);


        //TODO: modificare la posizione dei due tasti


        //pagina 1: Game
        buttonToScoreBoard = new JButton("Go to scoreboard");
        buttonToScoreBoard.addActionListener(this);
        panel1.add(buttonToScoreBoard);














        //pagina 2: Scoreboard
        buttonToMainPage = new JButton("Go to game");
        buttonToMainPage.addActionListener(this);
        panel2.add(buttonToMainPage);


        panelContainer.add(panel1, PANEL1);
        panelContainer.add(panel2, PANEL2);


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