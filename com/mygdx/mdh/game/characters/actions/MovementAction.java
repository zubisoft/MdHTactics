package com.mygdx.mdh.game.characters.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.mygdx.mdh.game.characters.CharacterActor;

/**
 * Created by zubisoft on 07/03/2016.
 */
public class MovementAction extends SequenceAction {

    boolean begin; //True until the action takes place the first time, false otherwise

    float stateTime;

    int targetx;
    int targety;

    float stepx;
    float stepy;

    float frameDuration;

    public MovementAction( float frameDuration) {
        this.frameDuration = frameDuration;
        this.begin=true;
        this.stateTime=0;
    }

    //We cant assign these in the constructor, as the actor is assigned right after the action is created
    public void setTargetPosition(float targetx, float targety) {

        //These need to come as actual graphical xy
        this.targetx=(int)targetx;
        this.targety=(int)targety;

    }


    @Override
    public boolean act (float delta)  {

        CharacterActor characterActor = (CharacterActor)actor;

        if (this.begin ) {
            this.stepx = Math.signum(targetx - actor.getX()) * 5;
            this.stepy = Math.signum(targety - actor.getY()) * 5;

            if (Math.signum(targetx - actor.getX()) != Math.signum(actor.getScaleX()))
                actor.setScaleX(-1 * actor.getScaleX());
            //System.out.println("[CharacterActor] Checking X "+actor.toString()+" "+targetx+" "+targety);
            this.begin = false;
        }

        stateTime += delta;
        if (stateTime >= frameDuration) {
            stateTime=0;




            if (Math.round(actor.getX())==targetx & Math.round(actor.getY())==targety) {
                //Target reached
                System.out.println("[CharacterActor] Target reached");
                actor.setBounds(targetx, targety, actor.getWidth(),actor.getHeight());

                //Stop moving
                characterActor.setState(CharacterActor.CHARACTER_STATE.IDLE);

                //Assign final destination in the game logic
                characterActor.getCharacter().setCellx(targetx);
                characterActor.getCharacter().setCelly(targety);
                characterActor.getCharacter().setAvailableActions(characterActor.getCharacter().getAvailableActions()-1);

                return true;

            } else {

                characterActor.setState(CharacterActor.CHARACTER_STATE.MOVING);

                //Update position
                if ( (stepx>0 & targetx - actor.getX() > stepx) | (stepx<0 & targetx - actor.getX() < stepx)) actor.setX(actor.getX()  + stepx);
                else {
                    actor.setX(targetx);
                    //System.out.println("[CharacterActor] MovingX "+actor.getX()+" "+targetx );
                }

                if ( (stepy>0 & targety - actor.getY() > stepy) | (stepy<0 & targety - actor.getY() < stepy)) actor.setY(actor.getY() + stepy);
                else {
                    actor.setY(targety);
                    //System.out.println("[CharacterActor] MovingY "+actor.getY()+" "+targety );
                }
            }
        }

        return false;

    }



}
