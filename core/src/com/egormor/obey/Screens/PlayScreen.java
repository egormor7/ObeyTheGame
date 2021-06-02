package com.egormor.obey.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.egormor.obey.OBEY;
import com.egormor.obey.Scenes.Hud;
import com.egormor.obey.Sprites.MainCharacter;
import com.egormor.obey.Sprites.RobotEnemy;
import com.egormor.obey.Tools.B2WorldCreator;
import com.egormor.obey.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    private OBEY game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private MainCharacter player;
    private RobotEnemy robotEnemy;
    private Array<RobotEnemy> robotEnemyArray;
    private Array<Float> robotEnemy_x_cords;
    private Array<Float> robotEnemy_y_cords;

    //  corresponding cords where to create robotEnemies (e.g. (start_robotEnemy_x_cords[0] ; start_robotEnemy_y_cords[0])
    //  cords for the first enemy, (start_robotEnemy_x_cords[1] ; start_robotEnemy_y_cords[1]) for the second, etc.)
    private static final float[] start_robotEnemy_x_cords = {323, 110, 408, 143};
    private static final float[] start_robotEnemy_y_cords = {110, 110, 246, 249};


    private float TimeOfLastSpacePress = 0;
    private float TimeOfLastTouch = 0;
    private float countOfLastTouches = 0;
    public boolean game_over = false;
    public boolean game_over_over = false;

    private Texture pause_button_texture;

    public boolean isPlayersBottomCollidesWall, isPlayersHeadCollidesWall, isPlayersLeftHandCollidesWall, isPlayersRightHandCollidesWall;

    public  PlayScreen(OBEY game){
        atlas = new TextureAtlas("sprites_2.atlas");

        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FillViewport(OBEY.V_WIDTH / OBEY.PPM, OBEY.V_HEIGHT / OBEY.PPM, gamecam);
        hud = new Hud(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("first_level_test_5.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / OBEY.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -40), true);
        b2dr = new Box2DDebugRenderer(false, false, false, true, false, false);
        //  b2dr = new Box2DDebugRenderer();    Uncomment for debug collisions, etc.

        player = new MainCharacter(this, 1400 / OBEY.PPM, 830 / OBEY.PPM);

        new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());

        robotEnemyArray = new Array<>();
        robotEnemy_x_cords = new Array<>();
        robotEnemy_y_cords = new Array<>();
        for (int i = 0; i < start_robotEnemy_x_cords.length; i++) {
            robotEnemy = new RobotEnemy(this, start_robotEnemy_x_cords[i], start_robotEnemy_y_cords[i], start_robotEnemy_x_cords[i], start_robotEnemy_y_cords[i], i);
            robotEnemyArray.add(robotEnemy);
            robotEnemy_x_cords.add(start_robotEnemy_x_cords[i]);
            robotEnemy_y_cords.add(start_robotEnemy_y_cords[i]);
        }

        pause_button_texture = new Texture(Gdx.files.internal("pause_menu_hud_button.png"));
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }


    @Override
    public void show() {
    }

    public void handleInput(float dt){
        //  handle double click which is calling pause menu
        if (Gdx.input.justTouched()){
            Gdx.app.log("touch " + countOfLastTouches, "" + hud.getWorldTimer());
            if (TimeOfLastTouch == 0) {
                TimeOfLastTouch = hud.getWorldTimer();
                countOfLastTouches = 1;
            }
            else{
                if ((countOfLastTouches == 1) && ((0.05 <= (hud.getWorldTimer() - TimeOfLastTouch)) && ((hud.getWorldTimer() - TimeOfLastTouch) <= 0.1))){
                    OBEY.manager.get(OBEY.SOUND_CLICK_PATH, Sound.class).play();
                    Gdx.app.log("double click", "");
                    game.StateOfGame = OBEY.State.PAUSE;
                    countOfLastTouches = 0;
                    game.setScreen(game.pauseMenuScreen);
                    game.pauseCurrentMusic();
                    pause();

                }
                else if (0.1 <= (hud.getWorldTimer() - TimeOfLastTouch)){
                    countOfLastTouches = 1;
                    TimeOfLastTouch = hud.getWorldTimer();
                }
                else
                    countOfLastTouches = 0;
            }
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.UP) || (Gdx.input.isTouched() && (countOfLastTouches == 1) && (Gdx.input.getX() > Gdx.graphics.getWidth() / 4) && (Gdx.input.getX() < 3 * Gdx.graphics.getWidth() / 4))) && (player.b2body.getLinearVelocity().y == 0) && !isPlayersHeadCollidesWall)
            player.b2body.applyLinearImpulse(new Vector2(0, 80), player.b2body.getWorldCenter(), true);
        if ((Gdx.input.isKeyPressed(Input.Keys.DOWN) || (Gdx.input.isTouched() && (countOfLastTouches == 1) && (Gdx.input.getX() > Gdx.graphics.getWidth() / 4) && (Gdx.input.getX() < 3 * Gdx.graphics.getWidth() / 4))) && (player.b2body.getLinearVelocity().y == 0) && !isPlayersBottomCollidesWall)
            player.b2body.applyLinearImpulse(new Vector2(0, -80), player.b2body.getWorldCenter(), true);

        //  forces MainCharacter right if pressed "right" button or user touched the right part of display
        if (((Gdx.input.isTouched() && (Gdx.input.getX() > 3 * Gdx.graphics.getWidth() / 4)) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && (player.b2body.getLinearVelocity().x <= 20) && !isPlayersRightHandCollidesWall) {
            player.b2body.applyLinearImpulse(new Vector2(10.8f, 0), player.b2body.getWorldCenter(), true);
            Gdx.app.log("RIGHT", "Before: " + TimeOfLastSpacePress + " After: " + hud.getWorldTimer());
        }

        //  forces MainCharacter left if pressed "left" button or user touched the left part of display
        if (((Gdx.input.isTouched() && (Gdx.input.getX() < Gdx.graphics.getWidth() / 4)) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && (player.b2body.getLinearVelocity().x >= -20) && !isPlayersLeftHandCollidesWall)
            player.b2body.applyLinearImpulse(new Vector2(-10.8f, 0), player.b2body.getWorldCenter(), true);

        //  Gravity change if user pushed "space" button or swiped corresponding down or up.
        //  Also, between two gravity changes must be a defined delay
        Gdx.app.log(" Swipe y: " +  TimeOfLastSpacePress, " " + hud.getWorldTimer());
        if ((Gdx.input.isTouched() && (((-Gdx.input.getDeltaY() > (Gdx.graphics.getHeight() / 25)) && (world.getGravity().y <= 0)) || ((Gdx.input.getDeltaY() > (Gdx.graphics.getHeight() / 25)) && (world.getGravity().y >= 0)))) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Gdx.app.log("Space", "Before: " + TimeOfLastSpacePress + " After: " + hud.getWorldTimer());
            //  flush the counter of clicks because that's swipe
            if (Gdx.input.isTouched() && (((Gdx.input.getDeltaY() > (Gdx.graphics.getHeight() / 25)) && (world.getGravity().y <= 0)) || ((-Gdx.input.getDeltaY() > (Gdx.graphics.getHeight() / 25)) && (world.getGravity().y >= 0))))
                countOfLastTouches = 0;
            if (TimeOfLastSpacePress == 0 || ((hud.getWorldTimer() - TimeOfLastSpacePress) >= 0.8)) {
                Gdx.app.log("Space", "Pressed");

                world.setGravity(new Vector2(world.getGravity().x, world.getGravity().y * -1));
                player.b2body.applyLinearImpulse(new Vector2(0, 1), player.b2body.getWorldCenter(), true);
                player.fallingDown = world.getGravity().y <= 0;

                for (int i = 0; i < robotEnemy_x_cords.size; i++) {
                    robotEnemyArray.get(i).fallingDown = world.getGravity().y <= 0;
                }

                TimeOfLastSpacePress = hud.getWorldTimer();
            }
        }
    }

    public void update(float dt){

        if (game_over && !game_over_over){
            game_over_over = true;
            dispose();
            game.StateOfGame = OBEY.State.GAME_OVER;
            game.setScreen(game.gameOverScreen);
            game.disposeCurrentMusic();
            game.setMusic(OBEY.GAME_OVER_SCREEN_MUSIC_PATH);
            return;
        }
        handleInput(dt);

        world.step(1/30f, 6, 2);

        player.update(dt);

        for (int i = 0; i < robotEnemy_x_cords.size; i++) {
            robotEnemyArray.get(i).update(dt);
        }

        hud.update(dt);

        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;

        gamecam.update();
        renderer.setView(gamecam);

    }


    @Override
    public void render(float delta) {
        if (game.StateOfGame != OBEY.State.GAME) {
            return;
        }
        if (game_over && game_over_over)
            return;
        update(delta);
        if (game_over && game_over_over)
            return;

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gamecam.combined);


        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.begin();
        player.draw(game.batch);

        for (int i = 0; i < robotEnemy_x_cords.size; i++) {
            robotEnemyArray.get(i).draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public float getPlayerX(){
        return player.getX();
    }

    public float getPlayerY(){
        return player.getY();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    public OBEY getGame(){
        return game;
    }

    public Hud getHud(){
        return hud;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        atlas.dispose();
        game.batch.flush();
    }
}
