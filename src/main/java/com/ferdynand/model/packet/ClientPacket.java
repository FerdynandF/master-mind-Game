package main.java.com.ferdynand.model.packet;

import main.java.com.ferdynand.model.player.Player;

/**
 * @author ferdynandf (https://github.com/FerdynandF)
 */
public class ClientPacket extends Packet {
    private Player player;

    public ClientPacket(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
