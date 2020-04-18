package com.nhuchhe.bangbang.utilities;

import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

public class Utilities {

    private static Vector3 tempVector = new Vector3();

    public static HashMap<String, String> getUserData(Object userData) {
        return (HashMap<String, String>) userData;
    }

    public static String getGameObjectMapKey(String assetName, int id) {
        return String.format(assetName, id);
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

    /**
     * Calculates magnitude of Vector(x,z) excludes y
     */
    public static float getVelocityMagnitude(Vector3 velocity) {
        return (float) Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
    }

    /**
     * calculates magnitude of Vector(x,y,z)
     */
    public static float magnitude(Vector3 vector3) {
        return (float) Math.sqrt(vector3.x * vector3.x + vector3.y * vector3.y + vector3.z * vector3.z);
    }

    public static Vector3 normalize(Vector3 vector3) {
        float mag = magnitude(vector3);
        vector3.x /= mag;
        vector3.y /= mag;
        vector3.z /= mag;
        return vector3;
    }

    public static Vector3 getNormalizedDirection(final Vector3 from, final Vector3 to) {
        tempVector.x = to.x - from.x;
        tempVector.y = to.y - from.y;
        tempVector.z = to.z - from.z;
        return Utilities.normalize(tempVector);
    }

    public static Vector3 getNormalizedProximity(final Vector3 from, final Vector3 to) {
        return getNormalizedDirection(from, to);
    }

}
