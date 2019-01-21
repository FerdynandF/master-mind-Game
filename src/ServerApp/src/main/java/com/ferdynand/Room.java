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
    private boolean playerGuessed = false;


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
        try {
            sendToAll(Actions.CHAT, "Finally you can start the Game.");
            sendToAll(Actions.GAME_BEGIN, null);
            for (int i = 0; i < players.size(); i++) {
                if (endOfTheGame) break;
                serverPacket.setAction(Actions.MAKER);
                objectOutputStreams.get(i).writeObject(serverPacket);
                Thread.sleep(30);
                sendToAll(Actions.CHAT, players.get(i).getUsername() + " is Code-Maker");
                Thread.sleep(30);
                waitForTheGuess();
                if (i + 1 >= players.size())
                    i = -1;

            }
        } catch (InterruptedException e){
            System.out.println("Thread interrupted: " + e.getMessage());
        }


    }

    private void waitForTheGuess() {
        while (true){
            synchronized (this) {
                try {
                    if(playerGuessed) {
                        sendToAll(Actions.END_OF_GAME, null);
                        sendToAll(Actions.CLEAR_UI, null);
//*************************************************************************************
//*************************************************************************************
//*************************************************************************************
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
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

