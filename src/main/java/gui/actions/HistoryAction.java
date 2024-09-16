package gui.actions;

import dataexchange.Request;
import dataexchange.Response;
import gui.ClientConnectionGUI;
import gui.GUIController;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;


@AllArgsConstructor
public class HistoryAction extends AbstractAction {
    private final ClientConnectionGUI client;
    private final GUIController controller = GUIController.getAccess();

    @Override
    public void actionPerformed(ActionEvent e) {

        Request request = new Request("history", new ArrayList<>(), controller.getUserName(), true);
        this.client.sendRequest(request);
        Response response = this.client.receiveResponse();
        JOptionPane.showMessageDialog(
                null,
                Arrays.stream(response.getResponse()).filter((el) -> !el.equals("show")).toArray(),
                GUIController.resourceBundle.getString("result"),
                JOptionPane.PLAIN_MESSAGE
        );

    }
}