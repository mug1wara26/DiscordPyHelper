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


    String BOT_FOLDER_PATH = null;
    private TreeItem<String> commandDir;
    private List<File> requiredFiles = null;

    public void setBOT_FOLDER_PATH(String bot_folder_path) {
        BOT_FOLDER_PATH = bot_folder_path;
        requiredFiles = Arrays.asList(Objects.requireNonNull(new File(BOT_FOLDER_PATH).listFiles()));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Messenger.setApplicationController(this);
        Main.getStage().setMaximized(true);

        //Set commandsTabPane in messenger class
        commandsTabPane.setPrefHeight(Main.getStage().getHeight() * 0.6);

        //Display file structure
        File projectFile = new File(BOT_FOLDER_PATH);
        TreeItem<String> rootItem = new TreeItem<>("");

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
        } catch (NoSuchElementException e) {
            content = "";
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

    public void displayFileStructure(TreeItem<String> root, File file) {
        root.setGraphic(new Label(file.getName()));

        ContextMenu contextMenu = new ContextMenu();
        MenuItem newFile = new MenuItem("New File");
        newFile.setOnAction(e -> {
            try {
                addFile(root, file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        contextMenu.getItems().add(newFile);
        root.getGraphic().setOnContextMenuRequested(e -> contextMenu.show(root.getGraphic(), e.getScreenX(), e.getScreenY()));
        fileTreeView.setRoot(root);
        for(File childFile : Objects.requireNonNull(file.listFiles())) {
            TreeItem<String> childItem = addChildItem(root, childFile);
            if(childFile.isDirectory()) displayFileStructure(childItem, childFile);
        }
    }

    public TreeItem<String> addChildItem(TreeItem<String> root, File childFile) {
        TreeItem<String> childItem = new TreeItem<>("");
        if(childFile.getName().endsWith(".DPH")) childItem.setGraphic(new Label(childFile.getName().substring(0, childFile.getName().length() - 4)));
        else childItem.setGraphic(new Label(childFile.getName()));

        ContextMenu contextMenu = new ContextMenu();

        //Check if child file is a directory
        if(childFile.isDirectory()) {
            Menu newFile = new Menu("+New");
            MenuItem addFile = new MenuItem("New File");

            addFile.setOnAction(e -> {
                try {
                    addFile(childItem, childFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
        return childItem;
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
            String fileContent = null;
            try {
                fileContent = new Scanner(file).useDelimiter("\\Z").next();
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (NoSuchElementException ex) {
                fileContent = "";
            }

            String tabContent = getCodeArea(tab).getText();

            if(fileContent.equals(tabContent)) commandsTabPane.getTabs().remove(tab);
            else {
                int index = commandsTabPane.getTabs().indexOf(tab);
                ButtonType buttonType = confirmation("Do you want to save this file before closing?");

                if(buttonType.equals(ButtonType.YES)) save(tab);
                commandsTabPane.getTabs().remove(tab);

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
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (NoSuchElementException e) {
            content = "";
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
                    String mainPyContent = null;
                    try {
                        mainPyContent =  new Scanner(mainPyFile).useDelimiter("\\Z").next();
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (NoSuchElementException e) {
                        mainPyContent = "";
                    }

                    assert mainPyContent != null;
                    if(mainPyContent.contains("bot.run(TOKEN)")) {
                        int lastIndexBotRun = mainPyContent.lastIndexOf("bot.run(TOKEN)");

                        String newMainPyContent = mainPyContent.substring(0, lastIndexBotRun) + content + mainPyContent.substring(lastIndexBotRun);

                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(mainPyFile));
                            writer.write(newMainPyContent);
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        for(Tab commandsTab : commandsTabPane.getTabs().subList(0, commandsTabPane.getTabs().size() - 1)) {
                            if(commandsTab.getTooltip().getText().equals(mainPyFile.getAbsolutePath())) {
                                CodeArea mainPyCA = getCodeArea(commandsTab);
                                mainPyCA.replaceText(newMainPyContent);
                            }
                        }
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

    @FXML
    public void handleRunBtn(ActionEvent e) {
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd " + BOT_FOLDER_PATH + "&& python main.py" + "\"");
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void addFile(TreeItem<String> rootTreeItem, File file) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/addFile.fxml"));

        AddFileController.setFile(file);
        Messenger.setRoot(rootTreeItem);
        Stage stage = new Stage();
        stage.setTitle("Create File");
        stage.setScene(new Scene(root, 320, 240));
        stage.showAndWait();
    }
}
