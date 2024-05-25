package gui.table;
import gui.ClientConnectionGUI;
import gui.GUIController;
import gui.actions.RemoveAction;

import javax.swing.*;
import java.awt.*;


public class ButtonCellEditor extends DefaultCellEditor {
    private String label;
    private JTable table;
    private VehicleTableModel model;
    private final ClientConnectionGUI client;

    public ButtonCellEditor(ClientConnectionGUI client, VehicleTableModel model, JTable table) {
        super(new JComboBox<>());
        this.model = model;
        this.table = table;
        this.client = client;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = GUIController.resourceBundle.getString("delete");
        JButton button = new JButton(label);
        button.addActionListener(e -> stopCellEditing());
        return button;
    }

    @Override
    public boolean stopCellEditing() {
        new RemoveAction(this.client, (int)model.getValueAt(table.convertRowIndexToModel(table.getEditingRow()),0)).actionPerformed(null);
        int row = table.convertRowIndexToModel(table.getEditingRow());
        model.removeRow(row);
        return super.stopCellEditing();
    }

}