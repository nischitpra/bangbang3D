package com.nhuchhe.bangbang;

import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

public class Utilities {
    public static HashMap<String, String> getUserData(Object userData) {
        return (HashMap<String, String>) userData;
    }

    public static String getGameObjectMapKey(String assetName, int id) {
        return assetName + "_" + id;
    }

    public static float getVelocityMagnitude(Vector3 velocity) {
        return (float) Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
    }
}
