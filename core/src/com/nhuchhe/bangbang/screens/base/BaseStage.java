package com.nhuchhe.bangbang.screens.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public abstract class BaseStage extends Stage {
    protected TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    protected Label.LabelStyle labelStyle = new Label.LabelStyle();
    protected BitmapFont font = new BitmapFont();

    public BaseStage() {
        Gdx.input.setInputProcessor(this);
        buttonStyle.font = font;
        labelStyle.font = font;
    }
}
