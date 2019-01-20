package main.java.com.ferdynand.player;

public class Player {
    private String username;
    private int id;
    private boolean isCodeMaker = false;
    private boolean isCodeBreaker = false;

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public boolean isCodeMaker() {
        return isCodeMaker;
    }

    public boolean isCodeBreaker() {
        return isCodeBreaker;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCodeMaker(boolean codeMaker) {
        isCodeMaker = codeMaker;
    }

    public void setCodeBreaker(boolean codeBreaker) {
        isCodeBreaker = codeBreaker;
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
}
