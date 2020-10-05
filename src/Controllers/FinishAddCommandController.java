package Controllers;

import Model.Command;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FinishAddCommandController implements Initializable {
    @FXML
    private Label commandDefLbl;

    private static Command command;

    public static void setCommand(Command c) {
        command = c;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commandDefLbl.setText(command.getCommandDef());
    }
}
