package com.mygdx.mdh.game.characters.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AfterAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.map.IsoMapCellActor;

/**
 * Created by zubisoft on 07/03/2016.
 */
public class MovementAction extends GameAction {

    boolean begin; //True until the action takes place the first time, false otherwise

    float stateTime;

    IsoMapCellActor targetCell;

    float stepx;
    float stepy;

    float targetx;
    float targety;

    float frameDuration;

    public MovementAction( float frameDuration) {
        this.frameDuration = frameDuration;
        this.begin=true;
        this.stateTime=0;

    }


    public void setTargetCell(IsoMapCellActor target) {

        //These need to come as actual graphical xy
        this.targetCell = target;

        targetx = target.getX();
        targety = target.getY();

    }

    //@Override
    public boolean act (float delta)  {

        CharacterActor characterActor = (CharacterActor)actor;

        if (this.begin ) {

            //TODO Do not move if the target is occupied  - but please dont break the bloody game
            //if (targetCell.getCell().isOccupied()) return true;

            this.stepx = Math.signum(targetx - actor.getX()) * 2;
            this.stepy = Math.signum(targety - actor.getY()) * 2;

            ((CharacterActor)actor).setState(CharacterActor.CHARACTER_STATE.MOVING);

            if (Math.signum(targetx - actor.getX()) != Math.signum(actor.getScaleX()))
                actor.setScaleX(-1 * actor.getScaleX());
            //System.out.println("[CharacterActor] Checking X "+actor.toString()+" "+targetx+" "+targety);
            this.begin = false;

            System.out.println("[CharacterActor] From  "+actor.getX()+" to "+targetx+" step "+stepx);
        }

        stateTime += delta;
        if (stateTime >= frameDuration) {
            stateTime=0;




            if (Math.round(actor.getX())==targetx & Math.round(actor.getY())==targety) {
                //Target reached

                actor.setBounds(targetx, targety, actor.getWidth(),actor.getHeight());

                //Stop moving
                characterActor.setState(CharacterActor.CHARACTER_STATE.IDLE);

                //Assign final destination in the game logic
                characterActor.getCharacter().setCell(targetCell.getCell());
                characterActor.getCharacter().setAvailableActions(characterActor.getCharacter().getAvailableActions()-1);

                System.out.println("[CharacterActor] Target reached "+characterActor);

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
