package com.mygdx.mdh.game.model;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 01/02/2016.
 */
public class MapCell {



    public enum CellType {
        NORMAL, IMPASSABLE
    }

    public enum SubCellType {
        PLAIN, ROCK
    }

    CellType cellType;



    SubCellType subCellType;

    Character character;

    protected boolean occupied;

    Vector2 mapCoordinates;
    Vector2 cartesianCoordinates;

    @JsonIgnore
    Map map;



    public MapCell() {
        this.cellType = CellType.NORMAL;
        this.mapCoordinates = new Vector2();
        this.cartesianCoordinates = new Vector2();
        character=null;
    }

    public MapCell(int column, int row) {
        this.cellType = CellType.NORMAL;
        this.mapCoordinates = new Vector2();
        this.cartesianCoordinates = new Vector2();
        setMapCoordinates(column,row);
        character=null;
    }


    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }


    public String toString () {
        return mapCoordinates.toString();
    }



    public boolean isOccupied() {
        return occupied;
    }

    public boolean isImpassable() {
        return cellType==CellType.IMPASSABLE;
    }

    public Character getCharacter() {
        return character;
    }

    void setOccupied(Character c) {

        if (c==null) {
            this.occupied = false;
            map.unblockMapCell(this);
        } else {
            this.occupied = true;
            map.blockMapCell(this);
        }

        this.character=c;

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

    public SubCellType getSubCellType() {
        return subCellType;
    }

    public void setSubCellType(SubCellType subCellType) {
        this.subCellType = subCellType;
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
