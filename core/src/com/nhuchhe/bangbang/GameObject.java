package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;

public class GameObject {
    public String name;
    public Model model; //  details of the model, how it looks, what its position is etc
    public ModelInstance instance; // instance is created from model. to be rendered
    public btRigidBody rigidBody;
    public btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    public GameObjectMotionState motionState;

    GameObject(final String name, final Model model) {
        this.name = name;
        this.model = model;
        this.instance = new ModelInstance(model);
    }

    public void createRigidBody(btRigidBodyConstructionInfo constructionInfo) {
        this.motionState = new GameObjectMotionState();
        this.motionState.transform = instance.transform;
        this.constructionInfo = constructionInfo;
        this.rigidBody = new btRigidBody(constructionInfo);
        this.rigidBody.setMotionState(motionState); // set callback for transformation
    }

    public boolean isVisible(final Camera cam, Vector3 position) {
        instance.transform.getTranslation(position);
        return cam.frustum.pointInFrustum(position);// todo: check this => cam.frustum.sphereInFrustum(position.add(center), radius);
    }

    public void dispose() {
        this.name = null;
        this.rigidBody.getCollisionShape().dispose(); //should call dispose on everything disposable
        this.constructionInfo.dispose();
        this.rigidBody.dispose();
        this.model.dispose();
        this.motionState.dispose();
    }
}
