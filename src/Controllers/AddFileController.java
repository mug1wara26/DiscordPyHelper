package Controllers;

import Model.Main;
import Model.Messenger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddFileController implements Initializable {
    @FXML
    private RadioButton folderRB;
    @FXML
    private RadioButton fileRB;
    @FXML
    private TextField inputNameTF;
    @FXML
    private Button finishBtn;

    private static File file;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        folderRB.setToggleGroup(toggleGroup);
        fileRB.setToggleGroup(toggleGroup);
        folderRB.setSelected(true);
        inputNameTF.setPromptText("Folder Name");
    }

    public void handleFolderRB(ActionEvent e) {
        inputNameTF.setPromptText("Folder Name");
    }

    public void handleFileRB(ActionEvent e) {
        inputNameTF.setPromptText("File Name");
    }

    public void inputNameTFOnKeyTyped(KeyEvent ke) {
        finishBtn.setDisable(inputNameTF.getText().isEmpty());
    }

    public void handleCancelBtn(ActionEvent e) {
        Stage stage = (Stage) finishBtn.getScene().getWindow();
        stage.close();
    }

    public static void setFile(File file) {
        AddFileController.file = file;
    }

    public void handleFinishBtn(ActionEvent e) throws IOException {
        File newFile = new File(file.getAbsolutePath() + "\\" + inputNameTF.getText());
        if(!newFile.exists()) {
            if (folderRB.isSelected()) newFile.mkdir();
            if (fileRB.isSelected()) newFile.createNewFile();
            Messenger.getApplicationController().addChildItem(Messenger.getRoot(), newFile);
            Stage stage = (Stage) finishBtn.getScene().getWindow();
            stage.close();
        }
        else Main.alert(Alert.AlertType.ERROR, "Folder/File already exists!");
    }
}
