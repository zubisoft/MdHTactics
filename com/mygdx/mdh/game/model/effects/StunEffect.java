package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.Gdx;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 09/04/2016.
 */
public class StunEffect  extends Effect  {

        String notification  = null;

        public StunEffect () {
            super();

            effectType=EffectType.STUN;

        }

        @Override
        public void init() {
            notification = null;
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

    }


