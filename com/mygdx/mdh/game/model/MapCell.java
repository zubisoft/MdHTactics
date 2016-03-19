package com.mygdx.mdh.game.model;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by zubisoft on 01/02/2016.
 */
public class MapCell {
    Vector2 mapCoordinates;


    public MapCell() {
        this.mapCoordinates = new Vector2();
    }

    public MapCell(Vector2 coordinates) {
        this.mapCoordinates = coordinates;
    }

    public MapCell(int x, int y) {
        this.mapCoordinates = new Vector2(x,y);
    }

    public Vector2 getMapCoordinates() {
        return mapCoordinates;
    }

    public String toString () {
        return mapCoordinates.toString();
    }


}
