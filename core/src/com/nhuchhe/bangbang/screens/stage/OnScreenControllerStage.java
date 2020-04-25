package com.nhuchhe.bangbang.screens.stage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.inputController.adapter.OnScreenControllerAdapter;
import com.nhuchhe.bangbang.screens.base.BaseStage;
import com.nhuchhe.bangbang.utilities.Constants;

public class OnScreenControllerStage extends BaseStage {
    Texture bg;
    TextureRegion bgTextureRegion;
    TextureRegionDrawable bgDrawable;
    Image bgImage;

    Texture knob;
    TextureRegion knobTextureRegion;
    TextureRegionDrawable knobDrawable;
    Image knobImage;

//    OnScreenControllerAdapter adapter;

    public Vector2 startTouchPosition = new Vector2();

    public OnScreenControllerStage() {
//        this.adapter = adapter;
        bg = BangBang.assetManager.imagesMap.get(Constants.ImagesName.ON_SCREEN_CONTROLLER_BG);
        knob = BangBang.assetManager.imagesMap.get(Constants.ImagesName.ON_SCREEN_CONTROLLER_KNOB);

        bgTextureRegion = new TextureRegion(bg);
        bgDrawable = new TextureRegionDrawable(bgTextureRegion);
        bgImage = new Image(bgDrawable);
        addActor(bgImage);

        knobTextureRegion = new TextureRegion(knob);
        knobDrawable = new TextureRegionDrawable(knobTextureRegion);
        knobImage = new Image(knobDrawable);
        addActor(knobImage);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX > Constants.CAMERA_WIDTH * .5f) return false;
        startTouchPosition.x = screenX;
        startTouchPosition.y = screenY;
        bgImage.setPosition(screenX, screenY);
        knobImage.setPosition(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (screenX > Constants.CAMERA_WIDTH * .5f) return false;
        knobImage.setPosition(screenX, screenY);
        return true;
    }
}
