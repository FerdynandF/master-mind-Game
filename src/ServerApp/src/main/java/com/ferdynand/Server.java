package main.java.com.ferdynand;

import settings.SettingConnection;
import settings.SettingConnectionDao;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private volatile int playersOnline = 0;
    private volatile boolean playerJoining = true;
    private boolean firstPlayer = true;
    private SettingConnection settingConnection;
    private List<Room> roomList = new ArrayList<>();


    public static void main(String[] args) {
        new Thread(new Server()).start();
    }

    @Override
    public void run() {
        settingConnection = SettingConnectionDao.getInstance().getSettingConnectionFromXMLFile("settingConnection.xml");
        System.out.println("Waiting for connection...");
        try (ServerSocket serverSocket = new ServerSocket(settingConnection.getPort())) {
            while(true){
                if(playersOnline % settingConnection.getPlayersInRoom() == 0 && playerJoining) {
                    Room room = new Room(this, serverSocket);
                    roomList.add(room);
                    new Thread(room).start();
                    playerJoining = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized SettingConnection getSettingConnection() {
        return settingConnection;
    }

    public synchronized void setPlayersOnline(int playersOnline) {
        this.playersOnline = playersOnline;
    }

    public synchronized int getPlayersOnline() {
        return playersOnline;
    }

    public synchronized void setPlayerJoining(boolean playerJoins) {
        this.playerJoining = playerJoins;
    }
}
