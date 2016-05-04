package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class DamageModifierEffect extends Effect {

    boolean initialized=false;
    int initialRoll;

    boolean negative;

    public DamageModifierEffect() {
        super();

        initialized = false;
        effectType=EffectType.DAMAGE_MODIFIER;
        color= Color.BLUE;
    }

    /**
     * Set the initial shield strength
     * @return
     */
    public void init() {
        if (!initialized) {
            rolledResult = (negative?-1:1)*Dice.roll(diceNumber, diceSides) + modifier;
        }
    }

    /**
     * This effect simply affects other damage effects, does nothing else
     * @param d
     * @return
     */
    public void process(Effect d) {

        if ( rolledResult == 0 ) return;

        if (d.getEffectType()==EffectType.DAMAGE) {

            d.getRoll().addModifier(rolledResult);

            LOG.print(2,"[DamageModifierEffect] Applied: "+rolledResult+" damage modifier", LOG.ANSI_BLUE);

            effectTriggered ();
        }
    }


    public String toString() {
        return "*"+getEffectType()+" ("+rolledResult+" HP)";
    }

    public String notification() {
        return " Applied "+(negative?"-":"+")+" damage modifier ";
    }

    public DamageModifierEffect copy () {

        DamageModifierEffect e = new DamageModifierEffect();
            e.name = name;
            e.effectType = effectType;
            e.effectSubType = effectSubType;
            e.gameSegment = gameSegment;
            e.duration = duration;
            e.roll = roll;
            e.chance = chance;
            e.source = source;
            e.target = target;
            e.pic = pic;
            e.icon = icon;
            e.outcome = outcome;
            e.color = color;
            e.diceNumber = diceNumber;
            e.diceSides = diceSides;
            e.percentModifier = percentModifier;
            e.rolledResult = rolledResult;
            e.stacking = stacking;
            e.effectListeners  = new ArrayList<>();
            e.modifier = modifier;

            e.negative = negative;

            return e;

    }

    public boolean isNegative() {
        return negative;
    }

    public void setNegative(boolean negative) {
        LOG.print("[DME] negative set "+negative);
        this.negative = negative;
    }
}
