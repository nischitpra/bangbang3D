package com.nhuchhe.bangbang.network;

import com.badlogic.gdx.Gdx;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.enums.network.GameManagerAction;
import com.nhuchhe.bangbang.inputController.adapter.NetworkControllerAdapter;
import com.nhuchhe.bangbang.inputController.base.BaseControllerListener;
import com.nhuchhe.bangbang.pojo.network.GameManagerPojo;
import com.nhuchhe.bangbang.pojo.network.InGamePojo;
import com.nhuchhe.bangbang.screens.GameScreen;
import com.nhuchhe.bangbang.screens.LobbyScreen;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Logger;
import com.nhuchhe.bangbang.utilities.Utilities;
import org.apache.commons.lang3.SerializationUtils;

import java.util.HashMap;

public class Network extends NetworkWire {

    HashMap<String, NetworkControllerAdapter> adapterMap = new HashMap<>();

    @Override
    public void init() {
        super.init();
    }

    public void addAdapter(String id, NetworkControllerAdapter adapter) {
        adapterMap.put(id, adapter);
    }

    GameManagerPojo gameManagerPojo = new GameManagerPojo();

    public boolean createLobby(String lobbyName) {
        gameManagerPojo.action = GameManagerAction.CREATE_LOBBY;
        gameManagerPojo.data = lobbyName;
        gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        return SerializationUtils.deserialize(gameManagerSocket.recv());
    }

    public String[] getLobbies() {
        gameManagerPojo.action = GameManagerAction.GET_LOBBY;
        gameManagerPojo.data = "";
        gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        return SerializationUtils.deserialize(gameManagerSocket.recv());
    }

    public boolean joinLobby(String lobbyName) {
        gameManagerPojo.action = GameManagerAction.JOIN_LOBBY;
        gameManagerPojo.data = lobbyName;
        boolean success = gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        if (success) {
            int id = SerializationUtils.deserialize(gameManagerSocket.recv());
            if (id < 0) return false;
            receiverSocket.subscribe(lobbyName);
            receiverSocket.subscribe("game." + lobbyName);
            BangBang.LOBBY_NAME = lobbyName;
            BangBang.currentScreen = new LobbyScreen(lobbyName);
            BangBang.PLAYER_ID = id;
            Logger.log("playerId: " + id);
            return true;
        }
        return false;
    }

    public int[] getPlayers(String lobbyName) {
        gameManagerPojo.action = GameManagerAction.GET_LOBBY_PLAYERS;
        gameManagerPojo.data = lobbyName;
        boolean success = gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        if (success) {
            int[] ids = SerializationUtils.deserialize(gameManagerSocket.recv());
            BangBang.PLAYER_IDS = ids;
            return ids;
        }
        return new int[]{};
    }

    public void startGame(String lobbyName) {
        gameManagerPojo.action = GameManagerAction.START_GAME;
        gameManagerPojo.data = lobbyName;
        gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        // game state will change from socket stream instead of req rep from here.
    }

    public int createGame(String lobbyName) {
        gameManagerPojo.action = GameManagerAction.CREATE_LOBBY;
        gameManagerPojo.data = lobbyName;
        boolean success = gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        if (success) {
            return Integer.parseInt(gameManagerSocket.recvStr());
        }
        return -1;
    }

    public void gameReceiveState(GameManagerPojo pojo) {
        switch (pojo.action) {
            case CHANGE_SCREEN:
                if (pojo.data.equals("GameScreen")) {
                    // libgdx is not thread safe. and opengl only works in the main thread
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            BangBang.currentScreen = new GameScreen();
                        }
                    });
                }
                break;
        }
    }

    //upstream for user movement inputs. every 30ms send a new update
    private Thread movementThread;

    public void upstreamPlayerMovementControl() {
        final BaseControllerListener listener = BangBang.inputControllerManager.playerInputController.controllerAdapter;
        final String lobbyName = "game." + BangBang.LOBBY_NAME;
        movementThread = new Thread(new Runnable() {
            @Override
            public void run() {
                InGamePojo inGamePojo = new InGamePojo();
                while (!Thread.currentThread().isInterrupted()) {
                    inGamePojo.id = BangBang.PLAYER_ID;
                    inGamePojo.health = listener.manager.player.health;
                    inGamePojo.inputX = listener.isDownX;
                    inGamePojo.inputY = listener.isDownY;
                    inGamePojo.lt = listener.lt;
                    inGamePojo.position = listener.manager.player.getPosition();
                    inGamePojo.timestamp = BangBang.currentMillis;
                    senderSocket.sendMore(lobbyName);
                    senderSocket.send(SerializationUtils.serialize(inGamePojo));
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        movementThread.start();
    }

    public void syncMovement(final InGamePojo pojo) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                String id = Utilities.createGameObjectId(Constants.GameObjectId.ENEMY, pojo.id);
                BaseControllerListener listener = BangBang.inputControllerManager.networkPlayerControllerMap.get(id).controllerAdapter;
                if (listener.lastUpdate > pojo.timestamp) return;
                listener.isDownX = pojo.inputX;
                listener.isDownY = pojo.inputY;
                listener.lt = pojo.lt;
                float distance = Utilities.distance(listener.manager.player.getPosition(), pojo.position);
                if (distance > 0.5) {
                    Logger.log("here");
                    listener.manager.player.setPosition(pojo.position);
                }
                listener.manager.player.health = pojo.health;
                if (pojo.majorAttackDown) {
                    listener.buttonDown(null, 97);
                }
                if (pojo.majorAttackUp) {
                    listener.buttonUp(null, 97);
                }
                if (pojo.minorAttackDown) {
                    listener.buttonDown(null, 99);
                }
                if (pojo.minorAttackUp) {
                    listener.buttonUp(null, 99);
                }
                listener.lastUpdate = pojo.timestamp;
            }
        });
    }

    InGamePojo actionPojo = new InGamePojo();

    public void majorAttackDown() {
        final String lobbyName = "game." + BangBang.LOBBY_NAME;
        actionPojo.id = BangBang.PLAYER_ID;
        actionPojo.position = GameScreen.gameObjectManger.player.getPosition();
        actionPojo.health = GameScreen.gameObjectManger.player.health;
        actionPojo.majorAttackDown = true;
        actionPojo.majorAttackUp = false;
        actionPojo.timestamp = BangBang.currentMillis;
        senderSocket.sendMore(lobbyName);
        senderSocket.send(SerializationUtils.serialize(actionPojo));
    }

    public void majorAttackUp() {
        final String lobbyName = "game." + BangBang.LOBBY_NAME;
        actionPojo.id = BangBang.PLAYER_ID;
        actionPojo.position = GameScreen.gameObjectManger.player.getPosition();
        actionPojo.health = GameScreen.gameObjectManger.player.health;
        actionPojo.majorAttackDown = false;
        actionPojo.majorAttackUp = true;
        actionPojo.timestamp = BangBang.currentMillis;
        senderSocket.sendMore(lobbyName);
        senderSocket.send(SerializationUtils.serialize(actionPojo));
    }

    public void minorAttackUp() {
        final String lobbyName = "game." + BangBang.LOBBY_NAME;
        actionPojo.id = BangBang.PLAYER_ID;
        actionPojo.position = GameScreen.gameObjectManger.player.getPosition();
        actionPojo.health = GameScreen.gameObjectManger.player.health;
        actionPojo.minorAttackDown = false;
        actionPojo.minorAttackUp = true;
        actionPojo.timestamp = BangBang.currentMillis;
        senderSocket.sendMore(lobbyName);
        senderSocket.send(SerializationUtils.serialize(actionPojo));
    }

    public void minorAttackDown() {
        final String lobbyName = "game." + BangBang.LOBBY_NAME;
        actionPojo.id = BangBang.PLAYER_ID;
        actionPojo.position = GameScreen.gameObjectManger.player.getPosition();
        actionPojo.health = GameScreen.gameObjectManger.player.health;
        actionPojo.minorAttackDown = true;
        actionPojo.minorAttackUp = false;
        actionPojo.timestamp = BangBang.currentMillis;
        senderSocket.sendMore(lobbyName);
        senderSocket.send(SerializationUtils.serialize(actionPojo));
    }

    public void disconnect() {
        gameManagerPojo.action = GameManagerAction.DISCONNECT;
        gameManagerPojo.data = BangBang.LOBBY_NAME;
        gameManagerPojo.extra = BangBang.PLAYER_ID;
        gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        super.dispose();
    }

}
