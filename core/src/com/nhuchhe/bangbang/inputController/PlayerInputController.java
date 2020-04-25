package com.nhuchhe.bangbang.inputController;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.nhuchhe.bangbang.inputController.adapter.OnScreenControllerAdapter;
import com.nhuchhe.bangbang.inputController.adapter.XBoxControllerAdapter;
import com.nhuchhe.bangbang.inputController.base.BaseInputController;
import com.nhuchhe.bangbang.screens.GameScreen;
import com.nhuchhe.bangbang.screens.stage.OnScreenControllerStage;
import com.nhuchhe.bangbang.utilities.Logger;

public class PlayerInputController extends BaseInputController {
    public static Controller controller;

    public void init() {
        try {
            controller = Controllers.getControllers().get(0);
            if (controller.getName().equals("Xbox Wireless Controller")) {
                controllerAdapter = new XBoxControllerAdapter(this);
            }
            controller.addListener(this.controllerAdapter);
        } catch (Exception e) {
            OnScreenControllerAdapter adapter = new OnScreenControllerAdapter(this);
            GameScreen.stage = new OnScreenControllerStage(adapter);
            controllerAdapter = adapter;
            Logger.log("OnScreen Controller set");
        }
    }

}
