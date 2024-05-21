package it.polimi.GC13.view.GUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class ImageIconRenderer extends JLabel implements TableCellRenderer {
    public ImageIconRenderer() {
        setHorizontalAlignment(JLabel.CENTER); // Centra l'icona
    }
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
