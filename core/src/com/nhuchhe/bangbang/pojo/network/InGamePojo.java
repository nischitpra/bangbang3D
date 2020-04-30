package com.nhuchhe.bangbang.pojo.network;

import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;

public class InGamePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector3 position = Vector3.Zero;
    public float health;
    public int id;
    public float inputX;
    public float inputY;
    public float lt;
    public boolean majorAttackDown;
    public boolean majorAttackUp;
    public boolean minorAttackDown;
    public boolean minorAttackUp;
    public long timestamp;

    @Override
    public String toString() {
        return String.format("id: %d, inputX: %f, inputY: %f, lt: %f", id, inputX, inputY, lt);
    }
}
