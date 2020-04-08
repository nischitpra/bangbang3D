package com.nhuchhe.bangbang;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

public class GamepadControllerAdapter implements ControllerListener {

    public float isDownX = 0f;
    public float isDownY = 0f;

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
                isDownX = 1;
                break;
            case 99:
                isDownX = -1;
                break;
            case 96:
                isDownY = 1;
                break;
            case 100:
                isDownY = -1;
                break;
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        switch (buttonCode) {
            case 97:
            case 99:
                isDownX = 0;
                break;
            case 96:
            case 100:
                isDownY = 0;
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
                break;
            case 1://y
                if (Math.abs(value) > 0.1) isDownY = value;
                else isDownY = 0;
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
