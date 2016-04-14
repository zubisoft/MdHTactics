package com.mygdx.mdh.game.map;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.mdh.game.controller.TiledMapClickListener;
import com.mygdx.mdh.game.model.Map;
import com.mygdx.mdh.game.model.MapCell;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.LOG;


/**
 * Created by zubisoft on 22/02/2016.
 */
public class IsoMapActor extends Group{
    Sprite s;
    IsoMapCellActor[][] mapCells = new IsoMapCellActor[10][10];

    final int CELLWIDTH = 128;
    final int CELLHEIGTH = 64;

    Color blueHighlight = new Color(0.0f,0.5f,1f,0.2f);
    Color greenHighlight = new Color(0.2f,1f,0.f,0.2f);
    Color redHighlight   = new Color(1f,0.1f,0.1f,0.5f);


/*
    public IsoMapActor() {

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {

                //Cartesian position (map cells)
                Vector2 logicalCoordinates = new Vector2();
                logicalCoordinates.x = x-(float)Math.floor(y/2.0f);
                logicalCoordinates.y = x+(float)Math.ceil(y/2.0f);

                MapCell c = new MapCell(logicalCoordinates);
                mapCells[x][y] = new IsoMapCellActor(c);

                //mapCells[x][y].setMapCoordinates((int)logicalCoordinates.x,(int)logicalCoordinates.y );
                mapCells[x][y].setWidth(CELLWIDTH);
                mapCells[x][y].setHeight(CELLHEIGTH);
                mapCells[x][y].setPosition(x*CELLWIDTH+Math.floorMod(y,2)*CELLWIDTH/2, y*CELLHEIGTH/2+100);


                EventListener eventListener = new TiledMapClickListener(mapCells[x][y]);
                mapCells[x][y].addListener(eventListener);

                this.addActor(mapCells[x][y]);

            }
        }






    }

*/


    public IsoMapActor(Map map) {

        //map = Map.loadFromJSON("core/assets/maps/map01.txt");


        for (int row = 0; row < map.getCellWidth(); row++) {
            for (int column = 0; column < map.getCellHeight(); column++) {


                //They come inversed in the file for convienece when writing the file...
                MapCell c = map.getMapCells()[row][column];
                c.setMapCoordinates(column,row);

                //c.setMapCoordinates(column, row);
                mapCells[column][row] = new IsoMapCellActor(c);

                //mapCells[x][y].setMapCoordinates((int)logicalCoordinates.x,(int)logicalCoordinates.y );
                mapCells[column][row].setWidth(CELLWIDTH);
                mapCells[column][row].setHeight(CELLHEIGTH);
                mapCells[column][row].setPosition(column*CELLWIDTH+Math.floorMod(row,2)*CELLWIDTH/2, (CELLHEIGTH*5)-(row*CELLHEIGTH/2)+75);


                EventListener eventListener = new TiledMapClickListener(mapCells[column][row]);
                mapCells[column][row].addListener(eventListener);

                this.addActor(mapCells[column][row]);

                if (c.getCellType()== MapCell.CellType.IMPASSABLE)  mapCells[column][row].setVisible(false);

            }
        }






    }


    public void draw(SpriteBatch batch) {

        for(int z = 0; z < 10; z++) {
            for(int x = 0; x < 10; x++) {
                if(mapCells[x][z].isVisible()) mapCells[x][z].draw(batch,1.0f);
            }
        }

    }

    public IsoMapCellActor getCell (int x, int y) {

        Vector2 aux;

        for(int z = 0; z < 10; z++) {
            for(int b = 0; b < 10; b++) {
                aux = mapCells[b][z].getMapCoordinates();
                if (aux.x == x & aux.y == y)
                     return mapCells[b][z];
            }
        }
        return null;

    }

    public IsoMapCellActor getCell (Vector2 coords) {

        Vector2 aux;

        for(int z = 0; z < 10; z++) {
            for(int b = 0; b < 10; b++) {
                aux = mapCells[b][z].getMapCoordinates();

                if (aux.x == coords.x & aux.y == coords.y)
                    return mapCells[b][z];
            }
        }
        return null;

    }

    public void highlightCells (IsoMapCellActor cell, int movementRadius, int abilityRadius) {
        removeHighlightCells();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (!mapCells[y][x].getCell().isOccupied()) {
                if ( distance(mapCells[y][x],cell) <= movementRadius ) {
                    //System.out.println("Highlighted "+mapCells[y][x].getMapCell()+" "+cell.getMapCell()+" "+Math.ceil(distance(mapCells[y][x],cell)));

                        mapCells[y][x].highlight(blueHighlight);
                    }
                } else { //occupied
                        if (distance(mapCells[y][x], cell) <= abilityRadius
                                //&&!mapCells[y][x].getCell().getCharacter().isFriendly()
                                && distance(mapCells[y][x], cell) >0
                                && !mapCells[y][x].getCell().getCharacter().isDead()) {
                            mapCells[y][x].highlight(redHighlight);

                        }
                }

            }
        }


    }

    public void removeHighlightCells () {

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {

                    mapCells[y][x].removeHighlight();

            }
        }
    }



    public static float distance (IsoMapCellActor c1, IsoMapCellActor c2) {

        return (float)Math.ceil(c1.getCell().getCartesianCoordinates().dst(c2.getCell().getCartesianCoordinates()));
    }

    public static float distance (Vector2 c1, Vector2 c2) {
        return (float)Math.ceil(c1.dst(c2));
    }

    public int getCellWidth () {
        return 10;
    }

    public int getCellHeigth () {
        return 10;
    }
}

