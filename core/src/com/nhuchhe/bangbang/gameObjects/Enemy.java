package com.nhuchhe.bangbang.gameObjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.PlayableGameObject;
import com.nhuchhe.bangbang.utilities.Constants;

import java.util.HashMap;

public class Enemy extends PlayableGameObject {

    private Vector3 rayFrom = new Vector3();
    private Vector3 rayTo = new Vector3();
    ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);

    public Enemy(String id) {
        super(id, Constants.AssetNames.ENEMY);
    }

//    @Override
//    public boolean isVisible(Camera cam) {
//        return super.isVisible(cam) && isVisibleToPlayer();
//    }

    /**
     * update this and refine it.. get list of objects hit by raytrace, and check if terrain comes before player.. if it does, return false, else return true
     *
     * @return
     */
    public boolean isVisibleToPlayer() {
        rayFrom = getPosition();
        rayTo = BangBang.gameObjectManger.player.getPosition();
        callback.setRayFromWorld(rayFrom);
        callback.setRayToWorld(rayTo);
        callback.setClosestHitFraction(1);

//        BangBang.debugDrawer.drawLine(rayFrom, rayTo, new Vector3(1, 1, 0));

        BangBang.world.rayTest(rayFrom, rayTo, callback);
        if (callback.hasHit()) {
            HashMap<String, String> userData = (HashMap<String, String>) callback.getCollisionObject().userData;
            return userData.get(Constants.UserData.ID).equals(Constants.GameObjectId.PLAYER);
        }
        return true;
    }
}
