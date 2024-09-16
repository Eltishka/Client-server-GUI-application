package gui.table;

import client.ArgumentChecker;
import gui.Colors;
import gui.StringFormatter;
import lombok.AllArgsConstructor;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;

@AllArgsConstructor
public class VehicleArgumentFormatter extends DefaultFormatter {

    private final ArgumentChecker checker;
    private final StringFormatter formatter;
    private final JLabel argLabel;
    private final JLabel statusLabel;
    private final JFormattedTextField argField;
    private final String errorText;

    @Override
    public Object stringToValue(String string){
        if(this.checker.check(string)){
            this.statusLabel.setVisible(false);
            this.argLabel.setForeground(Colors.OK);
            this.argField.setForeground(Colors.DEFAULT);
        } else {
            this.statusLabel.setText(errorText);
            this.statusLabel.setVisible(true);
            this.statusLabel.setForeground(Colors.ERROR);
            this.argField.setForeground(Colors.ERROR);
            this.argLabel.setForeground(Colors.ERROR);
        }
        return formatter.format(string);
    }
}
