package it.polimi.CG13.model;

public class Coordinates {
    private int x,y;

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public boolean evenVerifier() {
        return (x + y) % 2 == 0; //potremmo sollevare eccezione qua, rendere il metodo void e poi propagarla al controller
    }
}
