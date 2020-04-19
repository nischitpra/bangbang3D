package com.nhuchhe.bangbang.gameObjects.base;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.Bomb.Bullet;
import com.nhuchhe.bangbang.gameObjects.Bomb.Grenade;
import com.nhuchhe.bangbang.gameObjects.Bomb.base.BaseBomb;
import com.nhuchhe.bangbang.manager.BombManager;
import com.nhuchhe.bangbang.utilities.Utilities;

public class PlayableGameObject extends BaseGameObject {
    // game constants
    private final float MAX_MOVEMENT_SPEED = 2.5f;
    private final float MOVEMENT_FORCE_DELTA = 5;
    private final Vector3 MAJOR_ATTACK_POSITION = new Vector3(0, 0.5f, 0);
    private final float MINOR_ATTACK_POSITION_OFFSET = 0.3f;
    private final Vector3 PLAYER_ROTATION_AXIS = new Vector3(0, 1, 0);
    private final Vector3 SPAWN_POSITION = new Vector3(0, 10, 1);

    // physics constants
    private final float FRICTION = 3f;
    private final float LINEAR_DAMPING = 0.3f;

    // local variables
    private Vector3 playerDirection = new Vector3(0, 0, 0);
    private Grenade majorBomb;
    private long bombHoldAt;
    private Vector3 tempVector = new Vector3();

    public PlayableGameObject(String id, String assetName) {
        super(id);
        super.init(
                BangBang.assetManager.assetManager.get(assetName, Model.class),
                BangBang.collisionObjectHelper.getPlayerConstructionInfo()
        );
        rigidBody.setActivationState(4);
        rigidBody.translate(SPAWN_POSITION);
        rigidBody.setFriction(FRICTION);
        rigidBody.setDamping(LINEAR_DAMPING, rigidBody.getAngularDamping());
        rigidBody.setRestitution(0.25f);
    }

    public void controllerFeed(final float inputX, final float inputY, final float rotationRad, float lt) {
        rotatePlayer(rotationRad);
        movePlayer(inputX, inputY, lt);
    }

    public void initMajorAttack() {
        if (majorBomb != null) return;
        bombHoldAt = BangBang.currentMillis;
        majorBomb = (Grenade) BangBang.bombManager.getBomb(id, 1); // need to be able to set bomb type here
        updateBombPosition(majorBomb, MAJOR_ATTACK_POSITION);
    }

    public void performMajorAttack() {
        if (majorBomb == null) return;
        throwBomb();
        // calculate trajectory here
    }

    public void initMinorAttack() {
        performMinorAttack();
    }

    public void update() {
//        BangBang.debugDrawer.drawLine(bombPosition, new Vector3(bombPosition.x + MINOR_ATTACK_POSITION.x, bombPosition.y + MINOR_ATTACK_POSITION.y, bombPosition.z + MINOR_ATTACK_POSITION.z), new Vector3(1, 1, 0));
        updateBombPosition(majorBomb, MAJOR_ATTACK_POSITION);
//        trajectoryHelper.setTrajectory(getBombThrowForce(), Utilities.copyValueTo(getPosition(), tempVector));
    }


    private void rotatePlayer(final float rotationRad) {
        Utilities.copyValueTo(getPosition(), tempVector);
        motionState.transform.setToRotationRad(PLAYER_ROTATION_AXIS, rotationRad).setTranslation(tempVector);

        playerDirection.x = (float) Math.cos(rotationRad) * MINOR_ATTACK_POSITION_OFFSET;
        playerDirection.y = 0;
        playerDirection.z = (float) -Math.sin(rotationRad) * MINOR_ATTACK_POSITION_OFFSET;
    }

    private void movePlayer(final float inputX, final float inputY, final float lt) {
        Utilities.copyValueTo(rigidBody.getLinearVelocity(), tempVector);
        float velMag = Utilities.getVelocityMagnitude(tempVector);
        if (velMag < MAX_MOVEMENT_SPEED) {
            tempVector.x = inputX * MOVEMENT_FORCE_DELTA * (1.0f - lt);
            tempVector.y = 0;
            tempVector.z = inputY * MOVEMENT_FORCE_DELTA * (1.0f - lt);
            rigidBody.applyCentralImpulse(tempVector);
        }
    }

    private void throwBomb() {
        configureBeforeThrow(majorBomb);
        majorBomb.rigidBody.applyCentralImpulse(getBombThrowForce());
        majorBomb = null;
    }

    private void performMinorAttack() {
        Bullet bullet = (Bullet) BangBang.bombManager.getBomb(id, 0);
        configureBeforeThrow(bullet);
        updateBombPosition(bullet, playerDirection);
        Utilities.copyValueTo(playerDirection, tempVector);
        tempVector.scl(Bullet.BULLET_SPEED);
        bullet.rigidBody.setLinearVelocity(tempVector);
    }

    private void configureBeforeThrow(BaseBomb bomb) {
        BangBang.bombManager.activate(bomb);
        bomb.setDetonationTime();
        BombManager.usedBombQ.addLast(bomb);
    }

    private void updateBombPosition(BaseBomb bomb, Vector3 attackPositionOffset) {
        if (bomb != null) {
            Vector3 bombPosition = getPosition();
            bomb.instance.transform.setTranslation(bombPosition.x + attackPositionOffset.x, bombPosition.y + attackPositionOffset.y, bombPosition.z + attackPositionOffset.z);
            bomb.rigidBody.setWorldTransform(bomb.instance.transform);
        }
    }

    private Vector3 getBombThrowForce() {
        Utilities.copyValueTo(playerDirection, tempVector);//todo: normalize this to get direction and then apply force.
        tempVector.y += 0.05f;
        tempVector.scl(3.4f + Math.min(500, BangBang.currentMillis - bombHoldAt) / 5);
        return tempVector;
    }

}
