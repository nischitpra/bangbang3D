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

    private float bgOffset;
    private Image bgImage;

    private float knobOffset;
    private Image knobImage;
    private float CONTROLLER_WIDTH = 150;

    OnScreenControllerAdapter adapter;

    public Vector2 startTouchPosition = new Vector2();

    public OnScreenControllerStage(OnScreenControllerAdapter adapter) {
        this.adapter = adapter;
        Texture bg = BangBang.assetManager.imagesMap.get(Constants.ImagesName.ON_SCREEN_CONTROLLER_BG);
        Texture knob = BangBang.assetManager.imagesMap.get(Constants.ImagesName.ON_SCREEN_CONTROLLER_KNOB);

        TextureRegion bgTextureRegion = new TextureRegion(bg);
        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(bgTextureRegion);
        bgImage = new Image(bgDrawable);
        addActor(bgImage);
        bgOffset = bgImage.getWidth() / 2;

        TextureRegion knobTextureRegion = new TextureRegion(knob);
        TextureRegionDrawable knobDrawable = new TextureRegionDrawable(knobTextureRegion);
        knobImage = new Image(knobDrawable);
        addActor(knobImage);
        knobOffset = knobImage.getWidth() / 2;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX > Constants.CAMERA_WIDTH * .5f) return false;
        startTouchPosition.x = screenX;
        startTouchPosition.y = Constants.CAMERA_HEIGHT - screenY;
        bgImage.setPosition(screenX - bgOffset, startTouchPosition.y - bgOffset);
        knobImage.setPosition(screenX - knobOffset, startTouchPosition.y - knobOffset);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (screenX > Constants.CAMERA_WIDTH * .5f) return false;
        float dx = startTouchPosition.x - screenX;
        float dy = startTouchPosition.y - (Constants.CAMERA_HEIGHT - screenY);
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float xMove, yMove;
        if (distance > CONTROLLER_WIDTH) {
            float theta = (float) Math.atan2(-dy, dx);
            xMove = (float) -Math.cos(theta);
            yMove = (float) -Math.sin(theta);
            knobImage.setPosition(startTouchPosition.x - knobOffset + (float) (CONTROLLER_WIDTH * -Math.cos(theta)), startTouchPosition.y - knobOffset + (float) (CONTROLLER_WIDTH * Math.sin(theta)));
        } else {
            xMove = -dx / CONTROLLER_WIDTH;
            yMove = dy / CONTROLLER_WIDTH;
            knobImage.setPosition(screenX - knobOffset, Constants.CAMERA_HEIGHT - screenY - knobOffset);
        }
        adapter.isDownX = xMove;
        adapter.isDownY = yMove;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screenX > Constants.CAMERA_WIDTH * .5f) return false;
        bgImage.setPosition(startTouchPosition.x - bgOffset, startTouchPosition.y - bgOffset);
        knobImage.setPosition(startTouchPosition.x - knobOffset, startTouchPosition.y - knobOffset);
        startTouchPosition.x = 0;
        startTouchPosition.y = 0;

        adapter.isDownX = 0;
        adapter.isDownY = 0;
        return true;
    }
}
