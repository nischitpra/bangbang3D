package com.nhuchhe.bangbang.manager;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.nhuchhe.bangbang.gameObjects.Player;
import com.nhuchhe.bangbang.inputController.XBoxControllerAdapter;

public class InputControllerManager {

    public static Controller controller;
    private Player player;
    private XBoxControllerAdapter controllerAdapter;

    public void init() {
        try {
            this.controllerAdapter = new XBoxControllerAdapter(this);
            this.controller = Controllers.getControllers().get(0);
            this.controller.addListener(this.controllerAdapter);

        } catch (Exception e) {

        }
    }

    public void setPlayer(Player player) {
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
