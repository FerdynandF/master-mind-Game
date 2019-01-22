package main.java.com.ferdynand.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.com.ferdynand.controllers.UserAndModeWindowController;

public class Main extends Application {
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UserAndModeWindow.fxml"));

    @Override
    public void start(Stage primaryStage){
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Mastermind Game Intro");
            UserAndModeWindowController controller = loader.getController();
            primaryStage.setScene(new Scene(root, 350, 220));
            primaryStage.setMinWidth(330);
            primaryStage.setMinHeight(200);
            primaryStage.setMaxWidth(360);
            primaryStage.setMaxHeight(300);
            controller.setPreviousStage(primaryStage);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Error displaying user and mode input layout: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void stop(){
        UserAndModeWindowController controller = loader.getController();

        if(controller != null){
            controller.quit();
        }
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
