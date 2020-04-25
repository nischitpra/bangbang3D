package com.nhuchhe.bangbang.helper;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.AoeDetectionGameObject;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;
import com.nhuchhe.bangbang.gameObjects.base.MotionState;
import com.nhuchhe.bangbang.screens.GameScreen;
import com.nhuchhe.bangbang.utilities.Constants;

import java.util.HashMap;

public class RigidBodyHelper {
    public void createRigidBody(final BaseGameObject obj, final btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        obj.motionState = new MotionState();
        obj.motionState.transform = obj.instance.transform;
        obj.constructionInfo = constructionInfo;
        obj.rigidBody = new btRigidBody(constructionInfo);
        obj.rigidBody.setMotionState(obj.motionState); // set callback for transformation
        obj.rigidBody.userData = new HashMap<String, String>() {{
            put(Constants.UserData.ID, obj.id);
        }};
        GameScreen.world.addRigidBody(obj.rigidBody);
        GameScreen.gameObjectManger.gameObjectMap.put(obj.id, obj);
        GameScreen.gameObjectManger.renderList.add(obj);
    }

    public void createGhostObject(final AoeDetectionGameObject obj, final btCollisionShape collisionShape) {
        obj.aoe = new btGhostObject();
        obj.aoe.setCollisionShape(collisionShape);
        obj.aoe.setCollisionFlags(obj.aoe.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
        obj.aoe.setIgnoreCollisionCheck(GameScreen.gameObjectManger.terrain.rigidBody, true);
        obj.aoe.userData = new HashMap<String, String>() {{
            put(Constants.UserData.ID, obj.id);
        }};
        GameScreen.world.addCollisionObject(obj.aoe);
    }
}