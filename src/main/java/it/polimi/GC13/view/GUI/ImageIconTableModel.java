package it.polimi.GC13.view.GUI;

import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.view.GUI.game.CardManager;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ImageIconTableModel<K extends Enum<K>> extends AbstractTableModel implements CardManager {
    private final String[] columnNames;
    private final Object[][] data;

    public ImageIconTableModel(String[] columnNames, EnumMap<K, Integer> mapInInput) {
        this.columnNames = columnNames;
        this.data = new Object[mapInInput.size()][2];
        createTable(mapInInput);
    }


    public void createTable(EnumMap<K, Integer> mapInInput){
        try {
            for(Map.Entry<K, Integer> entry : mapInInput.entrySet()){
                K key = entry.getKey();
                Integer value = entry.getValue();
                data[CardManager.logos.indexOf(remappingResources(key))][0] = createResizedTokenImageIcon(remappingResources(key), 27);
                data[CardManager.logos.indexOf(remappingResources(key))][1] = value;
            }
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "No such resource found", "invalid resource", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    private static ImageIcon createResizedTokenImageIcon(String tokenImagePath, int dim) {
        return new ImageIcon(new ImageIcon(tokenImagePath).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
    }

    public String remappingResources(K key){
        if (key instanceof Resource) {
            switch ((Resource) key){
                case FUNGI -> {
                    return FUNGI_LOGO_DIR;
                }
                case ANIMAL -> {
                    return ANIMAL_LOGO_DIR;
                }
                case PLANT -> {
                    return PLANT_LOGO_DIR;
                }
                case INSECT -> {
                    return INSECT_LOGO_DIR;
                }
                case QUILL -> {
                    return QUILL_LOGO_DIR;
                }
                case MANUSCRIPT -> {
                    return MANUSCRIPT_LOGO_DIR;
                }
                case INKWELL -> {
                    return INKWELL_LOGO_DIR;
                }
                default -> {
                    return ERROR_IMAGE;
                }
            }
        } else /*if(key instanceof Position)*/{
            return ERROR_IMAGE;
        }
    }

    @Override
    public void showStarterCardAndPrivateObjectiveCard(List<Integer> hand) throws IOException {}
}
