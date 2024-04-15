package it.polimi.GC13.view;

import javax.swing.*;
import java.awt.*;

//classe da utilizzare per ricreare lo sfondo della copertina di Codex Naturalis
public class GeometricalShapes extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawCircumfernce(g, 380);
        drawCircumfernce(g, 410);
        drawCircumfernce(g, 420);
        drawRhombus(g, 440);
        g.setColor(Color.BLACK);
        drawRhombus(g, 420);
        g.setColor(Color.BLACK);
        drawRhombus(g, 400);

    }


    public void drawRhombus(Graphics g, int size){

        // Calculate the center of the panel
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Define the X and Y coordinates of the vertices
        int[] xPoints = {centerX - size / 2, centerX, centerX + size / 2, centerX};
        int[] yPoints = {centerY, centerY - size / 2, centerY, centerY + size / 2};

        // Draw the rhombus

        g.drawPolygon(xPoints, yPoints, 4);

        g.setColor(new Color(236, 186, 21,255));
        g.fillPolygon(xPoints, yPoints, 4);
    }


    public void drawCircumfernce(Graphics g, int circleSize){
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Define the X and Y coordinates of the top-left corner of the bounding box of the circle
        int circleX = centerX - circleSize / 2;
        int circleY = centerY - circleSize / 2;

        g.drawOval(circleX, circleY, circleSize, circleSize);
    }
}
