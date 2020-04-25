package com.nhuchhe.bangbang.screens.base;

import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class BaseScreen {
    protected Stage stage;

    public void render() {
        stage.draw();
    }

    public void dispose() {

    }
}
