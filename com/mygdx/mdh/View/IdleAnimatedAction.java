package com.mygdx.mdh.View;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.mdh.MDHTactics;

/**
 * Created by zubisoft on 02/02/2016.
 */
public class IdleAnimatedAction extends AnimatedAction {

    CharacterActor actor;
    TextureRegion currentFrame;

    public IdleAnimatedAction (float frameDuration, CharacterActor actor) {

        super(frameDuration);
        this.actor = actor;

        currentFrame = getKeyFrame(0);

    }


    @Override
    public void update (float newTime)  {

        currentFrame = getKeyFrame(newTime);

    }

    @Override
    public void draw(SpriteBatch batch) {
        //batch.draw(currentFrame,actor.getX(),actor.getY());
        batch.draw(currentFrame,actor.getX()+actor.getOriginX(),actor.getY()+actor.getOriginY(),actor.getOriginX(),actor.getOriginY(),actor.getHeight(),actor.getWidth(),actor.getScaleX(),actor.getScaleY(),actor.getRotation());
//actor.getOriginX(),actor.getOriginY()
    }

    public TextureRegion getKeyFrame(float stateTime) {
        return actor.getIdleAnimation().getKeyFrame(stateTime, true);

    }

}
