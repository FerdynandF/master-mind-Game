package java.com.ferdynand.model.guess;

import main.java.com.ferdynand.model.guess.Guess;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GuessTest {

    @Test
    public void byteToIntConversion(){
        Guess guess = new Guess();
        int[] guessRound = {1,2,3,4};
        guess.intToByteArray(guessRound);
        assertTrue(Arrays.equals(guessRound, guess.byteToIntArray()));
    }
}