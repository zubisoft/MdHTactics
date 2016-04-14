package com.mygdx.mdh.game.model.effects;

import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 14/04/2016.
 */
public class HealEffect  extends Effect{


        public HealEffect () {
            super();

            effectType=EffectType.HEAL;
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
                    LOG.print(2,"[HealEffect] Healed: "+roll.getTotalRoll(), LOG.ANSI_GREEN);
                    target.hit(-roll.getTotalRoll());
                }
            }

            effectTriggered ();
        }

        public String notification() {
            return "+"+roll.getTotalRoll()+" HP";
        }



}
