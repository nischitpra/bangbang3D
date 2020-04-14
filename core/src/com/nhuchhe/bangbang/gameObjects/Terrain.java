package com.nhuchhe.bangbang.gameObjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;

public class Terrain extends BaseGameObject {

    public Terrain(String id, String terrainName) {
        super(id);
        Model model = BangBang.assetManager.assetManager.get(terrainName, Model.class);
        super.init(
                model,
                BangBang.collisionObjectHelper.getTerrainConstructionInfo(model)
        );
        rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        BangBang.gameObjectManger.terrain = this;
    }

    @Override
    public boolean isVisible(Camera cam) {
        return true;
    }
}