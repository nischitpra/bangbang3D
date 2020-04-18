package com.nhuchhe.bangbang.gameObjects.Bomb;

import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.Bomb.base.BaseBomb;

public class Bullet extends BaseBomb {

    public static float BULLET_SPEED = 25f;

    public Bullet(String id) {
        super(id, 0, BangBang.collisionObjectHelper.getBulletConstructionInfo(), BangBang.collisionShapeHelper.getBulletExplosionShape(), 99999999, 35f, 0.01f);
        rigidBody.setGravity(new Vector3(0, 0, 0));
        rigidBody.setFriction(0);
        rigidBody.setRestitution(0.5f);
    }

    @Override
    public void update() {
        super.update();

        if (aoe.getNumOverlappingObjects() > 1 || (explodeAt > 0 && BangBang.currentMillis > explodeAt)) {
            explode();
        }
    }
}
