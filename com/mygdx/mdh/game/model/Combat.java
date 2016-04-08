package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

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



    boolean playerTurn;




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

    public void populateSampleMap() {


        Character c = Character.loadFromJSON("core/assets/characters/zubi_effects.txt");
        c.setCell(new MapCell(3,3));
        this.addCharacter(c);


        c = Character.loadFromJSON("core/assets/characters/fireknight.txt");
        c.setCell(new MapCell(8,3));
        this.addCharacter(c);

    }

    public GameStepType getGameStep() {
        return gameStep;
    }

    public void setGameStep(GameStepType gameStep) {
        this.gameStep = gameStep;
    }




    /**
     * Sets the combat map.
     * Used when loading from Json.
     * @param mapName
     */
    public void setMapFile(String mapName) {
        map = Map.loadFromJSON("core/assets/maps/"+mapName+".txt");
    }

    /**
     * Adds a character.
     * Used when loading from Json.
     * @param character
     */
    public void setCharacterFiles(List<Character> chars) {
        for(Character character: chars) {
            Character c = Character.loadFromJSON("core/assets/characters/" + character.getName() + ".txt");
            c.setRow(character.getRow());
            c.setColumn(character.getColumn());

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
        for (Character c: emp.characters) {
            System.out.println("row"+emp.map.getCell(c.getRow(),c.getColumn()));
            c.setCell(emp.map.getCell(c.getRow(),c.getColumn()));
        }

        return emp;

    }


    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}