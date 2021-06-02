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

public class GameOverScreen implements Screen {
    private OBEY game;

    private OrthographicCamera gamecam;
    private Viewport gamePort;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;

    protected Fixture fixture;

    private Float x_try_again, y_try_again, x_try_again_width, y_try_again_height;
    private Float x_menu, y_menu, x_menu_width, y_menu_height;
    private Rectangle rect_try_again_button, rect_menu_button;
    Vector3 touchPoint;

    public GameOverScreen(OBEY game) {
        this.game = game;

        gamecam = new OrthographicCamera();
        gamePort = new FillViewport(OBEY.MENU_WIDTH / OBEY.PPM, OBEY.MENU_HEIGHT / OBEY.PPM, gamecam);

        maploader = new TmxMapLoader();
        map = maploader.load("game_over_display.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / OBEY.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -20), true);

        touchPoint = new Vector3();

        // Getting "try again" and "go to menu" buttons from the corresponding map layers and save their cords
        for (MapObject object: map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            rect_try_again_button = ((RectangleMapObject) object).getRectangle();

            x_try_again = rect_try_again_button.getX() / OBEY.PPM;
            y_try_again = rect_try_again_button.getY() / OBEY.PPM;
            x_try_again_width = rect_try_again_button.getWidth() / OBEY.PPM;
            y_try_again_height = rect_try_again_button.getHeight() / OBEY.PPM;
        }

        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            rect_menu_button = ((RectangleMapObject) object).getRectangle();

            x_menu = rect_menu_button.getX() / OBEY.PPM;
            y_menu = rect_menu_button.getY() / OBEY.PPM;
            x_menu_width = rect_menu_button.getWidth() / OBEY.PPM;
            y_menu_height = rect_menu_button.getHeight() / OBEY.PPM;
        }
    }

    @Override
    public void render(float delta) {
        if (game.StateOfGame != OBEY.State.GAME_OVER)
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
            //  if pressed "go to main menu" button music and screen switches from current to main menu
            if (isInSquare(touchPoint.x, touchPoint.y, x_menu, y_menu, x_menu_width, y_menu_height)) {
                OBEY.manager.get(OBEY.SOUND_CLICK_PATH, Sound.class).play();
                game.StateOfGame = OBEY.State.MAIN_MENU;
                game.setScreen(game.mainMenuScreen);
                game.disposeCurrentMusic();
                game.setMusic(OBEY.MAIN_MENU_SCREEN_MUSIC_PATH);
                dispose();
            }
            //  if pressed "play" button music and screen switches from current to play screen
            else if (isInSquare(touchPoint.x, touchPoint.y, x_try_again, y_try_again, x_try_again_width, y_try_again_height)){
                OBEY.manager.get(OBEY.SOUND_CLICK_PATH, Sound.class).play();
                game.StateOfGame = OBEY.State.GAME;
                game.playScreen = new PlayScreen(game);
                game.setScreen(game.playScreen);
                game.disposeCurrentMusic();
                game.setMusic(OBEY.PLAY_SCREEN_MUSIC_PATH);
                dispose();
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
        //music.dispose();
    }
}
