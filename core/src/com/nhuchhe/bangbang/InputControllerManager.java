package com.nhuchhe.bangbang;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class InputControllerManager {

    public static Controller controller;
    private Player player;
    private final GamepadControllerAdapter controllerAdapter = new GamepadControllerAdapter();

    public void init() {
        this.controller = Controllers.getControllers().get(0);
        this.controller.addListener(this.controllerAdapter);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void update() {
        if (controllerAdapter.isDownX != 0 || controllerAdapter.isDownY != 0) {// need to adjust this for better movement
            player.controllerFeed(controllerAdapter.isDownX, controllerAdapter.isDownY);
        }
    }
}
