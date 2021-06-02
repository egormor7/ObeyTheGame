package com.egormor.obey.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.egormor.obey.OBEY;
import com.egormor.obey.Scenes.Hud;
import com.egormor.obey.Screens.GameOverScreen;
import com.egormor.obey.Screens.PlayScreen;

public class Laser extends InteractiveTileObject{
    private PlayScreen screen;
    public Laser(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        this.screen = screen;
        fixture.setUserData(this);
        setCategoryFilter(OBEY.LASER_BIT);
    }

    @Override
    public void onHandHit() {
        OBEY.manager.get(OBEY.SOUND_LASER_KILL_PATH, Sound.class).play();
        screen.game_over = true;
    }

    @Override
    public void onHeadHit() {
        OBEY.manager.get(OBEY.SOUND_LASER_KILL_PATH, Sound.class).play();
        screen.game_over = true;
    }

    @Override
    public void onBottomHit() {
        OBEY.manager.get(OBEY.SOUND_LASER_KILL_PATH, Sound.class).play();
        screen.game_over = true;
    }

}
