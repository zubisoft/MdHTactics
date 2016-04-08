package com.mygdx.mdh.game.model;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class Roll {

    int baseRoll;
    int modifier;
    float percentModifier;



    /**
     * Main type of Rolls
     */
    public enum RollType {
        DAMAGE, ATTACK, GENERIC
    }

    RollType rollType;

    public Roll (RollType type) {
        baseRoll=0;
        modifier=0;
        percentModifier=0;
        rollType=type;
    }

    public int getTotalRoll () {
        return Math.round((1+percentModifier)*(baseRoll+modifier));
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

    public RollType getEffectType() {
        return rollType;
    }

    public void setEffectType(RollType effectType) {
        this.rollType = effectType;
    }

}
