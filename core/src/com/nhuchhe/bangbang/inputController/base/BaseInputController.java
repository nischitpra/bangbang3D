package com.nhuchhe.bangbang.inputController.base;

import com.nhuchhe.bangbang.gameObjects.base.PlayableGameObject;

public abstract class BaseInputController {
    /**
     * This code design is really good. Congrats :) 19th April, 2020.
     * todo:
     * So, To make this into a multiplayer all you need to do is supply enemy game object in the setPlayer.
     * setup a generic controller adapter instead of the xboxControllerAdapter. The adapter for enemy will be set to something like a networkListener class.
     */

    public BaseControllerListener controllerAdapter;
    private PlayableGameObject player;

    public void setPlayer(PlayableGameObject player) {
        this.player = player;
    }

    public void update() {
        update(controllerAdapter.isDownX, controllerAdapter.isDownY, controllerAdapter.lt);
    }

    public void update(float isDownX, float isDownY, float lt) {
        if (isDownX != 0 || isDownY != 0) {// need to adjust this for better movement
            float rotationRad = (float) Math.atan2(-isDownY, isDownX);
            player.controllerFeed(isDownX, isDownY, rotationRad, lt);
        }
    }

    public void majorAttackDown() {
        player.initMajorAttack();
    }

    public void majorAttackUp() {
        player.performMajorAttack();
    }

    public void minorAttackDown() {
        player.initMinorAttack();
    }

    public void minorAttackUp() {
    }

}
