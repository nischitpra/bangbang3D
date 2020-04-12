package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;
import com.badlogic.gdx.utils.Queue;

import java.util.HashMap;

public class BombManager {
    public static String name;


    public static Queue<Bomb> bombPool = new Queue<>();
    public static Queue<Bomb> usedBombQ = new Queue<>();

    private static final Vector3 recyclePosition = new Vector3(0, 5, 0);
    private int bombCount;

    private btGhostObject createExplosionSphere(Bomb bomb) {
        btGhostObject explosionSphere = new btGhostObject();
        explosionSphere.setCollisionShape(CollisionObjectHelper.getExplosionSphereShape());
        explosionSphere.setCollisionFlags(explosionSphere.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
        explosionSphere.setIgnoreCollisionCheck(BangBang.gameObjectManger.terrain.rigidBody, true);
        explosionSphere.userData = new HashMap<String, String>() {{
            put(Constants.UserData.NAME, Constants.AssetNames.EXPLOSION_SPHERE);
        }};
        BangBang.world.addCollisionObject(explosionSphere);
        return explosionSphere;
    }

    private Bomb createBomb() {
        String objectKey = Utilities.getGameObjectMapKey(name, bombCount++);
        Bomb bomb = new Bomb(name, BangBang.assetManagerHelper.assetManager.get(name, Model.class));
        bomb.createRigidBody(CollisionObjectHelper.getBombRigidBodyConstructionInfo());
        bomb.instance.transform.setTranslation(recyclePosition);
        bomb.explosionSphere = createExplosionSphere(bomb);
        BangBang.world.addRigidBody(bomb.rigidBody);
        BangBang.gameObjectManger.gameObjectMap.put(objectKey, bomb);
        return bomb;
    }

    public Bomb getBomb(final String bombOwner) {
        Bomb bomb;
        if (bombPool.isEmpty()) {
            bomb = createBomb();
        } else {
            bomb = bombPool.removeFirst();
        }
        bomb.owner = bombOwner;
        ((HashMap<String, String>) bomb.rigidBody.userData).put(Constants.UserData.OWNER, bombOwner);
        return bomb;
    }

    // I think the game slows down after adding many bombs is because the recycled rigidbodies still collide.
    public static void recycleBomb(Bomb recycleBomb) {
        recycleBomb.rigidBody.setActivationState(3);// disable rigidBody
        recycleBomb.rigidBody.setCollisionFlags(recycleBomb.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE); // remove collision
        recycleBomb.rigidBody.translate(recyclePosition);
        recycleBomb.explosionSphere.getWorldTransform(recycleBomb.instance.transform);
        bombPool.addLast(recycleBomb);
    }

    public static void cleanup() {
        if (usedBombQ.isEmpty()) return;
        long currentTime = System.currentTimeMillis();
        Bomb usedBomb = usedBombQ.first();
        while (currentTime > usedBomb.explodeAt) {
            recycleBomb(usedBombQ.removeFirst());
            if (usedBombQ.isEmpty()) break;
            usedBomb = usedBombQ.first();
        }
    }

    public static void activate(Bomb bomb) {
        bomb.rigidBody.setActivationState(1);
        bomb.rigidBody.setCollisionFlags(bomb.rigidBody.getCollisionFlags() & ~btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE); // add collision
    }

}
