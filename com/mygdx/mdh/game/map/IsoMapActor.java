package com.mygdx.mdh.game.map;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.mdh.game.controller.TiledMapClickListener;
import com.mygdx.mdh.game.model.MapCell;


/**
 * Created by zubisoft on 22/02/2016.
 */
public class IsoMapActor extends Group{
    Sprite s;
    IsoMapCellActor[][] mapCells = new IsoMapCellActor[10][10];

    final int CELLWIDTH = 128;
    final int CELLHEIGTH = 64;

    public IsoMapActor() {
        Texture texture = new Texture(Gdx.files.internal("core/assets/graphics/tile-transparent-256x128.png"));
/*
        for (int z = 0; z < 10; z++) {
            for (int x = 0; x < 10; x++) {
                s = new Sprite(texture);
                //s.setPosition(x, z);
                s.setSize(CELLWIDTH, CELLHEIGTH);
                s.setPosition(x*CELLWIDTH+Math.floorMod(z,2)*CELLWIDTH/2, z*CELLHEIGTH/2);

                mapCells[x][z] = new IsoMapCellActor(s);
                mapCells[x][z].setMapCoordinates(x,z);
                mapCells[x][z].setWidth(CELLWIDTH);
                mapCells[x][z].setHeight(CELLHEIGTH);
                mapCells[x][z].setPosition(x*CELLWIDTH+Math.floorMod(z,2)*CELLWIDTH/2, z*CELLHEIGTH/2);


                this.addActor(mapCells[x][z]);

            }
        }
        */


        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {

                //Graphical position
                s = new Sprite(texture);
                s.setSize(CELLWIDTH, CELLHEIGTH);
                s.setPosition(x*CELLWIDTH+Math.floorMod(y,2)*CELLWIDTH/2, y*CELLHEIGTH/2+100);

                //Cartesian position (map cells)
                Vector2 logicalCoordinates = new Vector2();
                logicalCoordinates.x = x-(float)Math.floor(y/2.0f);
                logicalCoordinates.y = x+(float)Math.ceil(y/2.0f);

                MapCell c = new MapCell(logicalCoordinates);
                mapCells[x][y] = new IsoMapCellActor(s,c);

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


    public void draw(SpriteBatch batch) {

        for(int z = 0; z < 10; z++) {
            for(int x = 0; x < 10; x++) {
                mapCells[x][z].draw(batch);
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

    public void highlightCells (Color c, IsoMapCellActor cell, int radius) {

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {

                if ( distance(mapCells[y][x],cell) <= 3 ) {
                    //System.out.println("Highlighted "+mapCells[y][x].getMapCell()+" "+cell.getMapCell()+" "+Math.ceil(distance(mapCells[y][x],cell)));
                    mapCells[y][x].highlight(c);
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

        return (float)Math.ceil(c1.getMapCoordinates().dst(c2.getMapCoordinates()));
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

