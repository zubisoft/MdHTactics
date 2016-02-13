package com.mygdx.mdh.View;

import com.mygdx.mdh.MDHTactics;

/**
 * Created by zubisoft on 02/02/2016.
 */
public class IdleAnimatedAction extends AnimatedAction {

    CharacterActor actor;


    public IdleAnimatedAction (float frameDuration, CharacterActor actor) {
        super(frameDuration);
        this.actor = actor;
    }


    @Override
    public void draw(float stateTime) {

        MDHTactics.batch.draw(actor.getIdleAnimation().getKeyFrame(stateTime, true),actor.getX(),actor.getY());

    }
}
