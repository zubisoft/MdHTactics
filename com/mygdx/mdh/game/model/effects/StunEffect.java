package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;

/**
 * Created by zubisoft on 09/04/2016.
 */
public class StunEffect  extends Effect  {

        String notification  = null;

        public StunEffect () {
            super();

            effectType=EffectType.STUN;
            color= Color.YELLOW;
            notification  = null;

        }

        @Override
        public void init() {
            notification = "Stun applied!";
            LOG.print(2,"[StunEffect] Initialized. ", LOG.ANSI_RED);
        }


        public void execute () {

            LOG.print(2,"[StunEffect] Attempting stun. ", LOG.ANSI_RED);
            //For those effects that have a chance to happen
            if ( Math.random() > chance) return;

            if (duration>=0) {
                if (roll != null && target != null) {
                    LOG.print(2,"[StunEffect] Stunned!: ", LOG.ANSI_RED);
                    notification = "Stunned!";
                    target.setAvailableActions(target.getAvailableActions()-1);
                }
            }

            effectTriggered ();
        }

        public String notification() {
            return notification;
        }

    public StunEffect copy () {

        StunEffect e = new StunEffect();
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
        e.effectListeners = new ArrayList<>();
        e.modifier = modifier;

        return e;
    }
    }


