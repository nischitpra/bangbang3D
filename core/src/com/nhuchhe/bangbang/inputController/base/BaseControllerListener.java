package com.nhuchhe.bangbang.inputController.base;

import com.badlogic.gdx.controllers.ControllerListener;

public abstract class BaseControllerListener implements ControllerListener {
    public BaseInputController manager;

    public long lastUpdate;

    public float isDownX = 0f;
    public float isDownY = 0f;
    public float lt = 0f;

    public BaseControllerListener(BaseInputController manager) {
        this.manager = manager;
    }

}
