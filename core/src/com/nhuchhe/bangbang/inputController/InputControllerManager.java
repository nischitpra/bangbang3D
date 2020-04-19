package com.nhuchhe.bangbang.inputController;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.nhuchhe.bangbang.gameObjects.base.PlayableGameObject;

public class InputControllerManager {
    /**
     * This code design is really good. Congrats :) 19th April, 2020.
     * todo:
     * So, To make this into a multiplayer all you need to do is supply enemy game object in the setPlayer.
     * setup a generic controller adapter instead of the xboxControllerAdapter. The adapter for enemy will be set to something like a networkListener class.
     */

    public static Controller controller;
    private PlayableGameObject player;
    private XBoxControllerAdapter controllerAdapter;

    public void init() {
        try {
            this.controllerAdapter = new XBoxControllerAdapter(this);
            this.controller = Controllers.getControllers().get(0);
            this.controller.addListener(this.controllerAdapter);

        } catch (Exception e) {

        }
    }

    public void setPlayer(PlayableGameObject player) {
        this.player = player;
    }

    public void update() {
        if (controllerAdapter.isDownX != 0 || controllerAdapter.isDownY != 0) {// need to adjust this for better movement
            float rotationRad = (float) Math.atan2(-controllerAdapter.isDownY, controllerAdapter.isDownX);
            player.controllerFeed(controllerAdapter.isDownX, controllerAdapter.isDownY, rotationRad, controllerAdapter.lt);
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
