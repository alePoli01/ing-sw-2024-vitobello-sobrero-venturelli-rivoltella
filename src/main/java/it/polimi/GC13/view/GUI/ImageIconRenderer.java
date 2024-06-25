package it.polimi.GC13.view.GUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/**
 * Custom TableCellRenderer for displaying ImageIcon in a JTable cell.
 */
public class ImageIconRenderer extends JLabel implements TableCellRenderer {

    /**
     * Constructs an ImageIconRenderer.
     * <p>
     * Sets the horizontal alignment of the label to center the icon.
     * </p>
     */
    public ImageIconRenderer() {
        setHorizontalAlignment(JLabel.CENTER); // center the icon
    }

    /**
     * Returns the component used for rendering a table cell.
     * If the value is an instance of ImageIcon, sets it as the icon for the label.
     * If not, sets the icon to null.
     *
     * @param table The JTable that is asking the renderer to draw
     * @param value The value to assign to the cell at [row, column]
     * @param isSelected True if the cell is selected
     * @param hasFocus True if the cell has focus
     * @param row The row index of the cell being rendered
     * @param column The column index of the cell being rendered
     * @return The component used for rendering
     */
    @Override
    public JComponent getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ImageIcon) {
            setIcon((ImageIcon) value);
        } else {
            setIcon(null);
        }
        return this;
    }
}
