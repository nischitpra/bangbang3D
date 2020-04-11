package com.nhuchhe.bangbang;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class InputControllerManager {

    public static Controller controller;
    private Player player;
    private XBoxControllerAdapter controllerAdapter;

    public void init() {
        this.controllerAdapter = new XBoxControllerAdapter(this);
        this.controller = Controllers.getControllers().get(0);
        this.controller.addListener(this.controllerAdapter);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void update() {
        if (controllerAdapter.isDownX != 0 || controllerAdapter.isDownY != 0) {// need to adjust this for better movement
            float rotationRad = (float) Math.atan2(-controllerAdapter.isDownY, controllerAdapter.isDownX);
            player.controllerFeed(controllerAdapter.isDownX, controllerAdapter.isDownY, rotationRad);
        }
    }

    public void majorAttackDown() {
        player.initMajorAttack();
    }

    public void majorAttackUp() {
        player.performMajorAttack();
    }
}
