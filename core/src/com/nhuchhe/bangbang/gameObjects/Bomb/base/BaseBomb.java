package com.nhuchhe.bangbang.gameObjects.Bomb.base;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.animator.AnimationObject;
import com.nhuchhe.bangbang.enms.State;
import com.nhuchhe.bangbang.gameObjects.base.AoeDetectionGameObject;
import com.nhuchhe.bangbang.gameObjects.base.BaseGameObject;
import com.nhuchhe.bangbang.gameObjects.base.PlayableGameObject;
import com.nhuchhe.bangbang.manager.BombManager;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Logger;
import com.nhuchhe.bangbang.utilities.Utilities;

import java.util.HashMap;

public abstract class BaseBomb extends AoeDetectionGameObject {

    public int bombType;
    public String ownerId;
    public long explodeAt = -1;
    public State state;
    public float damage;
    public boolean proximity_damage = true;

    private final long EXPLODE_DELAY_MILLIS;
    protected final float EXPLOSION_HEIGHT_INCREMENT;
    protected final float EXPLOSION_FORCE;
    private AnimationObject animationObject;
    private Vector3 tempVector = new Vector3();


    public BaseBomb(String id, int bombType, btRigidBody.btRigidBodyConstructionInfo bombConstructionInfo, btCollisionShape aoeShape, long explodeDelayMillis, float explosionForce, float explosionHeightIncrement) {
        super(id, aoeShape);
        super.init(
                BangBang.assetManager.assetManager.get(Constants.Bombs[bombType], Model.class),
                bombConstructionInfo
        );
        this.damage = damage;
        this.bombType = bombType;
        this.EXPLODE_DELAY_MILLIS = explodeDelayMillis;
        this.EXPLOSION_FORCE = explosionForce;
        this.EXPLOSION_HEIGHT_INCREMENT = explosionHeightIncrement;
        animationObject = new AnimationObject(Constants.AssetNames.EXPLOSION_ANIM, "Sphere|explosion", false, BombManager.recyclePosition);
        BangBang.gameObjectManger.animationObjects.add(animationObject);
        rigidBody.setFriction(1.5f);
        rigidBody.setRestitution(0.5f);
    }

    protected boolean hasBombExpired() {
        return explodeAt > 0 && BangBang.currentMillis > explodeAt;
    }

    protected boolean shouldExplode() {
        return hasBombExpired();
    }

    protected void explodeAnimation() {
        animationObject.play(getPosition());
    }

    public void setDetonationTime() {
        this.explodeAt = BangBang.currentMillis + EXPLODE_DELAY_MILLIS;
    }

    public void update() {
        if (state != State.IS_BEING_USED) return;
        aoe.setWorldTransform(motionState.transform);
        if (shouldExplode()) {
            explode();
        }
    }

    protected boolean shouldNotApplyForce(String objectId) {
        return objectId.equals(id);
    }

    protected boolean shouldPlayExplosionAnimationAndRecycle(int countAffected) {
        return true;
    }

    private String getGameObjectIdFromCollisionObject(btRigidBody body) {
        HashMap<String, String> userData = Utilities.getUserData(body.userData);
        return userData.get(Constants.UserData.ID);
    }

    private void applyExplosionForce(String objectKey, Vector3 explosionCenter) {
        BaseGameObject bgo = BangBang.gameObjectManger.gameObjectMap.get(objectKey);
        Utilities.copyValueTo(bgo.getPosition(), tempVector);
        tempVector = Utilities.getNormalizedProximity(explosionCenter, tempVector);
        tempVector.y += EXPLOSION_HEIGHT_INCREMENT;
        tempVector.scl(EXPLOSION_FORCE);

        bgo.rigidBody.activate(true);
        bgo.rigidBody.applyCentralImpulse(tempVector);
        if (bgo instanceof PlayableGameObject) {
            if (proximity_damage) {
                float distance = Utilities.distance(explosionCenter, bgo.getPosition());
                float damage = Utilities.magnitude(tempVector) / distance / 3;
                Logger.log("bomb damage: " + damage);
                ((PlayableGameObject) bgo).applyDamage(damage);
            } else {
                Logger.log("bullet damage: " + damage);
                ((PlayableGameObject) bgo).applyDamage(damage);
            }
        }
    }

    // call this only during explosion
    protected void explode() {
        int count = aoe.getNumOverlappingObjects();
        int countAffected = 0;
        Vector3 explosionCenter = getPosition();
        for (int i = count - 1; i >= 0; i--) {
            String objectId = getGameObjectIdFromCollisionObject((btRigidBody) aoe.getOverlappingObject(i));
            if (shouldNotApplyForce(objectId)) continue;
            applyExplosionForce(objectId, explosionCenter);
            countAffected++;
        }
        if (shouldPlayExplosionAnimationAndRecycle(countAffected)) {
            explodeAnimation();
            state = State.SHOULD_RECYCLE;
        }
    }
}
