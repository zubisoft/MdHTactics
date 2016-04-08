package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zubisoft on 08/03/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "effectType")

@JsonSubTypes({
          @JsonSubTypes.Type(value = ShieldEffect.class, name = "SHIELD")
        , @JsonSubTypes.Type(value = DamageEffect.class, name = "DAMAGE")
        , @JsonSubTypes.Type(value = Effect.class, name = "HEAL")
        , @JsonSubTypes.Type(value = DamageEffect.class, name = "STUN")
})


public class Effect {


    public String name;

    /**
     * Main type of the effect
     */
    public enum EffectType {
        DAMAGE, HEAL, STUN, SHIELD
    }

    EffectType effectType;

    /**
     * Moment when the effect is applied
     */
    public enum GameSegmentType {
        TURN_START, IMMEDIATE
    }

    GameSegmentType gameSegment;

    //Effect definition
    int duration;
    Roll roll;
    float chance;

    Character source;
    Character target;

    //File with the textures
    String pic;
    String outcome;

    /**
     * Number and type of dice to roll, plus roll modifier (e.g. 2d6+3)
     */
    int diceNumber;
    int diceSides;
    int modifier;
    float percentModifier;
    int rolledResult;

    List<EffectListener> effectListeners;

    public Effect () {
        roll = new Roll(Roll.RollType.GENERIC);
        percentModifier=0;
        modifier=0;
        duration =0;
        chance=1;
        effectListeners = new ArrayList<>();
    }



    /**
     * Initializes the effect.
     * Default implementation rolls the dice.
     */
    public void init() {

        if(diceNumber>0) {
            rolledResult = Dice.roll(diceNumber, diceSides) + modifier;
            roll.setBaseRoll(rolledResult);
            roll.setModifier(modifier);
            roll.setPercentModifier(percentModifier);
        }

    }

    /**
     * Modifies an Effect object.
     * This is called every time damage is originated or directed toward any character,
     * in order to apply any modifiers to the damage to/from a character.
     * Generally used to apply the buffs/debuffs on the damage output of the source
     * and apply any protections from damage on the receiver.
     *
     * Default implementation does nothing.
     */
    public void process(Effect d) {

    }


    /**
     * Applies the effect to the target, according to the current status of the roll.
     * Default implementation simply attaches the effect to the target.
     */
    public void apply() {
            target.addEffect(this);
    }

    public void execute () {

    }

    public void effectTriggered () {

        if (effectListeners.size()==0) return;

        for (EffectListener l: effectListeners)
            l.onEffectTriggered(this);
    }

    public void addEffectListener (EffectListener l) {
        if (effectListeners.contains(l)) return;

        effectListeners.add(l);
    }

    public void startTurn() {
        duration--;
    }


    public Roll getRoll() {
        return roll;
    }

    public void setRoll(Roll roll) {
        this.roll = roll;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public EffectType getEffecType() {

        return effectType;
    }

    public void setEffecType(EffectType effecType) {

        //TODO this is just temporary for testing

        this.effectType = effecType;
    }


    public Character getSource() {
        return source;
    }

    public void setSource(Character source) {
        this.source = source;
    }

    public Character getTarget() {
        return target;
    }

    public void setTarget(Character target) {
        this.target = target;
    }


    public EffectType getEffectType() {
        return effectType;
    }

    public void setEffectType(EffectType effectType) {
        this.effectType = effectType;
    }

    public String getOutcome() {
        return outcome;
    }

    public int getDiceSides() {
        return diceSides;
    }

    public void setDiceSides(int diceSides) {
        this.diceSides = diceSides;
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public void setDiceNumber(int diceNumber) {
        this.diceNumber = diceNumber;
    }

    public GameSegmentType getGameSegment() {
        return gameSegment;
    }

    public void setGameSegment(GameSegmentType gameSegment) {
        this.gameSegment = gameSegment;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
       return "*"+getEffectType()+" ("+getDuration()+" rounds)";
    }

    public String notification() {
        return "*"+getEffectType()+" ("+getDuration()+" rounds)";
    }
}