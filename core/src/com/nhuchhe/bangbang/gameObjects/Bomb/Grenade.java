package com.nhuchhe.bangbang.gameObjects.Bomb;

import com.nhuchhe.bangbang.BangBang;
import com.nhuchhe.bangbang.gameObjects.Bomb.base.BaseBomb;

public class Grenade extends BaseBomb {


    public Grenade(String id, int bombType) {
        super(id, bombType, BangBang.collisionObjectHelper.getGrenadeConstructionInfo(), BangBang.collisionShapeHelper.getGrenadeExplosionShape(), 2000, 150f, 0.25f);
    }

    @Override
    public void update() {
        super.update();
        if (BangBang.currentMillis > explodeAt) {// recycle bomb
            explode();
        }
    }

}
