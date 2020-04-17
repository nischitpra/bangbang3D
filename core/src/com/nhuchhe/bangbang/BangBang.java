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
import com.nhuchhe.bangbang.animator.Animator;
import com.nhuchhe.bangbang.gameObjects.Bomb;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;
import com.nhuchhe.bangbang.helper.CollisionObjectHelper;
import com.nhuchhe.bangbang.helper.CollisionShapeHelper;
import com.nhuchhe.bangbang.helper.RigidBodyHelper;
import com.nhuchhe.bangbang.manager.AssetManager;
import com.nhuchhe.bangbang.manager.BombManager;
import com.nhuchhe.bangbang.manager.GameObjectManger;
import com.nhuchhe.bangbang.manager.InputControllerManager;

import java.util.ArrayList;

public class BangBang extends ApplicationAdapter {
    /**
     * using fbx converter:  LD_LIBRARY_PATH=. ./fbx-conv -f ../../blender/explosion_anim.fbx
     */
    /**
     * todo:
     * make two types of object: 1 for rendering an 2 for collision.
     */

    //libgdx
    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public static ModelBatch modelBatch;

    //bullet
    public static btDiscreteDynamicsWorld world;
    public static DebugDrawer debugDrawer;
    public btDefaultCollisionConfiguration collisionConfig;
    public btCollisionDispatcher dispatcher;
    public btDbvtBroadphase broadphase;
    public btSequentialImpulseConstraintSolver constraintSolver;

    //bangbang
    public static GameObjectManger gameObjectManger = new GameObjectManger();
    public static AssetManager assetManager = new AssetManager();
    public static InputControllerManager inputControllerManager = new InputControllerManager();
    public static CollisionShapeHelper collisionShapeHelper = new CollisionShapeHelper();
    public static CollisionObjectHelper collisionObjectHelper = new CollisionObjectHelper();
    public static RigidBodyHelper rigidBodyHelper = new RigidBodyHelper();
    public static BombManager bombManager = new BombManager();
    public static Animator animator = new Animator();


    public static long currentMillis; // This is the global clock for game. Always use this time.


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
        cam.position.set(2f, 10f, 2f);
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
        modelBatch = new ModelBatch();
        assetManager.loadResources(); //block until all resources are loaded
        inputControllerManager.init();
        initGame();
    }

    private void clearViewPort() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }


    private void update() {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());// 30 fps for render
        world.stepSimulation(delta, 5, 1f / 60f); // 60 fps for update

        inputControllerManager.update();

        gameObjectManger.player.update();

        Queue<Bomb> bombs = BombManager.usedBombQ;
        for (Bomb bomb : bombs) {
            bomb.update();
        }
//        Logger.log("fps: " + Gdx.graphics.getFramesPerSecond());
    }

    private void draw() {
        clearViewPort();
        modelBatch.begin(cam);
        ArrayList<BaseGameObject> baseGameObjects = gameObjectManger.renderList;
        for (int i = baseGameObjects.size() - 1; i >= 0; i--) {
            BaseGameObject baseGameObject = baseGameObjects.get(i);
            if (baseGameObject.isVisible(cam)) {
                modelBatch.render(baseGameObject.instance, environment);
            }
        }
        animator.render();
        modelBatch.end();
    }

    private void updateCamera() {
        Vector3 playerPosition = gameObjectManger.player.getPosition();
        cam.position.set(playerPosition.x + 2, playerPosition.y + 5, playerPosition.z + 2);
        cam.lookAt(playerPosition.x, playerPosition.y, playerPosition.z);
        cam.update();
        camController.update();
    }

    @Override
    public void render() {
        currentMillis = System.currentTimeMillis();
//        debugDrawer.begin(cam);
        update();
        updateCamera();
// todo: refactor this to function.
        draw();
//        world.debugDrawWorld();
//        debugDrawer.end();

        //cleanup after everything
        bombManager.cleanup();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        gameObjectManger.dispose();
        assetManager.dispose();

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
