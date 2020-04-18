package com.nhuchhe.bangbang.gameObjects.Bomb.base;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.animator.AnimationObject;
import com.nhuchhe.bangbang.gameObjects.base.AoeDetectionGameObject;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;
import com.nhuchhe.bangbang.manager.BombManager;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Logger;
import com.nhuchhe.bangbang.utilities.Utilities;

import java.util.HashMap;

public abstract class BaseBomb extends AoeDetectionGameObject {

    public int bombType;
    public String owner;
    public long explodeAt = -1;
    public boolean shouldRecycle;

    private final long EXPLODE_DELAY_MILLIS;
    private final float EXPLOSION_HEIGHT_INCREMENT;
    private final float EXPLOSION_FORCE;
    private AnimationObject animationObject;
    private Vector3 tempVector = new Vector3();


    public BaseBomb(String id, int bombType, btRigidBody.btRigidBodyConstructionInfo bombConstructionInfo, btCollisionShape aoeShape, long explodeDelayMillis, float explosionForce, float explosionHeightIncrement) {
        super(id, aoeShape);
        super.init(
                BangBang.assetManager.assetManager.get(Constants.Bombs[bombType], Model.class),
                bombConstructionInfo
        );
        this.bombType = bombType;
        this.EXPLODE_DELAY_MILLIS = explodeDelayMillis;
        this.EXPLOSION_FORCE = explosionForce;
        this.EXPLOSION_HEIGHT_INCREMENT = explosionHeightIncrement;
        animationObject = new AnimationObject(Constants.AssetNames.EXPLOSION_ANIM, "Sphere|explosion", false, BombManager.recyclePosition);
        BangBang.gameObjectManger.animationObjects.add(animationObject);
        rigidBody.setFriction(1.5f);
        rigidBody.setRestitution(0.5f);
    }

    private void explodeAnimation() {
        animationObject.play(getPosition());
    }

    public void setDetonationTime() {
        this.explodeAt = BangBang.currentMillis + EXPLODE_DELAY_MILLIS;
    }

    public void update() {
        aoe.setWorldTransform(motionState.transform);
    }

    // call this only during explosion
    protected void explode() {
        explodeAnimation();
        int count = aoe.getNumOverlappingObjects();
        Vector3 explosionCenter = getPosition();
        for (int i = count - 1; i >= 0; i--) {
            btRigidBody body = (btRigidBody) aoe.getOverlappingObject(i);
            HashMap<String, String> userData = Utilities.getUserData(body.userData);
            String objectKey = userData.get(Constants.UserData.ID);
            if (objectKey.equals(id)) continue;
            Logger.log(id + "::" + objectKey);
            BaseGameObject bgo = BangBang.gameObjectManger.gameObjectMap.get(objectKey);
            tempVector = bgo.getPosition();
            tempVector = Utilities.getNormalizedProximity(explosionCenter, tempVector);
            tempVector.y += EXPLOSION_HEIGHT_INCREMENT;
            tempVector.scl(EXPLOSION_FORCE);

            body.activate(true);
            body.applyCentralImpulse(tempVector);
        }
        shouldRecycle = true;
    }
}
