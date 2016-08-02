package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.controller.CharacterChangeListener;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.LOG;

import java.util.EnumSet;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class ProtectionEffect extends Effect implements CharacterChangeListener {

    boolean initialized=false;
    String notification;

    float perecentProtection;
    int   fixedProtection;

    EnumSet<EffectType> immunityType;
    EnumSet<EffectSubType> immunitySubType;


    public ProtectionEffect() {
        super();

        initialized = false;
        effectClass = EffectClass.SHIELD;
        effectType = EffectType.BUFF;
        color= Color.BLUE;

        immunityType = EnumSet.noneOf(EffectType.class);
        immunitySubType = EnumSet.noneOf(EffectSubType.class);

    }

    /**
     * Set the initial shield strength
     * @return
     */
    public void init() {
        super.init();
        if (!initialized) {
            roll = new Roll(Roll.RollType.GENERIC,diceNumber,diceSides,modifier);
            initialized=true;

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

        if ( Math.random() > chance) return;

        if (immunityType.contains(d.getEffectType()) || immunitySubType.contains(d.getEffectSubType())) {
            d.setDuration(-1); // Completely cancelled
            notification = "Immune!";
            effectTriggered ();
        }

        if (d.getEffectClass()== EffectClass.DAMAGE) {
            DamageEffect de = (DamageEffect)d;

            int originalDamage=0;
            int newDamage=0;
            for (int i=0; i<de.getHits();i++) {
                if (de.getDamageRolls().get(i).getRoll() != null) {
                    originalDamage += de.getDamageRolls().get(i).getRoll().getRoll();
                    de.getDamageRolls().get(i).getRoll().addPercentModifier(perecentProtection);
                    de.getDamageRolls().get(i).getRoll().addModifier(fixedProtection);
                    newDamage += de.getDamageRolls().get(i).getRoll().getRoll();
                }
            }

            notification = "Protected "+(originalDamage-newDamage)+"/"+originalDamage;
            effectTriggered ();
        }
    }


    public String toString() {
        return "*"+ getEffectClass()+" ("+roll.getRoll()+" HP)";
    }

    public String notification() {
        return notification;
    }

    public ProtectionEffect copy () {

        ProtectionEffect e = new ProtectionEffect();
        e.copy(this);

        e.perecentProtection = perecentProtection;
        e.fixedProtection = fixedProtection;

        return e;

    }


    public void onCharacterHit (int damage) {} ;
    public void onCharacterActive (Character c) {};
    public void onCharacterInactive (Character c) {};
}
