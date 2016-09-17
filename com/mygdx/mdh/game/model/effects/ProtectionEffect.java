package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.controller.CharacterChangeListener;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Roll;

import java.util.EnumSet;

/**
 * Created by zubisoft on 29/03/2016.
 */


/**
 * Protection Effects are used to apply immunities or reduce the damage to the character.
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
        if ( d.getSource() != this.getSource()) return; //Only inbound damage!

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
                if (de.getDamageRolls().get(i).getRolledDamage() != null) {
                    originalDamage += de.getDamageRolls().get(i).getRolledDamage().getRoll();
                    if (perecentProtection>0) de.getDamageRolls().get(i).getRolledDamage().addPercentModifier(perecentProtection);
                    if (fixedProtection>0) de.getDamageRolls().get(i).getRolledDamage().addModifier(fixedProtection);
                    if (diceNumber>0 || modifier>0) de.getDamageRolls().get(i).getRolledDamage().addModifier(roll.roll());
                    newDamage += de.getDamageRolls().get(i).getRolledDamage().getRoll();
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
        e.immunityType = immunityType;
        e.immunitySubType = immunitySubType;

        return e;

    }


    public void onCharacterHit (int damage) {} ;
    public void onCharacterActive (Character c) {};
    public void onCharacterInactive (Character c) {};
    public void onAbilityUnlock (Ability a) {};
}
