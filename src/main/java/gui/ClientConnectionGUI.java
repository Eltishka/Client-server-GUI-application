package gui;

import client.Client;
import dataexchange.Request;
import dataexchange.Response;

import javax.swing.*;
import java.io.IOException;
import java.net.ConnectException;

public class ClientConnectionGUI {
    private Client client;

    public ClientConnectionGUI(Client client) {
        this.client = client;
    }

    public void sendRequest(Request request){
        try {
            this.client.sendRequest(request);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    GUIController.resourceBundle.getString("server_not_aval"),
                    GUIController.resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
            throw new RuntimeException(e);
        }
    }

    public Response receiveResponse(){
        try {
            return this.client.receiveResponse();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    GUIController.resourceBundle.getString("server_not_aval"),
                    GUIController.resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(
                    null,
                    GUIController.resourceBundle.getString("version_error"),
                    GUIController.resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return null;
    }

    public Response start(){
        try {
            return this.client.start();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(
                    null,
                    GUIController.resourceBundle.getString("version_error"),
                    GUIController.resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(
                    null,
                    GUIController.resourceBundle.getString("server_not_aval"),
                    GUIController.resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return null;
    }
}
