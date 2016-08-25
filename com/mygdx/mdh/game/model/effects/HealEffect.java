package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;

/**
 * Created by zubisoft on 14/04/2016.
 */
public class HealEffect  extends Effect{


        public HealEffect () {
            super();

            effectClass = EffectClass.HEAL;
            effectType = EffectType.BUFF;
            color= Color.GREEN;
        }

        @Override
        public void init() {
            if ( duration < 0 ) return ;

            //For those effects that have a chance to happen
            //if ( Math.random() > chance) return ;

            roll = new Roll(Roll.RollType.GENERIC,diceNumber,diceSides,modifier);
            roll.roll();

        }


        public void execute () {

            //For those effects that have a chance to happen
            if ( Math.random() > chance) return;

            if (duration>=0) {
                if (roll != null && target != null) {
                    LOG.print(2,"[HealEffect] Healed: "+roll.getRoll(), LOG.ANSI_GREEN);
                    target.hit(-roll.getRoll());
                }
            }

            effectTriggered ();
        }

        public String notification() {
            return "+"+roll.getRoll()+" HP";
        }

    public HealEffect copy () {
        HealEffect e = new HealEffect();
        e.name = name;
        e.effectClass = effectClass;
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
        e.chanceModifier = chanceModifier;
        e.stacking = stacking;
        e.effectListeners  = new ArrayList<>();
        e.modifier = modifier;

        return e;
    }

    public String getDescription () {
        String description;

            description = "Heals ";

            if (hits>1)             description += hits+" times ";
            if (diceNumber!=0 )      description += ""+diceNumber+"d"+diceSides+(modifier>0?"+":"");
            if (modifier!=0 )        description += modifier;
            /*for (EffectSubType est: getEffectSubType())
                                    description += " " + est.name().toLowerCase();*/
            description += " damage";
            if (duration>0)         description += " ("+duration+" rounds)";



        return description;


    }


}
