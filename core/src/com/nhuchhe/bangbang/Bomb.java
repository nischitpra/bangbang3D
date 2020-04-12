package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import java.util.HashMap;

public class Bomb extends GameObject {
    public long startTime;
    public long explodeAt;

    public btGhostObject explosionSphere;
    public String owner;

    Bomb(String name, Model model) {
        super(name, model);
    }

    public void detonate() {
        BombManager.usedBombQ.addLast(this);
        startTime = System.currentTimeMillis();
        explodeAt = startTime + 3000;
    }

    public void update() {
        explosionSphere.setWorldTransform(instance.transform);
        if (System.currentTimeMillis() > explodeAt) {// recycle bomb
            applyExplosionForce();
        }
    }

    private Vector3 tempVector = new Vector3();

    private void applyExplosionForce() {
        int count = explosionSphere.getNumOverlappingObjects();
        Logger.log("count: " + count);
        Vector3 explosionCenter = getPosition();
        for (int i = count - 1; i >= 0; i--) {
            btRigidBody obj = (btRigidBody) explosionSphere.getOverlappingObject(i);
            HashMap<String, String> userData = Utilities.getUserData(obj.userData);
            String objectKey = userData.get(Constants.UserData.NAME);
            if (objectKey.equals(name) && userData.get(Constants.UserData.OWNER).equals(owner))
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
