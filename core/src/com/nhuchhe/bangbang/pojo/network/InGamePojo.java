package com.nhuchhe.bangbang.pojo.network;

import java.io.Serializable;

public class InGamePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;
    public float inputX;
    public float inputY;
    public float lt;
    public boolean majorAttackDown;
    public boolean majorAttackUp;
    public boolean minorAttackDown;
    public boolean minorAttackUp;

    @Override
    public String toString() {
        return String.format("id: %d, inputX: %f, inputY: %f, lt: %f", id, inputX, inputY, lt);
    }
}
