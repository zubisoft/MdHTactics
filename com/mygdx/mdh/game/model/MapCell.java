package com.mygdx.mdh.game.model;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by zubisoft on 01/02/2016.
 */
public class MapCell {



    public enum CellType {
        NORMAL, IMPASSABLE
    }

    CellType cellType;

    boolean occupied;

    Vector2 mapCoordinates;
    Vector2 cartesianCoordinates;



    public MapCell() {
        this.cellType = CellType.NORMAL;
        this.mapCoordinates = new Vector2();
        this.cartesianCoordinates = new Vector2();
    }

    public MapCell(int column, int row) {
        this.cellType = CellType.NORMAL;
        this.mapCoordinates = new Vector2();
        this.cartesianCoordinates = new Vector2();
        setMapCoordinates(column,row);
    }



    public String toString () {
        return mapCoordinates.toString();
    }



    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }


    /**
     * Set the type of the cell.
     * Required to load from JSON.
     * @param cellType
     */
    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public CellType getCellType() {
        return cellType;
    }

    /**
     * Sets the coordinates of this cell in a map.
     * Also sets the cartesian coordinates to help with distance calculations.
     * @return
     */
    public void setMapCoordinates(int column, int row) {
        this.mapCoordinates = new Vector2(column,row);

        cartesianCoordinates.x = mapCoordinates.x-(float)Math.floor(mapCoordinates.y/2.0f);
        cartesianCoordinates.y = mapCoordinates.x+(float)Math.ceil(mapCoordinates.y/2.0f);
    }

    public Vector2 getMapCoordinates() {
        return mapCoordinates;
    }

    public Vector2 getCartesianCoordinates() {
        return cartesianCoordinates;
    }



}
