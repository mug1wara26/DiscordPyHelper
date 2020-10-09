package Controllers;

import Model.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class InitializeController implements Initializable {
    @FXML
    private Label title1;
    @FXML
    private Label title2;
    @FXML
    private ImageView pythonLogo;
    @FXML
    private Button newBtn;
    @FXML
    private Button openBtn;
    @FXML
    private Button settingsBtn;


    private final Font TITLEFONT = Font.loadFont("file:resources/fonts/PatrickHand-Regular.ttf", 30);
    private File lastFolderOpened = null;
    public static String botFolderPath = null;

    final String INIT_PATH_KEY = "initPath";
    Preferences prefs = Preferences.userNodeForPackage(getClass());

    public static String getBotFolderPath() {
        return botFolderPath;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title1.setFont(TITLEFONT);
        title2.setFont(TITLEFONT);
        pythonLogo.setImage(new Image(new File("resources/1200px-Python-logo-notext.svg.png").toURI().toString()));

        interactiveBtn(newBtn);
        interactiveBtn(openBtn);
        interactiveBtn(settingsBtn);
    }

    //Method to make buttons more interactive
    private void interactiveBtn(Button btn) {
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-font-size:16");
            Main.getScene().setCursor(Cursor.HAND);
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-font-size:12");
            Main.getScene().setCursor(Cursor.DEFAULT);
        });
    }

    private void setDefaultDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Default Directory");
        if(lastFolderOpened != null) directoryChooser.setInitialDirectory(lastFolderOpened);

        File selectedFolder = directoryChooser.showDialog(null);
        if(selectedFolder != null) {
            lastFolderOpened = selectedFolder;
            prefs.put(INIT_PATH_KEY, selectedFolder.getAbsolutePath());
        }
    }

    public void handleNewBtn(ActionEvent e) throws IOException {
        if(prefs.get(INIT_PATH_KEY, null) == null) {
            Main.alert(AlertType.INFORMATION, "Default Directory has not been initialized yet, please select a default directory", true);
            setDefaultDirectory();
        }
        else {
            Main.changeScene("/View/getBotInfo.fxml");
        }
}

    public void handleOpenBtn(ActionEvent e) throws IOException {
        if(prefs.get(INIT_PATH_KEY, null) == null) {
            Main.alert(AlertType.ERROR, "Default Directory has not been initialized yet, please select a default directory", true);
            return;
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open");
        if(lastFolderOpened != null) directoryChooser.setInitialDirectory(lastFolderOpened);

        File selectedFolder = directoryChooser.showDialog(null);
        if(selectedFolder != null && prefs.get(INIT_PATH_KEY, null).equals(selectedFolder.getParentFile().getAbsolutePath())) {
            lastFolderOpened = selectedFolder;

            botFolderPath = selectedFolder.getAbsolutePath();
            Main.changeScene("/View/application.fxml");
        }
        else Main.alert(AlertType.ERROR, "Project must be in default directory!");
    }

    @FXML
    public void handleSettingsBtn(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/settings.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(new Scene(root, 320, 240));
        stage.showAndWait();
    }
}
