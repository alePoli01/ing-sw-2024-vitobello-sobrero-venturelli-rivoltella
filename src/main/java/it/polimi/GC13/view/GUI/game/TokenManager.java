package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.view.GUI.BackgroundImageSetter;

import javax.swing.*;
import java.awt.*;
import java.util.*;


public class TokenManager extends JPanel {
    final static String TOKEN_DIR = "src/main/resources/it/polimi/GC13/view/GUI/game/token/";
    final static String P_TOKEN_DIR = TOKEN_DIR + "playableToken/";
    final static String TOKEN_FILE_SUFFIX = "_token.png";

    private record GridData(JPanel grid, ArrayList<JLabel> emptyLabels){}

    private final Map<Integer, GridData> scoreGrid = new HashMap<>();
    private Map<String, TokenColor> tokenInGame;

    private final Map<String, Map<Integer, GridData>> tokenToScoreBoard = new HashMap<>();
    private final Map<String, Integer> previousPosition = new HashMap<>();


    public TokenManager(){
        BackgroundImageSetter scoreboard = new BackgroundImageSetter("src/main/resources/it/polimi/GC13/view/GUI/backgrounds/scoreboard.png");
        scoreboard.setOpaque(false);
        scoreboard.setLayout(new GridBagLayout());

        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 1));
        startPanel.setOpaque(false);
        startPanel.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));

        JPanel middlePanel = new JPanel(new GridLayout(4,4, 35, 25));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(25,15,15,15));
        middlePanel.setOpaque(false);

        JPanel endPanel = new JPanel(new GridLayout(4,5, 10, 20));
        endPanel.setOpaque(false);
        endPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,25));


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
            tokenToScoreBoard.put(player, scoreGrid);
            previousPosition.put(player, 0);

            tokenToScoreBoard.get(player).get(0).emptyLabels.stream().filter(l->!l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(0))).findFirst().ifPresent(l -> {
                l.setIcon(createPlayableTokenImageIcon(tokenInGame.get(player), 30));
                l.setText(player);
            });
        }
    }

    public int remappingScoreToPosition(int oldScore){
        return switch (oldScore) {
            case 21 -> 23;
            case 22 -> 28;
            case 23 -> 33;
            case 24 -> 37;
            case 25 -> 36;
            case 26 -> 35;
            case 27 -> 29;
            case 28 -> 24;
            case 29 -> 31;
            default -> oldScore;
        };
    }

    public int caseScore20(String player, boolean b){
        Optional<JLabel> label;
        if(b) {
            label = tokenToScoreBoard.get(player).get(21)
                    .emptyLabels.stream()
                    .filter(l -> l.getText().equals(player))
                    .findFirst();
        } else {
            label = tokenToScoreBoard.get(player).get(21)
                    .emptyLabels.stream()
                    .filter(l -> l.getText().equals(Integer.toString(21)))
                    .findFirst();
        }

        if(label.isPresent()){
            return 21;
        }else{
            return 26;
        }
    }


    public void updatePlayerScore(String player, int score) {
        if (previousPosition.get(player) < score) {
            if (score < 20) {
                tokenToScoreBoard.get(player).get(previousPosition.get(player)).emptyLabels.stream().filter(l -> l.getText().equals(player)).findFirst().ifPresent(l -> {
                    l.setIcon(null);
                    l.setText("X");
                });

                previousPosition.put(player, score);

                tokenToScoreBoard.get(player).get(score).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(score))).findFirst().ifPresent(l -> {
                    l.setIcon(createPlayableTokenImageIcon(tokenInGame.get(player), 30));
                    l.setText(player);
                });
            } else if (score < 30) {
                //old token research
                int oldPlayerCell;
                if (previousPosition.get(player) != 20) {
                    oldPlayerCell = remappingScoreToPosition(previousPosition.get(player));
                } else {
                    oldPlayerCell = caseScore20(player, true);
                }

                tokenToScoreBoard.get(player).get(oldPlayerCell).emptyLabels.stream().filter(l -> l.getText().equals(player)).findFirst().ifPresent(l -> {
                    l.setIcon(null);
                    l.setText("X");
                });

                //update previousPosition map
                previousPosition.put(player, score);

                //positioning new token
                int newPlayerCell;
                if (score == 20) {
                    newPlayerCell = caseScore20(player, false);
                } else {
                    newPlayerCell = remappingScoreToPosition(score);
                }

                tokenToScoreBoard.get(player).get(newPlayerCell).emptyLabels.stream().filter(l -> !l.getText().equals("X")).filter(l -> l.getText().equals(Integer.toString(newPlayerCell))).findFirst().ifPresent(l -> {
                    l.setIcon(createPlayableTokenImageIcon(tokenInGame.get(player), 30));
                    l.setText(player);
                });
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

    public void setTokenInGame(Map<String, TokenColor> tokenInGame) {
        this.tokenInGame = tokenInGame;
    }

}
