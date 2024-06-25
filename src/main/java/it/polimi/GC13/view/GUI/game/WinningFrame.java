package it.polimi.GC13.view.GUI.game;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The {@code WinningFrame} class represents a graphical window that displays the winner
 * and the ranking of players at the end of the game. It implements the {@link CardManager} interface.
 */
public class WinningFrame extends JFrame implements CardManager {
    private final JLabel winnerLabel;
    private final JLabel scoreLabel;
    private final JPanel scorePanel;

    /**
     * Constructs a new WinningFrame.
     * Sets up the main frame properties and initializes the components.
     */
    public WinningFrame() {
        setTitle("Winning Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel winnerPanel = new JPanel(new GridBagLayout());
        winnerPanel.setBackground(new Color(237, 230, 188, 255));

        winnerLabel = createTextLabelFont("", 256);
        setBorderInsets(winnerLabel, 0,0,10,0);
        winnerPanel.add(winnerLabel, createGridBagConstraints(0,0));

        scoreLabel = createTextLabelFont("", 64);
        setBorderInsets(scoreLabel, 0,0,100,0);
        winnerPanel.add(scoreLabel, createGridBagConstraints(0,1));

        scorePanel = new JPanel(new GridBagLayout());
        scorePanel.setOpaque(false);
        //scorePanel.setBorder(BorderFactory.createTitledBorder("Ranking"));
        winnerPanel.add(scorePanel, createGridBagConstraints(0,2));


        getContentPane().add(winnerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Creates a JLabel with specified text and font size.
     *
     * @param content The text content of the label
     * @param dim The font size of the label text
     * @return A JLabel with the specified text and font size
     */
    //Forse da mettere in CardManager
    private JLabel createTextLabelFont(String content, int dim) {
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return jLabel;
    }

    /**
     * Returns the JLabel that displays the winner's name.
     *
     * @return The winner JLabel
     */
    public JLabel getWinnerLabel() {
        return winnerLabel;
    }

    /**
     * Returns the JLabel that displays the score.
     *
     * @return The score JLabel
     */
    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    /**
     * Creates GridBagConstraints with specified gridx and gridy positions.
     *
     * @param x The gridx position
     * @param y The gridy position
     * @return The created GridBagConstraints
     */
    private GridBagConstraints createGridBagConstraints(int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;

        return gbc;
    }

    /**
     * Sets the border insets of a JComponent.
     *
     * @param jComponent   The component to set the border insets for
     * @param insetsTop    The top inset
     * @param insetsLeft   The left inset
     * @param insetsBottom The bottom inset
     * @param insetsRight  The right inset
     */
    private void setBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        jComponent.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    /**
     * Creates a resized ImageIcon from the specified path with given dimensions.
     *
     * @param tokenImagePath The path to the image
     * @param dim The desired dimension of the resized image
     * @return The resized ImageIcon
     */
    private static ImageIcon createResizedTokenImageIcon(String tokenImagePath, int dim) {
        return new ImageIcon(new ImageIcon(tokenImagePath).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
    }

    /**
     * Displays the ranking images for the players.
     *
     * @param winners The set of players who are winners
     * @param playerScores The map containing player scores
     * @param playersAvatarMap The map containing player avatars
     */
    public void showRankingImages(Set<String> winners, Map<String, Integer> playerScores, Map<String, String> playersAvatarMap){
        GridBagConstraints gbc = createGridBagConstraints(0,0);
        Random random = new Random();

        for(String player : playerScores.keySet()){
            for(String maxScorePlayer : winners){
                JPanel panel = new JPanel(new GridBagLayout());
                panel.setOpaque(false);
                setBorderInsets(panel, 0,50,0,50);
                JLabel labelPlayer = createTextLabelFont(player, 30);
                panel.add(labelPlayer, gbc);

                gbc.gridy = 1;
                JLabel labelScorePlayer = createTextLabelFont("Score: " + playerScores.get(player), 15);
                setBorderInsets(labelScorePlayer, 0, 0, 10, 0);
                panel.add(labelScorePlayer, gbc);

                gbc.gridy = 2;

                JLabel labelImage;
                if(!player.equals(maxScorePlayer)){
                    String gravestonePath = remappingAvatarToGravestone(playersAvatarMap.get(player));
                    labelImage = new JLabel(createResizedTokenImageIcon(gravestonePath, 250));
                } else {
                    int randomIndex = random.nextInt(4);
                    String selectedMonk = monks.get(randomIndex);
                    monks.remove(selectedMonk);
                    labelImage = new JLabel(createResizedTokenImageIcon(selectedMonk, 250));
                }

                panel.add(labelImage, gbc);

                scorePanel.add(panel);


            }
            gbc.gridy = 0;
            gbc.gridx++;
        }
    }

    /**
     * Remaps avatar paths to corresponding gravestone paths.
     *
     * @param avatarPath The path to the avatar
     * @return The path to the corresponding gravestone
     */
    private String remappingAvatarToGravestone(String avatarPath){
        switch(avatarPath){
            case FUNGI_JUDGE_DIR -> {
                return FUNGI_GRAVESTONE;
            }

            case ANIMAL_JUDGE_DIR -> {
                return ANIMAL_GRAVESTONE;
            }

            case PLANT_JUDGE_DIR -> {
                return PLANT_GRAVESTONE;
            }

            case INSECT_JUDGE_DIR -> {
                return INSECT_GRAVESTONE;
            }

            default -> {
                return avatarPath;
            }
        }
    }


    //Da mandare la lista delle carte obiettivo
    @Override
    public void showStarterCardAndPrivateObjectiveCard(List<Integer> hand) {}
}
