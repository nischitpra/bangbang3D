package com.nhuchhe.bangbang.network;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class NetworkWire {

    private ZContext context = new ZContext();
    private ZMQ.Socket senderSocket;
    private ZMQ.Socket receiverSocket;

    private Thread senderThread;
    private Thread receiverThread;

    public void init() {
        senderSocket = context.createSocket(SocketType.PUB);
        senderSocket.bind("tcp://localhost:5555");

        receiverSocket = context.createSocket(SocketType.SUB);
        receiverSocket.connect("tcp://localhost:5556");
        receiverSocket.subscribe("A");

        createServer();
    }

    public void createServer() {
        /**
         * setup zeromq context here and perform poll. and update adapter value
         */
        senderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    senderSocket.send("B-HELLO_FROM_CLIENT!");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        senderThread.start();

        receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String value = receiverSocket.recvStr();
                    System.out.println("val: " + value);
                }
            }
        });
        receiverThread.start();
    }
}
