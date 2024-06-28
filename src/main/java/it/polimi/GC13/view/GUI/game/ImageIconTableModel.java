package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.*;

/**
 * {@code ImageIconTableModel} is a custom table model for displaying images and values in a JTable.
 * <p>
 * This model supports dynamic addition of columns and custom rendering of enums as images.
 *</p>
 * @param <K> The type of the enum keys used in the input map
 * @param <V> The type of the values used in the input map
 */
public class ImageIconTableModel<K extends Enum<K>, V> extends AbstractTableModel {

    /**
     * The names of the columns in the table.
     */
    private String[] columnNames;

    /**
     * The data stored in the table, consisting of rows and columns.
     */
    private Object[][] data;


    /**
     * The list of paths for logo images used in the table.
     */
    private final ArrayList<String> logosPath;

    /**
     * The input map containing enum keys and their corresponding values.
     */
    private final EnumMap<K, V> mapInInput;

    /**
     * A map for converting values to token colors used for rendering images.
     */
    private final Map<V, TokenColor> conversionMap;

    /**
     * An instance of ResourceGetter used to retrieve resources (e.g., image URLs).
     */
    private final ResourceGetter resourceGetter = new ResourceGetter();


    /**
     * Constructs an {@code ImageIconTableModel}.
     *
     * @param columnNames The names of the columns
     * @param mapInInput The input map containing enum keys and their corresponding values
     * @param logosPath The list of paths for the logos
     * @param conversionMap The map for converting values to token colors
     * @param dim The dimension for resizing images
     */
    public ImageIconTableModel(String[] columnNames, EnumMap<K, V> mapInInput, ArrayList<String> logosPath, Map<V, TokenColor> conversionMap, int dim) {
        this.columnNames = columnNames;
        this.logosPath = logosPath;
        this.mapInInput = mapInInput;
        this.data = new Object[mapInInput.size()][columnNames.length];
        this.conversionMap = conversionMap;
        createTable(mapInInput, dim);
    }

    /**
     * Creates the table data from the input map.
     * <p>
     *     The table is designed to have only two columns at creation.
     * </p>
     *
     * @param mapInInput The input map containing enum keys and their corresponding values
     * @param dim The dimension for resizing images
     */
    public void createTable(EnumMap<K, V> mapInInput, int dim){
        try {
            for(Map.Entry<K, V> entry : mapInInput.entrySet()){
                K key = entry.getKey();
                V value = entry.getValue();
                this.data[this.logosPath.indexOf(remappingEnums(key))][0] = createResizedImageIcon(remappingEnums(key), dim);
                this.data[this.logosPath.indexOf(remappingEnums(key))][1] = value;
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

    /**
     * Sets the value at the specified cell in the table model.
     * If the value is a Boolean, it checks if it should be inserted as an icon.
     *
     * @param aValue      The new value to set
     * @param rowIndex    The row index of the cell
     * @param columnIndex The column index of the cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = checkIfIconInsertionOnColumn(aValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Adds a new column to the left side of the table.
     *
     * @param newColumnName The name of the new column
     * @param elements The elements to be added to the new column
     */
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

    /**
     * Adds a new column to the right side of the table.
     *
     * @param newColumnName The name of the new column
     * @param elements The elements to be added to the new column
     */
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

    /**
     * Checks if the element to be inserted in the column is an icon.
     *
     * @param element The element to be checked
     * @return The processed element
     */
    public Object checkIfIconInsertionOnColumn(Object element){
        if (element instanceof Boolean) {
            return createResizedImageIcon(remappingEnums(element), 27);
        }
        return element;
    }

    /**
     * Remaps enums to their corresponding string representation for image paths.
     *
     * @param key The key to be remapped
     * @return The string representation of the key
     */
    public String remappingEnums(Object key) {
        switch (key) {
            case Resource resource -> {
                switch (resource) {
                    case FUNGI -> {
                        return CardManager.FUNGI_LOGO_DIR;
                    }
                    case ANIMAL -> {
                        return CardManager.ANIMAL_LOGO_DIR;
                    }
                    case PLANT -> {
                        return CardManager.PLANT_LOGO_DIR;
                    }
                    case INSECT -> {
                        return CardManager.INSECT_LOGO_DIR;
                    }
                    case QUILL -> {
                        return CardManager.QUILL_LOGO_DIR;
                    }
                    case MANUSCRIPT -> {
                        return CardManager.MANUSCRIPT_LOGO_DIR;
                    }
                    case INKWELL -> {
                        return CardManager.INKWELL_LOGO_DIR;
                    }
                    default -> {
                        return CardManager.ERROR_MONK;
                    }
                }
            }
            case Boolean b -> {
                if (b) {
                    return CardManager.CROWN;
                } else {
                    return " ";
                }
            }
            case Position position -> {
                return this.conversionMap.get(this.mapInInput.get(position)).toString().toLowerCase() + "_token.png";
            }
            case null, default -> {
                return CardManager.ERROR_MONK;
            }
        }
    }

    /**
     * Creates a resized ImageIcon from the given path and dimension.
     *
     * @param ImagePath The path to the image
     * @param dim The dimension for resizing the image
     * @return The resized ImageIcon
     */
    private ImageIcon createResizedImageIcon(String ImagePath, int dim) {
        return new ImageIcon(new ImageIcon(this.resourceGetter.getURL(ImagePath)).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
    }

}
