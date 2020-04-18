package com.nhuchhe.bangbang.gameObjects;

import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.Bomb.Bullet;
import com.nhuchhe.bangbang.gameObjects.Bomb.Grenade;
import com.nhuchhe.bangbang.gameObjects.Bomb.base.BaseBomb;
import com.nhuchhe.bangbang.gameObjects.base.PlayableGameObject;
import com.nhuchhe.bangbang.manager.BombManager;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Utilities;

/**
 * instance.transform.translate(vector3) moves the object to the position relative to current position
 * instance.transform.setTranslate(vector3) moves the object to the absolute position
 * <p>
 * update instance.transfrom.translate and then rigidbody.setWorldTransfrom(instance.transform) always works like it should.
 * I don't know what motionstate.transfrom is used for
 */
public class Player extends PlayableGameObject {

    private final float MAX_SPEED = 5;
    private final float FORCE_DELTA = 5;
    private final Vector3 rotationAxis = new Vector3(0, 1, 0);

    private final Vector3 MAJOR_ATTACK_POSITION = new Vector3(0, 0.5f, 0);
    private Vector3 MINOR_ATTACK_POSITION = new Vector3(0, 0, 0);
    private float MINOR_ATTACK_POSITION_OFFSET = 0.3f;

    private long bombHoldAt;
    private Vector3 tempVector = new Vector3();

    private Grenade majorBomb;

    public Player(String id) {
        super(id, Constants.AssetNames.PLAYER);
        BangBang.gameObjectManger.player = this;
        BangBang.inputControllerManager.setPlayer(this);
    }

    private void rotatePlayer(final float rotationRad) {
        Utilities.copyValueTo(getPosition(), tempVector);
        motionState.transform.setToRotationRad(rotationAxis, rotationRad).setTranslation(tempVector);

        MINOR_ATTACK_POSITION.x = (float) Math.cos(rotationRad) * MINOR_ATTACK_POSITION_OFFSET;
        MINOR_ATTACK_POSITION.y = 0;
        MINOR_ATTACK_POSITION.z = (float) -Math.sin(rotationRad) * MINOR_ATTACK_POSITION_OFFSET;
    }

    private void movePlayer(final float inputX, final float inputY) {
        Utilities.copyValueTo(rigidBody.getLinearVelocity(), tempVector);
        float velMag = Utilities.getVelocityMagnitude(tempVector);
        if (velMag < MAX_SPEED) {
            tempVector.x = inputX * FORCE_DELTA;
            tempVector.y = 0;
            tempVector.z = inputY * FORCE_DELTA;
            rigidBody.applyCentralImpulse(tempVector);
        }
    }

    public void controllerFeed(final float inputX, final float inputY, final float rotationRad) {
        rotatePlayer(rotationRad);
        movePlayer(inputX, inputY);
    }

    private void updateBombPosition(BaseBomb bomb, Vector3 attackPositionOffset) {
        if (bomb != null) {
            Vector3 bombPosition = getPosition();
            bomb.instance.transform.setTranslation(bombPosition.x + attackPositionOffset.x, bombPosition.y + attackPositionOffset.y, bombPosition.z + attackPositionOffset.z);
            bomb.rigidBody.setWorldTransform(bomb.instance.transform);
        }
    }

    private void throwBomb() {
        BangBang.bombManager.activate(majorBomb);
        Utilities.copyValueTo(rigidBody.getLinearVelocity(), tempVector);//todo: normalize this to get direction and then apply force.
        tempVector.y += 2;
        tempVector.scl(Math.min(3.5f, 1.6f + (BangBang.currentMillis - bombHoldAt) / 25));
        majorBomb.rigidBody.applyCentralImpulse(tempVector);
        majorBomb.setDetonationTime();
        BombManager.usedBombQ.addLast(majorBomb);
        majorBomb = null;
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
    }

    public void initMinorAttack() {
        performMinorAttack();
    }

    public void performMinorAttack() {
        Bullet bullet = (Bullet) BangBang.bombManager.getBomb(id, 0);
        BangBang.bombManager.activate(bullet);
        updateBombPosition(bullet, MINOR_ATTACK_POSITION);


        Utilities.copyValueTo(MINOR_ATTACK_POSITION, tempVector);
        tempVector.scl(Bullet.BULLET_SPEED);
        bullet.rigidBody.setLinearVelocity(tempVector);
        bullet.setDetonationTime();
        BombManager.usedBombQ.addLast(bullet);

    }

    public void update() {
//        BangBang.debugDrawer.drawLine(bombPosition, new Vector3(bombPosition.x + MINOR_ATTACK_POSITION.x, bombPosition.y + MINOR_ATTACK_POSITION.y, bombPosition.z + MINOR_ATTACK_POSITION.z), new Vector3(1, 1, 0));
        updateBombPosition(majorBomb, MAJOR_ATTACK_POSITION);
    }

}
