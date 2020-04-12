package com.nhuchhe.bangbang;

import java.util.ArrayList;
import java.util.HashMap;

public class GameObjectManger {
    public Player player;
    public GameObject terrain;
    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    public ArrayList<GameObject> enemies = new ArrayList<>();
    public HashMap<String, GameObject> gameObjectMap = new HashMap<>();

    public void dispose() {
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            gameObjects.get(i).dispose();
        }
    }

}
