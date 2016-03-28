package com.mygdx.mdh.game.model;


/**
 * Created by zubisoft on 08/03/2016.
 */
public class Effect {




    /**
     * Main type of the effect
     */
    public enum EffectType {
        FIRE, HEAL, STUN, SHIELD
    }

    EffectType effectType;

    /**
     * Moment when the effect is applied
     */
    public enum GameSegmentType {
        TURN_START, BEFORE_HIT, AFTER_HIT, IMMEDIATE
    }

    GameSegmentType gameSegment;



    //Effect definition
    int duration;
    int damage;
    float chance;


    Character source;
    Character target;

    //File with the textures
    String pic;

    String outcome;

    /**
     * Number and type of dice to roll
     */
    int diceNumber;
    int diceSides;


    public Effect () {}

    /*
    public Effect(String type) {
        //Some sample effects
        if (type=="FIRE") {
            damage = 10;
            duration = 2;
            effectType = EffectType.FIRE;
            pic="core/assets/graphics/effects/effect_burning.png";
        }

        if (type=="MEDICAL") {
            damage = -10;
            duration = 2;
            effectType = EffectType.HEAL;
            pic="core/assets/graphics/effects/effect_burning.png";
        }

    }
    */

    public boolean apply() {

        if (target == null) return false;

        if (duration <= 0) return false;


        switch (effectType) {
            case FIRE:
                target.hit(damage);

                outcome = "Burning! " + damage + "HP";
                duration--;
                break;

            case HEAL:
                target.hit(damage);
                outcome = "Healed! " + damage + "HP";
                duration--;
                break;

            case STUN:
                if (Math.random() <= chance) {
                    target.setAvailableActions(target.getAvailableActions() - 1);
                    outcome = "Stunned!";
                }
                duration--;
                break;
        }
        return true;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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

}
