package com.nhuchhe.bangbang.gameObjects.base;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.nhuchhe.bangbang.BangBang;

public class BaseGameObject {

    public String id; // id used in GameObjectManager.gameObjectMap
    public Model model; //  details of the model, how it looks, what its position is etc
    public ModelInstance instance; // instance is created from model. to be rendered
    public btRigidBody rigidBody;
    public btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    public MotionState motionState;

    private Vector3 tempVector = new Vector3();

    public BaseGameObject(String id) {
        this.id = id;
    }

    public void init(Model model, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        this.model = model;
        this.instance = new ModelInstance(model);
        BangBang.rigidBodyHelper.createRigidBody(this, constructionInfo);
    }

    public Vector3 getPosition() {
        return instance.transform.getTranslation(tempVector);
    }

    public boolean isVisible(final Camera cam) {
        return cam.frustum.pointInFrustum(getPosition());// todo: check this => cam.frustum.sphereInFrustum(position.add(center), radius);
    }

    public void update() {
    }

    public void render() {
        if (isVisible(BangBang.cam)) {
            BangBang.modelBatch.render(instance, BangBang.environment);
        }
    }

    public void dispose() {
        model.dispose();
        instance.userData = null;
        rigidBody.dispose();
        constructionInfo.dispose();
        motionState.dispose();
    }

}
