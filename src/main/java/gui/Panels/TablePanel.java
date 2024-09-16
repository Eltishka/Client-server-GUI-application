package gui.Panels;

import gui.*;
import gui.table.ArgumentCellEditor;
import gui.table.ButtonCellEditor;
import gui.table.ButtonCellRenderer;
import gui.table.VehicleTableModel;
import objectspace.FuelType;
import objectspace.VehicleType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class TablePanel extends JPanel {
    private final ClientConnectionGUI client;
    public TablePanel(ClientConnectionGUI client, Object[] storage, String userName){

        this.client = client;
        BorderLayout layout = new BorderLayout();

        setLayout(layout);

        String[] columnNames = {
                GUIController.resourceBundle.getString("id"),
                GUIController.resourceBundle.getString("owner"),
                GUIController.resourceBundle.getString("name"),
                GUIController.resourceBundle.getString("coord_x"),
                GUIController.resourceBundle.getString("coord_y"),
                GUIController.resourceBundle.getString("engine_power"),
                GUIController.resourceBundle.getString("type"),
                GUIController.resourceBundle.getString("fuel_type"),
        };

        VehicleTableModel tableModel = new VehicleTableModel(storage, columnNames, userName);
        JTable table = new JTable(tableModel);
        tableModel.setTable(table);
        table.setDefaultEditor(Object.class, new ArgumentCellEditor(new JTextField(), table, userName, client));

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        sorter.setComparator(0, Comparator.naturalOrder());
        sorter.setComparator(3, Comparator.naturalOrder());
        sorter.setComparator(4, Comparator.naturalOrder());
        sorter.setComparator(5, Comparator.naturalOrder());


        JFormattedTextField nameFilterTextField = new JFormattedTextField();
        JFormattedTextField ownerFilterTextField = new JFormattedTextField();

        Dimension preferredSize = new Dimension(150, 25);
        nameFilterTextField.setPreferredSize(preferredSize);
        ownerFilterTextField.setPreferredSize(preferredSize);

        DocumentListener filterListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                newFilter();
            }

            private void newFilter() {
                ArrayList<RowFilter<TableModel, Object>> filters = new ArrayList<>(2);
                String nameText = nameFilterTextField.getText();
                String ownerText = ownerFilterTextField.getText();
                if (!nameText.isEmpty()) {
                    filters.add(RowFilter.regexFilter("(?i)" + nameText, 2));
                }
                if (!ownerText.isEmpty()) {
                    filters.add(RowFilter.regexFilter("(?i)" + ownerText, 1));
                }
                RowFilter<TableModel, Object> rf = RowFilter.andFilter(filters);
                sorter.setRowFilter(rf);
            }
        };

        nameFilterTextField.getDocument().addDocumentListener(filterListener);
        ownerFilterTextField.getDocument().addDocumentListener(filterListener);

        JComboBox<VehicleType> comboTypeBox = new JComboBox<>(VehicleType.values());
        DefaultCellEditor typeEditor = new ArgumentCellEditor(comboTypeBox, table, userName, client);
        TableColumn column = table.getColumnModel().getColumn(6);
        column.setCellEditor(typeEditor);

        JComboBox<FuelType> comboFuelBox = new JComboBox<>(FuelType.values());
        DefaultCellEditor fuelTypeEditor = new ArgumentCellEditor(comboFuelBox, table, userName, client);
        column = table.getColumnModel().getColumn(7);
        column.setCellEditor(fuelTypeEditor);


        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel(GUIController.resourceBundle.getString("filter_by_name")));
        filterPanel.add(nameFilterTextField);
        filterPanel.add(new JLabel(GUIController.resourceBundle.getString("filter_by_owner")));
        filterPanel.add(ownerFilterTextField);

        table.getColumn("Action").setCellRenderer(new ButtonCellRenderer());
        table.getColumn("Action").setCellEditor(new ButtonCellEditor(client, tableModel, table));


        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

}
