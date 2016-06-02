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
public class DamageEffect extends Effect {

    /**
     * If true, the default attack chance is ignored and the damage is always applied.
     */
    boolean directDamage = false;

    public DamageEffect () {
        super();

        effectType=EffectType.DAMAGE;
        color= Color.RED;
    }

    @Override
    public void init() {
        if ( duration < 0 ) return ;
        this.setAttackChance();

        //For those effects that have a chance to happen
        //if ( Math.random() > chance) return ;

    }

    public void setAttackChance () {
        setChance((10.0f-source.getAttack()+target.getDefence())/20);
    }


    public void execute () {

        if (duration>=0) {
            for (int i=0; i<hits;i++) {
                double attackRoll = Math.random();
                LOG.print(2, "[DamageEffect] Rolled: " + attackRoll+" Needed: "+chance, LOG.ANSI_RED);
                if ( attackRoll >= chance || isDirectDamage()) {

                    if (diceNumber >= 0) {
                        //A damage roll can never be negative - Use heal for that
                        rolledResult = Math.max(Dice.roll(diceNumber, diceSides) + modifier, 0);
                        roll.setBaseRoll(rolledResult);
                        roll.setModifier(modifier);
                        roll.setPercentModifier(percentModifier);
                        roll.setEffectType(Roll.RollType.DAMAGE);
                    }


                    if (roll != null && target != null) {
                        LOG.print(2, "[DamageEffect] Inflicted: " + roll.getTotalRoll(), LOG.ANSI_RED);
                        target.hit(roll.getTotalRoll());
                    }
                    effectTriggered();
                }
            }
        }


    }

    public String notification() {
        if (roll.getTotalRoll()>0) {
            if (effectSubType.contains(EffectSubType.FIRE))  return "Burning! -"+roll.getTotalRoll()+" HP";
        }
        return "-"+roll.getTotalRoll()+" HP";
    }

    public DamageEffect copy () {
        DamageEffect e = new DamageEffect();
        e.copy(this);

        return e;
    }

    public boolean isDirectDamage() {
        return directDamage;
    }

    public void setDirectDamage(boolean directDamage) {
        this.directDamage = directDamage;
    }


}
