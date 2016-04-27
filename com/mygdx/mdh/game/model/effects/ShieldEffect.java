package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class ShieldEffect extends Effect {

    boolean initialized=false;
    int initialRoll;

    public ShieldEffect () {
        super();

        initialized = false;
        effectType=EffectType.SHIELD;
        color= Color.BLUE;
    }

    /**
     * Set the initial shield strength
     * @return
     */
    public void init() {
        if (!initialized) {
            rolledResult = Dice.roll(diceNumber, diceSides) + modifier;
            initialRoll=rolledResult;
            effectTriggered();
            initialized=true;
        }

    }

    /**
     * This effect simply affects other damage effects, does nothing else
     * @param d
     * @return
     */
    public void process(Effect d) {

        if ( rolledResult <= 0 ) return;

        if (d.getEffectType()==EffectType.DAMAGE) {

            int blockedDamage = Math.min(rolledResult,d.getRoll().getTotalRoll());

            d.getRoll().addModifier(-blockedDamage);

            rolledResult-=blockedDamage;

            LOG.print(2,"[ShieldEffect] Absorbed: "+blockedDamage+" ("+rolledResult+") remaining", LOG.ANSI_BLUE);

            effectTriggered ();
        }
    }


    public String toString() {
        return "*"+getEffectType()+" ("+rolledResult+" HP)";
    }

    public String notification() {
        return " Shielded: "+rolledResult+"/"+initialRoll;
    }

    public ShieldEffect copy () {

        ShieldEffect e = new ShieldEffect();
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

            return e;

    }

}
