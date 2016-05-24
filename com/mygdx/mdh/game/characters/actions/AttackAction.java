package com.mygdx.mdh.game.characters.actions;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import java.util.List;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.model.Ability;

import java.util.ArrayList;

/**
 * Created by zubisoft on 07/03/2016.
 */
public class AttackAction extends GameAction {

    boolean begin; //True until the action takes place the first time, false otherwise

    float stateTime;
    float totalTime;
    Ability ability;
    //CharacterActor target;
    List<CharacterActor> targets;

    boolean applied;


    float frameDuration;

    public AttackAction(float frameDuration, Ability ability, CharacterActor target) {
        this.frameDuration = frameDuration;
        this.begin=true;
        this.stateTime=0;
        this.totalTime=0;
        this.ability=ability;
        this.applied=false;

        targets = new ArrayList<CharacterActor>();
        targets.add(target);
    }

    public AttackAction(float frameDuration, Ability ability, List<CharacterActor> target) {
        this.frameDuration = frameDuration;
        this.begin=true;
        this.stateTime=0;
        this.totalTime=0;
        this.ability=ability;

        targets = new ArrayList<CharacterActor>();
        targets.addAll(target);
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


                characterActor.setState(CharacterActor.CHARACTER_STATE.IDLE);

                return true;

            } else {

                characterActor.setState(CharacterActor.CHARACTER_STATE.ABILITY1);

                if (!applied) {

                        for (CharacterActor target : targets)
                            target.receiveAbility(ability);


                    applied = true;

                }

            }
        }

        return false;

    }



}
