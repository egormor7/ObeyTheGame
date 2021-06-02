package com.egormor.obey.Sprites;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.egormor.obey.OBEY;
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
