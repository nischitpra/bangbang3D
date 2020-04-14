package com.nhuchhe.bangbang.gameObjects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Logger;
import com.nhuchhe.bangbang.utilities.Utilities;
import com.nhuchhe.bangbang.gameObjects.base.AoeDetectionGameObject;
import com.nhuchhe.bangbang.manager.BombManager;

import java.util.HashMap;

public class Bomb extends AoeDetectionGameObject {
    public long startTime;
    public long explodeAt;

    public String owner;

    public Bomb(String id) {
        super(id, BangBang.collisionShapeHelper.getExplosionShape());
        super.init(
                BangBang.assetManager.assetManager.get(Constants.AssetNames.BOMB, Model.class),
                BangBang.collisionObjectHelper.getBombConstructionInfo()
        );
    }

    public void detonate() {
        BombManager.usedBombQ.addLast(this);
        startTime = System.currentTimeMillis();
        explodeAt = startTime + 3000;
    }

    public void update() {
        if (System.currentTimeMillis() > explodeAt) {// recycle bomb
            applyExplosionForce();
        } else {
            aoe.setWorldTransform(instance.transform);
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
            tempVector.x = tempVector.x - explosionCenter.x;
            tempVector.y = tempVector.y - explosionCenter.y;
            tempVector.z = tempVector.z - explosionCenter.z;
            tempVector.scl(100);
            obj.activate(true);
            obj.applyCentralImpulse(tempVector);
        }
    }

}
