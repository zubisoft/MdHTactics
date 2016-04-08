package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.Gdx;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class ShieldEffect extends Effect {

    boolean initialized=false;
    int initialRoll;

    public ShieldEffect () {
        super();

        effectType=EffectType.SHIELD;
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

}
