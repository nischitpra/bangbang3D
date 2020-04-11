package com.nhuchhe.bangbang;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;

import java.util.ArrayList;

public class AssetManagerHelper {
    /**
     * Remember:
     * need to check mtl files for texture and material. this is defined in the .obj file
     * bullet does not support scaling well. So never use scaling, instead edit the object to right size
     */
    public AssetManager assetManager = new AssetManager();

    private final String[] assets = {
            Constants.AssetNames.PLAYER,
            Constants.AssetNames.ENEMY,
            Constants.AssetNames.BOMB,
            Constants.AssetNames.TERRAIN
    };

    public void loadResources() {
        for (int i = assets.length - 1; i >= 0; i--) {
            assetManager.load(assets[i], Model.class);
        }
        assetManager.finishLoading(); // blocking until all assets are loaded
        populateResources();
    }

    private void populateResources() {
        ArrayList<GameObject> gameObjects = new ArrayList();
        for (int i = assets.length - 1; i >= 0; i--) {
            GameObject object = null;
            switch (assets[i]) {
                case Constants.AssetNames.PLAYER:
                    object = new Player(assets[i], assetManager.get(assets[i], Model.class));
                    object.instance.transform.setTranslation(0f, 10f, 0f);
                    object.createRigidBody(CollisionObjectHelper.getPlayerRigidBodyConstructionInfo());
                    object.rigidBody.setActivationState(4); //  4 is DISABLE_DEACTIVATION i.e rigidbodies sleep after sometime
                    Player player = (Player) object;
                    BangBang.gameObjectManger.player = player;
                    BangBang.inputControllerManager.setPlayer(player);
                    break;
                case Constants.AssetNames.ENEMY: //enemy ball
                    object = new Enemy(assets[i], assetManager.get(assets[i], Model.class));
                    object.instance.transform.setTranslation(0f, 10f, 2f);
                    object.createRigidBody(CollisionObjectHelper.getPlayerRigidBodyConstructionInfo());
                    BangBang.gameObjectManger.enemies.add(object);
                    break;
                case Constants.AssetNames.TERRAIN:
                    object = new GameObject(assets[i], assetManager.get(assets[i], Model.class));
                    object.createRigidBody(new btRigidBodyConstructionInfo(0, null, CollisionObjectHelper.getCompoundShape(object.model, true), new Vector3()));
                    object.rigidBody.setCollisionFlags(object.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
                    object.rigidBody.setFriction(2);
                    object.FORCE_VISIBLE = true;
                    break;
                case Constants.AssetNames.BOMB:
                    BombManager.name = assets[i];
                    continue; // don't add bomb to the render or anything
            }
            gameObjects.add(object);
            BangBang.world.addRigidBody(object.rigidBody);
        }
        BangBang.gameObjectManger.gameObjects = gameObjects;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
