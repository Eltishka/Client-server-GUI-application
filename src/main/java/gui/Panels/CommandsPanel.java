package gui.Panels;

import gui.CommandButton;
import gui.GUIController;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

public class CommandsPanel extends JPanel{
    private final HashMap<String, AbstractAction> buttonsModel;
    private final HashSet<CommandButton> buttons = new HashSet<>();
    private final GUIController guiController;

    public CommandsPanel(HashMap<String, AbstractAction> buttonsModel, GUIController guiController){
        this.buttonsModel = buttonsModel;
        this.guiController = guiController;

        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        for(String key: buttonsModel.keySet()){
            CommandButton button = new CommandButton(GUIController.resourceBundle.getString(key),
                                                     GUIController.resourceBundle.getString(key+"_help"));
            button.addActionListener(buttonsModel.get(key));
            button.addActionListener(this.guiController);
            this.add(button);
            this.buttons.add(button);
        }


        //mapButton.setPreferredSize(tableButton.getPreferredSize());





    }
}
