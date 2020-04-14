package com.nhuchhe.bangbang.gameObjects.base;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.nhuchhe.bangbang.BangBang;

public class AoeDetectionGameObject extends BaseGameObject {

    private btCollisionShape aoeShape;
    public btGhostObject aoe; // area of effect

    public AoeDetectionGameObject(String id, btCollisionShape aoeShape) {
        super(id);
        this.aoeShape = aoeShape;
    }

    @Override
    public void init(Model model, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super.init(model, constructionInfo);
        BangBang.rigidBodyHelper.createGhostObject(this, aoeShape);
    }

}
