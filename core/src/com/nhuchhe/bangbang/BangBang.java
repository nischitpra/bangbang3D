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

import java.util.ArrayList;

public class BangBang extends ApplicationAdapter {
    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;

    public GameObjectManger gameObjectManger = new GameObjectManger();
    public AssetManagerHelper assetManagerHelper = new AssetManagerHelper(this);

    private void initEnvironment() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    private void initCamera() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(1f, 1f, 1f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
// make camera interactable by touch
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void create() {
        Bullet.init();
        initEnvironment();
        initCamera();
        modelBatch = new ModelBatch();
        assetManagerHelper.loadResources(); //block until all resources are loaded
    }

    private void clearViewPort() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }


    private Vector3 tempRenderPosition = new Vector3();//temp position store to prevent object creation

    @Override
    public void render() {
        clearViewPort();
        camController.update(); // update changes done to camera such as pan, zoom etc

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
