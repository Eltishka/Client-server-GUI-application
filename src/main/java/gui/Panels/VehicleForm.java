package gui.Panels;

import client.ArgumentValidator;
import gui.GUIController;
import gui.table.VehicleArgumentFormatter;
import objectspace.FuelType;
import objectspace.VehicleType;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;

public class VehicleForm extends JPanel {


    private JLabel nameLabel = new JLabel(GUIController.resourceBundle.getString("name"));
    private JLabel coordXLabel = new JLabel(GUIController.resourceBundle.getString("coord_x"));
    private JLabel coordYLabel = new JLabel(GUIController.resourceBundle.getString("coord_y"));
    private JLabel enginePowerLabel = new JLabel(GUIController.resourceBundle.getString("engine_power"));
    private JLabel typeLabel = new JLabel(GUIController.resourceBundle.getString("type"));
    private JLabel fuelTypeLabel = new JLabel(GUIController.resourceBundle.getString("fuel_type"));

    private final JFormattedTextField nameField = new JFormattedTextField();
    private final JFormattedTextField coordXField = new JFormattedTextField();
    private final JFormattedTextField coordYField = new JFormattedTextField();
    private final JFormattedTextField enginePowerField = new JFormattedTextField();
    private final JComboBox<VehicleType> typeField = new JComboBox<>(VehicleType.values());
    private final JComboBox<FuelType> fuelTypeField = new JComboBox<>(FuelType.values());

    private final JLabel nameStatus = new JLabel("Ok");
    private final JLabel coordXStatus = new JLabel("Ok");
    private final JLabel coordYStatus = new JLabel("Ok");
    private final JLabel enginePowerStatus = new JLabel(GUIController.resourceBundle.getString("max_length_message"));

    public VehicleForm(){
        super();



        this.nameField.setFormatterFactory(new DefaultFormatterFactory(new VehicleArgumentFormatter(
                ArgumentValidator::checkName, arg -> arg, nameLabel, nameStatus, nameField, GUIController.resourceBundle.getString("name_error"))));
        this.coordXField.setFormatterFactory(new DefaultFormatterFactory(new VehicleArgumentFormatter(
                ArgumentValidator::checkCoordX, Double::parseDouble, coordXLabel, coordXStatus, coordXField, GUIController.resourceBundle.getString("coord_x_error"))));
        this.coordYField.setFormatterFactory(new DefaultFormatterFactory(new VehicleArgumentFormatter(
                ArgumentValidator::checkCoordY, Long::parseLong, coordYLabel, coordYStatus, coordYField, GUIController.resourceBundle.getString("coord_y_error"))));
        this.enginePowerField.setFormatterFactory(new DefaultFormatterFactory(new VehicleArgumentFormatter(
                ArgumentValidator::checkEnginePower, Long::parseLong, enginePowerLabel, enginePowerStatus, enginePowerField, GUIController.resourceBundle.getString("engine_power_error"))));

        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        this.setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameLabel)
                        .addComponent(coordXLabel)
                        .addComponent(coordYLabel)
                        .addComponent(enginePowerLabel)
                        .addComponent(typeLabel)
                        .addComponent(fuelTypeLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameField)
                        .addComponent(nameStatus)
                        .addComponent(coordXField)
                        .addComponent(coordXStatus)
                        .addComponent(coordYField)
                        .addComponent(coordYStatus)
                        .addComponent(enginePowerField)
                        .addComponent(enginePowerStatus)
                        .addComponent(typeField)
                        .addComponent(fuelTypeField)));


        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameField))
                .addGroup(layout.createParallelGroup()
                        .addComponent(nameStatus))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(coordXLabel)
                        .addComponent(coordXField))
                .addGroup(layout.createParallelGroup()
                        .addComponent(coordXStatus))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(coordYLabel)
                        .addComponent(coordYField))
                .addGroup(layout.createParallelGroup()
                        .addComponent(coordYStatus))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(enginePowerLabel)
                        .addComponent(enginePowerField))
                .addGroup(layout.createParallelGroup()
                        .addComponent(enginePowerStatus))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(typeLabel)
                        .addComponent(typeField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(fuelTypeLabel)
                        .addComponent(fuelTypeField)));


        this.setPreferredSize(new Dimension(this.enginePowerStatus.getPreferredSize().width*2, this.getPreferredSize().height));
        this.setupStatuses();
        System.out.println(this.getSize());
        System.out.println(this.getMinimumSize());
        System.out.println(this.enginePowerStatus.getPreferredSize());
    }

    private void setupStatuses(){
        this.nameStatus.setVisible(false);
        this.coordXStatus.setVisible(false);
        this.coordYStatus.setVisible(false);
        this.enginePowerStatus.setVisible(false);
    }

    public String getNameValue(){
        return this.nameField.getText();
    }

    public String getСoordXValue(){
        return (this.coordXField.getText());
    }

    public String getСoordYValue(){
        return (this.coordYField.getText());
    }

    public String getEnginePowerValue(){
        return this.enginePowerField.getText();
    }

    public String getTypeValue(){
        return this.typeField.getSelectedItem().toString();
    }

    public String  getFuelTypeValue(){
        return this.fuelTypeField.getSelectedItem().toString();
    }

    public boolean checkValidity(){
        return ArgumentValidator.checkName(this.nameField.getText()) &&
                ArgumentValidator.checkCoordX(this.coordXField.getText()) &&
                ArgumentValidator.checkCoordY(this.coordYField.getText()) &&
                ArgumentValidator.checkEnginePower(this.enginePowerField.getText());
    }
}
