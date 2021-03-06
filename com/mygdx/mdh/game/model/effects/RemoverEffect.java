package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;

import java.util.EnumSet;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class RemoverEffect extends Effect {

    boolean initialized=false;

    /**
     * Changes the sign of the dice roll (does not affect the modifier)
     */


    public EnumSet<EffectType> getTargetType() {
        return targetType;
    }

    public void setTargetType(EnumSet<EffectType> targetType) {
        this.targetType = targetType;
    }

    public EnumSet<EffectSubType> getTargetSubType() {
        return targetSubType;
    }

    public void setTargetSubType(EnumSet<EffectSubType> targetSubType) {
        this.targetSubType = targetSubType;
    }

    EnumSet<EffectType> targetType;
    EnumSet<EffectSubType> targetSubType;




    public RemoverEffect() {
        super();

        initialized = false;
        effectClass = EffectClass.REMOVER;
        effectType = EffectType.DEBUFF;
        color= Color.BLUE;

        targetType = EnumSet.noneOf(EffectType.class);
        targetSubType = EnumSet.noneOf(EffectSubType.class);

        if (pic ==null) pic="effect_red";

    }


    public RemoverEffect copy () {
        RemoverEffect e = new RemoverEffect();
        e.copy(this);

        e.targetType=targetType;
        e.targetSubType=targetSubType;

        return e;
    }

    public boolean process(Effect e) {
        if (!super.process(e)) return false;
        if (targetType.contains(e.getEffectType()) || targetSubType.contains(e.getEffectSubType())) {
            e.setDuration(-1);
        }
        return true;

    }


    public boolean execute () {
        if(!super.execute()) return false;



        for (Effect e: target.getEffects()) {

            if (targetType.contains(e.getEffectType()) || targetSubType.contains(e.getEffectSubType())) {
                e.setDuration(-1);
            }
        }
        target.cleanEffects();

        effectTriggered();
        return true;

    }



    public String toString() {
        return "*"+ getEffectClass()+" ("+roll.getRoll()+" HP)";
    }

    public String notification() {
        return "Removed Effects";
    }





}
