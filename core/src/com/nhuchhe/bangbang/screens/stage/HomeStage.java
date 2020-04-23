package com.nhuchhe.bangbang.screens.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.screens.stage.base.BaseStage;
import com.nhuchhe.bangbang.utilities.Constants;


public class HomeStage extends BaseStage {

    TextField inputText;
    TextButton getLobbyButton;


    public HomeStage() {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.fontColor = Color.RED;
        style.font = font;
        inputText = new TextField("Initial value", style);
        inputText.setPosition(Constants.CAMERA_WIDTH * 0.5f, Constants.CAMERA_HEIGHT * 0.25f);
        addActor(inputText);

        getLobbyButton = new TextButton("Get Lobby", this.buttonStyle);
        getLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                String[] lobbies = BangBang.network.getLobbies();
                createLobbyButtons(lobbies);
            }
        });
        getLobbyButton.setPosition(Constants.CAMERA_WIDTH * 0.5f, Constants.CAMERA_HEIGHT * 0.45f);
        addActor(getLobbyButton);
    }

    private void createLobbyButtons(String[] lobbies) {
        float offset = 0.9f;
        for (int i = lobbies.length - 1; i >= 0; i--) {
            TextButton button = createButton(lobbies[i]);
            button.setPosition(Constants.CAMERA_WIDTH * 0.5f, Constants.CAMERA_HEIGHT * offset);
            offset -= 0.05f;
            addActor(button);
        }
    }

    private TextButton createButton(final String text) {
        TextButton button = new TextButton(text, buttonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                BangBang.network.joinLobby(text);
            }
        });
        return button;
    }

}
