package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class GameObject {
    public String name;
    public Model model; //  details of the model, how it looks, what its position is etc
    public ModelInstance instance; // instance is created from model. to be rendered
    public btRigidBody rigidBody;
    public btRigidBody.btRigidBodyConstructionInfo constructionInfo;

    GameObject(final String name, final Model model) {
        this.name = name;
        this.model = model;
        this.instance = new ModelInstance(model);
        if (this.name.equals("table/table.obj")) {
            this.instance.transform.setTranslation(new Vector3(200f, 200f, 200f));
            this.instance.transform.setToScaling(0.1f, 0.1f, 0.1f); // todo: bullet does not support scaling well. So never use scaling, instead edit the object to right size
        }
    }

    public void createRigidBody(btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        this.constructionInfo = constructionInfo;
        this.rigidBody = new btRigidBody(constructionInfo);
        this.rigidBody.setWorldTransform(instance.transform); //update instance with rigidBody physics
    }

    public boolean isVisible(final Camera cam, Vector3 position) {
        instance.transform.getTranslation(position);
        return cam.frustum.pointInFrustum(position);// todo: check this => cam.frustum.sphereInFrustum(position.add(center), radius);
    }

    public void dispose() {
        //todo: dispose everything here
//        this.rigidBody.getCollisionShape().dispose(); //should call dispose on everything disposable
    }
}
