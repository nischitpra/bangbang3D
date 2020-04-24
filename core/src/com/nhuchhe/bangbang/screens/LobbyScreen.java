package com.nhuchhe.bangbang.screens;

import com.nhuchhe.bangbang.screens.core.BaseScreen;
import com.nhuchhe.bangbang.screens.stage.LobbyStage;

public class LobbyScreen extends BaseScreen {
    private String lobbyName;

    public LobbyScreen(String lobbyName) {
        this.lobbyName = lobbyName;
        this.stage = new LobbyStage(lobbyName);
    }

}
