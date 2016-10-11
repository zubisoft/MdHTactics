package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.mdh.game.EffectManager;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 28/01/2016.
 */

public class Combat {


    List<Character> characters;
    Map map;


    public enum GameStepType {
        START_TURN,MOVING,SELECTION,TARGETING,ACTION_SELECTION,BADDIES
    }

    GameStepType gameStep;
    GameStepType prevGameStep;



    boolean playerTurn;



    /** Experience gained during the combat so far **/
    int experience;



    public Combat () {
        characters = new ArrayList<Character>();
        gameStep=GameStepType.SELECTION;
        playerTurn = true;

    }

    public  List<Character> getCharacters() {
        return characters;
    }

    public void addCharacter(Character c) {
        characters.add(c);
    }
/*
    public void populateSampleMap() {


        Character c = Character.loadFromJSON("core/assets/characters/zubi_effects.txt");
        c.setCell(new MapCell(3,3));
        this.addCharacter(c);


        c = Character.loadFromJSON("core/assets/characters/fireknight.txt");
        c.setCell(new MapCell(8,3));
        this.addCharacter(c);

    }
*/
    public GameStepType getGameStep() {
        return gameStep;
    }

    public GameStepType getPrevGameStep() {
        return prevGameStep;
    }


    public void setGameStep(GameStepType gameStep) {


        this.prevGameStep = this.gameStep;
        this.gameStep = gameStep;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }



    /**
     * Sets the combat map.
     * Used when loading from Json.
     * @param mapName
     */
    public void setMapFile(String mapName) {
        map = Map.loadFromJSON(mapName);
    }

    /**
     * Adds a character.
     * Used when loading from Json.
     */
    public void setCharacterFiles(List<Character> chars) {
        for(Character character: chars) {
            Character c = Character.loadFromJSON(character.getName() );
            c.setRow(character.getRow());
            c.setColumn(character.getColumn());
            c.setFriendly(character.isFriendly());

            characters.add(c);
        }

    }

    /**
     * Loads the combat from a JSON file in the combats folder.
     * Loads the map and the characters.
     * @param name
     * @return
     */

    public static Combat loadFromJSON (String name) {

        FileHandle file = Gdx.files.internal("core/assets/combats/"+name+".txt");
        String jsonData = file.readString();

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        Combat emp = new Combat();

        try {

            emp = objectMapper.readValue(jsonData, Combat.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Important step, link characters with the map
        /*
        for (Character c: emp.characters) {
            c.setCell(emp.map.getCell(c.getRow(),c.getColumn()));
        }
        */

        emp.initCharacterPositions();

        return emp;

    }



    public void initCharacterPositions () {
        int row,col,i;
        MapCell cell;
        for (Character c: characters) {
            i=0;
            row = Dice.roll(Constants.MAX_MAP_CELLHEIGHT)-1;
            col = Dice.roll(2); //Place in the first 2 rows (excl. the border one)

            cell = map.getCell(row,(c.isFriendly()?col:Constants.MAX_MAP_CELLWIDTH-1-col));



            while (cell.isOccupied() || cell.isImpassable() ) {

                row = Dice.roll(Constants.MAX_MAP_CELLHEIGHT)-1;
                col = Dice.roll(2);

                cell = map.getCell(row,(c.isFriendly()?col:Constants.MAX_MAP_CELLWIDTH-1-col));
                i++;

                if (i>100) break;

            }

            //In some extreme case, it's possible that no cell is found if there are too many obstacles
            //The character might then be placed on top of an obstacle (not ideal, but not traumatic either)
            c.setCell(cell);
            c.setRow(row);
            c.setColumn(col);

            System.out.println("Map "+map.hashCode()+" Cell "+cell.hashCode()+" Locked "+cell+" "+cell.isOccupied());

        }
    }


    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}