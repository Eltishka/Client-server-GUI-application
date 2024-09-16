package gui.Panels;

import gui.GUIController;

import javax.swing.*;

public class RegisterPanel extends JPanel {

    private final JLabel username;
    private final JLabel password;


    private final JFormattedTextField nameField;
    private final JFormattedTextField passwordField;


    private final JLabel confirmPassword;
    private final JFormattedTextField confirmPasswordField;

    public RegisterPanel(){
        this.username = new JLabel(GUIController.resourceBundle.getString("nameInput"));
        this.password = new JLabel(GUIController.resourceBundle.getString("passwordInput"));
        this.passwordField = new JFormattedTextField();
        this.nameField = new JFormattedTextField();

        this.confirmPassword = new JLabel(GUIController.resourceBundle.getString("confirmPassword"));
        this.confirmPasswordField = new JFormattedTextField();
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(username)
                        .addComponent(password)
                        .addComponent(confirmPassword))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameField)
                        .addComponent(passwordField)
                        .addComponent(confirmPasswordField))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(username)
                        .addComponent(nameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(password)
                        .addComponent(passwordField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(confirmPassword)
                        .addComponent(confirmPasswordField))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        );

    }

    public String getPassword(){
        return this.passwordField.getText();
    }

    public String getLogin(){
        return this.nameField.getText();
    }



}
