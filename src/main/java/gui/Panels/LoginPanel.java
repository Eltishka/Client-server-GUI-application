package gui.Panels;

import gui.GUIController;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final JLabel username;
    private final JLabel password;

    private final JFormattedTextField nameField;
    private final JFormattedTextField passwordField;
    public LoginPanel(){
        this.username = new JLabel(GUIController.resourceBundle.getString("nameInput"));
        this.password = new JLabel(GUIController.resourceBundle.getString("passwordInput"));
        this.nameField = new JFormattedTextField();
        this.passwordField = new JFormattedTextField();
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(username)
                        .addComponent(password))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameField)
                        .addComponent(passwordField))

        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(username)
                        .addComponent(nameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(password)
                        .addComponent(passwordField))

        );
        this.setPreferredSize(new Dimension((int) (username.getPreferredSize().getWidth() * 2), (int) (username.getPreferredSize().getHeight() * 4)));
    }

    public String getPassword(){
        return this.passwordField.getText();
    }

    public String getLogin(){
        return this.nameField.getText();
    }


}
