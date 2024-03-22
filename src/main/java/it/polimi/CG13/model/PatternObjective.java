package it.polimi.CG13.model;

import it.polimi.CG13.enums.TypePattern;

public class PatternObjective extends ObjectiveCard{
    private boolean diagonal;  //what kind of disposition is required
    private int orientation; //orientation of the disposition

    @Override
    public int getObjectivePoints(Board board) {
        int X_max=50,X_min=50,Y_max=50,Y_min=50;

        //check the dimension of the board
        for(Coordinates xy : board.getBoardMap().keySet()){
            if(xy.getX()>X_max){
                X_max = xy.getX();
            }
            if(xy.getX()<X_min){
                X_min = xy.getX();
            }
            if(xy.getY()>Y_max){
                Y_max = xy.getY();
            }
            if(xy.getY()<Y_min){
                Y_min = xy.getY();
            }
        }

        //start to check the patterns
        if(diagonal){
           if(orientation==0||orientation==2){
               Coordinates iterable = new Coordinates(X_min,Y_min);

           }else{
               Coordinates iterable = new Coordinates(X_max,Y_min);
           }

        }else{

        }

        return getComboPoints();
    }

    public PatternObjective(Boolean isDiagonal) {
        this.diagonal = isDiagonal;
    }




}
