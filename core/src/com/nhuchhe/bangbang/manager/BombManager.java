package com.nhuchhe.bangbang.manager;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Queue;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.Bomb.Bullet;
import com.nhuchhe.bangbang.gameObjects.Bomb.Grenade;
import com.nhuchhe.bangbang.gameObjects.Bomb.base.BaseBomb;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class BombManager {

    public static HashMap<Integer, Queue<BaseBomb>> bombsPool = new HashMap<>();
    public static LinkedList<BaseBomb> usedBombQ = new LinkedList();
    public static final Vector3 recyclePosition = new Vector3(0, -10, 0);

    private int bombCount;

    private final int BOMB_TYPES_COUNT = Constants.Bombs.length;

    private boolean hasBombTypeInPool(int bombType) {
        try {
            return !bombsPool.get(bombType).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private BaseBomb createBomb(int bombType) {
        bombCount++;
        // create different types of bomb here
        switch (bombType) {
            case 0:
                return new Bullet(Utilities.getGameObjectMapKey(Constants.GameObjectId.BOMB, bombCount));
            case 1:
                return new Grenade(Utilities.getGameObjectMapKey(Constants.GameObjectId.BOMB, bombCount), bombType);
        }
        throw new RuntimeException(bombType + " BombType not supported! Please contact the developer nischipra@gmail.com");
    }

    public BaseBomb getBomb(final String ownerId, int bombType) {
        BaseBomb bomb;
        if (hasBombTypeInPool(bombType)) {
            bomb = bombsPool.get(bombType).removeFirst();
        } else {
            bomb = createBomb(bombType);
        }
        disable(bomb);
        bomb.ownerId = ownerId;
        ((HashMap<String, String>) bomb.rigidBody.userData).put(Constants.UserData.OWNER, ownerId);
        return bomb;
    }

    public void recycleBomb(BaseBomb recycleBomb) {
        disable(recycleBomb);
        recycleBomb.shouldRecycle = false;
        recycleBomb.instance.transform.setTranslation(recyclePosition);
        recycleBomb.rigidBody.setWorldTransform(recycleBomb.instance.transform);
        recycleBomb.aoe.setWorldTransform(recycleBomb.instance.transform);
        recycleBomb.rigidBody.clearForces();
        recycleBomb.rigidBody.setLinearVelocity(Constants.ZERO_VECTOR);
        recycleBomb.rigidBody.setAngularVelocity(Constants.ZERO_VECTOR);

        try {
            bombsPool.get(recycleBomb.bombType).addLast(recycleBomb);
        } catch (Exception e) {
            bombsPool.put(recycleBomb.bombType, new Queue<BaseBomb>());
            bombsPool.get(recycleBomb.bombType).addLast(recycleBomb);
        }
    }

    public void cleanup() {
        if (usedBombQ.isEmpty()) return;

        Iterator<BaseBomb> itr = usedBombQ.iterator();
        while (itr.hasNext()) {
            BaseBomb usedBomb = itr.next();
            if (usedBomb.shouldRecycle) {
                recycleBomb(usedBomb);
                itr.remove();
            }
        }
    }

    public void disable(BaseBomb bomb) {
        BangBang.world.removeRigidBody(bomb.rigidBody);
        bomb.rigidBody.setActivationState(3);
        bomb.rigidBody.setCollisionFlags(bomb.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE); // remove collision
        bomb.aoe.setActivationState(3);
    }

    public void activate(BaseBomb bomb) {
        BangBang.world.addRigidBody(bomb.rigidBody);
        bomb.rigidBody.setActivationState(1);
        bomb.rigidBody.setCollisionFlags(bomb.rigidBody.getCollisionFlags() & ~btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE); // add collision
        bomb.aoe.setActivationState(1);
    }

}
