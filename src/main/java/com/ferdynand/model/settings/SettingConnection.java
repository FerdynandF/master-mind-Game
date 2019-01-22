package main.java.com.ferdynand.model.settings;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SettingConnection {
    private String ip;
    private int port;
    private int playersInRoom;

    public String getIp() {
        return ip;
    }

    @XmlElement
    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    @XmlElement
    public void setPort(int port) {
        this.port = port;
    }

    public int getPlayersInRoom() {
        return playersInRoom;
    }

    @XmlElement
    public void setPlayersInRoom(int playersInRoom) {
        this.playersInRoom = playersInRoom;
    }
}
