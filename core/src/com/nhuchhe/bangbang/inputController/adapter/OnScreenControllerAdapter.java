package com.nhuchhe.bangbang.inputController.adapter;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.inputController.base.BaseControllerListener;
import com.nhuchhe.bangbang.inputController.base.BaseInputController;

public class OnScreenControllerAdapter extends BaseControllerListener {


    public OnScreenControllerAdapter(BaseInputController manager) {
        super(manager);
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        switch (buttonCode) {
            case 97:
                manager.majorAttackDown();
                return true;
            case 99:
                manager.minorAttackDown();
                return true;
            case 96:
            case 100:
                break;
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        switch (buttonCode) {
            case 97:
                manager.majorAttackUp();
                return true;
            case 99:
                manager.minorAttackUp();
                return true;
            case 96:
            case 100:
                break;
        }
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        switch (axisCode) {
            case 0://x
                if (Math.abs(value) > 0.1) isDownX = value;
                else isDownX = 0;
                return true;
            case 1://y
                if (Math.abs(value) > 0.1) isDownY = value;
                else isDownY = 0;
                return true;
            case 5://LT ( left top )
                lt = value;
                break;
        }
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
