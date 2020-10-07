package Controllers;

import Model.Main;
import Model.Messenger;
import Model.SyntaxArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

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
    @FXML
    private VBox rootVBox;
    @FXML
    private HBox outputHBox;


    final String BOT_FOLDER_PATH = GetBotInfoController.getBotFolderPath();
    private File lastFolderOpened = null;
    private TreeItem<String> commandDir;
    private List<File> requiredFiles = Arrays.asList(Objects.requireNonNull(new File(BOT_FOLDER_PATH).listFiles()));


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

        //Setting up code area for main.py
        VBox mainPyVBox = new VBox();
        CodeArea mainPyCA = new SyntaxArea().getCodeArea(true);
        mainPyCA.setPrefHeight(commandsTabPane.getPrefHeight());

        String content = null;
        File mainPyFile = new File(BOT_FOLDER_PATH + "\\main.py");
        try {
            content = new Scanner(mainPyFile).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mainPyCA.appendText(content);

        Tooltip mainPyTooltip = new Tooltip(mainPyFile.getAbsolutePath());
        mainPyTooltip.setShowDelay(Duration.seconds(0.3));
        mainPyTab.setTooltip(mainPyTooltip);

        mainPyVBox.getChildren().add(mainPyCA);
        mainPyTab.setContent(mainPyVBox);

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
                contextMenu.getItems().add(newFile);

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


                displayFileStructure(childItem, childFile);
            }
            else if(childFile.isFile()) {
                childItem.getGraphic().addEventHandler(MouseEvent.MOUSE_CLICKED, me -> {
                    if(me.getButton().equals(MouseButton.PRIMARY)) {
                        Label childItemLabel = (Label) childItem.getGraphic();
                        if(childFile.getName().endsWith(".DPH") || childFile.getName().endsWith(".py")) addTab(childItemLabel.getText(), true, childFile);
                        else addTab(childItemLabel.getText(), false, childFile);
                    }
                });

                //Adding context menu
                MenuItem save = new MenuItem("Save");
                save.setOnAction(e -> {
                    for(Tab tab : commandsTabPane.getTabs().subList(0, commandsTabPane.getTabs().size() - 1)) {
                        if(tab.getTooltip().getText().equals(childFile.getAbsolutePath())) {
                            save(tab);
                        }
                    }
                });
                contextMenu.getItems().add(save);

                if(!requiredFiles.contains(childFile)) {
                    MenuItem delete = new MenuItem("Delete");
                    delete.setOnAction(e -> {

                    });
                }
            }

            childItem.getGraphic().setOnContextMenuRequested(e -> contextMenu.show(childItem.getGraphic(), e.getScreenX(), e.getScreenY()));
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

        childItem.getGraphic().addEventHandler(MouseEvent.MOUSE_CLICKED, me -> {
            if(me.getButton().equals(MouseButton.PRIMARY)) {
                Label childItemLabel = (Label) childItem.getGraphic();
                if(file.getName().endsWith(".DPH") || file.getName().endsWith(".py")) addTab(childItemLabel.getText(), true, file);
                else addTab(childItemLabel.getText(), false, file);
            }
        });

        root.getChildren().add(childItem);
    }

    private void addTab(String title, boolean setIndent, File file) {
        ArrayList<String> tabsTooltips = new ArrayList<>();
        for(int i = 0; i < commandsTabPane.getTabs().size() - 1; i++) {
            Tab commandsTab = commandsTabPane.getTabs().get(i);
            tabsTooltips.add(commandsTab.getTooltip().getText());
        }

        if(tabsTooltips.contains(file.getAbsolutePath())) {
            return;
        }

        Tab tab = new Tab();
        tab.setText(title);

        tab.setOnCloseRequest(e -> {
            try {
                String fileContent = new Scanner(file).useDelimiter("\\Z").next();
                String tabContent = getCodeArea(tab).getText();

                if(fileContent.equals(tabContent)) commandsTabPane.getTabs().remove(tab);
                else {
                    int index = commandsTabPane.getTabs().indexOf(tab);
                    ButtonType buttonType = confirmation("Do you want to save this file before closing?");

                    if(buttonType.equals(ButtonType.YES)) save(tab);
                    commandsTabPane.getTabs().remove(tab);

                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

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
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(content);
        out.close();

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

    //Method to get code area from tab
    private CodeArea getCodeArea(Tab tab) {
        VBox vBox = (VBox)tab.getContent();
        return (CodeArea) vBox.getChildren().get(0);
    }

    //Method that saves the entire project
    private void save() {
        for(Tab tab : commandsTabPane.getTabs()) {
            save(tab);
        }
    }
    //Method to save only 1 file
    private void save(Tab tab) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                File file = new File(tab.getTooltip().getText());
                CodeArea codeArea = getCodeArea(tab);

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(codeArea.getText());
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if(tab.getTooltip().getText().endsWith(".DPH")) {
                    String content = codeArea.getText();


                    File mainPyFile = new File(BOT_FOLDER_PATH + "\\main.py");
                    try {
                        String mainPyContent =  new Scanner(mainPyFile).useDelimiter("\\Z").next();
                        if(mainPyContent.contains("bot.run(TOKEN)")) {
                            int lastIndexBotRun = mainPyContent.lastIndexOf("bot.run(TOKEN)");

                            String newMainPyContent = mainPyContent.substring(0, lastIndexBotRun) + content + mainPyContent.substring(lastIndexBotRun);

                            BufferedWriter writer = new BufferedWriter(new FileWriter(mainPyFile));
                            writer.write(newMainPyContent);
                            writer.close();

                            for(Tab commandsTab : commandsTabPane.getTabs().subList(0, commandsTabPane.getTabs().size() - 1)) {
                                if(commandsTab.getTooltip().getText().equals(mainPyFile.getAbsolutePath())) {
                                    CodeArea mainPyCA = getCodeArea(commandsTab);
                                    mainPyCA.replaceText(newMainPyContent);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private ButtonType confirmation(String content) {
        Alert a = new Alert(AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = a.showAndWait();
        return result.get();
    }

    //Handling Menu Bar Item: File
    @FXML
    public void handleMenuNew(ActionEvent e) throws IOException {
        ButtonType buttonType = confirmation("Do you want to save this project before creating a new one?");
        if (buttonType.equals(ButtonType.YES)) save();

        Main.getScene().getWindow().setHeight(480);
        Main.getScene().getWindow().setWidth(640);
        Main.changeScene("/View/getBotInfo.fxml");
    }

    @FXML
    public void handleMenuOpen(ActionEvent e) {
    }

    @FXML
    public void handleMenuClose(ActionEvent e) throws IOException {
        ButtonType buttonType = confirmation("Do you want to save this project before closing?");
        if (buttonType.equals(ButtonType.YES)) save();

        Main.getScene().getWindow().setHeight(480);
        Main.getScene().getWindow().setWidth(640);
        Main.changeScene("/View/initialize.fxml");
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
        ButtonType buttonType = confirmation("Do you want to save this project before exiting?");
        if (buttonType.equals(ButtonType.YES)) save();
        Main.getStage().close();

    }

    @FXML
    public void handleMenuCopyAbsolutePath(ActionEvent e) {
        StringSelection selection = new StringSelection(BOT_FOLDER_PATH);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
