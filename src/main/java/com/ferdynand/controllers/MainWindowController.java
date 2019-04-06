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
import main.java.com.ferdynand.model.guess.Guess;
import main.java.com.ferdynand.model.packet.ClientPacket;
import main.java.com.ferdynand.model.packet.ServerPacket;
import main.java.com.ferdynand.model.player.Actions;
import main.java.com.ferdynand.model.player.Player;

import main.java.com.ferdynand.model.settings.SettingConnection;
import main.java.com.ferdynand.model.settings.SettingConnectionDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author ferdynandf (https://github.com/FerdynandF)
 */
public class MainWindowController {

    private Stage GameStage;
    private Player player = new Player();
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ClientPacket clientPacket;
    private ServerPacket serverPacket;
    private Guess guess = new Guess();
    private boolean readyFlag = false;
    private Socket socket;

    private Circle[][] board;
    private Circle[][] hint;
    private Circle[] colors;
    private int gameRound;
    private Circle currentCircleColorSelected;
    private Effect effect;
    private Paint currentColor;
    private Paint hiddenColor;
    private Paint whiteColor;

    @FXML
    private Circle boardColor00, boardColor01, boardColor02, boardColor03, boardColor10, boardColor11, boardColor12, boardColor13, boardColor20, boardColor21, boardColor22, boardColor23, boardColor30, boardColor31, boardColor32, boardColor33, boardColor40, boardColor41, boardColor42, boardColor43, boardColor50, boardColor51, boardColor52, boardColor53, boardColor60, boardColor61, boardColor62, boardColor63, boardColor70, boardColor71, boardColor72, boardColor73, boardColor80, boardColor81, boardColor82, boardColor83, boardColor90, boardColor91, boardColor92, boardColor93, boardColor100, boardColor101, boardColor102, boardColor103;
    @FXML
    private Circle hint00, hint01, hint02, hint03, hint10, hint11, hint12, hint13, hint20, hint21, hint22, hint23, hint30, hint31, hint32, hint33, hint40, hint41, hint42, hint43, hint50, hint51, hint52, hint53, hint60, hint61, hint62, hint63, hint70, hint71, hint72, hint73, hint80, hint81, hint82, hint83, hint90, hint91, hint92, hint93;
    @FXML
    private Circle color1, color2, color3, color4, color5, color6;
    @FXML
    private Button sendGuessButton;
    @FXML
    private TextArea chatArea;
    @FXML
    private ToggleButton readyButton;
    @FXML
    public Button newGameButton;
    @FXML
    private Label loseLabel;
    @FXML
    public Label winLabel;
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
        board = new Circle[][]{{boardColor00, boardColor01, boardColor02, boardColor03},
                {boardColor10, boardColor11, boardColor12, boardColor13}, {boardColor20, boardColor21, boardColor22, boardColor23},
                {boardColor30, boardColor31, boardColor32, boardColor33}, {boardColor40, boardColor41, boardColor42, boardColor43},
                {boardColor50, boardColor51, boardColor52, boardColor53}, {boardColor60, boardColor61, boardColor62, boardColor63},
                {boardColor70, boardColor71, boardColor72, boardColor73}, {boardColor80, boardColor81, boardColor82, boardColor83},
                {boardColor90, boardColor91, boardColor92, boardColor93}, {boardColor100, boardColor101, boardColor102, boardColor103}};

        hint = new Circle[][]{{hint00, hint01, hint02, hint03}, {hint10, hint11, hint12, hint13}, {hint20, hint21, hint22, hint23},
                {hint30, hint31, hint32, hint33}, {hint40, hint41, hint42, hint43}, {hint50, hint51, hint52, hint53},
                {hint60, hint61, hint62, hint63}, {hint70, hint71, hint72, hint73}, {hint80, hint81, hint82, hint83},
                {hint90, hint91, hint92, hint93}};

        setOpacityInHints();
        colors = new Circle[] { color1, color2, color3, color4, color5, color6};
        gameRound = 0;
        sendGuessButton.setDisable(true);
        newGameButton.setDisable(true);
        newGameButton.setVisible(false);
        loseLabel.setVisible(false);
        winLabel.setVisible(false);
        currentCircleColorSelected = colors[5];
        effect = colors[5].getEffect();
        currentColor = colors[5].getFill();
        hiddenColor = boardColor100.getFill();
        whiteColor = boardColor23.getFill();
    }

    private void setOpacityInHints() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                hint[i][j].setOpacity(0.7);
            }
        }
    }

    private void clearBoard() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 4; j++) {
                if (i < 10){
                    board[i][j].setFill(whiteColor);
                    board[i][j].setDisable(true);
                    hint[i][j].setFill(whiteColor);
                    hint[i][j].setDisable(true);

                } else {
                    board[i][j].setFill(hiddenColor);
                    board[i][j].setDisable(true);
                }
            }
        }
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

        if(isAllCircleFillInRound() && readyButton.isSelected())
            sendGuessButton.setDisable(false);
    }

    public void getColor(MouseEvent mouseEvent) {
        currentCircleColorSelected.setEffect(null);
        Circle circle = (Circle)mouseEvent.getSource();
        currentColor = circle.getFill();
        circle.setEffect(effect);
        currentCircleColorSelected = circle;
    }

    private boolean isAllCircleFillInRound(){
        boolean returnValue = true;
        for(int i = 0; i < 4; i++) {
            Circle currentCircle = boardColor00;
            if (player.isCodeBreaker())
                currentCircle = board[gameRound][i];
            if (player.isCodeMaker())
                currentCircle = board[10][i];

            if (!readyFlag || currentCircle.getFill().equals(whiteColor) || currentCircle.getFill().equals(hiddenColor)){
                returnValue = false;
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
                        case (Actions.READY_FLAG):
                            readyFlag = true;
                            break;
                        case (Actions.MAKER):
                            player.setCodeMaker(true);
                            player.setCodeBreaker(false);
                            System.out.println(player.toString());
                            updateUIToMode();
                            break;
                        case (Actions.BREAKER):
                            player.setCodeMaker(false);
                            player.setCodeBreaker(true);
                            System.out.println(player.toString());
                            updateUIToMode();
                            break;
                        case (Actions.YOUCANSTART):
                            enableInputToCodeMaker();
                            break;
                        case (Actions.BEGIN_MATCH):
                            enableInputToCodeBreaker();
                            break;
                        case (Actions.SET_GUESS_COLOR):
                            colorCirclesInMakerView(serverPacket.getGuessOrHint().byteToIntArray());
                            break;
                        case (Actions.SET_HINT_COLOR): // increment gameRound
                            colorHints(serverPacket.getGuessOrHint().byteToIntArray());
                            break;
                        case (Actions.GAME_WON):
                            displayWin();
                            break;
                        case (Actions.GAME_LOST):
                            displayLose();
                            break;
                        case (Actions.NEW_GAME):
                            setUpNewGame();
                    }
                }
            } catch (SocketException e) {
                System.out.println("Exception while listening from server: " + e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setUpNewGame() {
        clearBoard();
        setOpacityInHints();
        readyLabel.setVisible(false);
        readyButton.setVisible(false);
        readyButton.setDisable(true);
        newGameButton.setDisable(true);
        loseLabel.setVisible(false);
        winLabel.setVisible(false);
        try {
            updateUIToMode();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        enableInputToCodeBreaker();
        enableInputToCodeMaker();
        gameRound = 0;

    }

    private void displayLose() {
        loseLabel.setVisible(true);
        disableCurrentRound();
        readyLabel.setVisible(false);
        readyButton.setDisable(true);
        readyButton.setVisible(false);
        newGameButton.setVisible(true);
        newGameButton.setDisable(false);
    }

    private void displayWin() {
        winLabel.setVisible(true);
        disableCurrentRound();
        readyLabel.setVisible(false);
        readyButton.setDisable(true);
        readyButton.setVisible(false);
        newGameButton.setVisible(true);
        newGameButton.setDisable(false);
    }

    private void disableCurrentRound(){
        for (int i = 0; i < 4; i++) {
            board[gameRound][i].setDisable(true);
        }
    }

    private void colorHints(int[] hints) {
        for (int i = 0; i < 4; i++) {
            displayHint(hints[i], i);

            if(gameRound < 9) {
                Circle currentCircle = board[gameRound+1][i];
                if(player.isCodeBreaker())
                    currentCircle.setDisable(false);
            }
        }
        gameRound++;
    }

    private void colorCirclesInMakerView(int[] colors) {
        for (int i = 0; i < 4; i++) {
            Circle currentCircle = board[gameRound][i];
            currentCircle.setFill(getColorFromId(colors[i]));
        }
    }

    private void enableInputToCodeBreaker() {
        if (player.isCodeBreaker()) {
            for (int i = 0; i < 4; i++) {
                board[gameRound][i].setDisable(false);
            }
        }
    }

    private void enableInputToCodeMaker() {
        if(player.isCodeMaker()) {
            for (int i = 0; i < 4; i++) {
                board[10][i].setDisable(false);
            }
        }
    }

    private void displayHint(int currentHint, int index) {
        if (currentHint == 12) {
            hint[gameRound][index].setFill(Color.BLACK);
            hint[gameRound][index].setOpacity(1);
        } else if (currentHint == 13) {
            hint[gameRound][index].setFill(Color.WHITE);
            hint[gameRound][index].setOpacity(1);
        }
    }

    private void updateUIToMode() throws IOException {
        clearBoard();
        if(player.isCodeMaker()){
            for (int i = 0; i < board[0].length; i++) {
                board[0][i].setDisable(true);
                board[10][i].setDisable(false);
            }
        } else if (player.isCodeBreaker()){
            for (int i = 0; i < board[0].length; i++) {
                board[0][i].setDisable(true);
                board[10][i].setDisable(true);
            }
        } else {
            throw new IOException();
        }
    }

    public void sendGuessAction(MouseEvent mouseEvent) {
        sendGuessButton.setDisable(true);
        try{
            if(player.isCodeMaker()){
                int[] code = getCodeMakerCode();
                guess.intToByteArray(code);
                clientPacket.setGuessOrHint(guess);
                clientPacket.getGuessOrHint().setGameRound(gameRound);
                clientPacket.getPlayer().setPrevAction(Actions.SEND_CODE);
                objectOutputStream.reset();
                objectOutputStream.writeObject(clientPacket);
            }
            if(player.isCodeBreaker()){
                int[] breakerGuess = getCodeBreakerGuess();
                guess.intToByteArray(breakerGuess);
                clientPacket.setGuessOrHint(guess);
                clientPacket.getGuessOrHint().setGameRound(gameRound);
                clientPacket.getPlayer().setPrevAction(Actions.SEND_GUESS);
                objectOutputStream.reset();
                objectOutputStream.writeObject(clientPacket);
            }
        } catch (IOException e) {
            System.out.println("SendGuessAction exception: " + e.getMessage());
        }
    }

    private int[] getCodeBreakerGuess() {
        return getCodeFromPlayer(gameRound);
    }

    private int[] getCodeMakerCode() {
        return getCodeFromPlayer(10);
    }

    private int[] getCodeFromPlayer(int index){
        int[] code = new int[4];
        for (int i = 0; i < code.length; i++) {
            Circle currentCircle = board[index][i];
            currentCircle.setDisable(true);
            code[i] = getColorId(currentCircle);
        }
        return code;
    }

    public int getColorId(Circle circle) {
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
        if(isAllCircleFillInRound() && readyButton.isSelected()){
            readyButton.setSelected(true);
            readyButton.setDisable(true);
            sendGuessButton.setDisable(false);
            readyLabel.setText("YOU ARE READY!");
        } else {
            readyButton.setSelected(true);
            readyButton.setDisable(true);
            readyLabel.setText("YOU ARE READY!");
        }
        try{
            objectOutputStream.reset();
            clientPacket.getPlayer().setReady(true);
            clientPacket.getPlayer().setPrevAction(Actions.READY);
            objectOutputStream.writeObject(clientPacket);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public Paint getColor(int i ){
        switch (i){
            case 1:
                return colors[i-1].getFill();
            case 2:
                return colors[i-1].getFill();
            case 3:
                return colors[i-1].getFill();
            case 4:
                return colors[i-1].getFill();
            case 5:
                return colors[i-1].getFill();
            case 6:
                return colors[i-1].getFill();
        }
        return null;
    }

    public void enterButtonTextFieldAction(KeyEvent keyEvent){
        try{
            objectOutputStream.reset();
            if(!inputTextField.getText().isEmpty() && keyEvent.getCode().equals(KeyCode.ENTER)) {
                clientPacket.getChatMessage().setMessage(inputTextField.getText());
                clientPacket.getPlayer().setPrevAction(Actions.CHAT);
                objectOutputStream.writeObject(clientPacket);
                inputTextField.clear();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMessageButtonAction(ActionEvent actionEvent){
        try {
            objectOutputStream.reset();
            if (!inputTextField.getText().isEmpty()) {
                clientPacket.getChatMessage().setMessage(inputTextField.getText());
                clientPacket.getPlayer().setPrevAction(Actions.CHAT);
                objectOutputStream.writeObject(clientPacket);
                inputTextField.clear();
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void newGameButtonAction(MouseEvent mouseEvent) {
        try {
            player.switchMode();
            clientPacket.getPlayer().setPrevAction(Actions.NEW_GAME);
            objectOutputStream.reset();
            objectOutputStream.writeObject(clientPacket);
            readyFlag = false;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
            clientPacket.getPlayer().setPrevAction(Actions.READY);
            objectOutputStream.reset();
            objectOutputStream.writeObject(clientPacket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void shutdown() {

        try {
            System.out.println("ShutDown");
            if (clientPacket != null){
                clientPacket.getPlayer().setPrevAction(Actions.WINDOW_CLOSE);
                objectOutputStream.writeObject(clientPacket);
            }
            if (objectOutputStream != null)
                objectOutputStream.close();
            if (objectInputStream != null)
                objectInputStream.close();
            if (socket != null)
                socket.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
