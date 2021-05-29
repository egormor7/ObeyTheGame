package com.egormor.obey.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.egormor.obey.OBEY;
import com.egormor.obey.Screens.PlayScreen;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds){
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / OBEY.PPM, (bounds.getY() + bounds.getHeight() / 2) / OBEY.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / OBEY.PPM, bounds.getHeight() / 2 / OBEY.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

    }

    public abstract void onHandHit();
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public void deleteCells(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        Gdx.app.log(" " + layer.getWidth(), " " + layer.getHeight());

        /*for (int i = 0;i < 1641; i++){
            for (int j = 0;j<480;j++){
                TiledMapTileLayer.Cell a = layer.getCell(i, j);
                if (!(a == null)){
                    Gdx.app.log(" ffff" + i, " " + j);
                    //a.setTile(null);
                    //a.setTile(null);
                }
            }
        }*/
        Gdx.app.log("" + (int)(body.getPosition().x * OBEY.PPM / OBEY.TILE_SIZE - bounds.getWidth() / OBEY.TILE_SIZE / 2) + " " + (int)(body.getPosition().x * OBEY.PPM / OBEY.TILE_SIZE + bounds.getWidth() / OBEY.TILE_SIZE / 2), "" + (int)(body.getPosition().y * OBEY.PPM / OBEY.TILE_SIZE - bounds.getHeight() / OBEY.TILE_SIZE / 2) + " " + (int)(body.getPosition().y * OBEY.PPM / OBEY.TILE_SIZE + bounds.getHeight() / OBEY.TILE_SIZE / 2));
        for (int i = (int)(body.getPosition().x * OBEY.PPM / OBEY.TILE_SIZE - bounds.getWidth() / OBEY.TILE_SIZE / 2);
             i<=(int)(body.getPosition().x * OBEY.PPM / OBEY.TILE_SIZE + bounds.getWidth() / OBEY.TILE_SIZE / 2); i++){
            for (int j = (int)(body.getPosition().y * OBEY.PPM / OBEY.TILE_SIZE - bounds.getHeight() / OBEY.TILE_SIZE / 2);
                 j<=(int)(body.getPosition().y * OBEY.PPM / OBEY.TILE_SIZE + bounds.getHeight() / OBEY.TILE_SIZE / 2);j++) {
                //Gdx.app.log("Brick " + i, "Collision " + j);
                //layer.getCell(i, j).setTile(null);
                //layer.getCell(348, 370).setTile(null);
                //TiledMapTileLayer.Cell a = layer.getCell(i, layer.getHeight() - j);
                TiledMapTileLayer.Cell a = layer.getCell(i, j);
                if (!(a == null)){
                    //Gdx.app.log(" ffff" + i, " " + j);
                    a.setTile(null);
                    }
                //Gdx.app.log(" " + i, " " + j);

            }
        }
    }
    /*public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        //Gdx.app.log("Brick", "Collision");
        return layer.getCell((int)(body.getPosition().x * OBEY.PPM / OBEY.TILE_SIZE),
                (int)(body.getPosition().y * OBEY.PPM / OBEY.TILE_SIZE));
    }*/
}
