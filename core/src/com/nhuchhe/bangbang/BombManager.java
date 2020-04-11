package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Queue;

import java.util.HashMap;

public class BombManager {
    public static String name;

    public static Queue<Bomb> bombPool = new Queue<>();
    public static Queue<Bomb> usedBombQ = new Queue<>();

    private static final Vector3 recyclePosition = new Vector3(0, 5, 0);

    private btRigidBody createExplosionSphere(Bomb bomb) {
        btRigidBody explosionSphereRigidBody = new btRigidBody(CollisionObjectHelper.getExplosionSphereConstructionInfo());
        explosionSphereRigidBody.setCollisionFlags(btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
//        explosionSphereRigidBody.setMotionState(bomb.motionState);
        explosionSphereRigidBody.userData = new HashMap<String, String>() {{
            put(Constants.UserData.NAME, Constants.AssetNames.EXPLOSION_SPHERE);
        }};
        BangBang.world.addRigidBody(explosionSphereRigidBody);
        return explosionSphereRigidBody;
    }

    private Bomb createBomb() {
        Bomb bomb = new Bomb(name, BangBang.assetManagerHelper.assetManager.get(name, Model.class));
        bomb.createRigidBody(CollisionObjectHelper.getBombRigidBodyConstructionInfo());
        bomb.instance.transform.setTranslation(recyclePosition);
        bomb.explosionSphereRigidBody = createExplosionSphere(bomb);
        BangBang.world.addRigidBody(bomb.rigidBody);
        return bomb;
    }

    public Bomb getBomb(final String bombOwner) {
        Bomb bomb;
        if (bombPool.isEmpty()) {
            bomb = createBomb();
        } else {
            bomb = bombPool.removeFirst();
        }
        ((HashMap<String, String>) bomb.rigidBody.userData).put(Constants.UserData.OWNER, bombOwner);
        return bomb;
    }

    // I think the game slows down after adding many bombs is because the recycled rigidbodies still collide.
    public static void recycleBomb(Bomb recycleBomb) {
        recycleBomb.rigidBody.setActivationState(3);// disable rigidBody
        recycleBomb.rigidBody.setCollisionFlags(recycleBomb.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE); // remove collision
        recycleBomb.rigidBody.translate(recyclePosition);
        recycleBomb.explosionSphereRigidBody.translate(recyclePosition);
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
