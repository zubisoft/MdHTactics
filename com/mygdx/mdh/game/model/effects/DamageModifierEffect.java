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

        LOG.print(2,"[DamageModifierEffect] Applied: "+roll.getRoll()+" damage modifier", LOG.ANSI_BLUE);

        if ( roll.getRoll() == 0 ) return;

        if (outbound && d.source != this.target) return;
        if (!outbound && d.source == this.target) return; //This would exclude damage going to oneself

        if (d.getEffectClass()== EffectClass.DAMAGE) {

            DamageEffect de = (DamageEffect)d;

            for (int i=0; i<de.getHits();i++) {
                if (de.getDamageRolls().get(i).getRolledDamage() != null)
                    de.getDamageRolls().get(i).getRolledDamage().addModifier(roll.getRoll());
            }

            d.setChanceModifier(this.getChanceModifier());

            LOG.print(2,"[DamageModifierEffect] Applied: "+roll.getRoll()+" damage modifier", LOG.ANSI_BLUE);

            effectTriggered ();
        }
    }


    public String toString() {
        return "*"+ getEffectClass()+" ("+roll.getRoll()+" HP)";
    }

    public String notification() {
        return "Damage Modifier";
    }






}
