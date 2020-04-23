package com.nhuchhe.bangbang.inputController;

import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.PlayableGameObject;
import com.nhuchhe.bangbang.inputController.adapter.NetworkControllerAdapter;
import com.nhuchhe.bangbang.inputController.base.BaseInputController;

public class NetworkInputController extends BaseInputController {

    public NetworkInputController(PlayableGameObject player) {
        controllerAdapter = new NetworkControllerAdapter(this);
        BangBang.network.addAdapter(player.id, (NetworkControllerAdapter) controllerAdapter);
        setPlayer(player);
    }

}
