package gui.actions;

import dataexchange.Request;
import dataexchange.Response;
import gui.ClientConnectionGUI;
import gui.GUIController;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@AllArgsConstructor
public class InfoAction extends AbstractAction {
    private final ClientConnectionGUI client;
    private final GUIController controller = GUIController.getAccess();
    @Override
    public void actionPerformed(ActionEvent e) {
        Request request = new Request("info", new ArrayList<>(), controller.getUserName(), true);
        this.client.sendRequest(request);
        Object[] response = this.client.receiveResponse().getResponse();
        Object[] userFriendlyResponse = new Object[]{
                GUIController.resourceBundle.getString("info_collection_typ"),
                response[0],
                GUIController.resourceBundle.getString("info_el_count"),
                response[1]
        };
        JOptionPane.showMessageDialog(
                null,
                userFriendlyResponse,
                "result",
                JOptionPane.PLAIN_MESSAGE
        );
    }
}
