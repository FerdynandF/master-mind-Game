package main.java.com.ferdynand;

import main.java.com.ferdynand.packet.ClientPacket;
import main.java.com.ferdynand.packet.ServerPacket;
import main.java.com.ferdynand.player.Actions;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

public class PlayerHandler implements Runnable {
    private Socket socket;
    private Server server;
    private Room room;
    private BufferedReader in;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ClientPacket packet;
    private ServerPacket serverPacket = new ServerPacket();
    private int playersWaiting = 0;
    private boolean won = false;
    private boolean lost = false;

    @Override
    public void run() {
        try {
            server.setPlayersOnline(server.getPlayersOnline() + 1);
            room.setPlayersConnected(room.getPlayersConnected() + 1);
            server.setPlayerJoining(true);
            System.out.println("Player connected.");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            room.getObjectOutputStreams().add(objectOutputStream);
            while (true) {
                packet = (ClientPacket) objectInputStream.readObject();

                switch (packet.getPlayer().getPrevAction()) {
                    case (Actions.CHAT):
                        if (!packet.getChatMessage().getMessage().isEmpty()) {
                            for (ObjectOutputStream objectOutputStream : room.getObjectOutputStreams()) {
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

                    case (Actions.SEND_CODE):
                        room.setTheCode(packet.getGuessOrHint().byteToIntArray());
                        room.setGameRound(packet.getGuessOrHint().getGameRound());
                        room.sendToAll(Actions.CHAT, "Breaker can start.");
                        Thread.sleep(30);
                        room.sendToAll(Actions.BEGIN_MATCH, null);
                        break;

                    case (Actions.SEND_GUESS):
                        room.setTheGuess(packet.getGuessOrHint().byteToIntArray());
                        room.setGameRound(packet.getGuessOrHint().getGameRound());
                        for (ObjectOutputStream oos : room.getObjectOutputStreams()) {
                            if(oos.equals(objectOutputStream)) continue;
                            serverPacket.setAction(Actions.SET_GUESS_COLOR);
                            serverPacket.setGuessOrHint(packet.getGuessOrHint());
                            oos.reset();
                            oos.writeObject(serverPacket);
                        }
                        Thread.sleep(30);
//                        Give feedback(hints) -->  13 - white  12 - black
                        int[] feedback = getHintArray();
                        for (ObjectOutputStream oos : room.getObjectOutputStreams()){
                            serverPacket.getGuessOrHint().intToByteArray(feedback);
                            serverPacket.setAction(Actions.SET_HINT_COLOR);
                            oos.reset();
                            oos.writeObject(serverPacket);
                        }
                        sendResultIfNecessary();
                        break;
                    case (Actions.NEW_GAME):
                        room.setGameRound(0);
                        room.setTheGuess(null);
                        room.setTheCode(null);
                        won = false;
                        lost = false;
                        playersWaiting++;
                        sendObject(Actions.NEW_GAME, null);
                        Thread.sleep(30);
                        String breakerOrMaker = getModeFromPacket();
                        sendObject(Actions.CHAT, "You are: " + breakerOrMaker);
                        Thread.sleep(20);
                        sendObject(Actions.READY_FLAG, null);
                        break;

                    case (Actions.CONNECTED):
                        room.getPlayers().add(packet.getPlayer());
                        if (room.getPlayers().size() == 1) {
                            packet.getPlayer().setCodeMaker(true);
                            packet.getPlayer().setCodeBreaker(false);
                            sendObject(Actions.MAKER, null);
                        }
                        if (room.getPlayers().size() == 2) {
                            packet.getPlayer().setCodeBreaker(true);
                            packet.getPlayer().setCodeMaker(false);
                            sendObject(Actions.BREAKER, null);
                        }
                        Thread.sleep(30);
                        String breakerOrMaker2 = getModeFromPacket();
                        objectOutputStream.reset();
                        sendObject(Actions.CHAT, "You are: " + breakerOrMaker2);
                        break;

                    case (Actions.WINDOW_CLOSE):
                        objectOutputStream.close();
                        socket.close();
                        break;
                }
            }
        } catch (EOFException e) {
            try {
                objectInputStream.close();
            } catch (IOException ex) {
                System.out.println("Close filed: " + ex.getMessage());
            }
        } catch (SocketException socetEx) {
            try {
                socket.close();
                System.out.println(socetEx.getMessage());
            } catch (IOException exc) {
                System.out.println("Socket exc:" + exc.getMessage());
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
            try {
                if (objectOutputStream != null)
                    objectOutputStream.close();
                if (objectInputStream != null)
                    objectInputStream.close();
            } catch (IOException ioe) {
                System.out.println("Streem Closing exception: " + ioe.getMessage());
            }
        }
    }

    private void sendResultIfNecessary() throws IOException {
        for (ObjectOutputStream oos : room.getObjectOutputStreams()){
            if (oos.equals(objectOutputStream)){
                if(lost && !won){
                    sendObject(Actions.GAME_LOST, null);
                } else if (!lost && won){
                    sendObject(Actions.GAME_WON, null);
                }
            } else {
                if(lost && !won){
                 serverPacket.setAction(Actions.GAME_WON);
                 playersWaiting = 0;
                 oos.reset();
                 oos.writeObject(serverPacket);
                } else if (!lost && won){
                    playersWaiting = 0;
                    serverPacket.setAction(Actions.GAME_LOST);
                    oos.reset();
                    oos.writeObject(serverPacket);
                }
            }
        }
    }

    private int[] getHintArray() throws IOException{
        int[] copyOfTheCode = Arrays.copyOf(room.getTheCode(), room.getTheCode().length);
        int[] message = Arrays.copyOf(room.getTheGuess(), room.getTheGuess().length);
        int[] feedback = new int[4]; // clues

        // the position in clues array
        int clue = findInPlaceClues(message, feedback, copyOfTheCode);

        if(clue == 4){
            won = true;
        } else {
            if(room.getGameRound() == 10){
                lost = true;
            } else {
                findOutPlaceClues(message, feedback, clue, copyOfTheCode);
            }
        }

        return feedback;
    }

    private void findOutPlaceClues(int[] message, int[] feedback, int clue, int[] copyOfTheCode) {
        for (int i = 0; i < message.length; i++) {
            if(message[i] != 0){
                int index = searchArray(copyOfTheCode, message[i]);
                if(index != -1){
                    feedback[clue] = 13;
                    clue++;
                    copyOfTheCode[index] = 0;
                }
            }
        }
    }

    private int findInPlaceClues(int[] message, int[] feedback, int[] copyOfTheCode) {
        int clue = 0;
        for (int i = 0; i < message.length; i++) {
            if(message[i] == copyOfTheCode[i]){
                feedback[clue] = 12;
                clue++;
                message[i] = 0;
                copyOfTheCode[i] = 0;
            }
        }
        return clue;
    }

    private int searchArray(int[] array, int key) {
        for (int i = 0; i < array.length; i++) {
            if(array[i] == key)
                return i;
        }
        return -1;
    }

    private void sendObject(String actions, String message) throws IOException {
        serverPacket.setAction(actions);
        serverPacket.getChatMessage().setMessage(message);
        objectOutputStream.reset();
        objectOutputStream.writeObject(serverPacket);
    }

    private String getModeFromPacket() {
        if (packet.getPlayer().isCodeMaker())
            return "Code-Maker";
        if (packet.getPlayer().isCodeBreaker())
            return "Code-Breaker";
        return "NUUUUL";
    }

    public PlayerHandler(Server server, Room room, Socket accept) {
        this.room = room;
        this.server = server;
        this.socket = accept;
        serverPacket.getChatMessage().setUsername("Mode");
    }
}
