package it.polimi.GC13.view.GUI.game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DragScrollListener implements MouseListener, MouseMotionListener {
    private final JScrollPane scrollPane;
    private Point startPoint;

    public DragScrollListener(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Salva il punto di partenza
        startPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Calcola la distanza trascinata
        Point movedPoint = e.getPoint();
        JViewport viewport = scrollPane.getViewport();
        Point viewPosition = viewport.getViewPosition();

        int deltaX = startPoint.x - movedPoint.x;
        int deltaY = startPoint.y - movedPoint.y;

        viewPosition.translate(deltaX, deltaY);
        ((JComponent) e.getSource()).scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
    }

    // Altri metodi del MouseListener e MouseMotionListener
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}

