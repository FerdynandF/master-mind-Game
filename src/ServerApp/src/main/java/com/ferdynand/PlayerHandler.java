package main.java.com.ferdynand;

import main.java.com.ferdynand.packet.ClientPacket;
import main.java.com.ferdynand.packet.ServerPacket;
import main.java.com.ferdynand.player.Actions;

import java.io.*;
import java.net.Socket;

public class PlayerHandler implements Runnable {
    private Socket socket;
    private Server server;
    private Room room;
    private BufferedReader in;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ClientPacket packet;
    private ServerPacket serverPacket = new ServerPacket();
    private boolean sth = false;

    @Override
    public void run() {
        try{
            server.setPlayersOnline(server.getPlayersOnline() + 1);
            room.setPlayersConnected(room.getPlayersConnected() + 1);
            server.setPlayerJoining(true);
            System.out.println("Player connected.");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            room.getObjectOutputStreams().add(objectOutputStream);
            while(true) {
                packet = (ClientPacket) objectInputStream.readObject();
                switch (packet.getPlayer().getPrevAction()) {
                    case (Actions.CHAT):
                        if(!packet.getChatMessage().getMessage().isEmpty()) {
                            for (ObjectOutputStream objectOutputStream : room.getObjectOutputStreams()){
                                serverPacket.setAction(Actions.CHAT);
                                serverPacket.setChatMessage(packet.getChatMessage());
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(serverPacket);
                            }
                        }
                        break;
                    case (Actions.READY):
                        if (packet.getPlayer().isReady())
                            room.setPlayersReady(room.getPlayersReady() + 1);
                        else
                            room.setPlayersReady(room.getPlayersReady());
                        break;
                    case (Actions.GUESS):
                        for (ObjectOutputStream oos : room.getObjectOutputStreams()) {
                            if (oos.equals(objectOutputStream)) continue;
                            serverPacket.setAction(Actions.GUESS);
                            serverPacket.setGuessOrHint(packet.getGuessOrHint());
                            oos.reset();
                            oos.writeObject(serverPacket);
                        }
                        break;
                    case (Actions.CONNECTED):
                        room.getPlayers().add(packet.getPlayer());
                        break;
                }
            }
        } catch (IOException |ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public PlayerHandler(Server server, Room room, Socket accept) {
        this.room = room;
        this.server = server;
        this.socket = accept;
    }
}
