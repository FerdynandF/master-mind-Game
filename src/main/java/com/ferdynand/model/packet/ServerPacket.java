package main.java.com.ferdynand.model.packet;

import main.java.com.ferdynand.model.player.Player;

import java.util.ArrayList;

/**
 * @author ferdynandf (https://github.com/FerdynandF)
 */
public class ServerPacket extends Packet {
    private String action;
    private ArrayList<Player> players;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
