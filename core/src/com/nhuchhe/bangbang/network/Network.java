package com.nhuchhe.bangbang.network;

import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.enums.network.GameManagerAction;
import com.nhuchhe.bangbang.inputController.adapter.NetworkControllerAdapter;
import com.nhuchhe.bangbang.pojo.network.GameManagerPojo;
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

    public String[] getLobbies() {
        gameManagerPojo.action = GameManagerAction.GET_LOBBY;
        gameManagerPojo.data = "";
        gameManagerSocket.send(SerializationUtils.serialize(gameManagerPojo));
        return SerializationUtils.deserialize(gameManagerSocket.recv());
    }

    public void joinLobby(String lobbyName) {
        receiverSocket.subscribe(lobbyName);
        BangBang.lobbyName = lobbyName;
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
}
