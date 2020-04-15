package com.nhuchhe.bangbang.gameObjects.base;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;

public class PlayableGameObject extends BaseGameObject {
    private final float friction = 3f;
    private final float linearDampening = 0.3f;

    private Vector3 spawnPosition = new Vector3(0, 10, 1);

    public PlayableGameObject(String id, String assetName) {
        super(id);
        super.init(
                BangBang.assetManager.assetManager.get(assetName, Model.class),
                BangBang.collisionObjectHelper.getPlayerConstructionInfo()
        );
        rigidBody.setActivationState(4);
        rigidBody.translate(spawnPosition);
        rigidBody.setFriction(friction);
        rigidBody.setDamping(linearDampening, rigidBody.getAngularDamping());
        rigidBody.setRestitution(0.25f);
    }
}
