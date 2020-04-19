package com.nhuchhe.bangbang.gameObjects.Bomb;

import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.Bomb.base.BaseBomb;

public class Bullet extends BaseBomb {

    public static float BULLET_SPEED = 25f;

    public Bullet(String id) {
        super(id, 0, BangBang.collisionObjectHelper.getBulletConstructionInfo(), BangBang.collisionShapeHelper.getBulletExplosionShape(), 5000, 35f, 0.01f);
        rigidBody.setGravity(new Vector3(0, 0, 0));
        rigidBody.setFriction(0);
        rigidBody.setRestitution(0.5f);
    }

    @Override
    protected boolean shouldExplode() {
        return aoe.getNumOverlappingObjects() > 1 || super.shouldExplode();
    }

    @Override
    protected boolean shouldNotApplyForce(String objectId) {
        return objectId.equals(id) || objectId.equals(ownerId);
    }

    @Override
    protected boolean shouldPlayExplosionAnimationAndRecycle(int countAffected) {
        return countAffected > 0 || hasBombExpired();
    }

}
