package main.java.com.ferdynand.guess;

import java.io.Serializable;

public class Guess implements Serializable {
    private byte[] clientGuess;

    public void intToByteArray(int[] ints) {
        clientGuess = new byte[ints.length];
        for(int i = 0; i < ints.length; i++){
            clientGuess[i] = (byte)ints[i];
        }
    }

    public int[] byteToIntArray() {
        int[] ints = new int[clientGuess.length];
        for (int i = 0; i < clientGuess.length; i++) {
            ints[i] = clientGuess[i];
        }
        return ints;
    }
}
