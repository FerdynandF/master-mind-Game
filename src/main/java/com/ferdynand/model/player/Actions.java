package main.java.com.ferdynand.model.player;

public interface Actions {
    String CONNECTED = "CONN";
    String CHAT = "C";
    String MAKER = "CM";
    String BREAKER = "CB";
    String READY = "R";
    String YOUCANSTART = "Waiting for the Code-Maker to set the code.";
    String SEND_CODE = "CODE";
    String BEGIN_MATCH = "BM";
    String SEND_GUESS = "GUESS";
    String SET_GUESS_COLOR = "SGC";
    String SET_HINT_COLOR = "HINT";
    String GAME_WON = "WON";
    String GAME_LOST = "LOST";
    String NEW_GAME = "NG"; // <<-----------
    String READY_FLAG = "RF";
    String WINDOW_CLOSE = "CLOSE";
}
