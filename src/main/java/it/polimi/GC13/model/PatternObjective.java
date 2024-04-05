package it.polimi.GC13.model;

import it.polimi.GC13.enums.ReignType;

//TODO controllare le condizioni dei for
public class PatternObjective extends ObjectiveCard{
    public final boolean diagonal;  //what kind of disposition is required
    public final int orientation; //orientation of the disposition

//prova
    public PatternObjective(int serialNumber, int comboPoints, boolean diagonal, int orientation) {
        super(serialNumber, comboPoints);
        this.diagonal = diagonal;
        this.orientation = orientation;
    }




    @Override
    public int getObjectivePoints(Board board) {
        int X_max = 50, X_min = 50, Y_max = 50, Y_min = 50;

        //check the dimension of the board
        for (Coordinates xy : board.getBoardMap().keySet()) {
            if (xy.getX() > X_max) {
                X_max = xy.getX();
            }
            if (xy.getX() < X_min) {
                X_min = xy.getX();
            }
            if (xy.getY() > Y_max) {
                Y_max = xy.getY();
            }
            if (xy.getY() < Y_min) {
                Y_min = xy.getY();
            }
        }

        //start to check the patterns by dividing the 2 cases
        if (diagonal) {
            int flag = 0, points = 0;
            //checking the diagonals going to the right
           if (orientation == 0 || orientation == 2) {

               Coordinates iterable = new Coordinates(X_min,Y_max);
               ReignType color;

               if (orientation == 0) {
                   color = ReignType.FUNGI;
               } else {
                   color = ReignType.ANIMAL;
               }

               Coordinates mover = iterable;

                //checking the diagonals of the upper part of the board
               for (int y_offset = 1; y_offset < (Y_max - 2) - Y_min; y_offset++){
                  mover = iterable;
                   for (int d = 1; d <= Y_max - Y_min; d++) {

                       if (board.getBoardMap().containsKey(mover)) {
                             if (board.getBoardMap().get(mover).getCardPointer().reign.equals(color)) {
                                flag++;
                             } else {
                                 flag = 0;
                             }
                       } else {
                           flag = 0;
                       }
                       if (flag == 3){
                           points++;
                           flag = 0;
                       }
                       mover.setX(iterable.getX() + d);
                       mover.setY(iterable.getY() - d);
                   }
                   iterable.setY(Y_max - y_offset);
                   flag=0;
               }

               //resetting variables except points
               iterable.setX(X_min + 1);
               iterable.setY(Y_max);
               flag=0;

               //checking the diagonals of the lower part of the board
               for (int x_offset = 1; x_offset < ((X_max - 2) - (X_min))-1; x_offset++){

                   mover = iterable;

                   for (int d = 1; d<X_max - X_min; d++) {

                       if (board.getBoardMap().containsKey(mover)) {
                           if (board.getBoardMap().get(mover).getCardPointer().reign.equals(color)){
                               flag++;
                           } else {
                               flag = 0;
                           }
                       } else {
                           flag = 0;
                       }
                       if (flag == 3){
                           points++;
                           flag = 0;
                       }
                       mover.setX(iterable.getX() + d);
                       mover.setY(iterable.getY() - d);
                   }
                   iterable.setX(X_min + x_offset);
                   flag=0;
               }
               return points;

           } else {//check the direction going to the left

               Coordinates iterable = new Coordinates(X_max, Y_max);
               ReignType color;

               if (orientation == 1){
                   color = ReignType.INSECT;
               } else {
                   color = ReignType.PLANT;
               }

               Coordinates mover = iterable;

               //checking the diagonals of the upper part of the board
               for (int y_offset = 1; y_offset < (Y_max - 2) - Y_min; y_offset++){
                   mover = iterable;
                   for (int d = 1; d<=Y_max - Y_min; d++) {

                       if (board.getBoardMap().containsKey(mover)) {
                           if (board.getBoardMap().get(mover).getCardPointer().reign.equals(color)){
                               flag++;
                           } else {
                               flag = 0;
                           }
                       } else {
                           flag = 0;
                       }
                       if (flag == 3){
                           points++;
                           flag = 0;
                       }
                       mover.setX(iterable.getX() - d);
                       mover.setY(iterable.getY() - d);
                   }
                   iterable.setY(Y_min-y_offset);
                   flag=0;
               }

               //resetting variables except points
               iterable.setX(X_max - 1);
               iterable.setY(Y_max);
               flag = 0;

               //checking the diagonals of the lower part of the board
               for (int x_offset = 1; x_offset < (X_max) - (X_min + 2); x_offset++){

                   mover = iterable;

                   for (int d = 1; d <= (X_max) - X_min; d++) {

                       if (board.getBoardMap().containsKey(mover)){
                           if (board.getBoardMap().get(mover).getCardPointer().reign.equals(color)){
                               flag++;
                           } else {
                               flag = 0;
                           }
                       }else{
                           flag = 0;
                       }
                       if(flag == 3){
                           points++;
                           flag = 0;
                       }
                       mover.setX(iterable.getX() - d);
                       mover.setY(iterable.getY() - d);
                       flag=0;
                   }
                   iterable.setX(X_max - x_offset);
               }
            return points;
           }

        } else {//checking the L pattern

            int points=0,flag=0;


        }

        return getComboPoints();
    }

 /*
    public PatternObjective(Boolean isDiagonal) {
        this.diagonal = isDiagonal;
    }
*/



}
