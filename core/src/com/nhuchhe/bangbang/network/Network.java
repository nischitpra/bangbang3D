package com.nhuchhe.bangbang.network;

import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.enums.network.GameManagerAction;
import com.nhuchhe.bangbang.inputController.adapter.NetworkControllerAdapter;
import com.nhuchhe.bangbang.pojo.network.GameManagerPojo;
import com.nhuchhe.bangbang.screens.GameScreen;
import com.nhuchhe.bangbang.screens.LobbyScreen;
import com.nhuchhe.bangbang.utilities.Logger;
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
                    BangBang.currentScreen = new GameScreen();
                }
                break;
        }
    }

    public void disconnect() {
        gameManagerPojo.action = GameManagerAction.DISCONNECT;
        gameManagerPojo.data = BangBang.LOBBY_NAME;
        gameManagerPojo.extra = BangBang.PLAYER_ID;
        gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        super.dispose();
    }

}
