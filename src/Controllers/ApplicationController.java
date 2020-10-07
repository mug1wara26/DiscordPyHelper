package Controllers;

import Model.Main;
import Model.Messenger;
import Model.SyntaxArea;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ApplicationController implements Initializable {
    @FXML
    private TabPane commandsTabPane;
    @FXML
    private Tab addBtnTab;
    @FXML
    private Button addBtn;
    @FXML
    private Tab mainPyTab;
    @FXML
    private TreeView<String> fileTreeView;


    final String BOT_FOLDER_PATH = GetBotInfoController.getBotFolderPath();
    final String COMMANDS_KEY = "commands";
    private File lastFolderOpened = null;
    private TreeItem<String> commandDir;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main.getStage().setMaximized(true);

        //Set commandsTabPane in messenger class
        commandsTabPane.setPrefHeight(Main.getStage().getHeight() * 0.6);
        Messenger.setApplicationController(this);

        //Display file structure
        File projectFile = new File(BOT_FOLDER_PATH);
        TreeItem<String> rootItem = new TreeItem<>(projectFile.getName());
        fileTreeView.setRoot(rootItem);

        displayFileStructure(rootItem, new File(BOT_FOLDER_PATH));

        addTab("main.py", true, new File(BOT_FOLDER_PATH + "\\main.py"));

        //Add tooltip to button that add commands
        Tooltip tooltip = new Tooltip("Add new command");
        tooltip.setShowDelay(Duration.seconds(0.3));
        addBtn.setTooltip(tooltip);

        addBtnTab.setDisable(true);
    }

    private void displayFileStructure(TreeItem<String> root, File file) {
        for(File childFile : Objects.requireNonNull(file.listFiles())) {
            TreeItem<String> childItem = new TreeItem<>("");
            if(childFile.getName().endsWith(".DPH")) childItem.setGraphic(new Label(childFile.getName().substring(0, childFile.getName().length() - 4)));
            else childItem.setGraphic(new Label(childFile.getName()));

            ContextMenu contextMenu = new ContextMenu();

            //Check if child file is a directory
            if(childFile.isDirectory()) {
                Menu newFile = new Menu("+New");
                MenuItem addFile = new MenuItem("New File");

                addFile.setOnAction(e -> {
                    // TODO: 10/7/2020  Create FXML file for new file
                });
                newFile.getItems().add(addFile);

                //Check is child file is the commands folder, if it is add a new command menu item
                if(childFile.getAbsolutePath().equals(GetBotInfoController.getBotFolderPath() + "\\commands")) {
                    commandDir = childItem;
                    MenuItem addCommand = new MenuItem("New Command");

                    addCommand.setOnAction(e -> {
                        try {
                            Parent p = FXMLLoader.load(getClass().getResource("/View/addCommand.fxml"));

                            Stage stage = new Stage();
                            stage.setTitle("Create Command");
                            stage.setScene(new Scene(p, 640, 480));
                            stage.show();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    newFile.getItems().add(addCommand);
                }

                contextMenu.getItems().add(newFile);
                childItem.getGraphic().setOnContextMenuRequested(e -> contextMenu.show(childItem.getGraphic(), e.getScreenX(), e.getScreenY()));

                displayFileStructure(childItem, childFile);
            }
            else if(childFile.isFile()) {
                childItem.getGraphic().addEventHandler(MouseEvent.MOUSE_CLICKED, me -> {
                    Label childItemLabel = (Label) childItem.getGraphic();
                    addTab(childItemLabel.getText() , true, childFile);
                });
            }
            root.getChildren().add(childItem);
        }
    }

    private void addChildItem(TreeItem<String> root, File file) {
        TreeItem<String> childItem = new TreeItem<>("");
        if(file.getName().endsWith(".DPH")) childItem.setGraphic(new Label(file.getName().substring(0, file.getName().length() - 4)));
        else childItem.setGraphic(new Label(file.getName()));

        ContextMenu contextMenu = new ContextMenu();
        // TODO: 10/7/2020 Add menu items to context menu

        childItem.getGraphic().setOnContextMenuRequested(e -> contextMenu.show(childItem.getGraphic(), e.getScreenX(), e.getScreenY()));

        if(root.equals(commandDir)) {
            childItem.getGraphic().addEventHandler(MouseEvent.MOUSE_CLICKED, me -> {
                Label childItemLabel = (Label) childItem.getGraphic();
                addTab(childItemLabel.getText(), true, file);
            });
        }

        root.getChildren().add(childItem);
    }

    private void addTab(String title, boolean setIndent, File file) {
        ArrayList<String> tabsTooltips = new ArrayList<>();
        for(int i = 0; i < commandsTabPane.getTabs().size() - 1; i++) {
            Tab commandsTab = commandsTabPane.getTabs().get(i);
            System.out.println(commandsTab.getText() + "," + commandsTab.getTooltip());
            tabsTooltips.add(commandsTab.getTooltip().getText());
        }

        if(tabsTooltips.contains(file.getAbsolutePath())) {
            return;
        }

        Tab tab = new Tab();
        tab.setText(title);

        Tooltip tooltip = new Tooltip(file.getAbsolutePath());
        tooltip.setShowDelay(Duration.seconds(0.3));
        tab.setTooltip(tooltip);

        VBox vBox = new VBox();
        CodeArea codeArea = new SyntaxArea().getCodeArea(setIndent);
        codeArea.setPrefHeight(commandsTabPane.getPrefHeight());
        vBox.getChildren().add(codeArea);


        String content = null;
        try {
            content = new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tab.setContent(vBox);
        codeArea.appendText(content);

        commandsTabPane.getTabs().add(commandsTabPane.getTabs().size() - 1, tab);
        commandsTabPane.getSelectionModel().select(tab);
    }

    public void addCommand(String name, String content) throws IOException {
        String commandPath = GetBotInfoController.getBotFolderPath() + "\\commands";

        File file = new File(commandPath + "\\" + name + ".DPH");
        if(file.exists()) {
            Main.alert(AlertType.ERROR, "File already exists!");
            return;
        }

        file.createNewFile();

        addTab(name, true, file);
        addChildItem(commandDir, file);
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private boolean saveConfirmation() {
        Alert a = new Alert(AlertType.CONFIRMATION, "Do you want to save your project?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = a.showAndWait();
        if(result.get() == ButtonType.YES) save();
        return result.get() == ButtonType.NO;
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
