package com.nhuchhe.bangbang.gameObjects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Utilities;

/**
 * instance.transform.translate(vector3) moves the object to the position relative to current position
 * instance.transform.setTranslate(vector3) moves the object to the absolute position
 * <p>
 * update instance.transfrom.translate and then rigidbody.setWorldTransfrom(instance.transform) always works like it should.
 * I don't know what motionstate.transfrom is used for
 */
public class Player extends BaseGameObject {

    private final float MAX_SPEED = 5;
    private final float FORCE_DELTA = 5;
    private final Vector3 rotationAxis = new Vector3(0, 1, 0);

    private long bombHoldAt;
    private float friction = 3f;
    private float linearDampening = 0.3f;
    private Vector3 tempVector = new Vector3();

    private Bomb majorBomb;

    private Vector3 spawnPosition = new Vector3(0, 10, 0);

    public Player(String id) {
        super(id);
        super.init(
                BangBang.assetManager.assetManager.get(Constants.AssetNames.PLAYER, Model.class),
                BangBang.collisionObjectHelper.getPlayerConstructionInfo()
        );
        rigidBody.setActivationState(4);
        rigidBody.translate(spawnPosition);
        rigidBody.setFriction(friction);
        rigidBody.setDamping(linearDampening, rigidBody.getAngularDamping());

        BangBang.gameObjectManger.player = this;
        BangBang.inputControllerManager.setPlayer(this);
    }

    private void rotatePlayer(final float rotationRad) {
        tempVector = getPosition();
        motionState.transform.setToRotationRad(rotationAxis, rotationRad).setTranslation(tempVector);
    }

    private void movePlayer(final float inputX, final float inputY) {
        tempVector = rigidBody.getLinearVelocity();
        float velMag = Utilities.getVelocityMagnitude(tempVector);
        if (velMag < MAX_SPEED) {
            tempVector.x = inputX * FORCE_DELTA;
            tempVector.y = 0;
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
            majorBomb.instance.transform.setTranslation(bombPosition.x, bombPosition.y + 0.3f, bombPosition.z);
            majorBomb.rigidBody.setWorldTransform(majorBomb.instance.transform);
        }
    }

    public void initMajorAttack() {
        if (majorBomb != null) return;
        bombHoldAt = BangBang.currentMillis;
        majorBomb = BangBang.bombManager.getBomb(Constants.BombOwner.PLAYER);
        updateBombPosition();
    }

    private void throwBomb() {
        BangBang.bombManager.activate(majorBomb);
        tempVector = rigidBody.getLinearVelocity();
        tempVector.y += 2;
        tempVector.scl(Math.min(25, 8 + (BangBang.currentMillis - bombHoldAt) / 25));
        majorBomb.rigidBody.applyCentralImpulse(tempVector);
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
