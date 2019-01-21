package main.java.com.ferdynand.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.java.com.ferdynand.guess.Guess;
import main.java.com.ferdynand.packet.ClientPacket;
import main.java.com.ferdynand.packet.ServerPacket;
import main.java.com.ferdynand.player.Actions;
import main.java.com.ferdynand.player.Player;
import settings.SettingConnection;
import settings.SettingConnectionDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class MainWindowController {
    private Stage GameStage;
    private Player player = new Player();
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ClientPacket clientPacket;
    private ServerPacket serverPacket;
    private Guess guess = new Guess();
    private Socket socket;

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
    public TextField inputTextField;

    @FXML
    public void initialize() {
        Platform.runLater(() ->{
            try{
                SettingConnection settingConnection = SettingConnectionDao.getInstance().getSettingConnectionFromXMLFile("settingConnection.xml");
                socket = new Socket(settingConnection.getIp(), settingConnection.getPort());
                clientPacket = new ClientPacket(player);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                clientPacket.getChatMessage().setUsername(player.getUsername());
                clientPacket.getPlayer().setPrevAction(Actions.CONNECTED);
                objectOutputStream.writeObject(clientPacket);
                listenFromServer();
            } catch (IOException e) {
                System.out.println("Client Socket exception: " + e.getMessage());
            }
        });
        initUI();
        chatArea.setWrapText(true);
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

    private void listenFromServer() {
        new Thread(() -> {
            try{
                while(true){
                    serverPacket = (ServerPacket) objectInputStream.readObject();
                    switch (serverPacket.getAction()) {
                        case (Actions.CHAT):
                            Platform.runLater(() -> chatArea.appendText(serverPacket.getChatMessage().toString() + "\n"));
                            break;
                        case (Actions.GAME_BEGIN):
                            sendGuessButton.setVisible(true);
                            break;
                        case (Actions.GUESS):

                            break;

                    }
                }
            } catch (SocketException e) {
                System.out.println("Exception while listening from server: " + e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendGuess(MouseEvent mouseEvent) {
        sendGuessButton.setDisable(true);
        int[] guess = getUserGuess();

//        try{
//            int[] serverHints =
//        }
    }

    private int[] getUserGuess() {
        int[] guess = new int[4];
        for (int i = 0; i < guess.length; i++) {
            Circle currentCircle = board[gameRound][i];
            currentCircle.setDisable(true);
            guess[i] = getColorId(currentCircle);
        }
        return guess;
    }

    private int getColorId(Circle circle) {
        int id = 0;

        Color color = (Color)circle.getFill();
        if(color.equals((Color)color1.getFill()))
            id = 1;
        else if(color.equals((Color)color2.getFill()))
            id = 2;
        else if (color.equals((Color)color3.getFill()))
            id = 3;
        else if (color.equals((Color)color4.getFill()))
            id = 4;
        else if (color.equals((Color)color5.getFill()))
            id = 5;
        else if (color.equals((Color)color6.getFill()))
            id = 6;

        return id;
    }

    private Color getColorFromId(int id) {
        Color color = Color.WHITE;

        switch (id) {
            case 1:
                color = (Color)color1.getFill();
                break;
            case 2:
                color = (Color)color2.getFill();
                break;
            case 3:
                color = (Color)color3.getFill();
                break;
            case 4:
                color = (Color)color4.getFill();
                break;
            case 5:
                color = (Color)color5.getFill();
                break;
            case 6:
                color = (Color)color6.getFill();
                break;
        }

        return color;
    }

    public void readyButtonAction(MouseEvent mouseEvent) {
        if(allCircleFillInRound() && readyButton.isSelected()){
            readyButton.setSelected(true);
            readyButton.setDisable(true);
            sendGuessButton.setDisable(false);
            readyLabel.setText("YOU ARE READY!");
        } else {
            readyButton.setSelected(true);
            readyButton.setDisable(true);
            readyLabel.setText("YOU ARE READY!");
        }

    }

    public void enterButtonTextFieldAction(KeyEvent keyEvent) throws IOException{
        try{
            objectOutputStream.reset();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if(!inputTextField.getText().isEmpty() && keyEvent.getCode().equals(KeyCode.ENTER)) {
            clientPacket.getChatMessage().setMessage(inputTextField.getText());
            clientPacket.getPlayer().setPrevAction(Actions.CHAT);
            objectOutputStream.writeObject(clientPacket);
            inputTextField.clear();
        }

    }

    public void sendButtonAction(ActionEvent actionEvent) throws IOException{
        objectOutputStream.reset();
        if(!inputTextField.getText().isEmpty()){
            clientPacket.getChatMessage().setMessage(inputTextField.getText());
            clientPacket.getPlayer().setPrevAction(Actions.CHAT);
            objectOutputStream.writeObject(clientPacket);
            inputTextField.clear();
        }
    }
}
