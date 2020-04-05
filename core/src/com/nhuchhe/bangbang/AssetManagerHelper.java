package com.nhuchhe.bangbang;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetManagerHelper {
    /**
     * Remember:
     * need to check mtl files for texture and material. this is defined in the .obj file
     * bullet does not support scaling well. So never use scaling, instead edit the object to right size
     */
    public AssetManager assetManager = new AssetManager();

    private BangBang appContext;
    private String[] assets = {
            "ball/ball.obj",
            "table/table.obj"
    };

    AssetManagerHelper(BangBang appContext) {
        this.appContext = appContext;
    }

    public void loadResources() {
        for (int i = assets.length - 1; i >= 0; i--) {
            assetManager.load(assets[i], Model.class);
        }
        assetManager.finishLoading(); // blocking until all assets are loaded
        populateResources();
    }

    private void populateResources() {
        ArrayList<GameObject> gameObjects = new ArrayList();
        HashMap<String, GameObject> gameObjectMap = new HashMap<>();
        HashMap<String, ModelInstance> instanceMap = new HashMap<>();
        for (int i = assets.length - 1; i >= 0; i--) {
            GameObject object = new GameObject(assets[i], assetManager.get(assets[i], Model.class));
            switch (assets[i]) {
                case "ball/ball.obj":
                    object.instance.transform.setTranslation(0, 10f, 0f);
                    object.createRigidBody(new btRigidBodyConstructionInfo(10, null, new btSphereShape(1), new Vector3()));
                    break;
                case "table/table.obj":
//                    object.instance.transform.setToScaling(0.1f, 0.1f, 0.1f); // todo:
                    object.createRigidBody(new btRigidBodyConstructionInfo(0, null, new btBoxShape(new Vector3(20, 5, 20)), new Vector3()));
                    object.rigidBody.setCollisionFlags(object.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
                    break;
            }
            gameObjects.add(object);
            gameObjectMap.put(assets[i] + i, object);
            instanceMap.put(assets[i] + i, object.instance);
            appContext.world.addRigidBody(object.rigidBody);
        }
        appContext.gameObjectManger.gameObjects = gameObjects;
        appContext.gameObjectManger.gameObjectMap = gameObjectMap;
        appContext.gameObjectManger.instanceMap = instanceMap;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
