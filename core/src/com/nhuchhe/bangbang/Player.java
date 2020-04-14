package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

public class Player extends GameObject {
    private Bomb majorBomb;
    private BombManager bombManager = new BombManager();

    private final float MAX_SPEED = 5;
    private final float FORCE_DELTA = 5;
    private float friction = 3f;
    private float linearDampening = 0.3f;

    private boolean isGrounded = true;

    Player(String name, Model model) {
        super(name, model);
    }

    private Vector3 tempVector = new Vector3();

    private final Vector3 rotationAxis = new Vector3(0, 1, 0);
    private float rotationRad;
    private long bombHoldAt;

    private void rotatePlayer(final float rotationRad) {
        this.rotationRad = rotationRad;
        tempVector = getPosition();
        instance.transform.setToRotationRad(rotationAxis, rotationRad).setTranslation(tempVector);
        rigidBody.setWorldTransform(instance.transform);
    }

    private void movePlayer(final float inputX, final float inputY) {
        rigidBody.setFriction(friction);
        rigidBody.setDamping(linearDampening, rigidBody.getAngularDamping());

        float velMag = Utilities.getVelocityMagnitude(tempVector);
        tempVector = rigidBody.getLinearVelocity();
        tempVector = rigidBody.getTotalForce();
        if (velMag < MAX_SPEED) {
            tempVector.x = inputX * FORCE_DELTA;
            tempVector.z = inputY * FORCE_DELTA;
            rigidBody.applyCentralImpulse(tempVector);
        }
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
        bombHoldAt = System.currentTimeMillis();
        majorBomb = bombManager.getBomb(Constants.BombOwner.PLAYER);
        updateBombPosition();
    }

    private void throwBomb() {
        BombManager.activate(majorBomb);
        tempVector = rigidBody.getLinearVelocity();
        tempVector.y += 2;
        tempVector.scl(Math.min(25, 8 + (System.currentTimeMillis() - bombHoldAt) / 25));
        majorBomb.rigidBody.applyCentralImpulse(tempVector);
        majorBomb.detonate();
        majorBomb = null;
    }

    public void performMajorAttack() {
        if (majorBomb == null) return;
        throwBomb();
    }

    public void update() {
        Logger.log(Utilities.getVelocityMagnitude(rigidBody.getLinearVelocity()) + "");
        updateBombPosition();
    }

}
