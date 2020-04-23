package com.nhuchhe.bangbang.screens.stage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.screens.stage.base.BaseStage;

public class LobbyStage extends BaseStage {
    Label lobbyNameLabel;
    TextButton startGameButton;

    public LobbyStage(final String lobbyName) {
        lobbyNameLabel = new Label(lobbyName, labelStyle);
        addActor(lobbyNameLabel);

        startGameButton = new TextButton("Start Game", buttonStyle);
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                BangBang.network.startGame(lobbyName);
            }
        });
    }

}
