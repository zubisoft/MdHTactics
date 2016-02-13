package com.mygdx.mdh.View;

import com.mygdx.mdh.MDHTactics;

/**
 * Created by zubisoft on 02/02/2016.
 */
public class MovementAnimatedAction extends AnimatedAction {
    int targetx;
    int targety;

    float stepx;
    float stepy;

    CharacterActor actor;


    public MovementAnimatedAction (float frameDuration, CharacterActor actor, int targetx, int targety) {
        super(frameDuration);
        this.actor = actor;

        //These need to come as actual graphical xy
        this.targetx = targetx;
        this.targety = targety;

        this.stepx = (targetx-actor.getX())/20;
        this.stepy = (targety-actor.getY())/20;

    }


    @Override
    public void update (float newTime)  {
        if (newTime - stateTime >= frameDuration) {
            stateTime = newTime;
            if (actor.getX()==targetx & actor.getY()==targety) {
                //Target reached
                actor.setBounds(targetx, targety, actor.getWidth(),actor.getHeight());
                actor.setCurrentAction(new IdleAnimatedAction(stateTime,actor));
            } else {
                //Update position
                actor.setX(actor.getX() + stepx);
                actor.setY(actor.getY() + stepy);
            }

        }
    }


    @Override
    public void draw(float stateTime){

        MDHTactics.batch.draw(actor.getWalkAnimation().getKeyFrame(stateTime, true),actor.getX(),actor.getY());

    }


}