package it.polimi.GC13.model;

import it.polimi.GC13.enums.Resource;

//TODO controllare le condizioni dei for
public class PatternObjective  extends ObjectiveCard {

    public final boolean diagonal;  //what kind of disposition is required
    public final int orientation; //orientation of the disposition

public PatternObjective(int serialNumber, int comboPoints, boolean diagonal, int orientation) {
    super(serialNumber, comboPoints);
    this.diagonal = diagonal;
    this.orientation = orientation;
}

public int getObjectivePoints(Board board) {

    int X_max = 50, X_min = 50, Y_max = 50, Y_min = 50;

    //creating the variables that rapresent the dimension of the board
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

            Coordinates iterable = new Coordinates(X_min, Y_max);
            Resource color;

            if (orientation == 0) {
                color = Resource.FUNGI;
            } else {
                color = Resource.ANIMAL;
            }

            Coordinates mover = new Coordinates(iterable.getX(), iterable.getY());

            //checking the diagonals of the upper part of the board
            for (int y_offset = 1; y_offset < (Y_max - 2) - Y_min+1; y_offset++) {

                mover.setX(iterable.getX());
                mover.setY(iterable.getY());

                for (int d = 1; d <= (Y_max - Y_min) + 1; d++) {

                    if (board.containsKeyOfValue(mover.getX(), mover.getY())) {

                        if (board.getBoardMap().get(board.get(mover.getX(), mover.getY())).getCardPointer().reign.equals(color)) {
                            flag++;
                        } else {
                            flag = 0;
                        }
                    } else {
                        flag = 0;
                    }
                    if (flag == 3) {
                        points++;
                        flag = 0;
                    }
                    mover.setX(iterable.getX() + d);
                    mover.setY(iterable.getY() - d);
                }
                iterable.setY(Y_max - y_offset);
                flag = 0;
            }

            //resetting variables except points
            iterable.setX(X_min + 1);
            iterable.setY(Y_max);
            flag = 0;

            //checking the diagonals of the lower part of the board
            for (int x_offset = 1; x_offset < ((X_max - 2) - (X_min)) - 1; x_offset++) {

                mover.setX(iterable.getX());
                mover.setY(iterable.getY());

                for (int d = 1; d < X_max - X_min; d++) {

                    if (board.containsKeyOfValue(mover.getX(), mover.getY())) {
                        if (board.getBoardMap().get(board.get(mover.getX(), mover.getY())).getCardPointer().reign.equals(color)) {
                            flag++;
                        } else {
                            flag = 0;
                        }
                    } else {
                        flag = 0;
                    }
                    if (flag == 3) {
                        points++;
                        flag = 0;
                    }
                    mover.setX(iterable.getX() + d);
                    mover.setY(iterable.getY() - d);
                }
                iterable.setX(X_min + x_offset);
                flag = 0;
            }

            return points * getComboPoints();

        } else {//check the direction going to the left

            Coordinates iterable = new Coordinates(X_max, Y_max);
            Resource color;

            if (orientation == 1) {
                color = Resource.INSECT;
            } else {
                color = Resource.PLANT;
            }

            Coordinates mover = new Coordinates(iterable.getX(), iterable.getY());

            //checking the diagonals of the upper part of the board
            for (int y_offset = 1; y_offset <= (Y_max - 2) - Y_min+1; y_offset++) {

                mover.setX(iterable.getX());
                mover.setY(iterable.getY());

                for (int d = 1; d <= (Y_max - Y_min)+1; d++) {

                    if (board.containsKeyOfValue(mover.getX(), mover.getY())) {
                        if (board.getBoardMap().get(board.get(mover.getX(), mover.getY())).getCardPointer().reign.equals(color)) {
                            flag++;
                        } else {
                            flag = 0;
                        }
                    } else {
                        flag = 0;
                    }
                    if (flag == 3) {
                        points++;
                        flag = 0;
                    }
                    mover.setX(iterable.getX() - d);
                    mover.setY(iterable.getY() - d);
                }
                iterable.setY(Y_max - y_offset);
                flag = 0;
            }

            //resetting variables except points
            iterable.setX(X_max - 1);
            iterable.setY(Y_max);
            flag = 0;

            //checking the diagonals of the lower part of the board
            for (int x_offset = 1; x_offset <= (X_max) - (X_min + 2)+1; x_offset++) {

                mover.setX(iterable.getX());
                mover.setY(iterable.getY());

                for (int d = 1; d <= (X_max - X_min)+1; d++) {

                    if (board.containsKeyOfValue(mover.getX(), mover.getY())) {
                        if (board.getBoardMap().get(board.get(mover.getX(), mover.getY())).getCardPointer().reign.equals(color)) {
                            flag++;
                        } else {
                            flag = 0;
                        }
                    } else {
                        flag = 0;
                    }
                    if (flag == 3) {
                        points++;
                        flag = 0;
                    }
                    mover.setX(iterable.getX() - d);
                    mover.setY(iterable.getY() - d);
                    flag = 0;
                }
                iterable.setX(X_max - x_offset);
            }
            return points * getComboPoints();
        }

    } else {//checking the L pattern

        int points = 0, flag = 0;

        Resource color = null;
        Resource colordiagonal = null;

        switch (orientation) {
            case (0):
                color = Resource.PLANT;
                colordiagonal = Resource.INSECT;
                break;
            case (1):
                color = Resource.FUNGI;
                colordiagonal = Resource.PLANT;
                break;
            case (2):
                color = Resource.ANIMAL;
                colordiagonal = Resource.FUNGI;
                break;
            case (3):
                color = Resource.INSECT;
                colordiagonal = Resource.ANIMAL;
                break;
        }

        Coordinates iterable = new Coordinates(X_min, Y_min);
        Coordinates mover=new Coordinates(iterable.getX(), iterable.getY());
        Coordinates moverdiagonal = new Coordinates(iterable.getX(), iterable.getY());

        for (int x_offset = 1; x_offset <= (X_max - X_min)+1; x_offset++) {
            
            mover.setX(iterable.getX());
            if(iterable.getY()%2==0) {
                if (mover.getX() % 2 == 0) {
                    //System.out.println("A");
                    mover.setY(iterable.getY());
                } else {
                    //System.out.println("B");
                    mover.setY(iterable.getY() + 1);
                }
            }else{
                if (mover.getX() % 2 != 0) {
                    //System.out.println("C");
                    mover.setY(iterable.getY());
                } else {
                    //System.out.println("D");
                    mover.setY(iterable.getY() + 1);
                }
            }
            //ystem.out.println(mover.getX()+" "+ mover.getY());
            for (int y_offset = 2; y_offset <= (Y_max - Y_min)+1; y_offset=y_offset+2) {

                if (board.containsKeyOfValue(mover.getX(), mover.getY())) {
                    if (board.getBoardMap().get(board.get(mover.getX(), mover.getY())).getCardPointer().reign.equals(color)) {
                        flag++;
                    } else {
                        flag = 0;
                    }
                } else {
                    flag = 0;
                }
                if (flag == 2) {
                    switch (orientation) {
                        case (0):
                            moverdiagonal.setX(mover.getX() - 1);
                            moverdiagonal.setY(mover.getY() + 1);
                            break;
                        case (1):
                            moverdiagonal.setX(mover.getX() + 1);
                            moverdiagonal.setY(mover.getY() + 1);
                            break;
                        case (2):
                            moverdiagonal.setX(mover.getX() + 1);
                            moverdiagonal.setY(mover.getY() - 3);
                            break;
                        case (3):
                            moverdiagonal.setX(mover.getX() - 1);
                            moverdiagonal.setY(mover.getY() - 3);
                            break;
                    }
                    //System.out.println(moverdiagonal.getX()+" "+ moverdiagonal.getY());
                    if (board.containsKeyOfValue(moverdiagonal.getX(), moverdiagonal.getY())) {
                        if (board.getBoardMap().get(board.get(moverdiagonal.getX(), moverdiagonal.getY())).getCardPointer().reign.equals(colordiagonal)) {
                            points++;
                        }
                    }
                    flag = 0;
                }

                if(iterable.getY()%2==0) {
                    if (mover.getX() % 2 == 0) {
                        mover.setY(iterable.getY() + y_offset);
                    }else{
                        mover.setY(iterable.getY() + y_offset+1);
                    }
                }
                else{
                    //System.out.println("ciao");
                    if (mover.getX() % 2 == 0){
                    mover.setY(iterable.getY() + y_offset+1);
                    }else{
                        mover.setY(iterable.getY() + y_offset);
                    }

                }
                //System.out.println(mover.getX()+" "+ mover.getY());
            }
            iterable.setX(X_min + x_offset);
            //System.out.println("---------------");
            flag = 0;
        }
        return points * getComboPoints();

    }


}

    public void printObjectiveCard() {

    //colors of the background
        String backgroundRed = "\u001b[41m";   // red background
        String backgroundGreen = "\u001b[42m"; // Green Background
        String backgroundBlue = "\u001b[46m";  // Blue background
        String backgroundmagenta = "\u001b[45m";  // Blue
        String resetbackground = "\u001b[0m";  // Reset color of the background
        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters
        String black = "\033[0;30m";

        if (this.diagonal) {
            switch (this.orientation) {
                case (0): {
                    System.out.println("╔═════════════════╗");
                    System.out.println("║  │"+gold+"2"+reset+"│            ║");
                    System.out.println("║          "+black+backgroundRed+"  F  "+resetbackground+reset+"  ║");
                    System.out.println("║      "+black+backgroundRed+"  F  "+resetbackground+reset+"      ║");
                    System.out.println("║  "+black+backgroundRed+"  F  "+resetbackground+reset+"          ║");
                    System.out.println("╚═════════════════╝");
                    break;
                }
                case (2): {
                    System.out.println("╔═════════════════╗");
                    System.out.println("║  │"+gold+"2"+reset+"│            ║");
                    System.out.println("║          "+black+backgroundBlue+"  A  "+resetbackground+reset+"  ║");
                    System.out.println("║      "+black+backgroundBlue+"  A  "+resetbackground+reset+"      ║");
                    System.out.println("║  "+black+backgroundBlue+"  A  "+resetbackground+reset+"          ║");
                    System.out.println("╚═════════════════╝");
                    break;
                }
                case (1): {
                    System.out.println("╔═════════════════╗");
                    System.out.println("║            │"+gold+"2"+reset+"│  ║");
                    System.out.println("║  "+black+backgroundmagenta+"  I  "+resetbackground+reset+"          ║");
                    System.out.println("║      "+black+backgroundmagenta+"  I  "+resetbackground+reset+"      ║");
                    System.out.println("║          "+black+backgroundmagenta+"  I  "+resetbackground+reset+"  ║");
                    System.out.println("╚═════════════════╝");
                    break;
                }
                case (3): {
                    System.out.println("╔═════════════════╗");
                    System.out.println("║            │"+gold+"2"+reset+"│  ║");
                    System.out.println("║  "+black+backgroundGreen+"  P  "+resetbackground+reset+"          ║");
                    System.out.println("║      "+black+backgroundGreen+"  P  "+resetbackground+reset+"      ║");
                    System.out.println("║          "+black+backgroundGreen+"  P  "+resetbackground+reset+"  ║");
                    System.out.println("╚═════════════════╝");
                    break;
                }
            }
        }else{
            switch (this.orientation) {
                case (0): {
                    System.out.println("╔═════════════════╗");
                    System.out.println("║  │"+gold+"3"+reset+"│            ║");
                    System.out.println("║        "+black+backgroundGreen+"  P  "+resetbackground+reset+"    ║");
                    System.out.println("║        "+black+backgroundGreen+"  P  "+resetbackground+reset+"    ║");
                    System.out.println("║    "+black+backgroundmagenta+"  I  "+resetbackground+reset+"        ║");
                    System.out.println("╚═════════════════╝");
                    break;
                }
                case (2): {
                    System.out.println("╔═════════════════╗");
                    System.out.println("║  │"+gold+"3"+reset+"│            ║");
                    System.out.println("║         "+black+backgroundRed+"  F  "+resetbackground+reset+"   ║");
                    System.out.println("║     "+black+backgroundBlue+"  A  "+resetbackground+reset+"       ║");
                    System.out.println("║     "+black+backgroundBlue+"  A  "+resetbackground+reset+"       ║");
                    System.out.println("╚═════════════════╝");
                    break;
                }
                case (1): {
                    System.out.println("╔═════════════════╗");
                    System.out.println("║            │"+gold+"3"+reset+"│  ║");
                    System.out.println("║    "+black+backgroundRed+"  F  "+resetbackground+reset+"        ║");
                    System.out.println("║    "+black+backgroundRed+"  F  "+resetbackground+reset+"        ║");
                    System.out.println("║        "+black+backgroundGreen+"  P  "+resetbackground+reset+"    ║");
                    System.out.println("╚═════════════════╝");
                    break;
                }
                case (3): {
                    System.out.println("╔═════════════════╗");
                    System.out.println("║            │"+gold+"3"+reset+"│  ║");
                    System.out.println("║   "+black+backgroundBlue+"  A  "+resetbackground+reset+"         ║");
                    System.out.println("║       "+black+backgroundmagenta+"  I  "+resetbackground+reset+"     ║");
                    System.out.println("║       "+black+backgroundmagenta+"  I  "+resetbackground+reset+"     ║");
                    System.out.println("╚═════════════════╝");
                    break;
                }
            }
        }
    }

}
