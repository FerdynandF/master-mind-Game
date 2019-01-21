package main.java.com.ferdynand.message;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String username;
    private String message;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return username + ": " + message;
    }

}
