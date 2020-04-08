package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

public class Player extends GameObject {

    private final float MAX_SPEED = 10;

    private boolean isGrounded = true;

    Player(String name, Model model) {
        super(name, model);
    }

    public void update() {
    }

    public void controllerFeed(final float inputX, final float inputY) {
        /**
         * need to disable controller feed when not in ground, during explosions etc
         */
        Vector3 vel = rigidBody.getLinearVelocity();
        vel.x = MAX_SPEED * inputX;
        vel.z = MAX_SPEED * inputY;
        rigidBody.setLinearVelocity(vel);
    }

}
