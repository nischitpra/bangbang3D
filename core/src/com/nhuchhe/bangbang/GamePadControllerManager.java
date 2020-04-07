package com.nhuchhe.bangbang;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class GamePadControllerManager {
    private final BangBang appContext;

    public GamePadControllerManager(final BangBang appContext) {
        this.appContext = appContext;
    }

    public void init() {
        for (Controller controller : Controllers.getControllers()) {
            System.out.println("NISCHIT::controller: " + controller.getName());
        }
    }
}
