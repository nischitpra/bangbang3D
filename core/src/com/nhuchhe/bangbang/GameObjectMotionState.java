package com.nhuchhe.bangbang;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

/**
 * MotionState creates callback for rigidBody transformation and then transforms the transform Matrix which is pointed to the model.instance.transform
 */
public class GameObjectMotionState extends btMotionState {
    Matrix4 transform;

    @Override
    public void getWorldTransform(Matrix4 worldTrans) {
        worldTrans.set(transform);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
        transform.set(worldTrans);
    }
}
