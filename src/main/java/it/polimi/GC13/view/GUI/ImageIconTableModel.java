package it.polimi.GC13.view.GUI;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class ImageIconTableModel extends AbstractTableModel {
    private final String[] columnNames;
    private final Object[][] data;

    public ImageIconTableModel(Object[][] data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    /*@Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }*/



}
