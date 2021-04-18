package com.egormor.obey;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.egormor.obey.Screens.PlayScreen;

public class OBEY extends Game {
	public static final int V_WIDTH = 500;
	public static final int V_HEIGHT = 308;
	public static final float PPM = 50;
	public static final float TILE_SIZE = 5;

	public static final short DEFAULT_BIT = 1;
	public static final short MAIN_CHARACTER_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short DESTROYED_BIT = 8;
	public static final short LASER_BIT = 16;

	public SpriteBatch batch;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
