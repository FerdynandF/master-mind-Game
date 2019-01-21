package main.java.com.ferdynand;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.com.ferdynand.controllers.UserAndModeWindowController;

public class Main extends Application {
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("views/UserAndModeWindow.fxml"));

    @Override
    public void start(Stage primaryStage){
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Mastermind Game Intro");
            UserAndModeWindowController controller = loader.getController();
            primaryStage.setScene(new Scene(root, 450, 340));
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(250);
            primaryStage.setMaxWidth(640);
            primaryStage.setMaxHeight(490);
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
