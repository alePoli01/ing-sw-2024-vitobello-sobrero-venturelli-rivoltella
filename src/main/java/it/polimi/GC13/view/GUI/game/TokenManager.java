package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.view.GUI.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;


public class TokenManager extends JPanel {
    final static String TOKEN_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/token/";
    final static String P_TOKEN_DIR = TOKEN_DIR + "playableToken/";
    final static String TOKEN_FILE_SUFFIX = "_token.png";
    final static String GREY_TOKEN_FILE_NAME = "grey";

    //private List<TokenColor> availableToken = List.of((TokenColor.values())); //per lavorare staticamente

    private record UserData(String playerNickname, TokenColor token){}
    private record GridData(JPanel grid, ArrayList<JLabel> emptyLabels){}


    private Map<UserData, Integer> tokenToGridMap = new HashMap<>();

    private final Map<Integer, GridData> scoreGrid = new HashMap<>();

    //private Map<String, TokenColor> tokenInGame;
    //private Map<String, JLabel> labelToken = new HashMap<>();


    public TokenManager(){
        UserData player1 = new UserData("Nico", TokenColor.BLUE); //in FrameManager, quando viene invocato StartCardSetupPhase, passo nicjìkname e token di tutti i giocatori
        UserData player2 = new UserData("Nico2", TokenColor.GREEN);
        UserData player3 = new UserData("Nico3", TokenColor.YELLOW);
        UserData player4 = new UserData("Nico4", TokenColor.RED);

        int scorePlayer1 = 3; //in FrameManager, quando si aggiornano i punteggi, aggiorno anche il punteggio della mappa e chiamo il metodo per spostare il token
        int scorePlayer2 = 8;
        int scorePlayer3 = 15;
        int scorePlayer4 = 18;

        tokenToGridMap.put(player1, scorePlayer1);
        tokenToGridMap.put(player2, scorePlayer2);
        tokenToGridMap.put(player3, scorePlayer3);
        tokenToGridMap.put(player4, scorePlayer4);


        BackgroundPanel scoreboard = new BackgroundPanel("src/main/resources/it/polimi/GC13/view/GUI/backgrounds/scoreboard.png", true);
        scoreboard.setOpaque(false);
        scoreboard.setLayout(new GridBagLayout());

        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 1));
        startPanel.setOpaque(false);
        startPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.CYAN), BorderFactory.createEmptyBorder(10,10,20,10)));

        JPanel middlePanel = new JPanel(new GridLayout(4,4, 30, 20));
        middlePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.CYAN), BorderFactory.createEmptyBorder(25,10,15,10)));
        middlePanel.setOpaque(false);

        JPanel endPanel = new JPanel(new GridLayout(4,5, 10, 20));
        endPanel.setOpaque(false);
        endPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.CYAN), BorderFactory.createEmptyBorder(30,30,10,25)));


        int k1=18;
        int k2=38;
        for(int i = 0; i<39; i++){
            if(i<3){
                startPanel.add(initializeTokenGrid(i));
            } else if(i<19){
                middlePanel.add(initializeTokenGrid(k1));
                k1--;
            } else {
                endPanel.add(initializeTokenGrid(k2));
                k2--;
            }
        }

        initializeTokenOnScoreboard();


        moveToken(player1.playerNickname);
        moveToken(player2.playerNickname);
        moveToken(player3.playerNickname);
        moveToken(player4.playerNickname);



        scoreboard.add(endPanel, createGridBagConstraints(0,0));
        scoreboard.add(middlePanel, createGridBagConstraints(0,1));
        scoreboard.add(startPanel, createGridBagConstraints(0,2));

        add(scoreboard);

    }


    public JPanel initializeTokenGrid(int k1){
        JPanel tokenGrid = new JPanel(new GridLayout(2,2,1, 1));
        ArrayList<JLabel> labelInTokenGrid = new ArrayList<>();
        tokenGrid.setOpaque(false);

        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                JLabel label = new JLabel(Integer.toString(k1));
                label.setPreferredSize(new Dimension(30,30));
                //label.setVisible(false);
                labelInTokenGrid.add(label);
                tokenGrid.add(label);
            }
        }

        GridData gridData = new GridData(tokenGrid,labelInTokenGrid);
        scoreGrid.put(k1, gridData);

        return tokenGrid;
    }


    /*public JPanel initializeTokenGrid(int k1){
        JPanel tokenGrid = new JPanel(new GridLayout(2,2,1, 1));
        tokenGrid.setOpaque(false);

        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                JPanel panel = new JPanel();
                //panel.setOpaque(false);
                panel.setPreferredSize(new Dimension(30,30));
                JLabel label = new JLabel(Integer.toString(k1));
                label.setVisible(false);
                panel.add(label);
                tokenGrid.add(panel);
            }
        }
        scoreGrid.put(k1, tokenGrid);
        return tokenGrid;
    }*/


    private void initializeTokenOnScoreboard(){
        int i = tokenToGridMap.size();
        for(UserData data : tokenToGridMap.keySet()){
            if(scoreGrid.get(0).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals("0"))){
                scoreGrid.get(0).emptyLabels.get(i-1).setText(data.playerNickname);
                scoreGrid.get(0).emptyLabels.get(i-1).setIcon(createPlayableTokenImageIcon(data.token, 30));
                i--;
            }
        }
    }



    public void moveToken(String player){
        for(UserData data : tokenToGridMap.keySet()){
            if(data.playerNickname.equals(player)){
                for(int i=0; i<scoreGrid.size(); i++){
                    if(scoreGrid.get(i).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(data.playerNickname))){
                        int finalI = i;
                        scoreGrid.get(i).emptyLabels.stream().filter(l->l.getText().equals(data.playerNickname)).findFirst().ifPresent(l-> {
                            l.setIcon(null);
                            l.setText(Integer.toString(finalI));
                        });
                    }
                }

                if(scoreGrid.get(tokenToGridMap.get(data)).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(tokenToGridMap.get(data))))){
                    scoreGrid.get(tokenToGridMap.get(data)).emptyLabels.stream().filter(l->l.getText().equals(Integer.toString(tokenToGridMap.get(data)))).findFirst().ifPresent(l->{
                        l.setIcon(createPlayableTokenImageIcon(data.token,30));
                        l.setText(Integer.toString(tokenToGridMap.get(data)));
                    });
                }else{
                    JOptionPane.showMessageDialog(this, "ErrorMsg: too much players", "Invalid position", JOptionPane.ERROR_MESSAGE);
                }
            }
        }


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




    private GridBagConstraints createGridBagConstraints(int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;

        return gbc;
    }

    // Metodo per creare una ImageIcon trasparente
    public static ImageIcon createTransparentIcon(ImageIcon icon, float transparency) {
        Image originalImage = icon.getImage();

        BufferedImage transparentImage = new BufferedImage(
                originalImage.getWidth(null),
                originalImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = transparentImage.createGraphics();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));

        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(transparentImage);
    }






}
