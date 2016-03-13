package com.mygdx.mdh.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 28/01/2016.
 */



public class Ability {






    public enum AbilityType {
        HEAL, RANGED, MELEE
    }

    Character source;
    Character target;

    AbilityType type;
    String pic;
    String name;

    String message;

    /**
     * Effects that are applied by using the ability
     */
    List<Effect> effects;

    public Ability() {
        effects = new ArrayList<Effect>();
        //addEffect(new Effect("FIRE"));
    }
/*
    public Ability(Character sourceCharacter, AbilityType actionType) {
        System.out.println("--------------effect");
        source = sourceCharacter;
        type = actionType;
        if (this.type == AbilityType.HEAL )
            this.pic="core/assets/btn-heal.png";
        if (this.type == AbilityType.MELEE )
            this.pic="core/assets/btn-attack.png";
        if (this.type == AbilityType.RANGED ) {
            this.pic = "core/assets/btn-attack.png";


        }

        effects = new ArrayList<Effect>();
    }
    */

    public void apply (Character target) {

        if (this.type == AbilityType.HEAL ) {
            //Apply the main ability
            target.hit( -50 );

            //Apply secondary effects
            target.addEffect(effects);

            System.out.println("[Ability] "+ source + " ha curado "+ target);
            message = "Healed 50 HP";

        } else {
            //Apply the main ability
            target.hit( 50 );

            //Apply secondary effects
            target.addEffect(effects);

            System.out.println("[Ability] "+ source+ " ha zumbado "+ target);
            message = "Hit 50 HP";
        }

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
    }

    public String getMessage() {
        return message;
    }

} //Ability
