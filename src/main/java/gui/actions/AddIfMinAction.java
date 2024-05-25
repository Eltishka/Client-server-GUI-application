package gui.actions;

import dataexchange.Request;
import dataexchange.Response;
import gui.ClientConnectionGUI;
import gui.GUIController;
import gui.Panels.VehicleForm;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

@AllArgsConstructor
public class AddIfMinAction extends AbstractAction {
    private final ClientConnectionGUI client;
    private final GUIController controller = GUIController.getAccess();

    @Override
    public void actionPerformed(ActionEvent e) {
        VehicleForm form = new VehicleForm();
        while(true){
            int result = JOptionPane.showOptionDialog(
                    null,
                    form,
                    GUIController.resourceBundle.getString("add_if_min"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new String[]{GUIController.resourceBundle.getString("add_if_min")},
                    GUIController.resourceBundle.getString("add_if_min")
            );
            if(result == JOptionPane.CLOSED_OPTION)
                break;
            if(result == JOptionPane.YES_NO_OPTION){
                if(form.checkValidity()){
                    ArrayList<String> element = new ArrayList<>();
                    element.add(form.getNameValue());
                    element.add(form.getСoordXValue() + " " + form.getСoordYValue());
                    element.add(form.getEnginePowerValue());
                    element.add(form.getTypeValue());
                    element.add(form.getFuelTypeValue());
                    Request request = new Request("add_if_min", element, controller.getUserName(), true);
                    this.client.sendRequest(request);
                    Response response = this.client.receiveResponse();
                    JOptionPane.showMessageDialog(
                            null,
                            GUIController.resourceBundle.getString(String.valueOf(response.getResponseCode())),
                            GUIController.resourceBundle.getString("result"),
                            JOptionPane.PLAIN_MESSAGE
                    );
                    break;
                }else {
                    JOptionPane.showMessageDialog(
                            null,
                            GUIController.resourceBundle.getString("invalid_data_message"),
                            GUIController.resourceBundle.getString("error"),
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        }
    }
}
