package com.nhuchhe.bangbang.inputController;

import com.nhuchhe.bangbang.gameObjects.base.PlayableGameObject;

import java.util.ArrayList;
import java.util.HashMap;

public class InputControllerManager {
    public PlayerInputController playerInputController = new PlayerInputController();

    protected HashMap<String, NetworkInputController> networkPlayerControllerMap = new HashMap<>();

    private ArrayList<String> networkPlayerIds = new ArrayList();

    public void addNetworkPlayer(PlayableGameObject player) {
        networkPlayerControllerMap.put(player.id, new NetworkInputController(player));
        networkPlayerIds.add(player.id);
    }

    public void init() {
        playerInputController.init();
    }

    public void update() {
        playerInputController.update();
        for (int i = networkPlayerIds.size() - 1; i >= 0; i--) {
            networkPlayerControllerMap.get(networkPlayerIds.get(i)).update();
        }
    }
}
