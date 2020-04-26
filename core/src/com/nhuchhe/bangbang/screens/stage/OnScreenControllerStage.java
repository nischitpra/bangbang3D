package com.nhuchhe.bangbang.screens.stage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.inputController.adapter.OnScreenControllerAdapter;
import com.nhuchhe.bangbang.screens.base.BaseStage;
import com.nhuchhe.bangbang.utilities.Constants;

public class OnScreenControllerStage extends BaseStage {

    private float bgOffset;
    private float knobOffset;
    private Image bgImage;
    private Image knobImage;
    private Image knobMoveImage;
    private ImageButton majorAttackButton;
    private ImageButton minorAttackButton;

    private Vector2 startKnobOffset = new Vector2();
    private float CONTROLLER_WIDTH = 100;

    OnScreenControllerAdapter adapter;


    public OnScreenControllerStage(final OnScreenControllerAdapter adapter) {
        this.adapter = adapter;
        Texture bg = BangBang.assetManager.imagesMap.get(Constants.ImagesName.ON_SCREEN_CONTROLLER_BG);
        Texture knob = BangBang.assetManager.imagesMap.get(Constants.ImagesName.ON_SCREEN_CONTROLLER_KNOB);
        Texture movableKnob = BangBang.assetManager.imagesMap.get(Constants.ImagesName.ON_SCREEN_CONTROLLER_MOVABLE_KNOB);
        Texture majorAttack = BangBang.assetManager.imagesMap.get(Constants.ImagesName.ON_SCREEN_CONTROLLER_MAJOR_ATTACK);
        Texture minorAttack = BangBang.assetManager.imagesMap.get(Constants.ImagesName.ON_SCREEN_CONTROLLER_MINOR_ATTACK);

        TextureRegion bgTextureRegion = new TextureRegion(bg);
        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(bgTextureRegion);
        bgImage = new Image(bgDrawable);
        bgOffset = bgImage.getWidth() / 2;
        bgImage.setPosition(Constants.CAMERA_WIDTH * 0.085f, Constants.CAMERA_HEIGHT * 0.15f);
        addActor(bgImage);

        TextureRegion movableKnobTextureRegion = new TextureRegion(movableKnob);
        TextureRegionDrawable movableKnobDrawable = new TextureRegionDrawable(movableKnobTextureRegion);
        knobMoveImage = new Image(movableKnobDrawable);

        TextureRegion knobTextureRegion = new TextureRegion(knob);
        TextureRegionDrawable knobDrawable = new TextureRegionDrawable(knobTextureRegion);
        knobImage = new Image(knobDrawable);

        knobOffset = knobImage.getWidth() / 2;
        startKnobOffset.x = Constants.CAMERA_WIDTH * 0.085f + bgOffset - knobOffset;
        startKnobOffset.y = Constants.CAMERA_HEIGHT * 0.15f + bgOffset - knobOffset;

        knobMoveImage.setPosition(startKnobOffset.x, startKnobOffset.y);
        knobImage.setPosition(startKnobOffset.x, startKnobOffset.y);
        addActor(knobMoveImage);
        addActor(knobImage);
        knobImage.addListener(new DragListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                float dx = x - knobOffset;
                float dy = y - knobOffset;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                float xMove, yMove;
                if (distance > CONTROLLER_WIDTH) {
                    float theta = (float) Math.atan2(dy, dx);
                    xMove = (float) Math.cos(theta);
                    yMove = (float) -Math.sin(theta);
                    knobMoveImage.setPosition(startKnobOffset.x + (float) (CONTROLLER_WIDTH * Math.cos(theta)), startKnobOffset.y + (float) (CONTROLLER_WIDTH * Math.sin(theta)));
                } else {
                    xMove = dx / CONTROLLER_WIDTH;
                    yMove = -dy / CONTROLLER_WIDTH;
                    knobMoveImage.setPosition(startKnobOffset.x + dx, startKnobOffset.y + dy);
                }
                adapter.isDownX = xMove;
                adapter.isDownY = yMove;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                knobMoveImage.setPosition(startKnobOffset.x, startKnobOffset.y);
                adapter.isDownX = 0;
                adapter.isDownY = 0;
            }
        });

//buttons
        TextureRegion majorAttackTextureRegion = new TextureRegion(majorAttack);
        TextureRegionDrawable majorAttackDrawable = new TextureRegionDrawable(majorAttackTextureRegion);
        majorAttackButton = new ImageButton(majorAttackDrawable);
        majorAttackButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return adapter.buttonDown(null, 97);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                adapter.buttonUp(null, 97);
            }
        });
        majorAttackButton.setPosition(Constants.CAMERA_WIDTH * 0.85f, Constants.CAMERA_HEIGHT * 0.25f);
        addActor(majorAttackButton);

        TextureRegion minorAttackTextureRegion = new TextureRegion(minorAttack);
        TextureRegionDrawable minorAttackDrawable = new TextureRegionDrawable(minorAttackTextureRegion);
        minorAttackButton = new ImageButton(minorAttackDrawable);
        minorAttackButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return adapter.buttonDown(null, 99);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                adapter.buttonUp(null, 99);
            }
        });
        minorAttackButton.setPosition(Constants.CAMERA_WIDTH * 0.70f, Constants.CAMERA_HEIGHT * 0.2f);
        addActor(minorAttackButton);
    }
}
