package Controllers;

import Model.Main;
import Model.Messenger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class GetBotInfoController implements Initializable {
    @FXML
    private Label displayPathLbl;
    @FXML
    private TextField inputNameTF;
    @FXML
    private TextField inputTokenTF;
    @FXML
    private TextField inputPrefixTF;
    @FXML
    private Button finishBtn;

    Preferences pref = Preferences.userNodeForPackage(getClass());
    final String INIT_PATH_KEY = "initPath";
    private final String DEFAULT_PATH = pref.get(INIT_PATH_KEY, null);
    private static String botFolderPath = null;

    public static String getBotFolderPath() {return botFolderPath;}


    //Method to check if TextFields are empty so i dont repeat code
    private void isTFEmpty() {
        if(!inputNameTF.getText().isEmpty() && !inputTokenTF.getText().isEmpty() && !inputPrefixTF.getText().isEmpty()) finishBtn.setDisable(false);
        else finishBtn.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayPathLbl.setText("Bot Project Path: " + DEFAULT_PATH + "\\");


        inputNameTF.setOnKeyTyped(e -> {
            isTFEmpty();

            displayPathLbl.setText("Bot Project Path: " + DEFAULT_PATH + "\\" + inputNameTF.getText());
        });

        inputTokenTF.setOnKeyTyped(e -> {
            isTFEmpty();
        });

        inputPrefixTF.setOnKeyTyped(e -> {
            isTFEmpty();
        });
    }


    @FXML
    public void handleFinishBtn(ActionEvent e) throws IOException {
        boolean error = false;
        boolean safeDelete = true;
        botFolderPath = DEFAULT_PATH + "\\" + inputNameTF.getText();

        File botFolder = new File(botFolderPath);
        if(botFolder.exists()) {
            Main.alert(Alert.AlertType.ERROR, "Folder already exists!");
            return;
        }


        boolean result = botFolder.mkdir();
        if(!result) {
            Main.alert(Alert.AlertType.ERROR, "Could not make project folder");
            return;
        }


        //ENV FILE CREATION
        File envFile = new File(botFolderPath + "\\.env");
        result = envFile.createNewFile();
        if(result) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(envFile));
            writer.write("DISCORD_TOKEN= \"" + inputTokenTF.getText() + "\"");
            writer.close();
        }
        else {
            Main.alert(Alert.AlertType.ERROR, "Could not make .env file", true);
            error = true;
        }


        //MAIN.PY FILE CREATION
        File mainPyFile = new File(botFolderPath + "\\" + "main.py");
        result = mainPyFile.createNewFile();
        if(!result) {
            Main.alert(Alert.AlertType.ERROR, "Could not make main.py file", true);
            error = true;
        }

        BufferedWriter mainPyWriter = new BufferedWriter(new FileWriter(mainPyFile));
        BufferedReader mainPyReader1 = new BufferedReader(new FileReader("resources/templates/templateMain1.txt"));
        BufferedReader mainPyReader2 = new BufferedReader(new FileReader("resources/templates/templateMain2.txt"));

        String line = mainPyReader1.readLine();
        StringBuilder content = new StringBuilder();
        while(line != null) {
            content.append(line).append("\n");
            line = mainPyReader1.readLine();
        }

        line = mainPyReader2.readLine();
        while(line != null) {
            content.append(line).append("\n");
            line = mainPyReader2.readLine();
        }

        String formattedContent = String.format(content.toString(), inputPrefixTF.getText());
        mainPyWriter.write(formattedContent);
        mainPyWriter.close();
        mainPyReader1.close();
        mainPyReader2.close();


        //Commands Directory Creation
        File commandsFolder = new File(botFolderPath + "\\commands");
        result = commandsFolder.mkdir();
        if(!result) {
            Main.alert(Alert.AlertType.ERROR, "Could not make commands directory", true);
            error = true;
        }


        //requirements.txt file creation
        File requirementsFile = new File(botFolderPath + "\\requirements.txt");

        BufferedWriter requirementsWriter = new BufferedWriter(new FileWriter(requirementsFile));
        BufferedReader requirementsReader = new BufferedReader(new FileReader("resources/templates/templateRequirement.txt"));

        line = requirementsReader.readLine();
        content = new StringBuilder();
        while(line != null) {
            content.append(line).append("\n");
            line = requirementsReader.readLine();
        }

        requirementsWriter.write(content.toString());
        requirementsWriter.close();
        requirementsReader.close();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("python -m pip install -r \"" + requirementsFile.getAbsolutePath() + "\"");


        //Switch to application.fxml
        if(error) {
            if(safeDelete) botFolder.delete();
        }
        else {
            Main.changeScene("/View/application.fxml");
        }
    }


    @FXML
    public void handlePreviousBtn(ActionEvent e) throws IOException {
        Main.changeScene("/View/Initialize.fxml");
    }
}
