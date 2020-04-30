package com.nhuchhe.bangbang.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.Enemy;
import com.nhuchhe.bangbang.gameObjects.Player;
import com.nhuchhe.bangbang.gameObjects.Terrain;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Logger;
import com.nhuchhe.bangbang.utilities.Utilities;

import java.util.HashMap;

public class AssetManager {
    int enemyCount = 0; // get from option
    int selectedTerrainIndex = 2; // get from option
    /**
     * Remember:
     * need to check mtl files for texture and material. this is defined in the .obj file
     * bullet does not support scaling well. So never use scaling, instead edit the object to right size
     */
    public com.badlogic.gdx.assets.AssetManager assetManager = new com.badlogic.gdx.assets.AssetManager();

    public HashMap<String, Texture> imagesMap = new HashMap<>();

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

    private void loadImages() {
        for (int i = Constants.Images.length - 1; i >= 0; i--) {
            String[] imageDetails = Constants.Images[i];
            imagesMap.put(imageDetails[0], new Texture(Gdx.files.internal(imageDetails[1])));
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
        loadImages(); // load images and textures
    }

    private void initEnemy() {
        for (int i = BangBang.PLAYER_IDS.length - 1; i >= 0; i--) {
            int id = BangBang.PLAYER_IDS[i];
            if (id == BangBang.PLAYER_ID) continue;
            new Enemy(Utilities.createGameObjectId(Constants.GameObjectId.ENEMY, id));
        }
    }

    /**
     * Call this to populate resources and initialize the game.
     */
    public void initGameScreen() {
        this.enemyCount = BangBang.PLAYER_IDS.length - 1;
        populateGameResources();
    }

    public void populateGameResources() {
        for (int i = Constants.Assets.length - 1; i >= 0; i--) {
            switch (Constants.Assets[i]) {
                case Constants.AssetNames.PLAYER:
                    new Player(Utilities.createGameObjectId(Constants.GameObjectId.PLAYER, BangBang.PLAYER_ID));
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
