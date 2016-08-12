package com.mygdx.mdh.game.characters.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.model.effects.Effect;

/**
 * Created by zubisoft on 09/03/2016.
 */
public class EffectAction  extends Action {

    boolean begin; //True until the action takes place the first time, false otherwise
    float stateTime;
    float frameDuration;

    Animation  effectAnimation;
    TextureRegion currentFrame;

    boolean finished;

    Effect effect;
    String message;

    int width;
    int height;

    int offsetX=0;
    int offsetY=0;


    int FRAME_COLS = 5;

    public EffectAction(Effect effect, float frameDuration) {
        this.frameDuration = frameDuration;
        this.begin=true;
        this.stateTime=0;
        this.effect = effect;
        this.message = effect.notification(); //This is done at this moment,as the final effect of the effect might be different when it has multiple hits.
        this.loadAnimations();
        this.finished = false;
        currentFrame = effectAnimation.getKeyFrame(0, true);
    }


    @Override
    public boolean act (float delta)  {

        //Effects are applied first, when the animation starts
        if (this.begin ) {
            effect.setTarget(((CharacterActor) target).getCharacter());
            ((CharacterActor) target).showMessage(effect.getIcon(),message, effect.getColor());
            //effect.apply();

            //((CharacterActor) target).showMessage(effect.getOutcome());

            this.begin = false;
        }

        if (effectAnimation.isAnimationFinished(stateTime)) {
            //actor.removeAction(this);
            finished=true;
            return true;
        }

        stateTime += delta;

        currentFrame = effectAnimation.getKeyFrame(stateTime, true);

        return false;
    }


    public void draw (SpriteBatch batch) {

        if(!finished)
            batch.draw(currentFrame
                    ,target.getX()+offsetX+target.getWidth()/2
                    ,target.getY()+offsetY+target.getHeight()/2
            );

    }



    public void loadAnimations () {


        //TODO: fix the reference to the files for gods sake
        Texture texture = new Texture(Gdx.files.internal(effect.getPic())); // #9
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/FRAME_COLS, texture.getHeight());              // #10
        TextureRegion[] frames = new TextureRegion[FRAME_COLS];
        int index = 0;

        for (int j = 0; j < FRAME_COLS; j++) {
            frames[index++] = tmp[0][j];
        }

        effectAnimation = new Animation(0.5f, frames);      // #11



        width = texture.getWidth()/FRAME_COLS;
        height = texture.getHeight();


        offsetX=0;
        //TODO configurar efectos como dios manda
        if(effect.getEffectClass()== Effect.EffectClass.SHIELD) {
            //83 is the width of a charaacteractor
            offsetX=0;
        }

    }


}
