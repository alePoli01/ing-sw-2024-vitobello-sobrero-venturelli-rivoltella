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


    private record GridData(JPanel grid, ArrayList<JLabel> emptyLabels){}
    private record UserData(String playerNickname, TokenColor token){}


    private final Map<Integer, GridData> scoreGrid = new HashMap<>();
    private Map<String, TokenColor> tokenInGame;
    private Map<UserData, Integer> tokenToGridMap = new HashMap<>();



    public TokenManager(){
        BackgroundPanel scoreboard = new BackgroundPanel("src/main/resources/it/polimi/GC13/view/GUI/backgrounds/scoreboard.png", true);
        scoreboard.setOpaque(false);
        scoreboard.setLayout(new GridBagLayout());

        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 1));
        startPanel.setOpaque(false);
        startPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.CYAN), BorderFactory.createEmptyBorder(10,10,20,10)));

        JPanel middlePanel = new JPanel(new GridLayout(4,4, 35, 25));
        middlePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.CYAN), BorderFactory.createEmptyBorder(25,15,15,15)));
        middlePanel.setOpaque(false);

        JPanel endPanel = new JPanel(new GridLayout(4,5, 10, 20));
        endPanel.setOpaque(false);
        endPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.CYAN), BorderFactory.createEmptyBorder(30,30,10,25)));


        int k1 = 15;
        int k2=14;
        int k3 = 7;
        int k4=6;
        int k5 = 38;
        for(int i = 0; i<39; i++){
            if(i<3){
                startPanel.add(initializeTokenGrid(i));
            } else if(i<19) {
                if (i < 7){
                    middlePanel.add(initializeTokenGrid(k1));
                    k1++;
                }else if(i<11){
                    middlePanel.add(initializeTokenGrid(k2));
                    k2--;
                }else if(i<15){
                    middlePanel.add(initializeTokenGrid(k3));
                    k3++;
                }else{
                    middlePanel.add(initializeTokenGrid(k4));
                    k4--;
                }
            } else {
                endPanel.add(initializeTokenGrid(k5));
                k5--;
            }
        }


        /*moveToken(player1.playerNickname);
        moveToken(player2.playerNickname);
        moveToken(player3.playerNickname);
        moveToken(player4.playerNickname);*/

        scoreboard.add(endPanel, createGridBagConstraints(0,0));
        scoreboard.add(middlePanel, createGridBagConstraints(0,1));
        scoreboard.add(startPanel, createGridBagConstraints(0,2));

        add(scoreboard);

    }


    public JPanel initializeTokenGrid(int k){
        JPanel tokenGrid = new JPanel(new GridLayout(2,2,1, 1));
        ArrayList<JLabel> labelInTokenGrid = new ArrayList<>();
        tokenGrid.setOpaque(false);

        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                JLabel label = new JLabel(Integer.toString(k));
                label.setPreferredSize(new Dimension(30,30));
                label.setForeground(new Color(0,0,0,0));
                labelInTokenGrid.add(label);
                tokenGrid.add(label);

                if(k>19) {
                    switch (k) {
                        case 20, 22, 25, 27, 30, 32, 34, 38:
                            label.setText("X");
                            break;
                        case 21:
                            if((i==1))
                                label.setText("X");
                            break;
                        case 26:
                            if((i==0))
                                label.setText("X");
                            break;
                    }
                }
            }
        }

        GridData gridData = new GridData(tokenGrid,labelInTokenGrid);
        scoreGrid.put(k, gridData);

        return tokenGrid;
    }

    public void initializeDataPlayer(){
        for(String player : tokenInGame.keySet()){
            UserData dataPlayer = new UserData(player, tokenInGame.get(player));
            tokenToGridMap.put(dataPlayer, 0);
        }
        initializeTokenOnScoreboard();
    }



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


    public void moveToken(String player) {
        for (UserData data : tokenToGridMap.keySet()) {
            if (data.playerNickname.equals(player)) {
                for (int i = 0; i < scoreGrid.size(); i++) {
                    if (scoreGrid.get(i).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(data.playerNickname))) {
                        scoreGrid.get(i).emptyLabels.stream().filter(l -> l.getText().equals(data.playerNickname)).findFirst().ifPresent(l -> {
                            l.setIcon(null);
                            l.setText("X");
                        });
                    }
                }

                if (tokenToGridMap.get(data)<20 && scoreGrid.get(tokenToGridMap.get(data)).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(tokenToGridMap.get(data))))) {
                    scoreGrid.get(tokenToGridMap.get(data)).emptyLabels.stream().filter(l->!l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(tokenToGridMap.get(data)))).findFirst().ifPresent(l -> {
                        l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                        l.setText("X");
                    });
                } else if(tokenToGridMap.get(data)<30) {

                    switch (tokenToGridMap.get(data)) {
                        case 20:
                            if(scoreGrid.get(21).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(21)))){
                                scoreGrid.get(21).emptyLabels.stream().filter(l->!l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(21))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            } else if(scoreGrid.get(26).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(26)))){
                                scoreGrid.get(26).emptyLabels.stream().filter(l->!l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(26))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;

                        case 21:
                            if(scoreGrid.get(23).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(23)))) {
                                scoreGrid.get(23).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(23))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;

                        case 22:
                            if(scoreGrid.get(28).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(28)))) {
                                scoreGrid.get(28).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(28))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;

                        case 23:
                            if(scoreGrid.get(33).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(33)))) {
                                scoreGrid.get(33).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(33))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;

                        case 24:
                            if(scoreGrid.get(37).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(37)))) {
                                scoreGrid.get(37).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(37))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;

                        case 25:
                            if(scoreGrid.get(36).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(36)))) {
                                scoreGrid.get(36).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(36))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;

                        case 26:
                            if(scoreGrid.get(35).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(35)))) {
                                scoreGrid.get(35).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(35))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;

                        case 27:
                            if(scoreGrid.get(29).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(29)))) {
                                scoreGrid.get(29).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(29))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;

                        case 28:
                            if(scoreGrid.get(24).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(24)))) {
                                scoreGrid.get(24).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(24))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;

                        case 29:
                            if(scoreGrid.get(31).emptyLabels.stream().anyMatch(jLabel -> jLabel.getText().equals(Integer.toString(31)))) {
                                scoreGrid.get(31).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(31))).findFirst().ifPresent(l -> {
                                    l.setIcon(createPlayableTokenImageIcon(data.token, 30));
                                    l.setText("X");
                                });
                            }
                            break;
                    }
                }else {
                    JOptionPane.showMessageDialog(this, "ErrorMsg: too much players", "Invalid position", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    public void updatePlayerScore(String player, int score){
        if(tokenToGridMap.keySet().stream().anyMatch(data -> data.playerNickname.equals(player))){
            tokenToGridMap.put(tokenToGridMap.keySet()
                    .stream()
                    .filter(data -> data.playerNickname.equals(player))
                    .findFirst()
                    .orElseThrow(), score);

            moveToken(player);
        } else JOptionPane.showMessageDialog(this, "Not found such player, retry.", "Invalid player", JOptionPane.ERROR_MESSAGE);


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




    public Map<String, TokenColor> getTokenInGame() {
        return tokenInGame;
    }

    public void setTokenInGame(Map<String, TokenColor> tokenInGame) {
        this.tokenInGame = tokenInGame;
    }
}
