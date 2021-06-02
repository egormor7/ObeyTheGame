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
	public static final String PLAY_SCREEN_MUSIC_PATH = "audio/music/tunetank.com_1746_abandoned-factory_by_finval.mp3";
	public static final String MAIN_MENU_SCREEN_MUSIC_PATH = "audio/music/tunetank.com_4063_they-will-come-for-us_by_rage-sound.mp3";
	public static final String GAME_OVER_SCREEN_MUSIC_PATH = "audio/music/tunetank.com_1735_ocean-floor_by_finval.mp3";
	public static final String PAUSE_MENU_SCREEN_MUSIC_PATH = "audio/music/tunetank.com_3629_calm-place_by_finval.mp3";
	public static final String SOUND_BREAK_BLOCK_PATH = "audio/sounds/android_assets_audio_sounds_breakblock.wav";
	public static final String SOUND_LASER_KILL_PATH = "audio/sounds/laser_kill.mp3";
    public static final String SOUND_CLICK_PATH = "audio/sounds/click.mp3";
	public static final String SOUND_ROBOT_ENEMY_KILL_PATH = "audio/sounds/robotenemyKill.mp3";


	public SpriteBatch batch;
	public BitmapFont font;

	//	Every possible screen in the game
	public GameOverScreen gameOverScreen;
	public PauseMenuScreen pauseMenuScreen;
	public MainMenuScreen mainMenuScreen;
	public PlayScreen playScreen;


	public static AssetManager manager;
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		//	Loading every music / sound that using in the game
		manager = new AssetManager();
		manager.load(PLAY_SCREEN_MUSIC_PATH, Music.class);
		manager.load(MAIN_MENU_SCREEN_MUSIC_PATH, Music.class);
		manager.load(GAME_OVER_SCREEN_MUSIC_PATH, Music.class);
		manager.load(PAUSE_MENU_SCREEN_MUSIC_PATH, Music.class);
        manager.load(SOUND_BREAK_BLOCK_PATH, Sound.class);
        manager.load(SOUND_CLICK_PATH, Sound.class);
		manager.load(SOUND_LASER_KILL_PATH, Sound.class);
		manager.load(SOUND_ROBOT_ENEMY_KILL_PATH, Sound.class);
        manager.finishLoading();

		StateOfGame = State.MAIN_MENU;

		//	Creating every possible screen in the game.
		//	That's optimization, because screens creating once, at begin of the game.
		//	(Except PlayScreen, which is recreating when user starts new game or when he dies)
		gameOverScreen = new GameOverScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		playScreen = new PlayScreen(this);
		pauseMenuScreen = new PauseMenuScreen(this);

		setMusic(MAIN_MENU_SCREEN_MUSIC_PATH);
		setScreen(mainMenuScreen);
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
