package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.LOG;

import java.util.*;


/**
 * Created by zubisoft on 08/03/2016.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "effectClass")

@JsonSubTypes({
          @JsonSubTypes.Type(value = ShieldEffect.class, name = "SHIELD")
        , @JsonSubTypes.Type(value = DamageEffect.class, name = "DAMAGE")
        , @JsonSubTypes.Type(value = ProtectionEffect.class, name = "PROTECTION")
        , @JsonSubTypes.Type(value = HealEffect.class, name = "HEAL")
        , @JsonSubTypes.Type(value = StunEffect.class, name = "STUN")
        , @JsonSubTypes.Type(value = RemoverEffect.class, name = "REMOVER")
        , @JsonSubTypes.Type(value = DamageModifierEffect.class, name = "DAMAGE_MODIFIER")
        , @JsonSubTypes.Type(value = AttributeModifierEffect.class, name = "ATTRIBUTE_MODIFIER")
        , @JsonSubTypes.Type(value = Effect.class, name = "GENERIC")
})


public class Effect  /*implements Cloneable*/  {


    public String name;

    /**
     * Class of the effect - Defines which class to use when instantiating effects.
     * This will determine what the effect does.
     */
    public enum EffectClass {
        DAMAGE, HEAL, STUN, SHIELD, DAMAGE_MODIFIER, ATTRIBUTE_MODIFIER, REMOVER
    }

    EffectClass effectClass;

    /**
     * Defines the top level classification of the effect.
     * Used to classify and handle effects.
     */
    public enum EffectType {
        DAMAGE, BUFF, DEBUFF, MELEE, RANGED
    }

    public EffectType getEffectType() {
        return effectType;
    }

    public void setEffectType(EffectType effectType) {
        this.effectType = effectType;
    }

    EffectType effectType;


    /**
     * Defines the second level classification of the effect.
     * Used to classify and handle effects in a more refined way.
     */
    public enum EffectSubType {
        FIRE, ICE, MELEE, RANGED, MAGIC, TECH, DIVINE, FANTASY, SCIFI, BIO, POISON, ENERGY, MENTAL, EVIL, GOOD,PIERCING, SLASHING,ELECTRIC
    }

    EnumSet<EffectSubType> effectSubType;

    public enum EffectTargetType {
        SELF, ALLIES, BADDIES, ALL
    }


    EffectTargetType effectTargetType;

    /**
     * Moment when the effect is applied
     */
    public enum GameSegmentType {
        TURN_START, IMMEDIATE
    }

    GameSegmentType gameSegment;

    @JsonIgnore    Character source;
    @JsonIgnore    Character target;

    //File with the textures
    String pic;
    String icon;
    String outcome;
    Color color;

    //Effect definition
    int duration;
    float chance;
    float chanceModifier;
    int hits;
    int stacking = 0;


    //Number and type of dice to roll, plus roll modifier (e.g. 2d6+3)

    @JsonIgnore Roll roll;
    @JsonIgnore float chanceRoll;

    int diceNumber;
    int diceSides;
    int modifier;

    @JsonIgnore
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    boolean cancelled = false;


    boolean defaultIcon=true;





    boolean failed = false;

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }


    /**
     *  Defines the list of effect names that will enable this effect to trigger.
     *  The default behaviour is to apply the effect only if any effect of the specified name is present.
     *  If empty, the effect is applied without checking the presence of any effects.
    * */
    Set<String> conditionalEffects;



    @JsonIgnore
    public boolean isValidTarget() {

        //Check if required effects are present in the target
        if (conditionalEffects.size()>0 ) {
            boolean valid = false;
            for (Effect e: target.getEffects()) {
                if (conditionalEffects.contains(e.getName())) {
                    valid = true;
                }
            }
            if (!valid) return false;
        }




        //Check if the target is applicable
        switch (effectTargetType) {
            case ALL: return true;
            case SELF: if (source == target) return true;
            case ALLIES: if (target.isFriendly()) return true;
            case BADDIES: if (!target.isFriendly()) return true;
        }
        return false;
    }





    List<EffectListener> effectListeners;

    public Effect () {
        roll = new Roll(Roll.RollType.GENERIC);
        chanceModifier =0;
        modifier=0;
        duration =0;
        chance=1;
        hits = 1;
        effectListeners = new ArrayList<>();
        effectSubType = EnumSet.noneOf(EffectSubType.class);
        icon="effect-icon-generic";
        color=Color.WHITE;

        effectTargetType = EffectTargetType.ALL;

        conditionalEffects = new HashSet<>();

        gameSegment = GameSegmentType.IMMEDIATE;
        pic="core/assets/graphics/effects/effect_gunshot.png";
    }


    /**
     * Copies an effect using another as template - Listeners are not carried to the copy.
     * @param e
     */
    public void copy (Effect e) {
        name = e.name;
        effectClass = e.effectClass;
        effectType = e.effectType;
        effectSubType = e.effectSubType;
        effectTargetType=e.effectTargetType;
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
        chanceModifier = e.chanceModifier;
        stacking = e.stacking;
        effectListeners = new ArrayList<>();
        modifier = e.modifier;
        hits = e.hits;
        cancelled=e.cancelled;
        effectTargetType = e.effectTargetType;
        conditionalEffects = e.conditionalEffects;

    }

    public Effect copy () {
        Effect e = new Effect();
        e.copy(this);
        return e;
    }

    /**
     * Initializes the effect.
     * Default implementation rolls the dice.
     */
    public void init() {

        if ( !isValidTarget() ) return ;
        if ( duration < 0 ) return ;

        chanceRoll = (float)Math.random();

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
        if ( !isValidTarget() ) return ;
        if ( duration < 0 ) return ;
        if (cancelled) return;

        if(chanceRoll>chance) return;
    }


    /**
     * Executes the effect itself on the target.
     *
     * Default implementation does nothing.
     */
    public void execute () {
        if ( !isValidTarget() ) return ;
        if ( duration < 0 ) return ;
        if ( cancelled ) return;

        if(chanceRoll>chance) return;

    }

    /**
     * Applies the effect to the target, according to the current status of the roll.
     * Default implementation simply attaches the effect to the target if the stacking is not maxed.
     */
    public void apply() {
        if ( !isValidTarget() ) return ;
        if ( duration < 0 ) return ;
        if (cancelled) return;

       // if(chanceRoll>chance) return;

        //System.out.println("[Effect] Applying Effect "+this.getEffectClass()+" stacking: "+target.getEffectsByName(this.getName()).size());
        for (Effect e: target.getEffects()) {
            System.out.println( "* "+e.getName()+"\n");
        }

        if (target.getEffectsByNameAndClass(this.name, this.effectClass).size() <= stacking)
            target.addEffect(this);
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
            return "Applies "+ name;
        else if (chance < 1)
            return "Applies "+ name +" ("+(int)Math.floor(chance*100)+"% chance, "+duration+" rounds)";
        else
            return "Applies "+ name +" ("+duration+" rounds)";

    }

    public void startTurn() {
        duration--;
    }



    public Set<String> getConditionalEffects() {
        return conditionalEffects;
    }

    public void setConditionalEffects(Set<String> conditionalEffects) {
        this.conditionalEffects = conditionalEffects;
    }

    public void addConditionalEffects(String effect) {
        this.conditionalEffects.add(effect);
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

    public EffectClass getEffecType() {

        return effectClass;
    }


    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }


    public void setEffecType(EffectClass effecType) {


        this.effectClass = effecType;
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


    public EffectClass getEffectClass() {
        return effectClass;
    }

    public void setEffectClass(EffectClass effectClass) {
        this.effectClass = effectClass;
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
       return ""+ getEffectClass()+" ("+getDuration()+" rounds)";
    }

    public String notification() {
        return ""+ getName()+" ("+getDuration()+" rounds)";
    }

    public boolean isDefaultIcon() {
        return defaultIcon;
    }

    /**
     * Sets the target of the effect
     * If this is set to self, it will override whatever target is provided and will retarget to the source.
     * This approach allows abilities with effects that execute on both target and source.
     * @return
     */
    public EffectTargetType getEffectTargetType() {
        if (effectTargetType == EffectTargetType.SELF) target = source;
        return effectTargetType;
    }

    public void setEffectTargetType(EffectTargetType effectTargetType) {
        this.effectTargetType = effectTargetType;
    }

    public float getChanceModifier() {
        return chanceModifier;
    }

    public void setChanceModifier(float chanceModifier) {
        this.chanceModifier = chanceModifier;
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
