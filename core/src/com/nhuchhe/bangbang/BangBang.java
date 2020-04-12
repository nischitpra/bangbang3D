package com.nhuchhe.bangbang;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
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
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;

public class BangBang extends ApplicationAdapter {
    /**
     * todo:
     * - clean up code
     * - create helper method to create rigid body.
     * - set id in every rigidBody.userData. It will be used to get the gameObject.
     * - user gameObjectManager.gameObjectMap to get objects from id.
     * - create external force and movement force as extra variables in gameObject so that we can limit the movement force without limiting external force.
     * <p>
     * major problems
     * - need to fix movement and external force.. maybe create another object MovableGameObject : GameObject ( this will have external force and movement force as separate values )
     * all the force to be applied is ( external + movement )
     * - need to structure code and its flow. Need to separate and modularize code for AssetManagerHelper.
     */
    //libgdx
    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;

    //bullet
    public static btDiscreteDynamicsWorld world;
    public static DebugDrawer debugDrawer;
    public btDefaultCollisionConfiguration collisionConfig;
    public btCollisionDispatcher dispatcher;
    public btDbvtBroadphase broadphase;
    public btSequentialImpulseConstraintSolver constraintSolver;

    //bangbang
    public static GameObjectManger gameObjectManger = new GameObjectManger();
    public static AssetManagerHelper assetManagerHelper = new AssetManagerHelper();
    public static InputControllerManager inputControllerManager = new InputControllerManager();

    private void initBullet() {
        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        broadphase.getOverlappingPairCache().setInternalGhostPairCallback(new btGhostPairCallback());
        constraintSolver = new btSequentialImpulseConstraintSolver();
        world = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        world.setGravity(new Vector3(0, -10f, 0));

        //for displaying wireframe
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        world.setDebugDrawer(debugDrawer);
    }

    private void initEnvironment() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    private void initCamera() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(2f, 2f, 2f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        // camera input controller
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    }

    private void initGame() {
    }

    @Override
    public void create() {
        initBullet();
        initEnvironment();
        initCamera();
        initGame();
        modelBatch = new ModelBatch();
        assetManagerHelper.loadResources(); //block until all resources are loaded
        inputControllerManager.init();
    }

    private void clearViewPort() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }


    private void update() {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());// 30 fps for render
        world.stepSimulation(delta, 5, 1f / 60f); // 60 fps for update

        inputControllerManager.update();

        gameObjectManger.player.update();
        BombManager.cleanup();

//        Logger.log("fps: " + Gdx.graphics.getFramesPerSecond());
    }

    private void draw() {
        clearViewPort();
        modelBatch.begin(cam);
        ArrayList<GameObject> gameObjects = gameObjectManger.gameObjects;
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject gameObject = gameObjects.get(i);
            if (gameObject.isVisible(cam)) {
                modelBatch.render(gameObject.instance, environment);
            }
        }
        Queue<Bomb> bombs = BombManager.usedBombQ;
        for (Bomb bomb : bombs) {
            bomb.update();
            if (bomb.isVisible(cam)) {
                modelBatch.render(bomb.instance, environment);
            }
        }
        modelBatch.end();
    }

    @Override
    public void render() {
//        debugDrawer.begin(cam);
        update();
//        world.debugDrawWorld();
// todo: refactor this to function.
        Vector3 playerPosition = gameObjectManger.player.getPosition();
        cam.position.set(playerPosition.x + 2, playerPosition.y + 2, playerPosition.z + 2);
        cam.update();
        camController.update();
        draw();
//        debugDrawer.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        gameObjectManger.dispose();
        assetManagerHelper.dispose();

        collisionConfig.dispose();
        dispatcher.dispose();
        broadphase.dispose();
        constraintSolver.dispose();
        debugDrawer.dispose();
        world.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
