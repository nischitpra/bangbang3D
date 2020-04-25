package com.nhuchhe.bangbang.animator;

import com.badlogic.gdx.Gdx;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.enums.AnimationState;
import com.nhuchhe.bangbang.screens.GameScreen;

import java.util.Iterator;
import java.util.LinkedList;

public class Animator {
    /**
     * need to handle delete of animation properly.
     */
    public void render() {
        LinkedList<AnimationObject> renderList = GameScreen.gameObjectManger.animationObjects;
        Iterator<AnimationObject> iterator = renderList.iterator();
        while (iterator.hasNext()) {
            AnimationObject animation = iterator.next();
            if (animation.state != AnimationState.SHOULD_PLAY) continue;
            animation.animationController.update(Gdx.graphics.getDeltaTime());
            BangBang.modelBatch.render(animation.modelInstance);
            if (animation.state == AnimationState.COMPLETED) {
                recycleAnimation(animation);
            }
        }
    }

    private void recycleAnimation(AnimationObject animationObject) {
        animationObject.reset();
    }

}