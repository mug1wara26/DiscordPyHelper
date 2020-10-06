package Controllers;

import Model.Command;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FinishAddCommandController implements Initializable {
    @FXML
    private Label commandDefLbl;
    @FXML
    private ComboBox<String> paramsCB;
    @FXML
    private Spinner<Integer> paramPosSpinner;

    private static Command command;
    private ArrayList<String> params;

    public static void setCommand(Command c) {
        command = c;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commandDefLbl.setText(command.getCommandDef());

        params = command.getParams();
        for(int i = 1; i <= params.size(); i++) {
            paramsCB.getItems().add(i + ". " + params.get(i - 1));
        }

        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, params.size());
        paramPosSpinner.setValueFactory(svf);
    }

    @FXML
    public void handleMoveParamPos(ActionEvent e) {

    }
}
