package com.mygdx.mdh.game.map;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.mdh.game.controller.TiledMapClickListener;
import com.mygdx.mdh.game.model.Map;
import com.mygdx.mdh.game.model.MapCell;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.LOG;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by zubisoft on 22/02/2016.
 */
public class IsoMapActor extends Group{

    Map map;

    Sprite s;
    IsoMapCellActor[][] mapCells = new IsoMapCellActor[10][10];

    final int CELLWIDTH = 128;
    final int CELLHEIGTH = 64;

    public static Color blueHighlight = new Color(0.0f,0.5f,1f,0.2f);
    public static Color greenHighlight = new Color(0.2f,1f,0.f,0.2f);
    public static Color yellowHighlight = new Color(1f,0.6f,0.f,0.5f);
    public static Color redHighlight   = new Color(1f,0.1f,0.1f,0.5f);

    public IsoMapActor(Map map) {

        //map = Map.loadFromJSON("core/assets/maps/map01.txt");

        this.map=map;


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
                mapCells[column][row].setZIndex(row);


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
                mapCells[x][z].draw(batch,1.0f);
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





    public IsoMapCellActor getCell (MapCell cell) {

        return getCell(cell.getMapCoordinates());

    }



    public void highlightCells(IsoMapCellActor cell, int radius, Color color) {

        removeHighlightCells();
/*
        if (radius==0) {
            if (cell.getCell().getCellType() != MapCell.CellType.IMPASSABLE)
                cell.highlight(color);
            return;
        }*/


        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {

                if (distance(mapCells[y][x], cell) <= radius) {
                    if (mapCells[y][x].getCell().getCellType() != MapCell.CellType.IMPASSABLE)
                        mapCells[y][x].highlight(color);
                }

            }
        }




    }



    public Set<MapCell> getCellsInRange(MapCell cell, float radius) {
        IsoMapCellActor source = getCell(cell);
        Vector2 coords = cell.getMapCoordinates();
        Set<MapCell> result = new HashSet<>();
        IsoMapCellActor auxCell;

        for (int i=(int)-radius; i<= radius; i++) {
            for (int j=(int)-radius; j<= radius; j++) {
                auxCell = getCell(new Vector2(coords.x + i, coords.y + j));
                if (auxCell!= null && source.isInRange(auxCell,radius)) {
                    result.add(auxCell.getCell());
                }

            }
        }

        return result;
    }

    /**
     * Special case of highlight cells, with default movement tile color and friendly/hostile colors *
     * @param cell
     * @param movementRadius
     * @param abilityRadius
     */
    public void highlightMovementCells(IsoMapCellActor cell, int movementRadius, int abilityRadius) {

        removeHighlightCells();



        map.unblockMapCell(cell.getCell());

        IsoMapCellActor auxCell;

        for (MapCell c: this.map.getCellsRecursive(cell.getCell(),movementRadius)) {


                auxCell = getCell(c);

                if (!auxCell.getCell().isOccupied()) {
                    if (auxCell.getCell().getCellType() != MapCell.CellType.IMPASSABLE)
                        auxCell.highlight(blueHighlight);
                } else {
                    if (auxCell.getCell().getCharacter().isFriendly())
                        auxCell.highlight(greenHighlight);
                    else auxCell.highlight(yellowHighlight);
                }


                //if (distance(mapCells[y][x], cell) <= abilityRadius) mapCells[y][x].highlight(redHighlight);


        }

        map.blockMapCell(cell.getCell());

        }











    public void outlineCells (IsoMapCellActor cell, int movementRadius, int abilityRadius) {


        int a,b;
        MapCell tmpCell;

        //Outline for the outermost border
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {


                tmpCell=getCell(x,y).getCell();
                a=(int)tmpCell.getMapCoordinates().x;
                b=(int)tmpCell.getMapCoordinates().y;

                boolean borderTopLeft=false, borderTopRight=false,borderBottomLeft=false, borderBottomRight=false;


                if (getCell(a,b).isInRange(cell,abilityRadius)) {

                    //LOG.print("MAP: "+abilityRadius+"  "+getCell(x,y)+" "+(getCell(x,y).isInRange(cell,abilityRadius)));

                    if (getCell(a-(1-Math.floorMod(b,2)),b+1)  !=null) {
                        //if(!getCell(a-(1-Math.floorMod(b,2)),b+1).isShowHighlight())    borderBottomLeft=true;
                        if(!getCell(a-(1-Math.floorMod(b,2)),b+1).isInRange(cell,abilityRadius))    borderBottomLeft=true;

                    }
                    else  borderBottomLeft=true;

                    if (getCell(a+1-(1-Math.floorMod(b,2)),b+1)!=null) {
                        //if(!getCell(a+1-(1-Math.floorMod(b,2)),b+1).isShowHighlight())  borderBottomRight=true;
                        if(!getCell(a+1-(1-Math.floorMod(b,2)),b+1).isInRange(cell,abilityRadius))  borderBottomRight=true;
                    }
                    else  borderBottomRight=true;

                    if (getCell(a-(1-Math.floorMod(b,2)),b-1)  !=null) {
                        //if(!getCell(a-(1-Math.floorMod(b,2)),b-1).isShowHighlight())    borderTopLeft=true;
                        if(!getCell(a-(1-Math.floorMod(b,2)),b-1).isInRange(cell,abilityRadius))    borderTopLeft=true;
                    }
                    else  borderTopLeft=true;

                    if (getCell(a+1-(1-Math.floorMod(b,2)),b-1)!=null) {
                        //if(!getCell(a+1-(1-Math.floorMod(b,2)),b-1).isShowHighlight())  borderTopRight=true;
                        if(!getCell(a+1-(1-Math.floorMod(b,2)),b-1).isInRange(cell,abilityRadius))  borderTopRight=true;
                    }
                    else  borderTopRight=true;

                }


                getCell(x,y).setBorders(borderBottomLeft, borderBottomRight, borderTopLeft, borderTopRight);


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

    public void removeBorders () {

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {

                mapCells[y][x].removeBorders();

            }
        }
    }



    public static float distance (IsoMapCellActor c1, IsoMapCellActor c2) {

        return distance(c1.getCell(),c2.getCell());
    }

    public static float distance (MapCell c1, MapCell c2) {


        return distance(c1.getCartesianCoordinates(),c2.getCartesianCoordinates());
    }

    public static float distance (Vector2 c1, Vector2 c2) {

        //Distance 1.5 to include all the surrounding cells of the actor.

        if (c1.dst(c2)>1 && c1.dst(c2) < 2.0f) {
            //LOG.print("distancia "+c1.getMapCoordinates()+c2.getMapCoordinates()+" "+distance(c1.getCartesianCoordinates(),c2.getCartesianCoordinates()));
            return 1.5f;
        }

        return (float)Math.ceil(c1.dst(c2));
    }

    public int getCellWidth () {
        return 10;
    }

    public int getCellHeigth () {
        return 10;
    }
}

