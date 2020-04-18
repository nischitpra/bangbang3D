package com.nhuchhe.bangbang.gameObjects.Bomb;

import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.Bomb.base.BaseBomb;

public class Bullet extends BaseBomb {

    public static float BULLET_SPEED = 25f;

    private Vector3 tempVector = new Vector3();

    public Bullet(String id) {
        super(id, 0, BangBang.collisionObjectHelper.getBulletConstructionInfo(), BangBang.collisionShapeHelper.getBulletExplosionShape(), 5000, 35f, 0.01f);
        rigidBody.setGravity(new Vector3(0, 0, 0));
        rigidBody.setFriction(0);
        rigidBody.setRestitution(0.5f);
    }

    @Override
    public void update() {
        super.update();

        if (aoe.getNumOverlappingObjects() > 1 || hasBombExpired()) {
            explode();
        }
    }

    @Override
    protected boolean shouldNotApplyForce(String objectId) {
        return objectId.equals(id) || objectId.equals(ownerId);
    }

    @Override
    protected boolean shouldExplode(int countAffected) {
        return countAffected > 0 || hasBombExpired();
    }
}
