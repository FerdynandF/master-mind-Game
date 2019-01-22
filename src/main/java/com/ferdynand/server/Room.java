package main.java.com.ferdynand.server;


import main.java.com.ferdynand.model.packet.ServerPacket;
import main.java.com.ferdynand.model.player.Actions;
import main.java.com.ferdynand.model.player.Player;

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
    private int gameRound;
    private int[] theCode = new int[4];
    private int[] theGuess = new int[4];


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
                    sendToAll(Actions.CHAT, Actions.YOUCANSTART);
                    try{
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                    sendToAll(Actions.READY_FLAG, null);
                    sortedPlayers.addAll(players);
                    serverPacket.setPlayers(sortedPlayers);
                    break;
                }
            }
        }).start();
    }

    void sendToAll(String action, String message){
        try {
            for (ObjectOutputStream objectOutputStream : objectOutputStreams) {
                serverPacket.setAction(action);
                serverPacket.getChatMessage().setMessage(message);
                objectOutputStream.reset();
                objectOutputStream.writeObject(serverPacket);
            }
        } catch (IOException e) {
            System.out.println("SenToAll exception: " + e.getMessage());
            closeAll();
        }
    }

    private void closeAll() {
        try{
            objectOutputStreams.get(0).close();
            objectOutputStreams.get(1).close();
        } catch (IOException e){
            System.out.println("Colse() refused: " + e.getMessage());
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

    public int[] getTheCode() {
        return theCode;
    }

    public void setTheCode(int[] theCode) {
        this.theCode = theCode;
    }

    public int[] getTheGuess() {
        return theGuess;
    }

    public void setTheGuess(int[] theGuess) {
        this.theGuess = theGuess;
    }

    public int getGameRound() {
        return gameRound;
    }

    public void setGameRound(int gameRound) {
        this.gameRound = gameRound;
    }
}

