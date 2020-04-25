package com.nhuchhe.bangbang.network;

import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.pojo.network.GameManagerPojo;
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
        gameManagerSocket.connect("tcp://localhost:5554"); // will use this to manage game state i.e create lobby, join lobby, wait for players, start game

        senderSocket = context.createSocket(SocketType.PUB);
        senderSocket.bind("tcp://localhost:5555");

        receiverSocket = context.createSocket(SocketType.SUB);
        receiverSocket.connect("tcp://localhost:5556");
        receiverSocket.subscribe("A");

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
                    String value = receiverSocket.recvStr();
                    GameManagerPojo pojo = SerializationUtils.deserialize(receiverSocket.recv());
                    BangBang.network.gameReceiveState(pojo);
                    Logger.log("val: " + value);
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
