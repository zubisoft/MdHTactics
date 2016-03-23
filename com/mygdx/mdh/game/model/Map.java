package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.mdh.game.util.Constants;

/**
 * Created by zubisoft on 20/03/2016.
 */
public class Map {

    MapCell[][] mapCells = new MapCell[Constants.MAX_MAP_CELLWIDTH][Constants.MAX_MAP_CELLHEIGHT];

    public Map() {}


    public static Map loadFromJSON (String name) {

        FileHandle file = Gdx.files.internal(name);
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

        return emp;

    }

    public MapCell[][] getMapCells() {
        return mapCells;
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
}
