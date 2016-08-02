package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.math.Vector2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

import java.util.*;

import org.jgrapht.graph.*;

/**
 * Created by zubisoft on 20/03/2016.
 */
public class Map {

    String mapId;

    final int numCells = Constants.MAX_MAP_CELLWIDTH*Constants.MAX_MAP_CELLHEIGHT;

    /** Actual map structure, organized by columns and rows  **/
    private MapCell[][] mapCells = new MapCell[Constants.MAX_MAP_CELLWIDTH][Constants.MAX_MAP_CELLHEIGHT];

    /** Auxiliar structure to access the map by cartesian coordinates **/
    private final HashMap mapCellsCartesian = new HashMap<Vector2, MapCell>();

    /** Auxiliar structure to establish cell connections, cost to move, inaccesibility **/
    private ListenableUndirectedWeightedGraph<MapCell,DefaultWeightedEdge> mapGraph;

    /** Proportion of cells that will be initialized as obstacles by default **/
    float obstacleRate = 0.1f;


    public Map() {
        mapGraph = new ListenableUndirectedWeightedGraph(DefaultWeightedEdge.class);

    }


    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }


    public void initObstacles () {

        int randomRow,randomCol;
        MapCell auxCell;

        for (int i = 0; i<= obstacleRate*numCells && i<1000; i++ ) {
            randomRow = Dice.roll(Constants.MAX_MAP_CELLHEIGHT)-1;
            randomCol = Dice.roll(Constants.MAX_MAP_CELLWIDTH)-1;


            auxCell = getCell(randomRow, randomCol);
            if (auxCell.getCellType() != MapCell.CellType.IMPASSABLE) {
                auxCell.setCellType(MapCell.CellType.IMPASSABLE);
                auxCell.setSubCellType(MapCell.SubCellType.ROCK);
                this.blockMapCell(auxCell);

            }

        }

    }



    public static Map loadFromJSON (String mapId) {
        FileHandle file = Gdx.files.internal("core/assets/data/maps/"+mapId+".txt");
        String jsonData = file.readString();

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        Map emp = new Map();
        emp.mapId = mapId;

        try {

            //System.out.println("Employee Object\n"+jsonData);
            emp = objectMapper.readValue(jsonData, Map.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int row = 0; row < emp.getCellWidth(); row++) {
            for (int column = 0; column < emp.getCellHeight(); column++) {
                emp.mapCells[row][column].setMapCoordinates(column,row);
                emp.mapCells[row][column].setMap(emp);
                emp.mapCellsCartesian.put(emp.mapCells[row][column].getCartesianCoordinates(), emp.mapCells[row][column]);
                emp.mapGraph.addVertex(emp.mapCells[row][column]);
             }
        }

        LOG.print(""+emp.mapCellsCartesian);

        Vector2 auxVector ;
        MapCell source, target;

        for (int row = 0; row < emp.getCellWidth(); row++) {
            for (int column = 0; column < emp.getCellHeight(); column++) {

                for (float x=-1; x<=1;x++) {
                    for (float y=-1; y<=1;y++) {

                            source = emp.mapCells[row][column];


                            auxVector = new Vector2(source.getCartesianCoordinates());
                            auxVector.set(auxVector.x+x,auxVector.y+y);
                            //LOG.print(""+auxVector);

                            target = (MapCell)emp.mapCellsCartesian.get(auxVector);

                            ///LOG.print(source+" "+target+" "+source.getCartesianCoordinates()+" "+auxVector);

                                if (target != null && target != source) {
                                    emp.mapGraph.addEdge(source,target);

                                    if (y==0 || x==0) {
                                        DefaultWeightedEdge e= emp.mapGraph.getEdge(source,target);
                                        emp.mapGraph.setEdgeWeight(e,1.0);
                                    } else {
                                        DefaultWeightedEdge e= emp.mapGraph.getEdge(source,target);
                                        emp.mapGraph.setEdgeWeight(e,1.5);
                                    }

                                }





                    }
                }




            }
        }

       // List<DefaultWeightedEdge> list = DijkstraShortestPath.findPathBetween(emp.mapGraph,emp.mapCells[1][1],emp.mapCells[2][4]);

        //LOG.print(""+list);

        emp.initObstacles ();



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

    public Set<MapCell> getCellsRecursive (MapCell cell, double radius) {

        Set l = new HashSet<MapCell>();


        MapCell auxCell;
        for(DefaultWeightedEdge ed: mapGraph.edgesOf(cell)) {

            if (mapGraph.getEdgeWeight(ed) <= radius) {

                auxCell =  mapGraph.getEdgeTarget(ed);
                if (auxCell== cell)  auxCell = mapGraph.getEdgeSource(ed); //Although graph is non directional, the source/target remain fixed and need to be fixed this way

                //LOG.print("Expanding "+auxCell+" radius "+(radius-mapGraph.getEdgeWeight(ed)));

                l.addAll(getCellsRecursive(auxCell,radius-mapGraph.getEdgeWeight(ed)));
            }

        }

        //LOG.print("Added "+cell);
        l.add(cell);

        return l;
    }


    public void blockMapCell(MapCell c) {

            for(DefaultWeightedEdge e: mapGraph.edgesOf(c)) {
                if (mapGraph.getEdgeWeight(e)<999999) //Avoid reblocking
                    mapGraph.setEdgeWeight(e,mapGraph.getEdgeWeight(e)*999999);
            }
    }
    public void unblockMapCell(MapCell c) {

        for(DefaultWeightedEdge e: mapGraph.edgesOf(c)) {
            if (mapGraph.getEdgeWeight(e)>=999999) //Avoid reunblocking
                mapGraph.setEdgeWeight(e,mapGraph.getEdgeWeight(e)/999999);
        }
    }


}

