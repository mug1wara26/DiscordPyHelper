package Controllers;

import Model.Main;
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

public class GetBotInfoController implements Initializable {
    @FXML
    private Label displayPathLbl;
    @FXML
    private TextField inputNameTF;
    @FXML
    private TextField inputTokenTF;
    @FXML
    private Button finishBtn;

    private final String DEFAULTPATH = new InitializeController().getDefaultPath();
    private String botFolderPath = null;

    public String getBotFolderPath() {return botFolderPath;}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayPathLbl.setText("Bot Project Path: " + DEFAULTPATH + "\\");
        inputNameTF.setOnKeyTyped(e -> {
            if(!inputNameTF.getText().isEmpty() && !inputTokenTF.getText().isEmpty()) finishBtn.setDisable(false);
            else finishBtn.setDisable(true);

            displayPathLbl.setText("Bot Project Path: " + DEFAULTPATH + "\\" + inputNameTF.getText());
        });

        inputTokenTF.setOnKeyTyped(e -> {
            if(!inputNameTF.getText().isEmpty() && !inputTokenTF.getText().isEmpty()) finishBtn.setDisable(false);
            else finishBtn.setDisable(true);
        });
    }


    @FXML
    public void handleFinishBtn(ActionEvent e) throws IOException {
        boolean error = false;
        boolean safeDelete = true;
        botFolderPath = DEFAULTPATH + "\\" + inputNameTF.getText();

        File botFolder = new File(botFolderPath);
        boolean result = botFolder.mkdir();
        if(result) {
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
                if(botFolder.exists()) safeDelete = false;
            }
        }
        else {
            Main.alert(Alert.AlertType.ERROR, "Could not make directory", true);
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

        mainPyWriter.write(content.toString());
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
            Main.getStage().setMaximized(true);
        }
    }


    @FXML
    public void handlePreviousBtn(ActionEvent e) throws IOException {
        Main.changeScene("/View/Initialize.fxml");
    }
}
