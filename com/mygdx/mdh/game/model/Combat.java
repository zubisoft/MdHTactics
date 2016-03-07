package com.mygdx.mdh.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 28/01/2016.
 */

public class Combat {

    List<Character> characters;

    String gameStep;
    Ability currentSelectedAbility;

    boolean playerTurn;

    public Combat () {
        characters = new ArrayList<Character>();
        gameStep="Selection";
        playerTurn = true;
    }

    public  List<Character> getCharacters() {
        return characters;
    }

    public void addCharacter(Character c) {
        characters.add(c);
    }

    public void populateSampleMap() {

        //Character c = new Character("hagen",true);
        Character c = Character.loadFromJSON("core/assets/characters/zubi.txt");
        c.setCellx(1);
        c.setCelly(1);
        this.addCharacter(c);
/*
        //c = new Character("zubi",true);
        c = Character.loadFromJSON("core/assets/characters/hagen.txt");
        c.setCellx(1);
        c.setCelly(2);
        this.addCharacter(c);
        */
        //c = new Character("trooper",true);
        c = Character.loadFromJSON("core/assets/characters/trooper.txt");
        c.setCellx(3);
        c.setCelly(3);
        this.addCharacter(c);
/*
        //c = new Character("walker",true);
        c = Character.loadFromJSON("core/assets/characters/walker.txt");
        c.setCellx(4);
        c.setCelly(3);
        this.addCharacter(c);
        */

        System.out.println("[Combat] Loaded character with  "+c.getHealth()+"/"+c.getMaxHealth());
    }

    public String getGameStep() {
        return gameStep;
    }

    public void setGameStep(String gameStep) {
        this.gameStep = gameStep;
    }

    public Ability getCurrentSelectedAbility() {
        return currentSelectedAbility;
    }

    public void setCurrentSelectedAbility(Ability currentSelectedAbility) {
        this.currentSelectedAbility = currentSelectedAbility;
    }


}