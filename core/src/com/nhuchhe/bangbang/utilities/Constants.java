package com.nhuchhe.bangbang.utilities;

import com.badlogic.gdx.math.Vector3;

public class Constants {
    public static float CAMERA_WIDTH;
    public static float CAMERA_HEIGHT;

    public static final Vector3 ZERO_VECTOR = new Vector3(0, 0, 0);

    public static final String[] Assets = {
            Constants.AssetNames.PLAYER,
            Constants.AssetNames.ENEMY,
            Constants.AssetNames.BOMB,
            Constants.AssetNames.TERRAIN,
            AssetNames.EXPLOSION_ANIM
    };

    // Can I convert these to enums? will be faster to compare
    public static class AssetNames {
        public static final String PLAYER = "player/player.obj";
        public static final String ENEMY = "enemy/player.obj";
        public static final String BOMB = "bomb_placeholder";
        public static final String BULLET = "bullet/bullet.obj";
        public static final String EXPLOSION_SPHERE = "explosionSphere";
        public static final String TERRAIN = "terrain_placeholder";
        public static final String EXPLOSION_ANIM = "animation/explosion/explosion_anim.g3db";
    }

    public static class UserData {
        public static final String OWNER = "owner";
        public static final String ID = "id";
    }

    public static class CollisionObject {
        public static final String PLAYER = "PLAYER";
        public static final String ENEMY = "ENEMY";
        public static final String GRENADE = "GRENADE";
        public static final String BULLET = "BULLET";
        public static final String GRENADE_EXPLOSION_SPHERE = "EXPLOSION_SPHERE";
        public static final String BULLET_EXPLOSION_SPHERE = "BULLET_EXPLOSION_SPHERE";
        public static final String TERRAIN = "TERRAIN";
    }

    public static class GameObjectId {
        public static final String PLAYER = "PLAYER_%d";
        public static final String ENEMY = "ENEMY_%d";
        public static final String BOMB = "BOMB_%d";
        public static final String EXPLOSION_SPHERE = "EXPLOSION_SPHERE_%d";
        public static final String TERRAIN = "TERRAIN_%d";
    }

    public static final String[] Bombs = {
            "bomb/bomb.obj",
            "bomb/bomb_1/bomb.obj"
    };

    public static final String[] Terrains = {
            "terrain/plane_test/plane_test.obj",
            "terrain/terrain_test.obj",
            "terrain/forest.obj"
    };

    // images and texture
    public static class ImagesName {
        public static final String ON_SCREEN_CONTROLLER_BG = "ON_SCREEN_CONTROLLER_BG";
        public static final String ON_SCREEN_CONTROLLER_KNOB = "ON_SCREEN_CONTROLLER_KNOB";
    }

    public static final String[][] Images = {
            new String[]{ImagesName.ON_SCREEN_CONTROLLER_BG, "onScreenController/bg.png"},
            new String[]{ImagesName.ON_SCREEN_CONTROLLER_KNOB, "onScreenController/knob.png"},
    };

}