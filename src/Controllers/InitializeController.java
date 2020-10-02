package Controllers;

import Model.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

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


    private final Font TITLEFONT = Font.loadFont("file:resources/fonts/PatrickHand-Regular.ttf", 30);
    private final String INITIALIZEPATH = "DPH/initialize.DPH";
    private File lastFolderOpened = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File dph = new File("DPH");
        if(!dph.exists()) {
            dph.mkdir();
            File initializeDph = new File("DPH/initialize.DPH");
            try {
                initializeDph.createNewFile();
            } catch (IOException e) {
                Main.alert(AlertType.ERROR, "Could not initialize files");
            }
        }
        title1.setFont(TITLEFONT);
        title2.setFont(TITLEFONT);
        pythonLogo.setImage(new Image(new File("resources/1200px-Python-logo-notext.svg.png").toURI().toString()));

        interactiveBtn(newBtn);
        interactiveBtn(openBtn);
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

    //Method to get tokens easier
    private String[] getTokens(String filePath, String regex) {
        ArrayList<String> tokens = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = "dummy";
            line = reader.readLine();
            if (line != null) Collections.addAll(tokens, line.split(regex));
            reader.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return tokens.toArray(new String[]{});
    }

    public void editTokens(String filePath, String[] editedTokens) {
        if(editedTokens.length != 0) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

                StringBuilder writeLine = new StringBuilder("");
                writeLine.append(editedTokens[0]);
                for(int i = 1; i < editedTokens.length; i++) {
                    writeLine.append(",").append(editedTokens[i]);
                }

                writer.write(writeLine.toString());
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getDefaultPath() {
        return getTokens(INITIALIZEPATH, "[,]")[0];
    }

    public void handleNewBtn(ActionEvent e) throws IOException {
        String[] tokens = getTokens(INITIALIZEPATH, "[,]");
        ArrayList<String> editedTokens = new ArrayList<>();

        if(tokens.length == 0) {
            Main.alert(AlertType.INFORMATION, "Default Directory has not been initialized yet, please select a default directory", true);

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose Default Directory");
            if(lastFolderOpened != null) directoryChooser.setInitialDirectory(lastFolderOpened);

            File selectedFolder = directoryChooser.showDialog(null);
            if(selectedFolder != null) {
                lastFolderOpened = selectedFolder;
                editedTokens.add(selectedFolder.getAbsolutePath());
                editTokens(INITIALIZEPATH, editedTokens.toArray(new String[]{}));
            }
        }
        else {
            Main.changeScene("/View/getBotInfo.fxml");
        }
}

    public void handleOpenBtn(ActionEvent e) {

    }
}
