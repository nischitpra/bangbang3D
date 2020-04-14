package com.nhuchhe.bangbang.gameObjects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;
import com.nhuchhe.bangbang.manager.BombManager;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Utilities;

public class Player extends BaseGameObject {

    private final float MAX_SPEED = 5;
    private final float FORCE_DELTA = 5;
    private final Vector3 rotationAxis = new Vector3(0, 1, 0);

    private long bombHoldAt;
    private float rotationRad;
    private float friction = 3f;
    private float linearDampening = 0.3f;
    private Vector3 tempVector = new Vector3();

    private Bomb majorBomb;
    private BombManager bombManager = new BombManager();


    public Player(String id) {
        super(id);
        super.init(
                BangBang.assetManager.assetManager.get(Constants.AssetNames.PLAYER, Model.class),
                BangBang.collisionObjectHelper.getPlayerConstructionInfo()
        );
        rigidBody.setActivationState(4);
        instance.transform.setTranslation(0, 10f, 0f);
        motionState.setWorldTransform(instance.transform);
        BangBang.gameObjectManger.player = this;
        BangBang.inputControllerManager.setPlayer(this);
    }

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
//        Logger.log(Utilities.getVelocityMagnitude(rigidBody.getLinearVelocity()) + "");
        updateBombPosition();
    }

}
