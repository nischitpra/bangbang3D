package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.g3d.Model;

public class Bomb extends GameObject {
    public long startTime;
    public long explodeAt;

    Bomb(String name, Model model) {
        super(name, model);
    }

    public void detonate() {
        BombManager.usedBombQ.addLast(this);
        startTime = System.currentTimeMillis();
        explodeAt = startTime + 3000;
    }

    public void update() {
        if (System.currentTimeMillis() > (startTime + 3000)) {// recycle bomb
            //     perform explosion
        }
    }

}
