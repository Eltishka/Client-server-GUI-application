package gui.actions;

import dataexchange.Request;
import dataexchange.Response;
import gui.ClientConnectionGUI;
import gui.GUIController;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

@AllArgsConstructor
public class UpdateAction extends AbstractAction {
    private final ClientConnectionGUI client;
    private final GUIController controller = GUIController.getAccess();
    private final ArrayList<String> el;
    private final String id;

    @Override
    public void actionPerformed(ActionEvent e) {
        Request request = new Request("update " + id, el, controller.getUserName(), true);
        this.client.sendRequest(request);
        Response response = this.client.receiveResponse();
        System.out.println(response.getResponse()[0]);

        JOptionPane.showMessageDialog(
                null,
                GUIController.resourceBundle.getString(String.valueOf(response.getResponseCode())),
                GUIController.resourceBundle.getString("result"),
                JOptionPane.PLAIN_MESSAGE
        );
        this.controller.refresh();
    }
}
