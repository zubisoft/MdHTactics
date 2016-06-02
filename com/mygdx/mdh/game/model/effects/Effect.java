package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;


/**
 * Created by zubisoft on 08/03/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "effectType")

@JsonSubTypes({
          @JsonSubTypes.Type(value = ShieldEffect.class, name = "SHIELD")
        , @JsonSubTypes.Type(value = DamageEffect.class, name = "DAMAGE")
        , @JsonSubTypes.Type(value = HealEffect.class, name = "HEAL")
        , @JsonSubTypes.Type(value = StunEffect.class, name = "STUN")
        , @JsonSubTypes.Type(value = DamageModifierEffect.class, name = "DAMAGE_MODIFIER")
})


public class Effect  /*implements Cloneable*/  {


    public String name;



    /**
     * Main type of the effect
     */
    public enum EffectType {
        DAMAGE, HEAL, STUN, SHIELD, DAMAGE_MODIFIER
    }

    EffectType effectType;


    public enum EffectSubType {
        FIRE, ICE, MELEE, RANGED, MAGIC
    }

    EnumSet<EffectSubType> effectSubType;

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

    int hits;


    Character source;
    Character target;

    //File with the textures
    String pic;
    String icon;
    String outcome;
    Color color;

    /**
     * Number and type of dice to roll, plus roll modifier (e.g. 2d6+3)
     */
    int diceNumber;
    int diceSides;
    int modifier;
    float percentModifier;
    int rolledResult;

    int stacking = 0;



    boolean defaultIcon=true;

    public void copy (Effect e) {
        name = e.name;
        effectType = e.effectType;
        effectSubType = e.effectSubType;
        gameSegment = e.gameSegment;
        duration = e.duration;
        roll = e.roll;
        chance = e.chance;
        source = e.source;
        target = e.target;
        pic = e.pic;
        icon = e.icon;
        outcome = e.outcome;
        color = e.color;
        diceNumber = e.diceNumber;
        diceSides = e.diceSides;
        percentModifier = e.percentModifier;
        rolledResult = e.rolledResult;
        stacking = e.stacking;
        effectListeners = new ArrayList<>();
        modifier = e.modifier;
        hits = e.hits;
    }

    public Effect copy () {
        Effect e = new Effect();
        e.copy(this);
        return e;
    }


    List<EffectListener> effectListeners;

    public Effect () {
        roll = new Roll(Roll.RollType.GENERIC);
        percentModifier=0;
        modifier=0;
        duration =0;
        chance=1;
        hits = 1;
        effectListeners = new ArrayList<>();
        effectSubType = EnumSet.noneOf(EffectSubType.class);
        icon="effect-icon-generic";
        color=Color.WHITE;
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
     * Default implementation simply attaches the effect to the target if the stacking is not maxed.
     */
    public void apply() {
        System.out.println("[Effect] Applying Effect "+this.getEffectType()+" stacking: "+target.getEffectsByName(this.getName()).size());
        for (Effect e: target.getEffects()) {
            System.out.println( "* "+e.getName()+"\n");
        }

        if (target.getEffectsByName(this.getName()).size() <= stacking)
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

    public String getDescription () {
        if (duration == 0)
            return "Applies "+effectType;
        else
            return "Applies "+effectType+" "+effectSubType +" during "+duration+" turns";

    }

    public void startTurn() {
        duration--;
    }



    public void setEffectSubType(EnumSet<EffectSubType> list) {
        effectSubType=list;
        /*
        effectSubType=EnumSet.noneOf(EffectSubType.class);
        effectSubType.addAll(list);*/
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {

    defaultIcon = false;
    this.icon = icon;
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


    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }


    public void setEffecType(EffectType effecType) {

        //TODO this is just temporary for testing

        this.effectType = effecType;
    }


    public EnumSet<EffectSubType> getEffectSubType() {
        return effectSubType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public boolean isDefaultIcon() {
        return defaultIcon;
    }

    /*
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            LOG.print("error cloning");
        }
        return null;
    }
    */

}
