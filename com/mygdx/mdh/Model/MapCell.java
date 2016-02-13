package com.mygdx.mdh.Model;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by zubisoft on 01/02/2016.
 */
public class MapCell extends TiledMapTileLayer.Cell{
    int Cellx;
    int Celly;



    public MapCell(int cellx, int celly) {
        Cellx = cellx;
        Celly= celly;
    }

    public int getCellx() {
        return Cellx;
    }

    public void setCellx(int cellx) {
        Cellx = cellx;
    }

    public int getCelly() {
        return Celly;
    }

    public void setCelly(int celly) {
        Celly = celly;
    }
}
