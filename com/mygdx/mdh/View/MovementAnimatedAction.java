package com.mygdx.mdh.View;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    TextureRegion currentFrame;


    public MovementAnimatedAction (float frameDuration, CharacterActor actor, float targetx, float targety) {
        super(frameDuration);
        this.actor = actor;

        //These need to come as actual graphical xy
        this.targetx = (int)targetx;
        this.targety = (int)targety;

        this.stepx = Math.signum(targetx - actor.getX())*5;
        this.stepy = Math.signum(targety - actor.getY())*5;

        //currentFrame.setRegion(actor.getX(),actor.getY(),actor.getWidth(),actor.getHeight());

        currentFrame = getKeyFrame(0);

    }


    @Override
    public void update (float newTime)  {
        System.out.println(newTime);
        if (newTime - stateTime >= frameDuration) {
            stateTime = newTime;
            if (Math.abs(actor.getX())==targetx & Math.round(actor.getY())==targety) {
                //Target reached
                System.out.println("[CharacterActor] Target reached");
                actor.setBounds(targetx, targety, actor.getWidth(),actor.getHeight());
                actor.setCurrentAction(new IdleAnimatedAction(stateTime,actor));
                actor.setState(CharacterActor.CHARACTER_STATE.IDLE);

                actor.getCharacter().setCellx(targetx);
                actor.getCharacter().setCelly(targety);
                actor.getCharacter().setAvailableActions(actor.getCharacter().getAvailableActions()-1);

            } else {
                System.out.println("[CharacterActor] Target moving "+targetx+" "+targety);
                actor.setState(CharacterActor.CHARACTER_STATE.MOVING);
                //Update position
                if ( (stepx>0 & targetx - actor.getX() > stepx) | (stepx<0 & targetx - actor.getX() < stepx)) actor.setX(actor.getX()  + stepx);

                else actor.setX(targetx);

                if ( (stepy>0 & targety - actor.getY() > stepy) | (stepy<0 & targety - actor.getY() < stepy)) actor.setY(actor.getY() + stepy);
                else actor.setY(targety);
            }
        }
        currentFrame = getKeyFrame(newTime);
    }


    @Override
    public void draw(SpriteBatch batch){

        batch.draw(currentFrame,actor.getX()+actor.getOriginX(),actor.getY()+actor.getOriginY(),actor.getOriginX(),actor.getOriginY(),actor.getHeight(),actor.getWidth(),actor.getScaleX(),actor.getScaleY(),actor.getRotation());

    }

    public TextureRegion getKeyFrame(float stateTime) {
        return actor.getWalkAnimation().getKeyFrame(stateTime, true);
    }



}