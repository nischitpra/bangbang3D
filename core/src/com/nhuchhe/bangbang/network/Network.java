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
            BangBang.GAME_LOBBY_NAME = "game." + lobbyName;
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

    private long nextSend = 0;

    public void upstreamPlayerMovementControl() {
        movementThread = new Thread(new Runnable() {
            @Override
            public void run() {
                InGamePojo inGamePojo = new InGamePojo();
                while (!Thread.currentThread().isInterrupted()) {
                    if (BangBang.currentMillis < nextSend) continue;
                    sendData(inGamePojo);
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

    InGamePojo inGamePojo = new InGamePojo();

    public void majorAttackDown() {
        inGamePojo.majorAttackDown = true;
        inGamePojo.majorAttackUp = false;
        sendData(inGamePojo);
    }

    public void majorAttackUp() {
        inGamePojo.majorAttackDown = false;
        inGamePojo.majorAttackUp = true;
        sendData(inGamePojo);
    }

    public void minorAttackUp() {
        inGamePojo.minorAttackDown = false;
        inGamePojo.minorAttackUp = true;
        sendData(inGamePojo);
    }

    public void minorAttackDown() {
        inGamePojo.minorAttackDown = true;
        inGamePojo.minorAttackUp = false;
        sendData(inGamePojo);
    }

    public void disconnect() {
        gameManagerPojo.action = GameManagerAction.DISCONNECT;
        gameManagerPojo.data = BangBang.LOBBY_NAME;
        gameManagerPojo.extra = BangBang.PLAYER_ID;
        gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        super.dispose();
    }

    BaseControllerListener listener = null;

    private void sendData(final InGamePojo inGamePojo) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (listener == null)
                    listener = BangBang.inputControllerManager.playerInputController.controllerAdapter;

                inGamePojo.id = BangBang.PLAYER_ID;
                inGamePojo.position = GameScreen.gameObjectManger.player.getPosition();
                inGamePojo.health = Math.max(GameScreen.gameObjectManger.player.health, 0);
                inGamePojo.inputX = listener.isDownX;
                inGamePojo.inputY = listener.isDownY;
                inGamePojo.lt = listener.lt;
                inGamePojo.timestamp = BangBang.currentMillis;
                senderSocket.sendMore(BangBang.GAME_LOBBY_NAME);
                senderSocket.send(SerializationUtils.serialize(inGamePojo));
                nextSend = BangBang.currentMillis;
            }
        });

    }
}
