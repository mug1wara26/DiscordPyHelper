package Controllers;

import Model.Main;
import Model.PythonSyntaxArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.prefs.Preferences;

public class ApplicationController implements Initializable {
    @FXML
    private VBox codeAreaVBox;
    @FXML
    private TabPane commandsTabPane;
    @FXML
    private Tab addBtnTab;
    @FXML
    private Button addBtn;
    @FXML
    private Tab mainPyTab;


    private File lastFolderOpened = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main.getStage().setMaximized(true);

        //Setting up code area for main.py
        VBox mainPyVBox = new VBox();
        CodeArea mainPyCA = PythonSyntaxArea.getCodeArea();
        mainPyCA.setPrefHeight(700);


        String content = null;
        try {
            content = new Scanner(new File(GetBotInfoController.getBotFolderPath() + "\\main.py")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mainPyCA.appendText(content);


        mainPyVBox.getChildren().add(mainPyCA);
        mainPyTab.setContent(mainPyVBox);

        //Add tooltip to button that add commands
        Tooltip tooltip = new Tooltip("Add new command");
        tooltip.setShowDelay(Duration.seconds(0.5));
        addBtn.setTooltip(tooltip);

        addBtnTab.setDisable(true);
    }

    @FXML
    public void handleAddBtn(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/addCommand.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Create Command");
        stage.setScene(new Scene(root, 640, 480));
        stage.show();


    }


    //Method that saves the entire project
    private void save() {
        save(new GetBotInfoController().getBotFolderPath());
    }

    private void save(String folderPath) {
    }

    private boolean saveConfirmation() {
        Alert a = new Alert(AlertType.CONFIRMATION, "Do you want to save your project?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = a.showAndWait();
        if(result.get() == ButtonType.YES) save();
        if(result.get() == ButtonType.YES || result.get() == ButtonType.NO) return true;
        else return false;
    }

    //Handling Menu Bar Item: File
    @FXML
    public void handleMenuNew(ActionEvent e) throws IOException {
        if(saveConfirmation()) {
            Main.getScene().getWindow().setHeight(480);
            Main.getScene().getWindow().setWidth(640);
            Main.changeScene("/View/getBotInfo.fxml");
        }
    }

    @FXML
    public void handleMenuOpen(ActionEvent e) {
    }

    @FXML
    public void handleMenuClose(ActionEvent e) throws IOException {
        if(saveConfirmation()) {
            Main.getScene().getWindow().setHeight(480);
            Main.getScene().getWindow().setWidth(640);
            Main.changeScene("/View/initialize.fxml");
        }
    }

    @FXML
    public void handleMenuSettings(ActionEvent e) {
    }

    @FXML
    public void handleMenuSave(ActionEvent e) {
        save();
    }

    @FXML
    public void handleMenuSaveAs(ActionEvent e) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Folder");

        if(lastFolderOpened != null) directoryChooser.setInitialDirectory(lastFolderOpened);

        File selectedFolder = directoryChooser.showDialog(null);
        if(selectedFolder != null) {
            lastFolderOpened = selectedFolder;
            save(selectedFolder.getAbsolutePath());
        }
    }

    @FXML
    public void handleMenuExit(ActionEvent e) {
        if(saveConfirmation()) Main.getStage().close();
    }

    @FXML
    public void handleMenuCopy(ActionEvent e) {
    }

    @FXML
    public void handleMenuCopyAbsolutePath(ActionEvent e) {
    }

    @FXML
    public void handleMenuPaste(ActionEvent e) {
    }
}
