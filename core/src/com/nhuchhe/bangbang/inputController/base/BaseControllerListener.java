package com.nhuchhe.bangbang.inputController.base;

import com.badlogic.gdx.controllers.ControllerListener;
import com.nhuchhe.bangbang.inputController.InputControllerManager;

public abstract class BaseControllerListener implements ControllerListener {
    public InputControllerManager manager;

    public float isDownX = 0f;
    public float isDownY = 0f;
    public float lt = 0f;

    public BaseControllerListener(InputControllerManager manager) {
        this.manager = manager;
    }

}
