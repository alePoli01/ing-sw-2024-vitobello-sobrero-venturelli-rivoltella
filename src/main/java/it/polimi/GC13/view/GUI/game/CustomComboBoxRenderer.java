package it.polimi.GC13.view.GUI.game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CustomComboBoxRenderer extends JLabel implements ListCellRenderer<JLabel>{
    private Map<String, ArrayList<Boolean>> newMessageMap;


    public CustomComboBoxRenderer(Map<String, ArrayList<Boolean>> newMessageMap) {
        this.newMessageMap = newMessageMap;
        setOpaque(true);
    }

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
