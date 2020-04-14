package com.nhuchhe.bangbang.helper;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.nhuchhe.bangbang.utilities.Constants;

import java.util.HashMap;
import java.util.Set;

public class CollisionObjectHelper {

    private CollisionShapeHelper collisionShapeHelper = new CollisionShapeHelper();
    private HashMap<String, btRigidBodyConstructionInfo> rigidBodyConstructionInfoMap = new HashMap<>();

    public btRigidBodyConstructionInfo getPlayerConstructionInfo() {
        return getRigidBodyConstructionInfo(Constants.CollisionObject.PLAYER, 10, collisionShapeHelper.getPlayerShape());
    }

    public btRigidBodyConstructionInfo getBombConstructionInfo() {
        return getRigidBodyConstructionInfo(Constants.CollisionObject.BOMB, 10, collisionShapeHelper.getBombShape());
    }

    public btRigidBodyConstructionInfo getTerrainConstructionInfo(final Model model) {
        return getRigidBodyConstructionInfo(Constants.CollisionObject.TERRAIN, 0, collisionShapeHelper.getTerrainShape(model, true));
    }

    private btRigidBodyConstructionInfo getRigidBodyConstructionInfo(String key, float mass, btCollisionShape shape) {
        btRigidBodyConstructionInfo info = rigidBodyConstructionInfoMap.get(key);
        if (info == null) {
            info = new btRigidBodyConstructionInfo(mass, null, shape, new Vector3());
            rigidBodyConstructionInfoMap.put(key, info);
        }
        return info;
    }

    public void dispose() {
        collisionShapeHelper.dispose();
        Set<String> keys = rigidBodyConstructionInfoMap.keySet();
        for (String key : keys) {
            rigidBodyConstructionInfoMap.get(key).dispose();
        }
    }

}