package com.nhuchhe.bangbang.utilities;

public class Constants {
    public static final String[] Assets = {
            Constants.AssetNames.PLAYER,
            Constants.AssetNames.ENEMY,
            Constants.AssetNames.BOMB,
            Constants.AssetNames.TERRAIN
    };

    // Can I convert these to enums? will be faster to compare
    public static class AssetNames {
        public static final String PLAYER = "player/player.obj";
        public static final String ENEMY = "enemy/player.obj";
        public static final String BOMB = "bomb_placeholder";
        public static final String EXPLOSION_SPHERE = "explosionSphere";
        public static final String TERRAIN = "terrain_placeholder";
    }

    public static class UserData {
        public static final String OWNER = "owner";
        public static final String ID = "id";
    }

    public static class BombOwner {
        public static final String PLAYER = "player";
    }

    public static class CollisionObject {
        public static final String PLAYER = "PLAYER";
        public static final String ENEMY = "ENEMY";
        public static final String BOMB = "BOMB";
        public static final String EXPLOSION_SPHERE = "EXPLOSION_SPHERE";
        public static final String TERRAIN = "TERRAIN";
    }

    public static class GameObjectId {
        public static final String PLAYER = "PLAYER";
        public static final String ENEMY = "ENEMY_%d";
        public static final String BOMB = "BOMB_%d";
        public static final String EXPLOSION_SPHERE = "EXPLOSION_SPHERE_%d";
        public static final String TERRAIN = "TERRAIN_%d";
    }

    public static final String[] Bombs = {
            "bomb/bomb.obj"
    };

    public static final String[] Terrains = {
            "terrain/terrain test3.obj"
    };

}