package com.egormor.obey.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.egormor.obey.OBEY;
import com.egormor.obey.Sprites.Wall;

import java.util.Locale;

public class MainMenuScreen implements Screen {
    private OBEY game;

    //OrthographicCamera camera;

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
        gamePort = new FillViewport(OBEY.MENU_WIDTH / OBEY.PPM, OBEY.MENU_HEIGHT / OBEY.PPM, gamecam); //(OBEY.V_WIDTH / OBEY.PPM, OBEY.V_HEIGHT / OBEY.PPM, gamecam);

        maploader = new TmxMapLoader();
        map = maploader.load("main_menu_hq.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / OBEY.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -20), true);

        //World world = .getWorld();
        //TiledMap map = screen.getMap();
        //BodyDef bdef = new BodyDef();
        //PolygonShape shape = new PolygonShape();
        //FixtureDef fdef = new FixtureDef();
        //Body body;


        //Float x_start, y_start, x_start_width, y_start_height;
        //create walls bodies/fixtures for other objects the same cycle

        touchPoint = new Vector3();


        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            rect_start_button = ((RectangleMapObject) object).getRectangle();

            x_start = rect_start_button.getX() / OBEY.PPM;
            y_start = rect_start_button.getY() / OBEY.PPM;
            x_start_width = rect_start_button.getWidth() / OBEY.PPM;
            y_start_height = rect_start_button.getHeight() / OBEY.PPM;
        }

        //Float x_exit, y_exit, x_exit_width, y_exit_height;
        //create walls bodies/fixtures for other objects the same cycle
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
        gamecam.update();
        renderer.setView(gamecam);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        game.batch.setProjectionMatrix(gamecam.combined);


        /*game.batch.begin();
        game.font.draw(game.batch, "OBEY ", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        game.batch.end();*/

        if (Gdx.input.justTouched()) {
            //Gdx.app.log("Clicked ", "Coordinates: " + String.format(Locale.US, "%d %d",  Gdx.input.getX(), Gdx.input.getY()));
            //Gdx.app.log("Start button ", "Coordinates: " + String.format(Locale.US, "%f %f %f %f",  x_start, y_start, x_start + x_start_width, y_start + y_start_height));
            //Gdx.app.log("Exit button ", "Coordinates: " + String.format(Locale.US, "%f %f %f %f",  x_exit, y_exit, x_exit + x_exit_width, y_exit + y_exit_height));
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gamecam.unproject(touchPoint);
            //Gdx.app.log("Clicked ", "Coordinates: " + String.format(Locale.US, "%f %f",  touchPoint.x, touchPoint.y));
            //Gdx.app.log("Start button ", "Coordinates: " + String.format(Locale.US, "%f %f %f %f",  x_start, y_start, x_start + x_start_width, y_start + y_start_height));
            //Gdx.app.log("Exit button ", "Coordinates: " + String.format(Locale.US, "%f %f %f %f",  x_exit, y_exit, x_exit + x_exit_width, y_exit + y_exit_height));
            //if (rect_start_button.contains(touchPoint.x, touchPoint.y))
            //    Gdx.app.log("Clicked on start", "Coordinates: " + String.format(Locale.US, "%d %d",  Gdx.input.getX(), Gdx.input.getY()));
            if (isInSquare(touchPoint.x, touchPoint.y, x_start, y_start, x_start_width, y_start_height)) {
                game.StateOfGame = OBEY.State.GAME;
                game.setScreen(new PlayScreen(game));
                dispose();
            }
            else if (isInSquare(touchPoint.x, touchPoint.y, x_exit, y_exit, x_exit_width, y_exit_height)){
                Gdx.app.log("exit button", "Coordinates: ");
                dispose();
                Gdx.app.exit();
                System.exit(0);
            }
        }
    }

    public boolean isInSquare(float x1, float y1, float x2, float y2, float width, float height){
        if ((x1 >= x2) && (x1 <= (x2 + width)) && (y1 >= y2) && (y1 <= (y2 + height)))
            return true;
        return false;
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
