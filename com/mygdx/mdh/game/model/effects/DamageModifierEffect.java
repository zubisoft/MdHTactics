package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.LOG;

import java.util.EnumSet;

/**
 * Created by zubisoft on 29/03/2016.
 */

/**
 * Damage Modifier Effects modify the inbound/outbound damage to/from a Character
 */
public class DamageModifierEffect extends Effect {

    boolean initialized=false;

    boolean notificationShown = false;

    /**
     * By default, applies the modifier to outbound damage (damage emitted from the current character)
     * However can be set to false to use it as a damage protection effect for inbound damage (damage coming from other characters)
     */
    boolean outbound;

    public DamageModifierEffect() {
        super();

        initialized = false;
        effectClass = EffectClass.DAMAGE_MODIFIER;
        effectType = EffectType.BUFF;
        color= Color.BLUE;
        outbound = true;

        if (pic ==null) pic="effect_blue";

    }


    public DamageModifierEffect copy () {
        DamageModifierEffect e = new DamageModifierEffect();
        e.copy(this);
        e.outbound = outbound;

        return e;
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

        if ( roll.getRoll() == 0 && this.getChanceModifier() == 0 ) return;

        if (outbound && d.source == this.target) return;

        if (!outbound && d.source == this.target) return; //This would exclude damage going to oneself

        if (d.getEffectClass()== EffectClass.DAMAGE) {

            DamageEffect de = (DamageEffect)d;

            for (int i=0; i<de.getHits();i++) {
                if (de.getDamageRolls().get(i).getRolledDamage() != null)
                    de.getDamageRolls().get(i).getRolledDamage().addModifier(roll.getRoll());
            }

            d.addChanceModifier(this.getChanceModifier());


            effectTriggered ();
        }
    }


    public String toString() {

        String description;
        String sign = "+";

        if (!outbound) sign="-";

        description = "Adds "+sign;
        if (!outbound) description = "Prevents ";

        if (diceNumber!=0 )      description += ""+Math.abs(diceNumber)+"d"+diceSides+(modifier>0?"+":"");
        if (modifier!=0 )        description += Math.abs(modifier);
        if (outbound ) description +=  " to ";
        description +=  " damage";
        if (this.getChanceModifier()!=0 )        description += " and "+sign+Math.floor(getChanceModifier()*20)+" to attack roll";
        if (duration!=0 )   description+=" ("+duration+" rounds)";
        return description;
    }
    public String getDescription () {
        return this.toString();
    }

    public String notification() {
        if (!notificationShown) {
            notificationShown=true;
            return ""+ getName()+" ("+getDuration()+" rounds)";
        }
        else return null;
    }






}
