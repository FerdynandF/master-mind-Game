package main.java.com.ferdynand;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("views/mainWindow.fxml"));

    @Override
    public void start(Stage primaryStage){
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Mastermind Game");
            primaryStage.setScene(new Scene(root, 300, 275));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
