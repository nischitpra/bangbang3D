package com.nhuchhe.bangbang.manager;

import com.nhuchhe.bangbang.gameObjects.Player;
import com.nhuchhe.bangbang.gameObjects.Terrain;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GameObjectManger {
    public Player player;
    public Terrain terrain;
    public ArrayList<BaseGameObject> gameObjects = new ArrayList<>();
    public ArrayList<BaseGameObject> enemies = new ArrayList<>();
    public HashMap<String, BaseGameObject> gameObjectMap = new HashMap<>();

    public ArrayList<BaseGameObject> renderList = new ArrayList<>();

    public void dispose() {
        for (int i = renderList.size() - 1; i >= 0; i--) {
            renderList.get(i).dispose();
        }
        renderList = null;
        gameObjects = null;
        enemies = null;
        gameObjectMap = null;
    }
}
