package gui.Panels;

import gui.GUIController;
import gui.NavigateButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NavigatePanel extends JPanel {
    public NavigatePanel(ActionListener contentPanel, String userName){

        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        JButton mapButton = new NavigateButton(GUIController.resourceBundle.getString("map"));
        JButton tableButton = new NavigateButton(GUIController.resourceBundle.getString("table"));

        mapButton.addActionListener(contentPanel);
        tableButton.addActionListener(contentPanel);

        mapButton.setPreferredSize(tableButton.getPreferredSize());

        JPanel centerButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        centerButtonsPanel.add(mapButton);
        centerButtonsPanel.add(tableButton);
        this.add(centerButtonsPanel, BorderLayout.CENTER);








    }
}
