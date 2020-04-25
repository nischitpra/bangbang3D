package com.nhuchhe.bangbang.gameObjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;
import com.nhuchhe.bangbang.screens.GameScreen;

public class Terrain extends BaseGameObject {

    public Terrain(String id, String terrainName) {
        super(id);
        Model model = BangBang.assetManager.assetManager.get(terrainName, Model.class);
        super.init(
                model,
                BangBang.collisionObjectHelper.getTerrainConstructionInfo(model)
        );
        rigidBody.setRestitution(1f);
        rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        GameScreen.gameObjectManger.terrain = this;
    }

    @Override
    public boolean isVisible(Camera cam) {
        return true;
    }
}