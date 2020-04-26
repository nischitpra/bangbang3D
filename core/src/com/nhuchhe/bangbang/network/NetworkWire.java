package com.nhuchhe.bangbang.network;

import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.pojo.network.GameManagerPojo;
import com.nhuchhe.bangbang.pojo.network.InGamePojo;
import com.nhuchhe.bangbang.utilities.Logger;
import org.apache.commons.lang3.SerializationUtils;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class NetworkWire {

    private ZContext context = new ZContext();
    public ZMQ.Socket gameManagerSocket;
    public ZMQ.Socket senderSocket;
    public ZMQ.Socket receiverSocket;

    private Thread receiverThread;

    public void init() {
        gameManagerSocket = context.createSocket(SocketType.REQ);
        gameManagerSocket.connect("tcp://192.168.0.169:5554"); // will use this to manage game state i.e create lobby, join lobby, wait for players, start game

        // instead of bind,, use req for up steam
        senderSocket = context.createSocket(SocketType.PUB);
        senderSocket.connect("tcp://192.168.0.169:5555");

        receiverSocket = context.createSocket(SocketType.SUB);
        receiverSocket.connect("tcp://192.168.0.169:5556");

        createServer();
    }

    protected void createServer() {
        /**
         * setup zeromq context here and perform poll. and update adapter value
         */
        receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    Logger.log("trying to receive from players");
                    String lobbyName = receiverSocket.recvStr();
                    if (lobbyName.startsWith("game.")) {
                        Logger.log("received data for: " + lobbyName);
                        InGamePojo pojo = SerializationUtils.deserialize(receiverSocket.recv());
                        if (pojo.id == BangBang.PLAYER_ID) continue;
                        BangBang.network.syncMovement(pojo);
                        Logger.log(pojo.toString());

                    } else {
                        Logger.log("lobby: " + lobbyName);
                        GameManagerPojo pojo = SerializationUtils.deserialize(receiverSocket.recv());
                        BangBang.network.gameReceiveState(pojo);
                    }
                }
            }
        });
        receiverThread.start();
    }

    public void dispose() {
        gameManagerSocket.close();
        receiverSocket.close();
        senderSocket.close();
        context.close();

        receiverThread.interrupt();
    }
}
