package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

public class Player extends GameObject {
    private Bomb majorBomb;
    private BombManager bombManager = new BombManager();

    private final float MAX_SPEED = 5;
    private boolean isGrounded = true;

    Player(String name, Model model) {
        super(name, model);
    }

    private Vector3 tempVector = new Vector3();

    private final Vector3 rotationAxis = new Vector3(0, 1, 0);

    private void rotatePlayer(final float rotationRad) {
        tempVector = getPosition();
        instance.transform.setToRotationRad(rotationAxis, rotationRad).setTranslation(tempVector);
        rigidBody.setWorldTransform(instance.transform);
    }

    private void movePlayer(final float inputX, final float inputY) {
        tempVector = rigidBody.getLinearVelocity();
        tempVector.x = MAX_SPEED * inputX;
        tempVector.z = MAX_SPEED * inputY;
        rigidBody.setLinearVelocity(tempVector);
    }

    public void controllerFeed(final float inputX, final float inputY, final float rotationRad) {
        rotatePlayer(rotationRad);
        movePlayer(inputX, inputY);
    }

    private void updateBombPosition() {
        if (majorBomb != null) {
            Vector3 bombPosition = getPosition();
            // cant use motionstate here cuz the rigidBody id deactivated
            majorBomb.instance.transform.setTranslation(bombPosition.x, bombPosition.y + 0.3f, bombPosition.z);
            majorBomb.rigidBody.setWorldTransform(majorBomb.instance.transform);
        }
    }

    public void initMajorAttack() {
        if (majorBomb != null) return;
        majorBomb = bombManager.getBomb(Constants.BombOwner.PLAYER);
        updateBombPosition();
    }

    private void throwBomb() {
        BombManager.activate(majorBomb);
        Vector3 velocity = rigidBody.getLinearVelocity();
        velocity.y += 4;
        velocity.scl(10); //  multiply by 10 to increase force
        majorBomb.rigidBody.applyCentralImpulse(velocity);
        majorBomb.detonate();
        majorBomb = null;
    }

    public void performMajorAttack() {
        if (majorBomb == null) return;
        throwBomb();
    }

    public void update() {
        updateBombPosition();
    }

}
