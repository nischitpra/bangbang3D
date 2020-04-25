package com.nhuchhe.bangbang.screens.stage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.screens.base.BaseStage;
import com.nhuchhe.bangbang.utilities.Constants;
import com.nhuchhe.bangbang.utilities.Logger;

import java.util.Arrays;

public class LobbyStage extends BaseStage {
    Label lobbyNameLabel;
    TextButton startGameButton;
    TextButton getPlayersButton;

    public LobbyStage(final String lobbyName) {
        lobbyNameLabel = new Label(lobbyName, labelStyle);
        lobbyNameLabel.setPosition(Constants.CAMERA_WIDTH * 0.5f, Constants.CAMERA_HEIGHT * 0.95f);
        addActor(lobbyNameLabel);

        getPlayersButton = new TextButton("Get Players Id", buttonStyle);
        getPlayersButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                int[] players = BangBang.network.getPlayers(lobbyName);
                Logger.log(Arrays.toString(players));
            }
        });
        getPlayersButton.setPosition(Constants.CAMERA_WIDTH * 0.5f, Constants.CAMERA_HEIGHT * 0.125f);
        addActor(getPlayersButton);

        startGameButton = new TextButton("Start Game", buttonStyle);
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                BangBang.network.startGame(lobbyName);
            }
        });
        startGameButton.setPosition(Constants.CAMERA_WIDTH * 0.5f, Constants.CAMERA_HEIGHT * 0.05f);
        addActor(startGameButton);
    }

}
