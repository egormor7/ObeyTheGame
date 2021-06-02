package com.egormor.obey.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.egormor.obey.Screens.PlayScreen;

public class Wall extends InteractiveTileObject{
    private PlayScreen screen;

    public Wall(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        this.screen = screen;
        fixture.setUserData(this);
    }

    @Override
    public void onHandHit() {
    }

    public void onLeftHandHit() {
        screen.isPlayersLeftHandCollidesWall = true;
    }

    public void onEndLeftHandHit(){ screen.isPlayersLeftHandCollidesWall = false; }

    public void onRightHandHit() {
        screen.isPlayersRightHandCollidesWall = true;
    }

    public void onEndRightHandHit(){
        screen.isPlayersRightHandCollidesWall = false;
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Head", "Collision");
        screen.isPlayersHeadCollidesWall = true;
        screen.isPlayersBottomCollidesWall = false;
    }

    @Override
    public void onBottomHit() {
        Gdx.app.log("Bottom", "Collision");
        screen.isPlayersBottomCollidesWall = true;
        screen.isPlayersHeadCollidesWall = false;
    }
}
