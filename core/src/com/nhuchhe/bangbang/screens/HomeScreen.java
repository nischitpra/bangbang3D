package com.nhuchhe.bangbang.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nhuchhe.bangbang.screens.base.BaseScreen;
import com.nhuchhe.bangbang.screens.stage.HomeStage;
import com.nhuchhe.bangbang.utilities.Constants;

public class HomeScreen extends BaseScreen {

    private OrthographicCamera camera;

    public HomeScreen() {
        stage = new HomeStage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
    }

}
