package com.mygdx.mdh.View;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.mdh.Model.MapCell;

/**
 * Created by zubisoft on 28/01/2016.
 */
public class MapCellActor extends Actor {

    private TiledMap tiledMap;

    //private TiledMapTileLayer tiledLayer;
    //private TiledMapTileLayer.Cell cell;

    MapCell mapCell;

    public MapCellActor(TiledMap tiledMap, MapCell mapCell) {
        this.setWidth(100);
        this.setHeight(100);
        this.setBounds(mapCell.getCellx()*this.getWidth(),mapCell.getCelly()*this.getHeight(),this.getWidth(),this.getHeight());

        this.tiledMap = tiledMap;
        //this.tiledLayer = tiledLayer;
        //this.cell = cell;
        this.mapCell = mapCell;
    }

    public MapCell getMapCell() {
        return mapCell;
    }


}