package com.egormor.obey;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.egormor.obey.Screens.GameOverScreen;
import com.egormor.obey.Screens.MainMenuScreen;
import com.egormor.obey.Screens.PlayScreen;
import com.egormor.obey.Sprites.RobotEnemy;

public class OBEY extends Game {
	public static final int V_WIDTH = 1000;
	public static final int V_HEIGHT = 600;
    public static final int MENU_WIDTH = 2560;
    public static final int MENU_HEIGHT = 1440;
	public static final float PPM = 5;
	public static final float TILE_SIZE = 5;

	public static final short GROUND_BIT = 1;
	public static final short MAIN_CHARACTER_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short DESTROYED_BIT = 8;
	public static final short LASER_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;

	public enum State { MAIN_MENU, GAME, PAUSE, GAME_OVER};
	public State StateOfGame;

	public static final String MUSIC_PATH = "audio/music/tunetank.com_1746_abandoned-factory_by_finval.mp3";	//https://tunetank.com/t/1bpr/4063-they-will-come-for-us
	public static final String SOUND_BREAK_BLOCK_PATH = "audio/sounds/android_assets_audio_sounds_breakblock.wav";	//https://tunetank.com/track/1746-abandoned-factory
	//https://tunetank.com/t/1bpr/1735-ocean-floor

	public SpriteBatch batch;
	public BitmapFont font;

	private Screen game_over_screen;

	public static AssetManager manager;
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		manager = new AssetManager();
		manager.load(MUSIC_PATH, Music.class);
        manager.load(SOUND_BREAK_BLOCK_PATH, Sound.class);
        manager.finishLoading();

        //game_over_screen = new GameOverScreen(this);
		//setScreen(new PlayScreen(this));
		StateOfGame = State.MAIN_MENU;
		setScreen(new MainMenuScreen(this));
		//Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true, true, false, true, false, true);

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
