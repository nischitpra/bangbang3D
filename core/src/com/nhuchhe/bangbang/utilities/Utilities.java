package com.nhuchhe.bangbang.utilities;

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

    public static String createGameObjectId(String assetName, int id) {
        return String.format(assetName, id);
    }

    public static Vector3 addVector(Vector3 addTo, Vector3 delta) {
        addTo.x += delta.x;
        addTo.y += delta.y;
        addTo.z += delta.z;
        return addTo;
    }
}