package com.egormor.obey.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.egormor.obey.Screens.PlayScreen;

public class Wall extends InteractiveTileObject{
    public Wall(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onHandHit() {
        //Gdx.app.log("Brick", "Collision");
    }
}
