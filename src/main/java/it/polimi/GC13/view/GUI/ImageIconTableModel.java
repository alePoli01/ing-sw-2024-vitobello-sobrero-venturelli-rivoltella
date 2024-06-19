package it.polimi.GC13.view.GUI;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.view.GUI.game.CardManager;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ImageIconTableModel<K extends Enum<K>, V> extends AbstractTableModel implements CardManager {
    private String[] columnNames;
    private Object[][] data;
    private final ArrayList<String> logosPath;
    private final EnumMap<K, V> mapInInput;
    private final Map<V, TokenColor> conversionMap;


    public ImageIconTableModel(String[] columnNames, EnumMap<K, V> mapInInput, ArrayList<String> logosPath, Map<V, TokenColor> conversionMap, int dim) {
        this.columnNames = columnNames;
        this.logosPath = logosPath;
        this.mapInInput = mapInInput;
        this.data = new Object[mapInInput.size()][columnNames.length];
        this.conversionMap = conversionMap;
        createTable(mapInInput, dim);
    }

    public void createTable(EnumMap<K, V> mapInInput, int dim){
        try {
            for(Map.Entry<K, V> entry : mapInInput.entrySet()){
                K key = entry.getKey();
                V value = entry.getValue();
                data[logosPath.indexOf(remappingEnums(key))][0] = createResizedTokenImageIcon(remappingEnums(key), dim);
                data[logosPath.indexOf(remappingEnums(key))][1] = value;
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
        data[rowIndex][columnIndex] = checkIfIconInsertionOnColumn(aValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addColumnOnLeft(String newColumnName, ArrayList<Object> elements) {
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
            if (i < elements.size() && elements.get(i) != null) {
                newData[i][0] = checkIfIconInsertionOnColumn(elements.get(i));
            } else {
                newData[i][0] = null;
            }
        }

        data = newData;

        fireTableStructureChanged();
    }

    public void addColumnOnRight(String newColumnName, ArrayList<Object> elements) {
        String[] newColumnNames = new String[columnNames.length + 1];
        System.arraycopy(columnNames, 0, newColumnNames, 0, columnNames.length);
        newColumnNames[columnNames.length] = newColumnName;
        columnNames = newColumnNames;

        int newRowCount = data.length;
        int newColumnCount = data[0].length + 1;

        Object[][] newData = new Object[newRowCount][newColumnCount];
        for (int i = 0; i < newRowCount; i++) {
            System.arraycopy(data[i], 0, newData[i], 0, data[i].length);
        }

        for (int i = 0; i < newRowCount; i++) {
            if (i < elements.size() && elements.get(i) != null) {
                newData[i][newColumnCount - 1] = checkIfIconInsertionOnColumn(elements.get(i));
            } else {
                newData[i][newColumnCount - 1] = null;
            }
        }

        data = newData;

        fireTableStructureChanged();
    }

    public Object checkIfIconInsertionOnColumn(Object element){
        if(element instanceof Boolean){
            return createResizedTokenImageIcon(remappingEnums(element), 27);
        }
        return element;
    }

    public String remappingEnums(Object key){
        switch (key) {
            case Resource resource -> {
                switch (resource) {
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
                        return ERROR_FISH;
                    }
                }
            }
            case Boolean b -> {
                if (b) {
                    return CROWN;
                } else {
                    return " ";
                }
            }
            case Position position -> {
                return P_TOKEN_DIR + getTokenFileName(conversionMap.get(mapInInput.get(position)));
            }
            case null, default -> {
                return ERROR_FISH;
            }
        }
    }

    private String getTokenFileName(TokenColor tokenColor) {
        return tokenColor.toString().toLowerCase() + TOKEN_FILE_SUFFIX;
    }

    private static ImageIcon createResizedTokenImageIcon(String tokenImagePath, int dim) {
        return new ImageIcon(new ImageIcon(tokenImagePath).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
    }

    @Override
    public void showStarterCardAndPrivateObjectiveCard(List<Integer> hand){}
}
