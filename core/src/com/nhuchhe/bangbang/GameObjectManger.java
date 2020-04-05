package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.HashMap;

public class GameObjectManger {
    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    public HashMap<String, GameObject> gameObjectMap = new HashMap<>();
    public HashMap<String, ModelInstance> instanceMap = new HashMap<>(); // todo: check if you need this

    public void dispose() {
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            gameObjects.get(i).dispose();
            gameObjectMap = null;
            instanceMap = null;
        }
    }

}
