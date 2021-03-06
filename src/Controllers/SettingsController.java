package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.prefs.Preferences;

public class SettingsController {
    @FXML
    private TextField pathTF;
    @FXML
    private Button doneBtn;

    final String INIT_PATH_KEY = "initPath";

    public void handleBrowseBtn(ActionEvent e) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open");

        File selectedFolder = directoryChooser.showDialog(null);
        if(selectedFolder != null) {
            pathTF.setText(selectedFolder.getAbsolutePath());
            doneBtn.setDisable(false);
        }
        else {
            pathTF.setText("");
            doneBtn.setDisable(true);
        }
    }

    public void handleDoneBtn(ActionEvent e) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.put(INIT_PATH_KEY, pathTF.getText());

        Stage stage = (Stage) doneBtn.getScene().getWindow();
        stage.close();
    }
}
