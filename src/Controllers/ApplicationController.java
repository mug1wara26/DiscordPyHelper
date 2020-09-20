package Controllers;

import Model.Main;
import Model.PythonSyntaxArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {
    @FXML
    private VBox codeAreaVBox;
    @FXML
    private TabPane commandsTab;


    private File lastFolderOpened = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Adding Code Area
        PythonSyntaxArea pythonSyntaxArea = new PythonSyntaxArea();
        CodeArea codeArea = pythonSyntaxArea.getCodeArea();
        codeArea.setPrefHeight(500);

        codeAreaVBox.getChildren().add(codeArea);


        //Add Button to Tab Pane
        Button addBtn = new Button();
        Tab btnTab = new Tab("+");
        btnTab.setGraphic(addBtn);
        addBtn.setOnAction(e -> {
            handleAddBtn();
        });

    }

    private void handleAddBtn() {

    }


    //Method that saves the entire project
    private void save() {
        save(new GetBotInfoController().getBotFolderPath());
    }

    private void save(String folderPath) {}

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
