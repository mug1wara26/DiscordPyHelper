package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception{
        Main.primaryStage = stage;
        primaryStage.setTitle("DiscordPyHelper");

        Parent root = FXMLLoader.load(getClass().getResource("/View/application.fxml"));
        scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {return primaryStage;}

    public static void changeScene(String fxml) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource(fxml));

        primaryStage.getScene().setRoot(root);
    }

    //Methods to make alerts easier
    public static void alert(Alert.AlertType at, String content) {
        Alert a = new Alert(at);
        a.setContentText(content);
        a.show();
    }


    public static void alert(Alert.AlertType at, String content, boolean showAndWait) {
        Alert a = new Alert(at);
        a.setContentText(content);
        if(showAndWait) a.showAndWait();
        else a.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
