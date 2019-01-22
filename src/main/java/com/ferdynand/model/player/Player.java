package main.java.com.ferdynand.model.player;

import java.io.Serializable;

public class Player implements Serializable {
    private String username;
    private int id;
    private boolean codeMaker = false;
    private boolean codeBreaker = false;
    private boolean ready = false;

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    private String prevAction;

    public boolean isReady() {
        return ready;
    }

    public String getPrevAction() {
        return prevAction;
    }

    public void setPrevAction(String prevAction) {
        this.prevAction = prevAction;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public boolean isCodeMaker() {
        return codeMaker;
    }

    public boolean isCodeBreaker() {
        return codeBreaker;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCodeMaker(boolean codeMaker) {
        this.codeMaker = codeMaker;
    }

    public void setCodeBreaker(boolean codeBreaker) {
        this.codeBreaker = codeBreaker;
    }


    @Override
    public String toString(){
        String returnString;
        returnString = "Username: " + this.getUsername() + "\n";
        returnString += "You are";
        if(this.isCodeBreaker()){
            returnString += " the code-breaker.\n";
        }
        if(this.isCodeMaker()){
            returnString += " the code-maker.\n";
        }
        return returnString;
    }

    public void switchMode() {
        setCodeMaker(!isCodeMaker());
        setCodeBreaker(!isCodeBreaker());
    }
}
