package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 20/03/2016.
 */
public class Map {

    MapCell[][] mapCells = new MapCell[Constants.MAX_MAP_CELLWIDTH][Constants.MAX_MAP_CELLHEIGHT];



    public Map() {

    }

    /*
    @JsonCreator
    public  Map(@JsonProperty("mapId") String mapId) {

        FileHandle file = Gdx.files.internal("core/assets/data/maps/"+"map_C01M01"+".txt");
        String jsonData = file.readString();


        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        Map emp = new Map();

        try {

            //System.out.println("Employee Object\n"+jsonData);
            emp = objectMapper.readValue(jsonData, Map.class);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    */




    public static Map loadFromJSON (String mapId) {
        FileHandle file = Gdx.files.internal("core/assets/data/maps/"+mapId+".txt");
        String jsonData = file.readString();

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        Map emp = new Map();

        try {

            //System.out.println("Employee Object\n"+jsonData);
            emp = objectMapper.readValue(jsonData, Map.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int row = 0; row < emp.getCellWidth(); row++) {
            for (int column = 0; column < emp.getCellHeight(); column++) {
                emp.mapCells[row][column].setMapCoordinates(column,row);
             }
        }


        return emp;

    }

    public MapCell[][] getMapCells() {
        return mapCells;
    }

    public MapCell getCell(int row, int column) {
        return mapCells[row][column];
    }

    /**
     * Sets the mapCells array.
     * Required to load from JSON.
     * @param mapCells
     */
    public void setMapCells(MapCell[][] mapCells) {
        this.mapCells = mapCells;
    }

    public String toString () {
        String s = "";
        for (int x=0; x<10; x++)
            for (int y=0; y<10; y++)
                s = s+mapCells[x][y].cellType+",";
        return s;
    }

    public int getCellWidth () {
        return Constants.MAX_MAP_CELLWIDTH;
    }

    public int getCellHeight() {
        return Constants.MAX_MAP_CELLHEIGHT;
    }


    public static float distance (MapCell c1, MapCell c2) {

        return (float)Math.ceil(c1.getCartesianCoordinates().dst(c2.getCartesianCoordinates()));
    }

    /**
     * Getll all cells in the map within the given radius (in cells) from the cell.
     * @param cell
     * @param radius
     * @return
     */
    public List<MapCell> getCells (MapCell cell, int radius) {
        List<MapCell> cells = new ArrayList<>();

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if ( distance(mapCells[y][x],cell) <= radius &&  !mapCells[y][x].isOccupied()  ) {
                    cells.add(mapCells[y][x]);
                }
            }
        }

        return cells;
    }

}

