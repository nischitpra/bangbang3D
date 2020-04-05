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
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;

import java.util.ArrayList;

public class BangBang extends ApplicationAdapter {
    //libgdx
    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;

    //bullet
    public DebugDrawer debugDrawer;
    public btDiscreteDynamicsWorld world;
    public btDefaultCollisionConfiguration collisionConfig;
    public btCollisionDispatcher dispatcher;
    public btDbvtBroadphase broadphase;
    public btSequentialImpulseConstraintSolver constraintSolver;

    //bangbang
    public GameObjectManger gameObjectManger = new GameObjectManger();
    public AssetManagerHelper assetManagerHelper = new AssetManagerHelper(this);

    private void initBullet() {
        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
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
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        // camera input controller
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

    }

    @Override
    public void create() {
        initBullet();
        initEnvironment();
        initCamera();
        modelBatch = new ModelBatch();
        assetManagerHelper.loadResources(); //block until all resources are loaded
    }

    private void clearViewPort() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }


    private void update() {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());// 30 fps
        world.stepSimulation(delta, 5, 1f / 60f);

        ArrayList<GameObject> gameObjects = gameObjectManger.gameObjects;
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.rigidBody.getWorldTransform(gameObject.instance.transform); //update instance with rigidBody physics
        }
    }

    private Vector3 tempRenderPosition = new Vector3();//temp position store to prevent object creation

    private void draw() {
        clearViewPort();
        modelBatch.begin(cam);
        ArrayList<GameObject> gameObjects = gameObjectManger.gameObjects;
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject gameObject = gameObjects.get(i);
            if (gameObject.isVisible(cam, gameObject.instance.transform.getTranslation(tempRenderPosition))) {
                modelBatch.render(gameObject.instance, environment);
            }
        }
        modelBatch.end();
    }

    @Override
    public void render() {
        debugDrawer.begin(cam);
        update();
        camController.update();
        draw();
        debugDrawer.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        gameObjectManger.dispose();
        assetManagerHelper.dispose();
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
