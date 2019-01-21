package main.java.com.ferdynand;

import main.java.com.ferdynand.packet.ServerPacket;
import main.java.com.ferdynand.player.Actions;
import main.java.com.ferdynand.player.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Room implements Runnable{
    private ServerSocket serverSocket;
    private Server server;
    private List<ObjectOutputStream> objectOutputStreams = new ArrayList<>();



    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> sortedPlayers = new ArrayList<>();
    private ServerPacket serverPacket = new ServerPacket();

    private volatile int playersReady = 0;
    private volatile int playersConnected = 1;
    private boolean endOfTheGame = false;


    public Room(Server server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        System.out.println("New Room");
        serverPacket.getChatMessage().setUsername("Server");
        waitForBeginning();
        while (true) {
            try {
                new Thread(new PlayerHandler(server, this, serverSocket.accept())).start();
                if (playersConnected - server.getSettingConnection().getPlayersInRoom() == 0)
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void waitForBeginning() {
        new Thread(() -> {
            while(true) {
                if (getPlayersReady() - server.getSettingConnection().getPlayersInRoom() == 0 && playersReady != 0){
                   try {
                       sendToAll(Actions.CHAT, Actions.WELCOME);
                       sortedPlayers.addAll(players);
                       serverPacket.setPlayers(sortedPlayers);
                       break;
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                }
            }
            try {
                preparePlayersForGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void preparePlayersForGame() throws IOException {
        sendToAll(Actions.CHAT, "Finally you can start the Game.");
        sendToAll(Actions.GAME_BEGIN, null);

    }

    private void sendToAll(String action, String message) throws IOException {
        for (ObjectOutputStream objectOutputStream : objectOutputStreams) {
            serverPacket.setAction(action);
            serverPacket.getChatMessage().setMessage(message);
            objectOutputStream.reset();
            objectOutputStream.writeObject(serverPacket);
        }
    }

    public synchronized int getPlayersReady() {
        return playersReady;
    }

    public void setPlayersReady(int playersReady) {
        this.playersReady = playersReady;
    }

    public synchronized int getPlayersConnected() {
        return playersConnected;
    }

    public synchronized void setPlayersConnected(int playersConnected) {
        this.playersConnected = playersConnected;
    }

    public synchronized List<ObjectOutputStream> getObjectOutputStreams() {
        return objectOutputStreams;
    }

    public synchronized ArrayList<Player> getPlayers() {
        return players;
    }
}

