package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;

/**
 * Created by zubisoft on 09/04/2016.
 */
public class StunEffect  extends Effect  {

        String notification  = null;

        public StunEffect () {
            super();

            effectClass = EffectClass.STUN;
            effectType = EffectType.DEBUFF;
            color= Color.YELLOW;
            notification  = null;

        }

        @Override
        public void init() {
            super.init();
            notification = "Stun applied!";
            LOG.print(2,"[StunEffect] Initialized. ", LOG.ANSI_RED);
        }


        public void execute () {
            super.execute();

            LOG.print(2,"[StunEffect] Attempting stun. ", LOG.ANSI_RED);
            //For those effects that have a chance to happen
            if ( Math.random() > chance) {
                notification = "Failed!";
            } else {
                LOG.print(2, "[StunEffect] Stunned!: ", LOG.ANSI_RED);
                notification = "Stunned!";
                target.setAvailableActions(target.getAvailableActions() - 1);
            }
            effectTriggered ();
        }

        public String notification() {
            return notification;
        }

    public StunEffect copy () {

        StunEffect e = new StunEffect();
        e.copy(this);

        return e;
    }
    }


