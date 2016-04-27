package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;

/**
 * Created by zubisoft on 14/04/2016.
 */
public class HealEffect  extends Effect{


        public HealEffect () {
            super();

            effectType=EffectType.HEAL;
            color= Color.GREEN;
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

    public HealEffect copy () {
        HealEffect e = new HealEffect();
        e.name = name;
        e.effectType = effectType;
        e.effectSubType = effectSubType;
        e.gameSegment = gameSegment;
        e.duration = duration;
        e.roll = roll;
        e.chance = chance;
        e.source = source;
        e.target = target;
        e.pic = pic;
        e.icon = icon;
        e.outcome = outcome;
        e.color = color;
        e.diceNumber = diceNumber;
        e.diceSides = diceSides;
        e.percentModifier = percentModifier;
        e.rolledResult = rolledResult;
        e.stacking = stacking;
        e.effectListeners  = new ArrayList<>();
        e.modifier = modifier;

        return e;
    }

}
