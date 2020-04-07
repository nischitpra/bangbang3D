package com.nhuchhe.bangbang;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class GamePadControllerManager {

    public static Controller controller;

    private GameObject player;

    private final GamepadControllerAdapter controllerAdapter = new GamepadControllerAdapter();

    private final int MAX_SPEED = 5;

    public void init() {
        this.controller = Controllers.getControllers().get(0);
        this.controller.addListener(this.controllerAdapter);
    }

    public void setPlayer(GameObject player) {
        this.player = player;
    }

    public void update() {
        Vector3 vel = player.rigidBody.getLinearVelocity();
        vel.x += MAX_SPEED * controllerAdapter.isDownX;
        vel.z += MAX_SPEED * controllerAdapter.isDownY;
        vel.x = MathUtils.clamp(vel.x, -MAX_SPEED, MAX_SPEED);
        vel.z = MathUtils.clamp(vel.z, -MAX_SPEED, MAX_SPEED);
        player.rigidBody.setLinearVelocity(vel);
    }
}
