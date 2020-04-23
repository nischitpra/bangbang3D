package com.nhuchhe.bangbang.animator;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.nhuchhe.bangbang.enums.AnimationState;

public class CAnimationListener implements AnimationController.AnimationListener {
    private AnimationObject animationObject;

    public CAnimationListener(AnimationObject animationObject) {
        this.animationObject = animationObject;
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {
    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {
        if (!animationObject.shouldLoop) {
            animationObject.state = AnimationState.COMPLETED;
        }
    }
}
