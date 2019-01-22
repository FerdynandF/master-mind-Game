package main.java.com.ferdynand.packet;

import main.java.com.ferdynand.guess.Guess;
import main.java.com.ferdynand.message.ChatMessage;

import java.io.Serializable;

public class Packet implements Serializable {
    private ChatMessage chatMessage = new ChatMessage();
    private Guess guessOrHint;

    public Guess getGuessOrHint() {
        return guessOrHint;
    }

    public void setGuessOrHint(Guess guessOrHint){
        this.guessOrHint = guessOrHint;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
