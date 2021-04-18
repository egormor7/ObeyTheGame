package com.egormor.obey.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.egormor.obey.OBEY;

import java.util.Locale;


public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private  Integer worldTimer;
    private float timeCount;
    private static Integer score;

    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label obeyLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(OBEY.V_WIDTH, OBEY.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format(Locale.US, "%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format(Locale.US,"%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        obeyLabel = new Label("OBEY", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(obeyLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }

    public void update(float dt){
        timeCount += dt;
        if (timeCount >= 1){
            worldTimer--;
            countdownLabel.setText(String.format(Locale.US, "%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format(Locale.US,"%06d", score));
    }

    public float getWorldTimer(){
        return worldTimer;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
