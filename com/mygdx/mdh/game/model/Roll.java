package com.mygdx.mdh.game.model;

import com.mygdx.mdh.game.util.Dice;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class Roll {

    int baseRoll;
    int modifier;
    float percentModifier;

    int diceNumber;
    int diceSides;

    boolean rolled;
    int rolledResult;



    /**
     * Main type of Rolls
     */
    public enum RollType {
        DAMAGE, ATTACK, GENERIC
    }

    RollType rollType;

    public Roll () {
        baseRoll=0;
        modifier=0;
        percentModifier=0;
        this.rolledResult = 0;
    }

    public Roll (RollType type) {
        baseRoll=0;
        modifier=0;
        percentModifier=0;
        rollType=type;
        this.rolledResult = 0;
    }


    public Roll (RollType type, int diceNumber, int diceSides, int modifier) {
        baseRoll=0;
        this.modifier=modifier;
        percentModifier=0;
        rollType=type;
        this.diceNumber=diceNumber;
        this.diceSides=diceSides;
        this.rolledResult = 0;
    }

    public int roll () {
        baseRoll = Dice.roll(Math.abs(diceNumber), diceSides) + modifier;
        if (diceNumber<0) baseRoll = -baseRoll;
        rolled = true;
        return Math.round((1+percentModifier)*(baseRoll+modifier));
    }

    public int getRoll () {
        if (!rolled) roll();
        return Math.round((1+percentModifier)*(baseRoll));
    }

    public int getBaseRoll() {
        return baseRoll;
    }

    public void setBaseRoll(int roll) {
        this.baseRoll = roll;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public void addModifier(int modifier) {
        this.modifier += modifier;
    }

    public float getPercentModifier() {
        return percentModifier;
    }

    public void setPercentModifier(float percentModifier) {
        this.percentModifier = percentModifier;
    }

    public void addPercentModifier(float percentModifier) {
        this.percentModifier += percentModifier;
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public void setDiceNumber(int diceNumber) {
        this.diceNumber = diceNumber;
    }

    public int getDiceSides() {
        return diceSides;
    }

    public void setDiceSides(int diceSides) {
        this.diceSides = diceSides;
    }

    public RollType getRollType() {
        return rollType;
    }

    public void setRollType(RollType rollType) {
        this.rollType = rollType;
    }


}
