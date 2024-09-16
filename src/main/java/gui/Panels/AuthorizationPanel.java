package gui.Panels;

import dataexchange.Request;
import dataexchange.Response;
import gui.AuthorizationException;
import gui.ClientConnectionGUI;
import gui.GUIController;
import hashing.Hasher;

import javax.swing.*;
import java.util.ArrayList;

public class AuthorizationPanel {
    private final ClientConnectionGUI client;
    private final Object[] options;
    public AuthorizationPanel(ClientConnectionGUI client) {
        this.client = client;
        this.options = new Object[]{
                GUIController.resourceBundle.getString("login"),
                GUIController.resourceBundle.getString("register"),
        };


    }

    public String auth() throws AuthorizationException {
        int choice = JOptionPane.showOptionDialog(null,
                GUIController.resourceBundle.getString("welcome_auth"),
                GUIController.resourceBundle.getString("auth_title"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            throw new AuthorizationException("Authorization canceled by the user");
        }
        if(options[choice].equals(GUIController.resourceBundle.getString("login")))
            return login();
        return register();
    }

    private String login() throws AuthorizationException {
        LoginPanel panel = new LoginPanel();
        while (true) {
            int result = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    GUIController.resourceBundle.getString("login"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new String[]{
                            GUIController.resourceBundle.getString("login"),
                    },
                    GUIController.resourceBundle.getString("login")
            );
            if (result == JOptionPane.CLOSED_OPTION)
                throw new AuthorizationException("Authorization canceled by the user");

            String password = Hasher.hashPasswordWith512(panel.getPassword());
            Request request = new Request("login " + password, new ArrayList<String>(), panel.getLogin(), true);
            this.client.sendRequest(request);
            Response response = this.client.receiveResponse();

            if(Boolean.parseBoolean((String)response.getResponse()[0])){
                return panel.getLogin();
            }

            JOptionPane.showMessageDialog(
                    null,
                    GUIController.resourceBundle.getString(String.valueOf(response.getResponseCode())),
                    GUIController.resourceBundle.getString("result"),
                    JOptionPane.PLAIN_MESSAGE
            );

        }
    }

    private String register() throws AuthorizationException {
        LoginPanel panel = new LoginPanel();
        while (true) {
            int result = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    GUIController.resourceBundle.getString("register"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new String[]{
                            GUIController.resourceBundle.getString("register"),
                    },
                    GUIController.resourceBundle.getString("register")
            );
            if (result == JOptionPane.CLOSED_OPTION)
                throw new AuthorizationException("Authorization canceled by the user");

            String password = Hasher.hashPasswordWith512(panel.getPassword());
            Request request = new Request("register " + password, new ArrayList<String>(), panel.getLogin(), true);
            this.client.sendRequest(request);
            Response response = this.client.receiveResponse();

            if(Boolean.parseBoolean((String)response.getResponse()[0])){
                return panel.getLogin();
            }


            JOptionPane.showMessageDialog(
                    null,
                    GUIController.resourceBundle.getString(String.valueOf(response.getResponseCode())),
                    GUIController.resourceBundle.getString("result"),
                    JOptionPane.PLAIN_MESSAGE
            );
        }

    }
}
