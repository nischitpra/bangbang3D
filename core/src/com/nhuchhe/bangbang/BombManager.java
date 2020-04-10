package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Queue;

import java.util.HashMap;

public class BombManager {
    public static String name;
    public static Model model; //  details of the model, how it looks, what its position is etc
    public static btRigidBody.btRigidBodyConstructionInfo constructionInfo;

    public static Queue<Bomb> bombPool = new Queue<>();
    public static Queue<Bomb> usedBombQ = new Queue<>();

    private static final Vector3 recyclePosition = new Vector3(0, 5, 0);

    private Bomb createBomb() {
        Bomb bomb = new Bomb(name, model);
        bomb.createRigidBody(constructionInfo);
        bomb.instance.transform.setTranslation(recyclePosition);
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
        bomb.rigidBody.userData = new HashMap<String, String>() {{
            put("owner", bombOwner);
        }};
//        bomb.rigidBody.setCollisionFlags(1); // reactivate rigidBody
//        bomb.rigidBody.setCollisionFlags(bomb.rigidBody.getCollisionFlags() & ~btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE); //  reactivate collision
        return bomb;
    }

    public static void recycleBomb(Bomb recycleBomb) {
//        recycleBomb.rigidBody.setActivationState(5); // disable simulation i.e make this deactivated
        recycleBomb.rigidBody.translate(recyclePosition);
//        recycleBomb.rigidBody.setCollisionFlags(recycleBomb.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE); // remove collision
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

}
