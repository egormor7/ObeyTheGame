package com.egormor.obey.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.egormor.obey.Screens.PlayScreen;
import com.egormor.obey.Sprites.Brick;
import com.egormor.obey.Sprites.Laser;
import com.egormor.obey.Sprites.Wall;

public class B2WorldCreator {
    private TiledMap map;

    public B2WorldCreator(PlayScreen screen){
        map = screen.getMap();

        //  create walls bodies/fixtures for other objects the same cycle
        for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Wall(screen, rect);
        }

        for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rect);
        }

        for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Laser(screen, rect);
        }
    }
}
