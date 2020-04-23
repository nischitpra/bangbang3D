package com.nhuchhe.bangbang.screens.core;

import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class BaseScreen {
    protected Stage stage;

    public void render() {
        stage.draw();
    }
}
