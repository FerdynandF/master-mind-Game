package main.java.com.ferdynand.model.guess;

import java.io.Serializable;

public class Guess implements Serializable {
    private byte[] clientGuessOrHint;
    private int gameRound;
    private static final long serialVersionUID = 7987345586021750378L;

    public void intToByteArray(int[] ints) {
        clientGuessOrHint = new byte[ints.length];
        for(int i = 0; i < ints.length; i++){
            clientGuessOrHint[i] = (byte)ints[i];
        }
    }

    public int[] byteToIntArray() {
        int[] ints = new int[clientGuessOrHint.length];
        for (int i = 0; i < clientGuessOrHint.length; i++) {
            ints[i] = clientGuessOrHint[i];
        }
        return ints;
    }

    public int getGameRound() {
        return gameRound;
    }

    public void setGameRound(int gameRound) {
        this.gameRound = gameRound;
    }

    @Override
    public String toString(){
        return clientGuessOrHint.toString();
    }
}
