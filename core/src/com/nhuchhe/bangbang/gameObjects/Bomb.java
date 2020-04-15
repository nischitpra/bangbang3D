package com.nhuchhe.bangbang.gameObjects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.AoeDetectionGameObject;
import com.nhuchhe.bangbang.manager.BombManager;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Logger;
import com.nhuchhe.bangbang.utilities.Utilities;

import java.util.HashMap;

public class Bomb extends AoeDetectionGameObject {
    public final int EXPLODE_DELAY_MILLIS = 2500;
    public final float EXPLOSION_FORCE = 150;
    public final float EXPLOSION_HEIGHT_INCREMENT = 0.25f;

    public long startTime;
    public long explodeAt;


    public String owner;

    public Bomb(String id, int bombType) {
        super(id, BangBang.collisionShapeHelper.getExplosionShape());
        super.init(
                BangBang.assetManager.assetManager.get(Constants.Bombs[bombType], Model.class),
                BangBang.collisionObjectHelper.getBombConstructionInfo()
        );
        rigidBody.setFriction(1.5f);
        rigidBody.setRestitution(0.5f);
    }

    public void detonate() {
        BombManager.usedBombQ.addLast(this);
        startTime = BangBang.currentMillis;
        explodeAt = startTime + EXPLODE_DELAY_MILLIS;
    }

    public void update() {
        if (BangBang.currentMillis > explodeAt) {// recycle bomb
            applyExplosionForce();
        } else {
            aoe.setWorldTransform(motionState.transform);
        }
    }

    private Vector3 tempVector = new Vector3();

    private void applyExplosionForce() {
        int count = aoe.getNumOverlappingObjects();
        Logger.log("count: " + count);
        Vector3 explosionCenter = getPosition();
        for (int i = count - 1; i >= 0; i--) {
            btRigidBody obj = (btRigidBody) aoe.getOverlappingObject(i);
            HashMap<String, String> userData = Utilities.getUserData(obj.userData);
            String objectKey = userData.get(Constants.UserData.ID);
            if (objectKey.equals(id) && userData.get(Constants.UserData.OWNER).equals(owner))
                continue;
            Logger.log(obj.userData + "");
            obj.getWorldTransform().getTranslation(tempVector);
            tempVector = Utilities.getNormalizedProximity(tempVector, explosionCenter);
            tempVector.y += EXPLOSION_HEIGHT_INCREMENT;
            tempVector.scl(EXPLOSION_FORCE);
            obj.activate(true);
            obj.applyCentralImpulse(tempVector);
        }
    }

}
