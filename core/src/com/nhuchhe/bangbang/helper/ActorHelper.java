package com.nhuchhe.bangbang.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nhuchhe.bangbang.BangBang;

public class ActorHelper {
    public Texture getTexture(String imageName) {
        return BangBang.assetManager.imagesMap.get(imageName);
    }

    public TextureRegion getTextureRegion(String imageName) {
        return new TextureRegion(getTexture(imageName));
    }

    public TextureRegionDrawable getTextureRegionDrawable(String imageName) {
        return new TextureRegionDrawable(getTextureRegion(imageName));
    }

    public Image getImageActor(String imageName) {
        return new Image(getTextureRegion(imageName));
    }

    public ImageButton getImageButtonActor(String imageName) {
        return new ImageButton(getTextureRegionDrawable(imageName));
    }

}
