package main.java.com.ferdynand.controllers;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.java.com.ferdynand.model.player.Player;

import java.io.IOException;

public class UserAndModeWindowController {
    @FXML
    public TextField userNameTextField;
    @FXML
    public Button startGameButton;
    @FXML
    public Label usernameErrorLabel;

    private Stage previousStage;

    @FXML
    public void initialize(){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userNameTextField.requestFocus();
            }
        });
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public boolean handleStartGameButton() {
        if (userNameTextField.getText().isEmpty()) {
            usernameErrorLabel.setVisible(true);
            return false;
        } else {

            openGameWindow();
            return true;
        }
    }

    private void openGameWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/mainWindow.fxml"));

            Pane gamePane = loader.load();
            MainWindowController controller = (MainWindowController) loader.getController();
            updatePlayer(controller.getPlayer());
            Stage stage = new Stage();
            stage.setTitle("Mastermind Game");
            Scene scene = new Scene(gamePane);
            stage.setScene(scene);
            controller.setGameStage(stage);
            previousStage.close();
            stage.setMaxHeight(500);
            stage.setMaxWidth(650);
            stage.setMinHeight(465);
            stage.setMinWidth(630);
            stage.setOnHidden(e -> {
                controller.shutdown();
                Platform.exit();
            });
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading game window. " + e.getMessage());
        }
    }

    private void updatePlayer(Player player) {
        player.setUsername(userNameTextField.getText());
    }

    public void quit() {
//        game.quit()
        System.exit(0);
    }

    public void keyPressedAction(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            handleStartGameButton();
        }
    }
}
