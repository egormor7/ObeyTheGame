package com.egormor.obey.Scenes;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.egormor.obey.OBEY;


public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;


    private float timeCount, worldTimer;
    private static Integer score;

    public Hud(SpriteBatch sb){
        worldTimer = 0;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(OBEY.V_WIDTH, OBEY.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
    }

    public void update(float dt){
        worldTimer += dt;
    }

    public float getWorldTimer(){
        //return worldTimer;
        return TimeUtils.timeSinceMillis(OBEY.startTime) / 1000f;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
