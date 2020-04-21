package com.nhuchhe.bangbang.manager;

import com.badlogic.gdx.graphics.g3d.Model;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Logger;
import com.nhuchhe.bangbang.utilities.Utilities;
import com.nhuchhe.bangbang.gameObjects.Enemy;
import com.nhuchhe.bangbang.gameObjects.Player;
import com.nhuchhe.bangbang.gameObjects.Terrain;

public class AssetManager {
    int enemyCount = 3; // get from option
    int selectedTerrainIndex = 1; // get from option
    /**
     * Remember:
     * need to check mtl files for texture and material. this is defined in the .obj file
     * bullet does not support scaling well. So never use scaling, instead edit the object to right size
     */
    public com.badlogic.gdx.assets.AssetManager assetManager = new com.badlogic.gdx.assets.AssetManager();

    private void loadModelBomb() {
        for (int i = Constants.Bombs.length - 1; i >= 0; i--) {
            Logger.log(Constants.Bombs[i]);
            assetManager.load(Constants.Bombs[i], Model.class);
        }
    }

    private void loadModelTerrain() {
        for (int i = Constants.Terrains.length - 1; i >= 0; i--) {
            Logger.log(Constants.Terrains[i]);
            assetManager.load(Constants.Terrains[i], Model.class);
        }
    }

    public void loadResources() {
        for (int i = Constants.Assets.length - 1; i >= 0; i--) {
            String asset = Constants.Assets[i];
            if (asset == Constants.AssetNames.TERRAIN) {
                loadModelTerrain();
            } else if (asset == Constants.AssetNames.BOMB) {
                loadModelBomb();
            } else {
                Logger.log(asset);
                assetManager.load(asset, Model.class);
            }
        }
        assetManager.finishLoading(); // blocking until all assets are loaded
        populateResources();
    }

    private void initEnemy() {
        for (int i = enemyCount - 1; i >= 0; i--) {
            new Enemy(Utilities.createGameObjectId(Constants.GameObjectId.ENEMY, i));
        }
    }

    public void populateResources() {
        for (int i = Constants.Assets.length - 1; i >= 0; i--) {
            switch (Constants.Assets[i]) {
                case Constants.AssetNames.PLAYER:
                    new Player(Constants.GameObjectId.PLAYER);
                    break;
                case Constants.AssetNames.ENEMY:
                    initEnemy();
                    break;
                case Constants.AssetNames.BOMB:
                    // maybe create a few bombs
                    // BombManager will do this.. not needed here.
                    continue;
                case Constants.AssetNames.TERRAIN:
                    new Terrain(Utilities.createGameObjectId(Constants.GameObjectId.TERRAIN, selectedTerrainIndex), Constants.Terrains[selectedTerrainIndex]);
                    break;
            }
        }
    }

    public void dispose() {
        assetManager.dispose();
    }
}
