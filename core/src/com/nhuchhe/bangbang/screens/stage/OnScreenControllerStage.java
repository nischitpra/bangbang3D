package com.nhuchhe.bangbang.screens.stage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.inputController.adapter.OnScreenControllerAdapter;
import com.nhuchhe.bangbang.screens.base.BaseStage;
import com.nhuchhe.bangbang.utilities.Constants;

public class OnScreenControllerStage extends BaseStage {

    private float knobOffset;
//    private Image knobMoveImage;

    private Vector2 startKnobOffset = new Vector2();
    private float CONTROLLER_WIDTH = 100;

    OnScreenControllerAdapter adapter;


    public OnScreenControllerStage(final OnScreenControllerAdapter adapter) {
        this.adapter = adapter;

        final Image bgImage = BangBang.actorHelper.getImageActor(Constants.ImagesName.ON_SCREEN_CONTROLLER_BG);
        bgImage.setPosition(Constants.CAMERA_WIDTH * 0.085f, Constants.CAMERA_HEIGHT * 0.15f);
        addActor(bgImage);
        final float bgOffset = bgImage.getWidth() / 2;

        final Image knobImage = BangBang.actorHelper.getImageActor(Constants.ImagesName.ON_SCREEN_CONTROLLER_KNOB);
        knobOffset = knobImage.getWidth() / 2;
        startKnobOffset.x = Constants.CAMERA_WIDTH * 0.085f + bgOffset - knobOffset;
        startKnobOffset.y = Constants.CAMERA_HEIGHT * 0.15f + bgOffset - knobOffset;

        final Image knobMoveImage = BangBang.actorHelper.getImageActor(Constants.ImagesName.ON_SCREEN_CONTROLLER_MOVABLE_KNOB);
        knobMoveImage.setPosition(startKnobOffset.x, startKnobOffset.y);
        addActor(knobMoveImage);

        knobImage.setPosition(startKnobOffset.x, startKnobOffset.y);
        addActor(knobImage); // should be above knobMove because we are applying listener to this.
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
        ImageButton majorAttackButton = BangBang.actorHelper.getImageButtonActor(Constants.ImagesName.ON_SCREEN_CONTROLLER_MAJOR_ATTACK);
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

        ImageButton minorAttackButton = BangBang.actorHelper.getImageButtonActor(Constants.ImagesName.ON_SCREEN_CONTROLLER_MINOR_ATTACK);
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
