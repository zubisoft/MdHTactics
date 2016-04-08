package com.mygdx.mdh.game.model;

import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.util.Dice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 28/01/2016.
 */



public class Ability {



    /**
     * Main type of the ability
     */
    public enum AbilityType {
        RANGED, MELEE, BUFF, DEBUFF, HEAL
    }

    AbilityType type;

    /**
     * Type of target for the ability
     */
    public enum AbilityTarget {
        SELF, ONE_ALLY, ALL_ALLIES, ONE_ENEMY, ALL_ENEMIES, ONE_ANY, ANY
    }

    AbilityTarget targetType;




    Character source;
    Character target;

    String pic;
    String name;

    String message;

    /**
     * Number and type of dice to roll
     */
    int diceNumber;
    int diceSides;

    /**
     * Effects that are applied by using the ability
     */
    List<Effect> effects;

    public Ability() {
        effects = new ArrayList<Effect>();
        //addEffect(new Effect("FIRE"));
    }


    public void apply (Character target) {

        /*
        int rolledTotal=0;
        if (diceNumber>0)  rolledTotal = Dice.roll(diceNumber, diceSides);

        //TODO Handle any modifiers
        target.getEffects();

        switch (type) {
            case HEAL:
                //Apply the main ability
                target.hit(- rolledTotal);

                //Apply secondary effects
                if (effects != null)
                    target.addEffect(effects);

                System.out.println("[Ability] "+ source + " ha curado "+ target);
                message = "Healed";
                break;

            case RANGED:
                //Apply the main ability
                target.hit(rolledTotal );

                //Apply secondary effects
                if (effects != null)
                    target.addEffect(effects);

                System.out.println("[Ability] "+ source+ " ha zumbado "+ target);
                message = "Hit";
                break;

            case MELEE:
                //Apply the main ability
                target.hit(rolledTotal );

                //Apply secondary effects
                if (effects != null)
                    target.addEffect(effects);

                System.out.println("[Ability] "+ source+ " ha zumbado "+ target);
                message = "Hit";
                break;

            case BUFF:
                //Apply secondary effects
                if (effects != null)
                    target.addEffect(effects);

                System.out.println("[Ability] Applied BUFF to "+target+" "+effects.size());
                message = "Applied Buff";
                break;

            case DEBUFF:
                //Apply secondary effects
                if (effects != null)
                    target.addEffect(effects);

                System.out.println("[Ability] "+ source+ " ha zumbado "+ target);
                message = "Applied DeBuff";
                break;
        }
        */

        //Apply secondary effects
        if (effects != null)
            target.addEffect(effects);

        source.setAvailableActions(source.getAvailableActions() - 1);

        System.out.println("[Ability] Actions left: "+ source.getAvailableActions());
    }

    public Character getSource() {
        return source;
    }

    public void setSource(Character source) {
        this.source = source;
    }

    public AbilityType getType() {
        return type;
    }

    public String getPic() {
        return pic;
    }

    public void setType(AbilityType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void addEffect(Effect e) {
        effects.add(e);
    }

    public Character getTarget() {
        return target;
    }

    public void setTarget(Character target) {
        this.target = target;
        if (effects.size() >0) {
            for(Effect e: effects) e.setTarget(target);
        }
    }

    public String getMessage() {
        return message;
    }

    public int getRange() {
        return 2;
    }

    public AbilityTarget getTargetType() {
        return targetType;
    }

    public void setTargetType(AbilityTarget targetType) {
        this.targetType = targetType;
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


} //Ability