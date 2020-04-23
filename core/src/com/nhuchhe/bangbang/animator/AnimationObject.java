package com.nhuchhe.bangbang.animator;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.enums.AnimationState;

public class AnimationObject {
    public AnimationController animationController;
    public ModelInstance modelInstance;
    public AnimationState state;
    public Vector3 recyclePosition;
    public boolean shouldLoop;

    private int loopCount;

    public AnimationObject(String assetName, String animationName, boolean shouldLoop, Vector3 recyclePosition) {
        this.shouldLoop = shouldLoop;
        this.loopCount = -1;
        this.modelInstance = new ModelInstance(BangBang.assetManager.assetManager.get(assetName, Model.class));
        this.animationController = new AnimationController(this.modelInstance);
        this.animationController.setAnimation(animationName, loopCount, new CAnimationListener(this));
        this.recyclePosition = recyclePosition;
    }

    public void reset() {
        state = AnimationState.IS_RECYCLED;
        modelInstance.transform.setTranslation(recyclePosition.x, recyclePosition.y, recyclePosition.z);
        animationController.paused = true;
    }

    public void play(Vector3 position) {
        modelInstance.transform.setTranslation(position.x, position.y, position.z);
        state = AnimationState.SHOULD_PLAY;
        animationController.paused = false;
    }

}
