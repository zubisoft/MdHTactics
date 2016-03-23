package com.mygdx.mdh.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 28/01/2016.
 */

public class Combat {

    List<Character> characters;

    public enum GameStepType {
        START_TURN,MOVING,SELECTION,TARGETING,ACTION_SELECTION,BADDIES
    }

    GameStepType gameStep;



    Ability currentSelectedAbility;

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

    public Ability getCurrentSelectedAbility() {
        return currentSelectedAbility;
    }

    public void setCurrentSelectedAbility(Ability currentSelectedAbility) {
        this.currentSelectedAbility = currentSelectedAbility;
    }



    /**
     * Sets the combat map.
     * @param mapName
     */
    public void setMapFile(String mapName) {

    }



}