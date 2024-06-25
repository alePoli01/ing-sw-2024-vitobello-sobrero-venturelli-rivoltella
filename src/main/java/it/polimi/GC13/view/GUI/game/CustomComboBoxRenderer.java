package it.polimi.GC13.view.GUI.game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom renderer for rendering JLabels in a JComboBox.
 * This renderer customizes the appearance of items based on their selection status and additional data.
 */
public class CustomComboBoxRenderer extends JLabel implements ListCellRenderer<JLabel>{
    private final Map<String, ArrayList<Boolean>> newMessageMap;

    /**
     * Constructs a {@code CustomComboBoxRenderer} with the specified map of new message indicators.
     *
     * @param newMessageMap A map where keys are labels in which is written the player's nickname (or 'global')
     *                      and values are lists indicating new message status.
     *                      <br>
     *                      If there are no new messages, each values of the map contains only one element 'false',
     *                      otherwise it contains a 'true' value for each new message in the queue.
     */
    public CustomComboBoxRenderer(Map<String, ArrayList<Boolean>> newMessageMap) {
        this.newMessageMap = newMessageMap;
        setOpaque(true);
    }

    /**
     * Returns a component that renders an item in a JComboBox.
     *
     * @param list The JList being rendered
     * @param value The value to be rendered
     * @param index The index of the value in the list
     * @param isSelected True if the specified cell is selected
     * @param cellHasFocus True if the specified cell has the focus
     * @return A component to render the specified value in the list
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends JLabel> list, JLabel value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        // Set the text and font of the label
        if (value != null) {
            setText(value.getText());
            setFont(list.getFont());

            // Set background and foreground colors based on selection status
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                if (newMessageMap.containsKey(value.getText()) && newMessageMap.get(value.getText()).contains(Boolean.TRUE)) {
                    setForeground(Color.RED);  // Set foreground to red if there are new messages
                } else {
                    setForeground(list.getForeground());  // Default foreground color
                }
            }
        }

        return this;
    }
}
