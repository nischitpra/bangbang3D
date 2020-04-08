package com.nhuchhe.bangbang;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector3;

public class InputControllerManager {

    public static Controller controller;

    private GameObject player;

    private final GamepadControllerAdapter controllerAdapter = new GamepadControllerAdapter();

    private final float MAX_SPEED = 5;

    public void init() {
        this.controller = Controllers.getControllers().get(0);
        this.controller.addListener(this.controllerAdapter);
    }

    public void setPlayer(GameObject player) {
        this.player = player;
    }

    public void update() {
        if (controllerAdapter.isDownX != 0 || controllerAdapter.isDownY != 0) {// need to adjust this for better movement
            Vector3 vel = player.rigidBody.getLinearVelocity();
            vel.x = (MAX_SPEED * controllerAdapter.isDownX);
            vel.z = (MAX_SPEED * controllerAdapter.isDownY);
            player.rigidBody.setLinearVelocity(vel);
            System.out.println(vel);
        }
    }
}
