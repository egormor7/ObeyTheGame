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
import com.egormor.obey.Screens.PauseMenuScreen;
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

	private Music music;
	public static final String PLAY_SCREEN_MUSIC_PATH = "audio/music/tunetank.com_1746_abandoned-factory_by_finval.mp3";	//https://tunetank.com/t/1bpr/4063-they-will-come-for-us
	public static final String MAIN_MENU_SCREEN_MUSIC_PATH = "audio/music/tunetank.com_4063_they-will-come-for-us_by_rage-sound.mp3";
	public static final String GAME_OVER_SCREEN_MUSIC_PATH = "audio/music/tunetank.com_1735_ocean-floor_by_finval.mp3";
	public static final String PAUSE_MENU_SCREEN_MUSIC_PATH = "audio/music/tunetank.com_3629_calm-place_by_finval.mp3";
	public static final String SOUND_BREAK_BLOCK_PATH = "audio/sounds/android_assets_audio_sounds_breakblock.wav";	//https://tunetank.com/track/1746-abandoned-factory

	//https://tunetank.com/t/1bpr/1735-ocean-floor

	public SpriteBatch batch;
	public BitmapFont font;

	public Screen game_over_screen, pause_menu_screen, main_menu_screen, play_screen;

	public static AssetManager manager;
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		manager = new AssetManager();
		manager.load(PLAY_SCREEN_MUSIC_PATH, Music.class);
		manager.load(MAIN_MENU_SCREEN_MUSIC_PATH, Music.class);
		manager.load(GAME_OVER_SCREEN_MUSIC_PATH, Music.class);
		manager.load(PAUSE_MENU_SCREEN_MUSIC_PATH, Music.class);
        manager.load(SOUND_BREAK_BLOCK_PATH, Sound.class);
        manager.finishLoading();

		//setScreen(new PlayScreen(this));
		StateOfGame = State.MAIN_MENU;

		game_over_screen = new GameOverScreen(this);
		main_menu_screen = new MainMenuScreen(this);
		play_screen = new PlayScreen(this);
		pause_menu_screen = new PauseMenuScreen(this);

		setMusic(MAIN_MENU_SCREEN_MUSIC_PATH);
		setScreen(main_menu_screen);
		//Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true, true, false, true, false, true);

	}

	public void setMusic(String path){
		music = OBEY.manager.get(path, Music.class);
		music.setLooping(true);
		music.play();
	}

	public void disposeCurrentMusic(){
		music.dispose();
	}

	public void pauseCurrentMusic(){
		music.pause();
	}

	public void unPauseCurrentMusic(){
		music.play();
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
		disposeCurrentMusic();
	}
}
