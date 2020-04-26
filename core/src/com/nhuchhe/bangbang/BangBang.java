package com.nhuchhe.bangbang;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.animator.Animator;
import com.nhuchhe.bangbang.helper.ActorHelper;
import com.nhuchhe.bangbang.helper.CollisionObjectHelper;
import com.nhuchhe.bangbang.helper.CollisionShapeHelper;
import com.nhuchhe.bangbang.helper.RigidBodyHelper;
import com.nhuchhe.bangbang.inputController.InputControllerManager;
import com.nhuchhe.bangbang.manager.AssetManager;
import com.nhuchhe.bangbang.network.Network;
import com.nhuchhe.bangbang.screens.GameScreen;
import com.nhuchhe.bangbang.screens.base.BaseScreen;
import com.nhuchhe.bangbang.utilities.Constants;

public class BangBang extends ApplicationAdapter {
    /**
     * using fbx converter:  LD_LIBRARY_PATH=. ./fbx-conv -f ../../blender/explosion_anim.fbx
     * todo:
     * <p>
     * ALWAYS USE {@link com.nhuchhe.bangbang.utilities.Utilities#copyValueTo(Vector3, Vector3)}
     * <p>
     * Need to make ScreenManager to change screens and dispose previous screen. Also show splash screen inbetween transision.
     */

    //libgdx
    public static ModelBatch modelBatch;
    public static SpriteBatch spriteBatch;

    //bangbang
    public static AssetManager assetManager = new AssetManager();
    public static CollisionShapeHelper collisionShapeHelper = new CollisionShapeHelper();
    public static CollisionObjectHelper collisionObjectHelper = new CollisionObjectHelper();
    public static RigidBodyHelper rigidBodyHelper = new RigidBodyHelper();
    public static Animator animator = new Animator();
    public static InputControllerManager inputControllerManager = new InputControllerManager();
    public static Network network = new Network();
    public static ActorHelper actorHelper = new ActorHelper();

    public static BaseScreen currentScreen;
    public static String LOBBY_NAME = "";
    public static int PLAYER_ID = -1;
    public static int[] PLAYER_IDS = new int[]{};

    public static long currentMillis; // This is the global clock for game. Always use this time.

    private void initFirst() {
        Constants.CAMERA_WIDTH = Gdx.graphics.getWidth();
        Constants.CAMERA_HEIGHT = Gdx.graphics.getHeight();
    }

    private void initGame() {
        network.init();
//        currentScreen = new HomeScreen();
        currentScreen = new GameScreen();
    }

    @Override
    public void create() {
        initFirst();
        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();
        assetManager.loadResources(); //block until all resources are loaded
        initGame();
    }

    private void clearViewPort() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void render() {
        currentMillis = System.currentTimeMillis();
        clearViewPort();
        currentScreen.render();
    }

    @Override
    public void dispose() {
        network.disconnect();
        modelBatch.dispose();
//        assetManager.dispose();
        currentScreen.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {
    }
}
