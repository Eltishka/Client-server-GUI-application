package gui.table;

import lombok.Setter;
import objectspace.Vehicle;
import server.utilities.VehicleOwnerPair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Objects;

public class VehicleTableModel extends DefaultTableModel {
    private Object[] storage;
    private String userName;

    @Setter
    private JTable table;

    public VehicleTableModel(Object[] storage, String[] columns, String userName){
        Object[][] data = new Object[storage.length][columns.length];
        for(int i = 0; i < storage.length; i++) {
            VehicleOwnerPair<Vehicle, String> el = (VehicleOwnerPair<Vehicle, String>) storage[i];
            Vehicle vehicle = el.getFirst();
            data[i][0] = vehicle.getId();
            data[i][1] = el.getSecond();
            data[i][2] = vehicle.getName();
            data[i][3] = vehicle.getCoordinates().getX();
            data[i][4] = vehicle.getCoordinates().getY();
            data[i][5] = vehicle.getEnginePower();
            data[i][6] = vehicle.getType();
            data[i][7] = vehicle.getFuelType();
        }
        String[] columnsWithButton = new String[columns.length + 1];
        System.arraycopy(columns, 0, columnsWithButton, 0, columns.length);
        columnsWithButton[columns.length] = "Action"; // Column name for the button
        setDataVector(data, columnsWithButton);
        this.storage = storage;
        this.userName = userName;

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        int actualRow = this.table.convertRowIndexToModel(row);
        System.out.println(((VehicleOwnerPair)(this.storage[actualRow])).getSecond().equals(this.userName));
        if(getColumnName(column).equals("owner") || getColumnName(column).equals("id"))
            return false;
        return ((VehicleOwnerPair)(this.storage[actualRow])).getSecond().equals(this.userName) || Objects.equals(this.userName, "admin");
    }

    @Override
    public void removeRow(int row) {
        int modelRow = table.convertRowIndexToModel(row);
        super.removeRow(modelRow);
        Object[] newStorage = new Object[storage.length - 1];
        int idx = 0;
        for (int i = 0; i < storage.length; i++) {
            if (i != modelRow) {
                newStorage[idx++] = storage[i];
            }
        }
        storage = newStorage;
    }


}
