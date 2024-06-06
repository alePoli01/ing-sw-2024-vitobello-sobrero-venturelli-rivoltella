package it.polimi.GC13.view.GUI;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.view.GUI.game.CardManager;
import it.polimi.GC13.view.GUI.game.MainPage;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ImageIconTableModel<K extends Enum<K>, V> extends AbstractTableModel implements CardManager {
    private String[] columnNames;
    private Object[][] data;

    public ImageIconTableModel(String[] columnNames, EnumMap<K, V> mapInInput) {
        this.columnNames = columnNames;
        this.data = new Object[mapInInput.size()][columnNames.length];
        createTable(mapInInput);
    }


    public void createTable(EnumMap<K, V> mapInInput){
        try {
            for(Map.Entry<K, V> entry : mapInInput.entrySet()){
                K key = entry.getKey();
                V value = entry.getValue();
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

    public void addColumnOnLeft(String newColumnName, Object[] elements) {
        String[] newColumnNames = new String[columnNames.length + 1];
        newColumnNames[0] = newColumnName;
        System.arraycopy(columnNames, 0, newColumnNames, 1, columnNames.length);
        columnNames = newColumnNames;

        int newRowCount = data.length;
        int newColumnCount = data[0].length + 1;

        Object[][] newData = new Object[newRowCount][newColumnCount];
        for(int i = 0; i < newRowCount; i++){
            System.arraycopy(data[i], 0, newData[i], 1, data[i].length);
        }

        for (int i = 0; i < newRowCount; i++) {
            if (i < elements.length && elements[i] != null) {
                newData[i][0] = checkIfIconInsertionOnColumn(elements[i]);
            } else {
                newData[i][0] = null;
            }
        }

        data = newData;

        fireTableStructureChanged();
    }

    public Object checkIfIconInsertionOnColumn(Object element){
        if(element instanceof Boolean){
            return createResizedTokenImageIcon(remappingResources(element), 27);
        }
        return element;
    }


    private static ImageIcon createResizedTokenImageIcon(String tokenImagePath, int dim) {
        return new ImageIcon(new ImageIcon(tokenImagePath).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
    }

    public String remappingResources(Object key){
        if (key instanceof Resource) {
            switch ((Resource) key) {
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
        } else if (key instanceof Boolean) {
            if((Boolean) key){
                return CROWN;
            }else{
                return " ";
            }
        } else
            return ERROR_IMAGE;
    }

    @Override
    public void showStarterCardAndPrivateObjectiveCard(List<Integer> hand) throws IOException {}
}
