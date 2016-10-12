package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;

/**
 * Created by zubisoft on 09/04/2016.
 */
public class StunEffect  extends Effect  {

        String notification  = null;
    boolean initialized = false;

        public StunEffect () {
            super();

            effectClass = EffectClass.STUN;
            effectType = EffectType.DEBUFF;
            color= Color.YELLOW;
            notification  = null;

            if (pic ==null) pic="effect_red";

        }

        @Override
        public void init() {
            if (!initialized) {
                super.init();
                notification = "Stun applied!";
                effectTriggered();
                LOG.print(3,"[StunEffect] Initialized. ", LOG.ANSI_RED);
                initialized = true;
            }


        }


        public void execute () {
            super.execute();


            //For those effects that have a chance to happen
            if ( Math.random() > chance) {
                LOG.print(3,"[StunEffect] Stun failed. ", LOG.ANSI_RED);
                notification = "Stun failed.";
            } else {
                LOG.print(3, "[StunEffect] Stunned!: ", LOG.ANSI_RED);
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


