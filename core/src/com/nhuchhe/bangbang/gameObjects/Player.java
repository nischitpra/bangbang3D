package com.nhuchhe.bangbang.gameObjects;

import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.PlayableGameObject;
import com.nhuchhe.bangbang.screens.GameScreen;
import com.nhuchhe.bangbang.utilities.Constants;

/**
 * instance.transform.translate(vector3) moves the object to the position relative to current position
 * instance.transform.setTranslate(vector3) moves the object to the absolute position
 * <p>
 * update instance.transfrom.translate and then rigidbody.setWorldTransfrom(instance.transform) always works like it should.
 * I don't know what motionstate.transfrom is used for
 */
public class Player extends PlayableGameObject {

//    public ProjectileTrajectoryHelper trajectoryHelper = new ProjectileTrajectoryHelper();

    public Player(String id) {
        super(id, Constants.AssetNames.PLAYER);
        GameScreen.gameObjectManger.player = this;
        BangBang.inputControllerManager.playerInputController.setPlayer(this);
    }

}
