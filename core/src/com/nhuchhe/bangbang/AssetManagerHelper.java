package com.nhuchhe.bangbang;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;

import java.util.ArrayList;

public class AssetManagerHelper {
    /**
     * Remember:
     * need to check mtl files for texture and material. this is defined in the .obj file
     * bullet does not support scaling well. So never use scaling, instead edit the object to right size
     */
    public AssetManager assetManager = new AssetManager();

    private final BangBang appContext;
    private final String[] assets = {
            "ball/ball.obj",
            "ball2/ball.obj",
            "bomb/ball.obj",
            "terrain/terrain test3.obj"
    };

    AssetManagerHelper(final BangBang appContext) {
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
        for (int i = assets.length - 1; i >= 0; i--) {
            GameObject object = null;
            switch (assets[i]) {
                case "ball/ball.obj":
                    object = new Player(assets[i], assetManager.get(assets[i], Model.class));
                    object.instance.transform.setTranslation(0f, 10f, 0f);
                    object.createRigidBody(new btRigidBodyConstructionInfo(10, null, new btSphereShape(1), new Vector3()));
                    object.rigidBody.setActivationState(4); //  4 is DISABLE_DEACTIVATION i.e rigidbodies sleep after sometime
                    Player player = (Player) object;
                    appContext.gameObjectManger.player = player;
                    appContext.inputControllerManager.setPlayer(player);
                    break;
                case "ball2/ball.obj": //enemy ball
                    object = new Enemy(assets[i], assetManager.get(assets[i], Model.class));
                    object.instance.transform.setTranslation(0f, 10f, 2f);
                    object.createRigidBody(new btRigidBodyConstructionInfo(10, null, new btSphereShape(1), new Vector3()));
                    appContext.gameObjectManger.enemies.add(object);
                    break;
                case "terrain/terrain test3.obj":
                    object = new GameObject(assets[i], assetManager.get(assets[i], Model.class));
                    object.createRigidBody(new btRigidBodyConstructionInfo(0, null, CollisionObjectHelper.getCompoundShape(object.model, true), new Vector3()));
                    object.rigidBody.setCollisionFlags(object.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
                    object.rigidBody.setFriction(2);
                    object.FORCE_VISIBLE = true;
                    break;
                case "bomb/ball.obj":
                    BombManager.name = assets[i];
                    BombManager.model = assetManager.get(assets[i], Model.class);
                    BombManager.constructionInfo=new btRigidBodyConstructionInfo(10, null, new btSphereShape(1), new Vector3());
                    continue; // don't add bomb to the render or anything
            }
            gameObjects.add(object);
            appContext.world.addRigidBody(object.rigidBody);
        }
        BangBang.gameObjectManger.gameObjects = gameObjects;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
