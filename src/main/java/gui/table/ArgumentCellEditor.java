package gui.table;

import client.ArgumentValidator;
import gui.ClientConnectionGUI;
import gui.GUIController;
import gui.actions.UpdateAction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ArgumentCellEditor extends DefaultCellEditor {

    private JTable table;
    private final String userName;
    private final ClientConnectionGUI client;
    private JComboBox comboBox;
    private int editingRow;
    private int editingColumn;

    private Object oldValue;
    private Object curValue;

    public ArgumentCellEditor(JTextField textField, JTable table, String userName, ClientConnectionGUI client) {
        super(textField);
        this.table = table;
        this.userName = userName;
        this.client = client;
    }

    public ArgumentCellEditor(JComboBox comboBox, JTable table, String userName, ClientConnectionGUI client) {
        super(comboBox);
        this.comboBox = comboBox;
        this.table = table;
        this.userName = userName;
        this.client = client;
    }

    @Override
    public boolean stopCellEditing() {

        this.curValue = this.comboBox == null ? getCellEditorValue() : this.comboBox.getSelectedItem();
        int row = this.editingRow;
        int columnCount = table.getColumnCount();
        String[] rowData = new String[columnCount];

        System.out.println(row);
        for (int i = 0; i < columnCount; i++) {
            rowData[i] = String.valueOf(table.getValueAt(row, i));
        }
        rowData[this.editingColumn] = curValue.toString();
        ArrayList<String> data = new ArrayList<>();
        data.add(rowData[2]);
        data.add(rowData[3] + " " + rowData[4]);
        data.add(rowData[5]);
        data.add(rowData[6]);
        data.add(rowData[7]);

        if(ArgumentValidator.checkFullObject(data)) {
            System.out.println(oldValue);
            if(oldValue.equals(curValue)){
                cancelCellEditing();
                return false;
            }
            (new UpdateAction(client, data, rowData[0])).actionPerformed(null);
            return super.stopCellEditing();
        }
        String msg = "";
        String columnName = table.getColumnName(table.getEditingColumn());
        if(columnName.equals(GUIController.resourceBundle.getString("name")))
            msg = GUIController.resourceBundle.getString("name_error");
        else if(columnName.equals(GUIController.resourceBundle.getString("coord_x")))
            msg = GUIController.resourceBundle.getString("coord_x_error");
        else if(columnName.equals(GUIController.resourceBundle.getString("coord_y")))
            msg = GUIController.resourceBundle.getString("coord_y_error");
        else if(columnName.equals(GUIController.resourceBundle.getString("engine_power")))
            msg = GUIController.resourceBundle.getString("engine_power_error");

        if(this.comboBox != null) {
            cancelCellEditing();
            return false;
        }

        JOptionPane.showMessageDialog(
                null,
                msg,
                GUIController.resourceBundle.getString("error"),
                JOptionPane.ERROR_MESSAGE
        );

        cancelCellEditing();
        return false;
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.editingRow = row;
        this.editingColumn = column;
        this.oldValue = value;
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }


}
