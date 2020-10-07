package Controllers;

import Model.Command;
import Model.Main;
import Model.Messenger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
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
    @FXML
    private Button finishBtn;

    private static Command command;
    private ArrayList<String> params;

    public static void setCommand(Command c) {
        command = c;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commandDefLbl.setText(command.getCommandDef());

        params = command.getParams();
        for(String param : params) {
            paramsCB.getItems().add(param);
        }

        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, params.size());
        paramPosSpinner.setValueFactory(svf);
    }

    @FXML
    public void handleMoveParamPos(ActionEvent e) {
        int beforeIndex = paramsCB.getItems().indexOf(paramsCB.getValue());
        int afterIndex = paramPosSpinner.getValue() - 1;
        command.moveParams(beforeIndex, afterIndex);

        paramsCB.getItems().clear();
        paramsCB.getItems().addAll(command.getParams());
        commandDefLbl.setText(command.getCommandDef());
    }

    @FXML
    public void handlePreviousBtn(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/View/addCommand.fxml"));
        commandDefLbl.getScene().setRoot(root);
    }

    @FXML
    public void handleFinishBtn(ActionEvent e) throws IOException {
        finishBtn.setDisable(true);
        ApplicationController applicationController = Messenger.getApplicationController();

        applicationController.addCommand(command.getName(), command.getCommandDef());

        Stage stage = (Stage) commandDefLbl.getScene().getWindow();
        stage.close();
    }
}
