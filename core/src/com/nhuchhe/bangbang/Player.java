package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

public class Player extends GameObject {
    private Bomb majorBomb;
    private BombManager bombManager = new BombManager();

    private final float MAX_SPEED = 10;
    private boolean isGrounded = true;

    Player(String name, Model model) {
        super(name, model);
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

    public void initMajorAttack() {
        if (majorBomb != null) return;
        Vector3 bombPosition = getPosition();
        bombPosition.y += 2; // hover bomb above player

        majorBomb = bombManager.getBomb("player");
//        majorBomb.rigidBody.translate(bombPosition);
        majorBomb.instance.transform.getTranslation(bombPosition);
    }

    public void performMajorAttack() {
        if (majorBomb == null) return;
//        Vector3 velocity = rigidBody.getLinearVelocity();
//        velocity.y += 10;
//        velocity.scl(10); //  multiply by 10 to increase force
//        majorBomb.rigidBody.applyCentralImpulse(velocity);
        majorBomb.detonate();
        majorBomb = null;
    }

}
