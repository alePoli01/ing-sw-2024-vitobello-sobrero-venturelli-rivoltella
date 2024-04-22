package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class TokenManager {
    private ArrayList<String> imageFile;
    private ArrayList<Token> tokenInGame;
    private int k;


    public TokenManager(){
        this.tokenInGame = new ArrayList<>();
        this.imageFile = new ArrayList<>(Arrays.asList("src/main/utils/token/yellow_token.png", "src/main/utils/token/blue_token.png", "src/main/utils/token/red_token.png", "src/main/utils/token/green_token.png"));
        this.k=1;
    }

    public void createToken(/*Player player,*/JLabel label){
        Random random = new Random();
        int randomIndex = random.nextInt(imageFile.size());
        //System.out.println(randomIndex);
        Token tokenPlayer = new Token(/*player,*/imageFile.get(randomIndex), label);
        addToken(tokenPlayer);
        removeToList(randomIndex);
        setInitialPosition(tokenPlayer);
    }


    public void addToList(String directory){
        imageFile.add(directory);
    }

    public void removeToList(int randomIndex){
        imageFile.remove(randomIndex);
    }

    public void addToken(Token token){
        tokenInGame.add(token);
    }

    public void removeToken(Token token){
        tokenInGame.remove(token);
    }

    public void setInitialPosition(Token token){ //possibilmente dsa rifare con cicli for
        switch(k){
            case 0:
                token.setX(700);
                token.setY(800);
                k++;
                break;
            case 1:
                token.setX(750);
                token.setY(800);
                k++;
                break;
            case 2:
                token.setX(700);
                token.setY(850);
                k++;
                break;
            case 3:
                token.setX(750);
                token.setY(850);
                k++;
                break;
            default:
                //System.out.println("Error, ");
                k=0;
                setInitialPosition(token);
                break;
        }
    }

    public void moveHorizontal(Token token){
        token.setX(token.getX()+100);
        token.getLabel().setBounds(token.getX(), token.getY(), token.getLabel().getIcon().getIconWidth(), token.getLabel().getIcon().getIconHeight());
        token.getLabel().repaint();
    }

    public void moveVertical(Token token){
        token.setY(token.getY()-100);
        token.getLabel().setBounds(token.getX(), token.getY(), token.getLabel().getIcon().getIconWidth(), token.getLabel().getIcon().getIconHeight());
        token.getLabel().repaint();
    }

    public void moveDiagonal(Token token){ //DA TESTARE
        token.setX(token.getX()+100);
        token.setY(token.getY()-50);
        token.getLabel().setBounds(token.getX(), token.getY(), token.getLabel().getIcon().getIconWidth(), token.getLabel().getIcon().getIconHeight());
        token.getLabel().repaint();
    }

    public ArrayList<Token> getTokenInGame() {
        return tokenInGame;
    }
}
