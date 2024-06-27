package it.polimi.GC13.view.GUI.game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A mouse listener and mouse motion listener that enables dragging to scroll functionality for a JScrollPane.
 * <br>
 * This listener allows the user to drag the viewport of the scroll pane by clicking and dragging with the mouse.
 */
public class DragScrollListener implements MouseListener, MouseMotionListener {
    private final JScrollPane scrollPane;
    private Point startPoint;

    /**
     * Constructs a DragScrollListener for the specified scroll pane.
     *
     * @param scrollPane The JScrollPane to enable dragging scroll functionality for
     */
    public DragScrollListener(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    /**
     * Saves the starting point of the mouse drag when pressed.
     *
     * @param e The MouseEvent containing information about the event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // saves starting point
        startPoint = e.getPoint();
    }

    /**
     * Scrolls the JScrollPane's viewport based on the mouse drag movement.
     *
     * @param e The MouseEvent containing information about the event
     */
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

    // Methods from MouseListener and MouseMotionListener interfaces
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

