package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.LOG;

import java.util.EnumSet;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class AttributeModifierEffect extends Effect {

    boolean initialized = false;

    /**
     * Changes the sign of the dice roll (does not affect the modifier)
     */
    boolean negative;


    EnumSet<EffectTargetType> attributeType;

    public enum EffectTargetType {
        DEFENCE, ATTACK, MOVEMENT, ACTIONS
    }



    public AttributeModifierEffect() {
        super();

        initialized = false;
        effectClass = EffectClass.ATTRIBUTE_MODIFIER;
        effectType = EffectType.BUFF;
        color = Color.BLUE;
        negative = false;

        if (pic == null) pic = "effect_blue";
    }




    public boolean isNegative() {
        return negative;
    }

    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    public EnumSet<EffectTargetType> getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(EnumSet<EffectTargetType> attributeType) {
        this.attributeType = attributeType;
    }

    public int getAttributeModifier() {

        return roll.getRoll();

    }


    /**
     * Sets the modifier to be applied. This happens only once when the effect is first initialized.
     *
     * @return
     */
    public void init() {
        super.init();
        if (!initialized) {
            roll = new Roll(Roll.RollType.GENERIC, diceNumber, diceSides, modifier);
            roll.roll();

            if (attributeType.contains(EffectTargetType.ACTIONS)) {
                getTarget().setAvailableActions(getTarget().getAvailableActions() + roll.getRoll());
            }

            effectTriggered();
            initialized = true;
        }
    }

    /**
     * This effect simply affects other damage effects, does nothing else
     *
     * @param d
     * @return
     */
    public void process(Effect d) {
        super.process(d);
    }


    public String toString() {
        return "*" + getEffectClass() + " (" + roll.getRoll() + " HP)";
    }


    public String getDescription() {
        String description = "Adds ";

        if (diceNumber != 0) description += "" + diceNumber + "d" + diceSides + (modifier > 0 ? "+" : "");
        if (modifier != 0) description += "" + modifier + " ";
        for (EffectTargetType ett : attributeType)
            description += " " + ett.name().toLowerCase();
        if (duration > 0) description += " (" + duration + " rounds)";


        return description;


    }

    public void copy(AttributeModifierEffect e) {
        System.out.println("Copied 2");
        super.copy(e);
        this.attributeType = e.attributeType;
        this.negative = e.negative;

    }

    public AttributeModifierEffect copy() {
        System.out.println("Copied 1");
        AttributeModifierEffect e = new AttributeModifierEffect();
        e.copy(this);
        e.negative = negative;
        e.attributeType = attributeType;

        return e;
    }


}
