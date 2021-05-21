package com.egormor.obey.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
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

    private Music music;

    private float TimeOfLastSpacePress = 0;

    public  PlayScreen(OBEY game){
        atlas = new TextureAtlas("sprites_2.atlas");

        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(OBEY.V_WIDTH / OBEY.PPM, OBEY.V_HEIGHT / OBEY.PPM, gamecam);
        hud = new Hud(game.batch);
        //Gdx.files.internal("/android/assets");

        maploader = new TmxMapLoader();
        map = maploader.load("first_level_test_4.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / OBEY.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -20), true);
        b2dr = new Box2DDebugRenderer();

        player = new MainCharacter(this);

        new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());

        music = OBEY.manager.get(OBEY.MUSIC_PATH, Music.class);
        music.setLooping(true);
        music.play();

        robotEnemy = new RobotEnemy(this, .32f, .32f);

    }

    public TextureAtlas getAtlas(){
        return atlas;
    }


    @Override
    public void show() {

    }

    public void handleInput(float dt){

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y == 0)
            player.b2body.applyLinearImpulse(new Vector2(0, 10), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().y == 0)
            player.b2body.applyLinearImpulse(new Vector2(0, -10), player.b2body.getWorldCenter(), true);
        if (((Gdx.input.isTouched() && (Gdx.input.getX() > Gdx.graphics.getWidth() / 2)) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.b2body.getLinearVelocity().x <= 6) {

            player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), player.b2body.getWorldCenter(), true);
            Gdx.app.log("Touch at x:", Float.toString(Gdx.input.getX()));
            Gdx.app.log("Cam at x:", Float.toString(Gdx.graphics.getWidth()));
        }
        if (((Gdx.input.isTouched() && (Gdx.input.getX() < Gdx.graphics.getWidth() / 2)) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && player.b2body.getLinearVelocity().x >= -6)
            player.b2body.applyLinearImpulse(new Vector2(-0.2f, 0), player.b2body.getWorldCenter(), true);
        if ((Gdx.input.isTouched() && (((Gdx.input.getDeltaY() < (Gdx.graphics.getHeight() / 25)) && (world.getGravity().y <= 0)) || ((Gdx.input.getDeltaY() > (Gdx.graphics.getHeight() / 25)) && (world.getGravity().y >= 0)))) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Gdx.app.log("Space", "Before: " + TimeOfLastSpacePress + " After: " + hud.getWorldTimer());
            if (TimeOfLastSpacePress == 0 || ((TimeOfLastSpacePress - hud.getWorldTimer()) >= 1)) {
                Gdx.app.log("Space", "Pressed");

                world.setGravity(new Vector2(world.getGravity().x, world.getGravity().y * -1));
                player.b2body.applyLinearImpulse(new Vector2(0, 1), player.b2body.getWorldCenter(), true);
                player.fallingDown = world.getGravity().y <= 0;
                TimeOfLastSpacePress = hud.getWorldTimer();
            }
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.W))
            gamecam.position.y += 10 * dt;
        else if (Gdx.input.isKeyPressed(Input.Keys.A))
            gamecam.position.x -= 10 * dt;
        else if (Gdx.input.isKeyPressed(Input.Keys.S))
            gamecam.position.y -= 10 * dt;
        else if (Gdx.input.isKeyPressed(Input.Keys.D))
            gamecam.position.x += 10 * dt;
    }

    public void update(float dt){
        handleInput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);
        robotEnemy.update(dt);
        hud.update(dt);

        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;

        gamecam.update();
        renderer.setView(gamecam);

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gamecam.combined);


        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        robotEnemy.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
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
    }
}
