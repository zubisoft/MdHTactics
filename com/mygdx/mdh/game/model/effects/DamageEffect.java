package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.Gdx;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class DamageEffect extends Effect {

    public DamageEffect () {
        super();

        effectType=EffectType.DAMAGE;
    }

    @Override
    public void init() {
        if ( duration < 0 ) return ;

        //For those effects that have a chance to happen
        //if ( Math.random() > chance) return ;

        if(diceNumber>=0) {
            rolledResult = Dice.roll(diceNumber, diceSides) + modifier;
            roll.setBaseRoll(rolledResult);
            roll.setModifier(modifier);
            roll.setPercentModifier(percentModifier);
            roll.setEffectType(Roll.RollType.DAMAGE);
        }

    }


    public void execute () {

        //For those effects that have a chance to happen
        if ( Math.random() > chance) return;

        if (duration>=0) {
            if (roll != null && target != null) {
                LOG.print(2,"[DamageEffect] Inflicted: "+roll.getTotalRoll(), LOG.ANSI_RED);
                target.hit(roll.getTotalRoll());
            }
        }

        effectTriggered ();
    }

    public String notification() {
        return "-"+roll.getTotalRoll()+" HP";
    }

}
