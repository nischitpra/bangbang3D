package com.nhuchhe.bangbang.manager;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Queue;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.Bomb;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Utilities;

import java.util.HashMap;

public class BombManager {

    public static Queue<Bomb> bombPool = new Queue<>();
    public static Queue<Bomb> usedBombQ = new Queue<>();

    public static final Vector3 recyclePosition = new Vector3(0, -10, 0);
    private int bombCount;

    private int bombType = 0;
    private final int BOMB_TYPES_COUNT = Constants.Bombs.length;

    private Bomb createBomb() {
        return new Bomb(Utilities.getGameObjectMapKey(Constants.GameObjectId.BOMB, bombCount++), bombCount % 2);
    }

    public Bomb getBomb(final String bombOwner) {
        Bomb bomb;
        if (bombPool.isEmpty()) {
            bomb = createBomb();
        } else {
            bomb = bombPool.removeFirst();
        }
        disable(bomb);
        bomb.owner = bombOwner;
        ((HashMap<String, String>) bomb.rigidBody.userData).put(Constants.UserData.OWNER, bombOwner);
        return bomb;
    }

    public void recycleBomb(Bomb recycleBomb) {
        disable(recycleBomb);
        recycleBomb.instance.transform.translate(recyclePosition);
        recycleBomb.rigidBody.setWorldTransform(recycleBomb.instance.transform);
        recycleBomb.aoe.setWorldTransform(recycleBomb.instance.transform);
        bombPool.addLast(recycleBomb);
    }

    public void cleanup() {
        if (usedBombQ.isEmpty()) return;
        Bomb usedBomb = usedBombQ.first();
        while (BangBang.currentMillis > usedBomb.explodeAt) {
            recycleBomb(usedBombQ.removeFirst());
            if (usedBombQ.isEmpty()) break;
            usedBomb = usedBombQ.first();
        }
    }

    public void disable(Bomb bomb) {
        bomb.rigidBody.setActivationState(3);
        bomb.rigidBody.setCollisionFlags(bomb.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE); // remove collision
        bomb.aoe.setActivationState(3);
    }

    public void activate(Bomb bomb) {
        bomb.rigidBody.setActivationState(1);
        bomb.rigidBody.setCollisionFlags(bomb.rigidBody.getCollisionFlags() & ~btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE); // add collision
        bomb.aoe.setActivationState(1);
    }

}
