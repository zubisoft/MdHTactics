package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.LOG;

import java.util.EnumSet;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class AttributeModifierEffect extends Effect {

    boolean initialized=false;

    /**
     * Changes the sign of the dice roll (does not affect the modifier)
     */
    boolean negative;



    EnumSet<EffectTargetType> attributeType;

    public enum EffectTargetType {
        DEFENCE, ATTACK
    }


    public AttributeModifierEffect() {
        super();

        initialized = false;
        effectClass = EffectClass.ATTRIBUTE_MODIFIER;
        effectType = EffectType.BUFF;
        color= Color.BLUE;
        negative = false;
    }


    public AttributeModifierEffect copy () {
        AttributeModifierEffect e = new AttributeModifierEffect();
        e.copy(this);
        e.negative = negative;
        e.attributeType=attributeType;

        return e;
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
     * @return
     */
    public void init() {
        super.init();
        if (!initialized) {
            roll = new Roll(Roll.RollType.GENERIC,diceNumber,diceSides,modifier);
            roll.roll();

            effectTriggered();
        }
    }

    /**
     * This effect simply affects other damage effects, does nothing else
     * @param d
     * @return
     */
    public void process(Effect d) {
        super.process(d);
    }


    public String toString() {
        return "*"+ getEffectClass()+" ("+roll.getRoll()+" HP)";
    }

    public String notification() {
        return "Damage Modifier";
    }


    public String getDescription () {
        String description = "Adds "+(diceNumber*diceSides+modifier>0?"+":"-");

        if (diceNumber!=0 )      description += " "+diceNumber+"d"+diceSides+(modifier>0?"+":"");
        if (modifier!=0 )        description += " "+modifier+" to";
        for (EffectTargetType ett: attributeType)
            description += " " + ett.name().toLowerCase();
        if (duration>0)         description += " ("+duration+" rounds)";


        return description;


    }


}
