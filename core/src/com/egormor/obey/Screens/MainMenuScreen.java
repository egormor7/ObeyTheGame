package com.egormor.obey.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.egormor.obey.OBEY;


public class MainMenuScreen implements Screen {
    private OBEY game;

    private OrthographicCamera gamecam;
    private Viewport gamePort;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;

    protected Fixture fixture;

    private Float x_start, y_start, x_start_width, y_start_height;
    private Float x_exit, y_exit, x_exit_width, y_exit_height;
    private Rectangle rect_start_button;
    Vector3 touchPoint;

    public MainMenuScreen(OBEY game) {
        this.game = game;

        gamecam = new OrthographicCamera();
        gamePort = new FillViewport(OBEY.MENU_WIDTH / OBEY.PPM, OBEY.MENU_HEIGHT / OBEY.PPM, gamecam);

        maploader = new TmxMapLoader();
        map = maploader.load("main_menu_hq.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / OBEY.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -20), true);

        touchPoint = new Vector3();

        // Getting "start the game" and "exit the game" buttons from the corresponding map layers and save their cords
        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            rect_start_button = ((RectangleMapObject) object).getRectangle();

            x_start = rect_start_button.getX() / OBEY.PPM;
            y_start = rect_start_button.getY() / OBEY.PPM;
            x_start_width = rect_start_button.getWidth() / OBEY.PPM;
            y_start_height = rect_start_button.getHeight() / OBEY.PPM;
        }

        for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            x_exit = rect.getX() / OBEY.PPM;
            y_exit = rect.getY() / OBEY.PPM;
            x_exit_width = rect.getWidth() / OBEY.PPM;
            y_exit_height = rect.getHeight() / OBEY.PPM;
        }
    }

    @Override
    public void render(float delta) {
        if (game.StateOfGame != OBEY.State.MAIN_MENU)
            return;

        gamecam.update();
        renderer.setView(gamecam);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        game.batch.setProjectionMatrix(gamecam.combined);

        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gamecam.unproject(touchPoint);

            //  if pressed "play" button music and screen switches from current to play screen
            if (isInSquare(touchPoint.x, touchPoint.y, x_start, y_start, x_start_width, y_start_height)) {
                OBEY.manager.get(OBEY.SOUND_CLICK_PATH, Sound.class).play();
                game.StateOfGame = OBEY.State.GAME;
                game.playScreen = new PlayScreen(game);
                game.setScreen(game.playScreen);
                game.disposeCurrentMusic();
                game.setMusic(OBEY.PLAY_SCREEN_MUSIC_PATH);
                dispose();
            }
            //  if pressed "exit" button then everything is disposing
            else if (isInSquare(touchPoint.x, touchPoint.y, x_exit, y_exit, x_exit_width, y_exit_height)){
                OBEY.manager.get(OBEY.SOUND_CLICK_PATH, Sound.class).play();
                Gdx.app.log("exit button", "Coordinates: ");
                dispose();
                game.dispose();
                Gdx.app.exit();
                System.exit(0);
            }
        }
    }

    //  function checks if (x1 ; y1) is in rectangle (x2; y2; x2 + width; y2 + height)
    public boolean isInSquare(float x1, float y1, float x2, float y2, float width, float height){
        return (x1 >= x2) && (x1 <= (x2 + width)) && (y1 >= y2) && (y1 <= (y2 + height));
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
