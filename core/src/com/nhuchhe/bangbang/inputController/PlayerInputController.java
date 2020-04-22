package com.nhuchhe.bangbang.inputController;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.nhuchhe.bangbang.inputController.adapter.XBoxControllerAdapter;
import com.nhuchhe.bangbang.inputController.base.BaseControllerListener;
import com.nhuchhe.bangbang.inputController.base.BaseInputController;

public class PlayerInputController extends BaseInputController {
    public static Controller controller;
    private BaseControllerListener controllerAdapter;

    public void init() {
        try {
            controller = Controllers.getControllers().get(0);
            if (controller.getName().equals("Xbox Wireless Controller")) {
                controllerAdapter = new XBoxControllerAdapter(this);
            }
            controller.addListener(this.controllerAdapter);

        } catch (Exception e) {

        }
    }

}
