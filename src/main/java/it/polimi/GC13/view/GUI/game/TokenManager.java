package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.TokenColor;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.*;

/**
 * The {@code TokenManager} class is responsible for managing the tokens on a scoreboard.
 * It extends {@link JPanel} and manages the token grids and their positions.
 */
public class TokenManager extends JPanel {

    /**
     * Represents a grid data structure containing a JPanel and a list of empty JLabels.
     * <br>
     * The JPanel represents a score position on the scoreboard, and for each of them there are four JLabel in which
     * the players' token can be printed
     */
    private record GridData(JPanel grid, ArrayList<JLabel> emptyLabels){}

    /**
     * Maps score positions to their corresponding GridData, which holds grid panels and empty labels.
     */
    private final Map<Integer, GridData> scoreGrid = new HashMap<>();

    /**
     * Maps player names to their token colors currently in the game.
     */
    private Map<String, TokenColor> tokenInGame;

    /**
     * Maps each player to their scoreboard representation, mapping score positions to GridData.
     */
    private final Map<String, Map<Integer, GridData>> tokenToScoreBoard = new HashMap<>();

    /**
     * Maps each player to their previous score position on the scoreboard.
     */
    private final Map<String, Integer> previousPosition = new HashMap<>();

    /**
     * ResourceGetter instance used for fetching resources like images.
     */
    private final ResourceGetter resourceGetter = new ResourceGetter();



    /**
     * Constructs a new {@code TokenManager}.
     * Initializes the token grids and layout for the scoreboard.
     */
    public TokenManager(){
        BackgroundImageSetter scoreboard = new BackgroundImageSetter(this.resourceGetter.getURL("scoreboard.png"));
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

    /**
     * Initializes a token grid at the specified position.
     *
     * @param k The position of the token grid
     * @return The initialized token grid JPanel
     */
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

    /**
     * Initializes every players' tokens previous position to 0
     * and put the tokens on the scoreboard at the grid 0.
     */
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

    /**
     * Remaps the score to a corresponding position on the internal scoreGrid of the scoreboard.
     *
     * @param oldScore The old score to remap
     * @return The remapped position in the {@link #scoreGrid}
     */
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

    /**
     * Handles special cases for score 20.
     *
     * @param player The player's name
     * @param b A boolean flag indicating a specific condition
     * @return The new position based on the special case handling
     */
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

    /**
     * Updates the player's position on the scoreboard, based on the new score in input.
     *
     * @param player The player's name
     * @param score  The new score of the player
     */
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

//    /**
//     * Creates a resized ImageIcon from the specified path with given dimensions.
//     *
//     * @param tokenImagePath The path to the token image
//     * @param dim The dimensions to resize the image to
//     * @return The resized ImageIcon
//     */
    private ImageIcon createResizedTokenImageIcon(URL tokenImagePath, int dim) {
        return new ImageIcon(new ImageIcon(tokenImagePath).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
    }

    /**
     * Creates a playable token ImageIcon based on its color and given dimensions.
     *
     * @param tokenColor The color of the token
     * @param dim The dimensions to resize the image to
     * @return The resized playable token ImageIcon
     */
    private ImageIcon createPlayableTokenImageIcon(TokenColor tokenColor, int dim) {
        return createResizedTokenImageIcon(this.resourceGetter.getURL(tokenColor.toString().toLowerCase() + "_token.png"), dim);
    }

    /**
     * Creates a GridBagConstraints object with specified grid coordinates.
     *
     * @param x The x-coordinate of the grid
     * @param y The y-coordinate of the grid
     * @return The GridBagConstraints object
     */
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

    /**
     * Sets the tokens in the game with their corresponding colors.
     *
     * @param tokenInGame A map of player names to their token colors
     */
    public void setTokenInGame(Map<String, TokenColor> tokenInGame) {
        this.tokenInGame = tokenInGame;
    }

}
