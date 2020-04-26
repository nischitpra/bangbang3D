package com.nhuchhe.bangbang.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btGhostPairCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;
import com.nhuchhe.bangbang.manager.BombManager;
import com.nhuchhe.bangbang.manager.GameObjectManger;
import com.nhuchhe.bangbang.screens.base.BaseScreen;
import com.nhuchhe.bangbang.utilities.Constants;

import java.util.ArrayList;

public class GameScreen extends BaseScreen {
    //libgdx
    public static Environment environment;
    public static PerspectiveCamera cam;

    //bullet
    public static btDiscreteDynamicsWorld world;
    public static DebugDrawer debugDrawer;
    public btDefaultCollisionConfiguration collisionConfig;
    public btCollisionDispatcher dispatcher;
    public btDbvtBroadphase broadphase;
    public btSequentialImpulseConstraintSolver constraintSolver;

    //bangbang game
    public static GameObjectManger gameObjectManger = new GameObjectManger();
    public static BombManager bombManager = new BombManager();

    public GameScreen() {
        init();
    }

    private void initBullet() {
        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        broadphase.getOverlappingPairCache().setInternalGhostPairCallback(new btGhostPairCallback());
        constraintSolver = new btSequentialImpulseConstraintSolver();
        GameScreen.world = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        GameScreen.world.setGravity(new Vector3(0, -10f, 0));

        //for displaying wireframe
        GameScreen.debugDrawer = new DebugDrawer();
        GameScreen.debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        GameScreen.world.setDebugDrawer(GameScreen.debugDrawer);
    }

    private void initEnvironment() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    private void initCamera() {
        cam = new PerspectiveCamera(67, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        cam.position.set(0f, 10f, 2f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
    }

    private void initResourceManger() {
        BangBang.assetManager.initGameScreen();
    }

    public void init() {
        initBullet();
        initEnvironment();
        initCamera();
        initResourceManger();
        BangBang.inputControllerManager.init();
        BangBang.network.upstreamPlayerMovementControl();
    }

    private void updateCamera() {
        Vector3 playerPosition = gameObjectManger.player.getPosition();
        cam.position.set(playerPosition.x, playerPosition.y + 5, playerPosition.z + 2);
        cam.lookAt(playerPosition.x, playerPosition.y, playerPosition.z);
        cam.update();
//        camController.update();
    }

    public void update() {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());// 30 fps for render
        GameScreen.world.stepSimulation(delta, 5, 1f / 60f); // 60 fps for update
//        Logger.log("fps: " + Gdx.graphics.getFramesPerSecond());

        BangBang.inputControllerManager.update();
    }

    private void draw() {
        BangBang.modelBatch.begin(cam);
        ArrayList<BaseGameObject> baseGameObjects = gameObjectManger.renderList;
        for (int i = baseGameObjects.size() - 1; i >= 0; i--) {
            BaseGameObject bgo = baseGameObjects.get(i);
            bgo.update();
            bgo.render();
        }
        BangBang.animator.render();
        BangBang.modelBatch.end();
    }

    @Override
    public void render() {
//        debugDrawer.begin(cam);
        update();
        updateCamera();
        draw();
//        world.debugDrawWorld();
//        debugDrawer.end();
        if (stage != null) super.render();
        //cleanup after everything
        bombManager.cleanup();
    }

    @Override
    public void dispose() {
        super.dispose();
        gameObjectManger.dispose();
        debugDrawer.dispose();
        world.dispose();
        collisionConfig.dispose();
        dispatcher.dispose();
        broadphase.dispose();
        constraintSolver.dispose();
    }

}
