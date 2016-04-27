package com.mygdx.mdh.game.characters.actions;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.model.Ability;

/**
 * Created by zubisoft on 07/03/2016.
 */
public class AttackAction extends GameAction {

    boolean begin; //True until the action takes place the first time, false otherwise

    float stateTime;
    float totalTime;
    Ability ability;
    CharacterActor target;


    float frameDuration;

    public AttackAction(float frameDuration, Ability ability, CharacterActor target) {
        this.frameDuration = frameDuration;
        this.begin=true;
        this.stateTime=0;
        this.totalTime=0;
        this.ability=ability;
        this.target=target;
    }



    @Override
    public boolean act (float delta)  {

        CharacterActor characterActor = (CharacterActor)actor;


        stateTime += delta;
        totalTime += delta;
        if (stateTime >= frameDuration) {
            stateTime=0;


            if (totalTime > 3) {
                //Stop moving
                target.receiveAbility(ability);
                characterActor.setState(CharacterActor.CHARACTER_STATE.IDLE);

                return true;

            } else {

                characterActor.setState(CharacterActor.CHARACTER_STATE.ABILITY1);


            }
        }

        return false;

    }



}
