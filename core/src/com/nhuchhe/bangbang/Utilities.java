package com.nhuchhe.bangbang;

import java.util.HashMap;

public class Utilities {
    public static HashMap<String, String> getUserData(Object userData) {
        return (HashMap<String, String>) userData;
    }

    public static String getGameObjectMapKey(String assetName, int id) {
        return assetName + "_" + id;
    }
}
