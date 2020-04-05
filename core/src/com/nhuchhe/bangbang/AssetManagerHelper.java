package com.nhuchhe.bangbang;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetManagerHelper {
    // todo: need to check mtl files for texture and material. this is defined in the .obj file
    public AssetManager assetManager = new AssetManager();

    private BangBang appContext;
    private String[] assets = {"ball/ball.obj", "table/table.obj"};

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
                    object.createRigidBody(new btRigidBody.btRigidBodyConstructionInfo(1, null, new btSphereShape(1), new Vector3()));
                    break;
                case "table/table.obj":
                    object.createRigidBody(new btRigidBody.btRigidBodyConstructionInfo(10, null, new btBoxShape(new Vector3(5, 5, 5)), new Vector3()));
                    break;
            }
            gameObjects.add(object);
            gameObjectMap.put(assets[i], object);
            instanceMap.put(assets[i], object.instance);
            appContext.world.addRigidBody(object.rigidBody);
        }
        appContext.gameObjectManger.gameObjects = gameObjects;
        appContext.gameObjectManger.gameObjectMap = gameObjectMap;
        appContext.gameObjectManger.instanceMap = instanceMap;
    }

    public void dispose() {
        //todo: dispose everything here
    }
}
