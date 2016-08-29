package com.mygdx.mdh.game.model.effects;


import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.controller.CharacterChangeListener;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class ShieldEffect extends Effect implements CharacterChangeListener {

    boolean initialized=false;
    int initialRoll;
    int blockedDamage = 0;
    String notification;

    public ShieldEffect () {
        super();

        initialized = false;
        effectClass = EffectClass.SHIELD;
        effectType = EffectType.BUFF;
        color= Color.BLUE;
    }

    /**
     * Set the initial shield strength
     * @return
     */
    public void init() {
        super.init();

        notification = this.getName();

        if (hits>0 && !initialized) {
            initialRoll = 0;
            initialized=true;
            //notification = "Shielded vs "+hits+" hits";

            effectTriggered();
        }

        if (!initialized) {
            roll = new Roll(Roll.RollType.GENERIC,diceNumber,diceSides,modifier);
            initialRoll= roll.roll();

            initialized=true;
            //notification = "Shielded "+initialRoll+"/"+initialRoll;

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
        if ( roll.getRoll() <= 0 ) return;

        if (d.getEffectClass()== EffectClass.DAMAGE) {

            DamageEffect de = (DamageEffect)d;

            int blocked=0;
            for (int i=0; i<de.getHits();i++) {
                if (de.getDamageRolls().get(i).getRoll() != null) {
                    if (hits > 0) {
                        blocked = de.getDamageRolls().get(i).getRoll().getRoll();
                        hits--;
                    } else {
                        blocked = Math.min(initialRoll - blockedDamage, de.getDamageRolls().get(i).getRoll().getRoll());
                    }

                    de.getDamageRolls().get(i).getRoll().addModifier(-blocked);
                    blockedDamage += blocked;

                }
            }

            if (blockedDamage>0) {
                notification = "Shielded " + (initialRoll - blockedDamage) + "/" + initialRoll;
                effectTriggered ();
            }

        }
    }


    public String toString() {
        return ""+ getEffectClass()+" ("+roll.getRoll()+" HP)";
    }

    public String notification() {
        return notification;
    }

    public ShieldEffect copy () {

        ShieldEffect e = new ShieldEffect();
        e.copy(this);

        return e;

    }


    public void onCharacterHit (int damage) {} ;
    public void onCharacterActive (Character c) {};
    public void onCharacterInactive (Character c) {};
}
