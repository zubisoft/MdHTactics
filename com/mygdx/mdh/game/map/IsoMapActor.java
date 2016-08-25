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
import org.jgrapht.alg.DijkstraShortestPath;

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


    public float[][] dijkstra (MapCell initial, boolean ignoreOccupied) {

        boolean[][] visited = new boolean[10][10];
        float[][] distances = new float[10][10];

        int cartesian_x = (int)initial.getCartesianCoordinates().x;
        int cartesian_y = (int)initial.getCartesianCoordinates().y;
        int table_x = (int)initial.getMapCoordinates().y;
        int table_y = (int)initial.getMapCoordinates().x;
        int aux_tablex ;
        int aux_tabley ;


        MapCell aux;


        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                distances[i][j] = 99999;
            }
        }
        distances[table_x][table_y]=0;
        float minDist=0;
        int counter=0;



        while (minDist < 99999 && counter<100) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0)
                            //&& (current_x + i >= 0) && (current_x + i < 10)
                            //&& (current_y + j >= 0) && (current_y + j < 10)
                           ) {


                        aux = (MapCell) map.mapCellsCartesian.get(new Vector2(cartesian_x + i, cartesian_y + j));



                        if (aux != null  && !aux.isImpassable()) {
                            if ( !aux.isOccupied() || ignoreOccupied) {
                                aux_tablex = (int) aux.getMapCoordinates().y;
                                aux_tabley = (int) aux.getMapCoordinates().x;

                                if (!visited[aux_tablex][aux_tabley]) {
                                    minDist = distances[table_x][table_y] + (((i == 0) || (j == 0)) ? 1f : 1.5f);
                                    if (minDist < distances[aux_tablex][aux_tabley])
                                        distances[aux_tablex][aux_tabley] = minDist;
                                }
                            }
                        }


                    }
                }
            }

            visited[table_x][table_y]= true;


            minDist = 99999;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (!visited[i][j] && distances[i][j] < minDist) {
                        minDist = distances[i][j];
                        table_x = i;
                        table_y = j;

                    }
                }
            }


            aux =  map.getCell(table_x,table_y);


            if (aux != null) {
                cartesian_x = (int)aux.getCartesianCoordinates().x;
                cartesian_y = (int)aux.getCartesianCoordinates().y;
                table_x = (int)aux.getMapCoordinates().y;
                table_y = (int)aux.getMapCoordinates().x;
            }

            counter++;

        }

/*
        System.out.println ("Dijkstra: \n ");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                    if (distances[i][j]==99999) System.out.print("x\t");
                    else                     System.out.print(distances[i][j]+"\t");

            }
            System.out.println();

        }
*/

        return distances;




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
                    if (mapCells[y][x].getCell().getCellType() != MapCell.CellType.IMPASSABLE) {
                        mapCells[y][x].highlight(color);


                    }

                }

            }
        }





    }



    public Set<MapCell> getCellsInRange(MapCell cell, float radius) {

        Set<MapCell> result = new HashSet<>();
        /*
        IsoMapCellActor source = getCell(cell);
        Vector2 coords = cell.getMapCoordinates();

        IsoMapCellActor auxCell;

        for (int i=(int)-radius; i<= radius; i++) {
            for (int j=(int)-radius; j<= radius; j++) {
                auxCell = getCell(new Vector2(coords.x + i, coords.y + j));
                if (auxCell!= null && source.isInRange(auxCell,radius)) {
                    result.add(auxCell.getCell());
                }

            }
        }
        */
        float[][] distances = dijkstra(cell,true);
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                if (distances[i][j] <= radius) {
                    result.add(mapCells[j][i].getCell());
                }
            }
        }



        return result;
    }




    public Set<MapCell> getMovementCellsInRange(MapCell cell, float radius) {

        Set<MapCell> result = new HashSet<>();

        float[][] distances = dijkstra(cell,false);
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                if (distances[i][j] <= radius) {
                    result.add(mapCells[j][i].getCell());
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

        for (MapCell c: getMovementCellsInRange(cell.getCell(),movementRadius)) {

            getCell(c).highlight(blueHighlight);
        }



    /*
        map.unblockMapCell(cell.getCell());

        IsoMapCellActor auxCell;

        for (MapCell c: this.map.getCells (cell.getCell(),movementRadius)) {


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
*/


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
                        else if(getCell(a-(1-Math.floorMod(b,2)),b+1).getCell().isImpassable())    borderBottomLeft=true;

                    }
                    else  borderBottomLeft=true;

                    if (getCell(a+1-(1-Math.floorMod(b,2)),b+1)!=null) {
                        //if(!getCell(a+1-(1-Math.floorMod(b,2)),b+1).isShowHighlight())  borderBottomRight=true;
                        if(!getCell(a+1-(1-Math.floorMod(b,2)),b+1).isInRange(cell,abilityRadius))  borderBottomRight=true;
                        else if(getCell(a+1-(1-Math.floorMod(b,2)),b+1).getCell().isImpassable())    borderBottomRight=true;
                    }
                    else  borderBottomRight=true;

                    if (getCell(a-(1-Math.floorMod(b,2)),b-1)  !=null) {
                        //if(!getCell(a-(1-Math.floorMod(b,2)),b-1).isShowHighlight())    borderTopLeft=true;
                        if(!getCell(a-(1-Math.floorMod(b,2)),b-1).isInRange(cell,abilityRadius))    borderTopLeft=true;
                        else if(getCell(a-(1-Math.floorMod(b,2)),b-1).getCell().isImpassable())    borderTopLeft=true;
                    }
                    else  borderTopLeft=true;

                    if (getCell(a+1-(1-Math.floorMod(b,2)),b-1)!=null) {
                        //if(!getCell(a+1-(1-Math.floorMod(b,2)),b-1).isShowHighlight())  borderTopRight=true;
                        if(!getCell(a+1-(1-Math.floorMod(b,2)),b-1).isInRange(cell,abilityRadius))  borderTopRight=true;
                        else if(getCell(a+1-(1-Math.floorMod(b,2)),b-1).getCell().isImpassable())    borderTopRight=true;
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

