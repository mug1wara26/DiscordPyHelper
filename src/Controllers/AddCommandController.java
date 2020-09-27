package Controllers;

import Model.Main;
import Model.PosArgs;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCommandController implements Initializable {
    @FXML
    private RadioButton positionalRB;
    @FXML
    private RadioButton variableRB;
    @FXML
    private TextField posArgSetNameTF;
    @FXML
    private ComboBox<String> posArgNameCB;
    @FXML
    private Spinner<Integer> posArgSpinner;
    @FXML
    private Hyperlink helpArgHL;
    @FXML
    private Button posArgNameBtn;
    @FXML
    private Label posArgLbl;
    @FXML
    private Label setPosArgNameLbl;

    private ObservableList<String> posArgsNames;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Setting ToggleGroup for radio buttons in arguments tab
        ToggleGroup argGroup = new ToggleGroup();
        positionalRB.setToggleGroup(argGroup);
        variableRB.setToggleGroup(argGroup);

        positionalRB.setSelected(true);

        //Initialize PosArgSpinner
        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99);
        posArgSpinner.setValueFactory(svf);

        //Initialize posArgs
        posArgVis(true);
        posArgSetNameTF.setDisable(true);
        posArgNameBtn.setDisable(true);
    }


    //ARGUMENTS TAB
    //Method to change which controls are visible when clicking on radio buttons
    private void posArgVis(boolean b) {
        posArgLbl.setVisible(b);
        posArgSpinner.setVisible(b);
        setPosArgNameLbl.setVisible(b);
        posArgNameCB.setVisible(b);
        posArgNameBtn.setVisible(b);

        posArgSpinner.setDisable(!b);
        posArgNameCB.setDisable(!b);
        posArgNameBtn.setDisable(!b);
    }

    //Handlers for help hyperlinks
    @FXML
    public void handleHelpArgHL(ActionEvent e) throws URISyntaxException, IOException {
        helpArgHL.setVisited(false);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("https://discordpy.readthedocs.io/en/latest/ext/commands/commands.html#positional"));
        }
    }

    //Handlers for Radio Buttons
    @FXML
    public void handlePositionalRB(ActionEvent e) {
        posArgVis(true);
    }

    @FXML
    public void handleVariableRB(ActionEvent e) {
        posArgVis(false);
    }

    @FXML
    public void handlePosArgSetNameTF(KeyEvent ke) {
        if(posArgSetNameTF.getText().isEmpty()) posArgSetNameTF.setDisable(true);
        else posArgSetNameTF.setDisable(false);
    }

    @FXML
    public void posArgNameCBME(MouseEvent me) {
        if(posArgNameCB.isShowing()) {
            posArgsNames = posArgNameCB.getItems();
            if(posArgSpinner.getValue() < posArgsNames.size()) posArgsNames.remove(posArgSpinner.getValue(), posArgsNames.size());

            else {
                for (int i = posArgsNames.size(); i < posArgSpinner.getValue(); i++) {
                    if (posArgSpinner.getValue() == 1) posArgsNames.add("arg");
                    else posArgsNames.add("arg" + (i + 1));
                }
            }
        }

        posArgNameCB.setItems(posArgsNames);
    }

    @FXML
    public void handlePosArgNameCB(ActionEvent e) {
        String posArgName = posArgNameCB.getValue();
        if(posArgName != null) {
            posArgNameBtn.setDisable(false);
            posArgSetNameTF.setDisable(false);
        }
        else {
            posArgNameBtn.setDisable(true);
            posArgSetNameTF.setDisable(true);
        }
    }

    @FXML
    public void handlePosArgNameBtn(ActionEvent e) {
        boolean argExists = false;
        for (String posArgs : posArgsNames) {
            if (posArgs.equals(posArgSetNameTF.getText())) {
                argExists = true;
                Main.alert(Alert.AlertType.ERROR, "Argument with the same name already exists!");
            }
        }

        if (!argExists) {
            if(!posArgSetNameTF.getText().substring(0,1).equals("*")) {
                for (int i = 0; i < posArgsNames.size(); i++) {
                    if (posArgsNames.get(i).equals(posArgNameCB.getValue())) {
                        posArgsNames.remove(i);
                        posArgsNames.add(i, posArgSetNameTF.getText());
                    }
                }
            }
            else Main.alert(Alert.AlertType.ERROR, "Argument name cannot start with \"*\"!");
        }

        posArgNameCB.setItems(posArgsNames);
    }
}
