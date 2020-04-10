package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
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

    public void update() {
        updateBombPosition();
    }

    private void updateBombPosition() {
        if (majorBomb != null) {
            Vector3 bombPosition = getPosition();
            bombPosition.y += 2.5; // hover bomb above player
            Matrix4 tra = rigidBody.getWorldTransform();
            tra.setTranslation(bombPosition);
            majorBomb.rigidBody.setWorldTransform(tra);
            majorBomb.instance.transform.translate(bombPosition);
        }
    }

    public void initMajorAttack() {
        if (majorBomb != null) return;
        majorBomb = bombManager.getBomb("player");
        updateBombPosition();
    }

    private void throwBomb() {
        Vector3 velocity = rigidBody.getLinearVelocity();
        velocity.y += 10;
        velocity.scl(10); //  multiply by 10 to increase force
        majorBomb.rigidBody.applyCentralImpulse(velocity);
        majorBomb.detonate();
        majorBomb = null;
    }

    public void performMajorAttack() {
        if (majorBomb == null) return;
        throwBomb();
    }

}
