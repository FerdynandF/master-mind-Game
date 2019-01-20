package main.java.com.ferdynand.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.java.com.ferdynand.player.Player;

public class MainWindowController {
    private Stage GameStage;
    private Player player = new Player();

    private Circle[][] board;
    private Circle[][] hint;
    private Circle[] colors;
    private int gameRound;;
    private Circle currentCircleColorSelected;
    private Effect effect;
    private Paint currentColor;



    @FXML
    private Circle boardColor00, boardColor01, boardColor02, boardColor03,
                    boardColor10, boardColor11, boardColor12, boardColor13,
                    boardColor20, boardColor21, boardColor22, boardColor23,
                    boardColor30, boardColor31, boardColor32, boardColor33,
                    boardColor40, boardColor41, boardColor42, boardColor43,
                    boardColor50, boardColor51, boardColor52, boardColor53,
                    boardColor60, boardColor61, boardColor62, boardColor63,
                    boardColor70, boardColor71, boardColor72, boardColor73,
                    boardColor80, boardColor81, boardColor82, boardColor83,
                    boardColor90, boardColor91, boardColor92, boardColor93,
                    boardColor100, boardColor101, boardColor102, boardColor103;
    @FXML
    private Circle hint00, hint01, hint02, hint03,
                    hint10, hint11, hint12, hint13,
                    hint20, hint21, hint22, hint23,
                    hint30, hint31, hint32, hint33,
                    hint40, hint41, hint42, hint43,
                    hint50, hint51, hint52, hint53,
                    hint60, hint61, hint62, hint63,
                    hint70, hint71, hint72, hint73,
                    hint80, hint81, hint82, hint83,
                    hint90, hint91, hint92, hint93;
    @FXML
    private Circle color1, color2, color3, color4, color5, color6;
    @FXML
    private Button sendGuessButton;
    @FXML
    private TextArea chatArea;
    @FXML
    private ToggleButton readyButton;
    @FXML
    private Label resultLabel;
    @FXML
    private Label readyLabel;

    @FXML
    public void initialize() {
        initUI();

    }

    private void initUI() {
        board = new Circle[][]{
                {boardColor00, boardColor01, boardColor02, boardColor03},
                {boardColor10, boardColor11, boardColor12, boardColor13},
                {boardColor20, boardColor21, boardColor22, boardColor23},
                {boardColor30, boardColor31, boardColor32, boardColor33},
                {boardColor40, boardColor41, boardColor42, boardColor43},
                {boardColor50, boardColor51, boardColor52, boardColor53},
                {boardColor60, boardColor61, boardColor62, boardColor63},
                {boardColor70, boardColor71, boardColor72, boardColor73},
                {boardColor80, boardColor81, boardColor82, boardColor83},
                {boardColor90, boardColor91, boardColor92, boardColor93},
                {boardColor100, boardColor101, boardColor102, boardColor103}
        };

        hint = new Circle[][]{
                {hint00, hint01, hint02, hint03},
                {hint10, hint11, hint12, hint13},
                {hint20, hint21, hint22, hint23},
                {hint30, hint31, hint32, hint33},
                {hint40, hint41, hint42, hint43},
                {hint50, hint51, hint52, hint53},
                {hint60, hint61, hint62, hint63},
                {hint70, hint71, hint72, hint73},
                {hint80, hint81, hint82, hint83},
                {hint90, hint91, hint92, hint93}
        };

        colors = new Circle[] { color1, color2, color3, color4, color5, color6};
        gameRound = 0;
        sendGuessButton.setDisable(true);
        resultLabel.setVisible(false);
        currentCircleColorSelected = colors[5];
        effect = colors[5].getEffect();
        currentColor = colors[5].getFill();
    }


    public Player getPlayer() {
        return player;
    }

    public void setGameStage(Stage gameStage) {
        GameStage = gameStage;
    }


    public void setColor(MouseEvent mouseEvent) {
        Circle selectedCircle = (Circle)mouseEvent.getSource();
        selectedCircle.setFill(currentColor);

        if(allCircleFillInRound() && readyButton.isSelected())
            sendGuessButton.setDisable(false);
    }

    public void getColor(MouseEvent mouseEvent) {
        currentCircleColorSelected.setEffect(null);

        Circle circle = (Circle)mouseEvent.getSource();
        currentColor = circle.getFill();
        circle.setEffect(effect);
        currentCircleColorSelected = circle;
    }

    private boolean allCircleFillInRound(){
        boolean returnValue = true;
        for(int i = 0; i < 4; i++) {
            Circle currentCircle = board[gameRound][i];
            if (currentCircle.getFill().equals(Color.WHITE)){
                returnValue = false;
                System.out.println("Round not end. Fill other circles..");
                break;
            }
        }

        return returnValue;
    }

    public void sendGuess(MouseEvent mouseEvent) {

    }

    public void readyButtonAction(MouseEvent mouseEvent) {
        if(allCircleFillInRound() && readyButton.isSelected()){
            readyButton.setSelected(true);
            sendGuessButton.setDisable(false);
        }

    }
}
