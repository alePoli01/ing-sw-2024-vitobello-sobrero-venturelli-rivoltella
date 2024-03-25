package it.polimi.CG13.model;

import it.polimi.CG13.enums.ReignType;
import it.polimi.CG13.enums.TypePattern;

//TODO controllare le condizioni dei for
public class PatternObjective extends ObjectiveCard{
    private boolean diagonal;  //what kind of disposition is required
    private int orientation; //orientation of the disposition

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

            //checking the diagonals going to the right
           if (orientation == 0 || orientation == 2) {

               Coordinates iterable = new Coordinates(X_min,Y_min);
               ReignType color;
               int flag = 0, points = 0;

               if (orientation == 0) {
                   color = ReignType.FUNGI;
               } else {
                   color = ReignType.ANIMAL;
               }

               Coordinates mover = iterable;

                //checking the diagonals of the upper part of the board
               for (int y_offset = 0; y_offset < (Y_max - 2) - Y_min; y_offset++){
                  mover = iterable;
                   for (int d = 0; d < Y_max - y_offset; d++) {

                       if (board.getBoardMap().containsKey(mover)) {
                             if (board.getBoardMap().get(mover).getCardPointer().getReign().equals(color)) {
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
                       mover.setY(iterable.getY() + d);
                   }
                   iterable.setY(Y_min + y_offset);
               }
               //resetting variables except points
               iterable.setX(X_min + 1);
               iterable.setY(Y_min);
               flag=0;

               //checking the diagonals of the lower part of the board
               for (int x_offset = 0; x_offset < (X_max - 2) - (X_min + 1); x_offset++){

                   mover = iterable;

                   for (int d = 0; d<X_max - x_offset; d++) {

                       if (board.getBoardMap().containsKey(mover)) {
                           if (board.getBoardMap().get(mover).getCardPointer().getReign().equals(color)){
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
                       mover.setY(iterable.getY() + d);
                   }
                   iterable.setX(X_min + x_offset);
               }

           } else {//check the direction going to the left

               Coordinates iterable = new Coordinates(X_max, Y_min);
               ReignType color;

               int flag = 0, points = 0;

               if (orientation == 1){
                   color = ReignType.INSECT;
               } else {
                   color = ReignType.PLANT;
               }

               Coordinates mover = iterable;

               //checking the diagonals of the upper part of the board
               for (int y_offset = 0; y_offset < (Y_max - 2) - Y_min; y_offset++){
                   mover = iterable;
                   for (int d = 0; d<Y_max - y_offset; d++) {

                       if (board.getBoardMap().containsKey(mover)) {
                           if (board.getBoardMap().get(mover).getCardPointer().getReign().equals(color)){
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
                       mover.setY(iterable.getY() + d);
                   }
                   iterable.setY(Y_min+y_offset);
               }

               //resetting variables except points
               iterable.setX(X_max - 1);
               iterable.setY(Y_min);
               flag = 0;

               //checking the diagonals of the lower part of the board
               for (int x_offset = 0; x_offset < (X_max - 1) - (X_min + 2); x_offset++){

                   mover = iterable;

                   for (int d = 0; d < (X_max) - x_offset; d++) {

                       if (board.getBoardMap().containsKey(mover)){
                           if (board.getBoardMap().get(mover).getCardPointer().getReign().equals(color)){
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
                       mover.setY(iterable.getY() + d);
                   }
                   iterable.setX(X_min - x_offset);
               }

           }

        } else {//checking the L pattern
        //filler



        }

        return getComboPoints();
    }

    public PatternObjective(Boolean isDiagonal) {
        this.diagonal = isDiagonal;
    }




}
