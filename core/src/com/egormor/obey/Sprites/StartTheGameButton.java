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

public class StartTheGameButton extends InteractiveTileObject{
    //private static TiledMapTileSet tileSet; !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! WRONG
    public StartTheGameButton(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        //tileSet = map.getTileSets().getTileSet("texture");
        fixture.setUserData(this);
        //setCategoryFilter(OBEY.BRICK_BIT);
    }

    @Override
    public void onHandHit() {
        Gdx.app.log("StartTheGameButton", "Collision");
        setCategoryFilter(OBEY.DESTROYED_BIT);
        deleteCells();
        Hud.addScore(200);
        OBEY.manager.get(OBEY.SOUND_BREAK_BLOCK_PATH, Sound.class).play();
    }
}
