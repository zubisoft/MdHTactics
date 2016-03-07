package com.mygdx.mdh.game.characters.actions;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by zubisoft on 02/02/2016.
 */
public class AnimatedAction {

    float stateTime;
    float frameDuration;

    public AnimatedAction (float frameDuration) {
        stateTime = 0;
        this.frameDuration = frameDuration;
    }

    public void update (float newTime) {

    }

    public void draw(SpriteBatch batch) {


    }

    public String toString() {
        return this.getClass().toString();
    }

}