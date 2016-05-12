package com.mygdx.mdh.game.characters.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.util.LOG;

import java.awt.*;

/**
 * Created by zubisoft on 07/03/2016.
 */
public class TakeDamageAction extends Action {

    boolean begin; //True until the action takes place the first time, false otherwise

    float stateTime;
    float totalTime;



    float frameDuration;

    public TakeDamageAction(float frameDuration) {
        this.frameDuration = frameDuration;
        this.begin=true;
        this.stateTime=0;
        this.totalTime=0;

    }



    @Override
    public boolean act (float delta)  {

        CharacterActor characterActor = (CharacterActor)actor;


        totalTime += delta;

            if (totalTime > 3) {
                //Stop moving
                characterActor.setState(CharacterActor.CHARACTER_STATE.IDLE);

                return true;

            } else {

                characterActor.setState(CharacterActor.CHARACTER_STATE.HIT);


            }


        return false;

    }



}
