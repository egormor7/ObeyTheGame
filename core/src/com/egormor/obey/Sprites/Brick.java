package com.egormor.obey.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.egormor.obey.OBEY;
import com.egormor.obey.Scenes.Hud;
import com.egormor.obey.Screens.PlayScreen;

public class Brick extends InteractiveTileObject{
    public Brick(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(OBEY.BRICK_BIT);
    }

    @Override
    public void onHandHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(OBEY.DESTROYED_BIT);
        deleteCells();
        OBEY.manager.get(OBEY.SOUND_BREAK_BLOCK_PATH, Sound.class).play();
    }
}
