package gui;

import javax.swing.*;
import java.awt.*;

public class CommandButton extends JButton {
    public CommandButton(String name, String helpString) {
        super(name);

        this.setPreferredSize(new Dimension(150, 40));

        JButton helpButton = new JButton("?");
        helpButton.setPreferredSize(new Dimension(20, 20));
        helpButton.addActionListener((e) -> JOptionPane.showMessageDialog(
                null,
                helpString,
                GUIController.resourceBundle.getString("help"),
                JOptionPane.PLAIN_MESSAGE
        ));

        this.setLayout(new BorderLayout());
        this.add(helpButton, BorderLayout.EAST);
    }

}
