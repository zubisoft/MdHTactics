package com.mygdx.mdh.game.characters.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AfterAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.map.IsoMapActor;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.model.MapCell;

import java.util.List;

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

    int currentargetIndex;
    float currenttargetx;
    float currenttargety;


    float frameDuration;

    IsoMapActor map;
    List<MapCell> path;

    public MovementAction( float frameDuration) {
        this.frameDuration = frameDuration;
        this.begin=true;
        this.stateTime=0;

    }


    public void setTargetCell(IsoMapCellActor target, IsoMapActor map) {

        //These need to come as actual graphical xy
        this.targetCell = target;

        targetx = target.getX();
        targety = target.getY();

        this.map = map;

    }

    //@Override
    public boolean act (float delta)  {

        CharacterActor characterActor = (CharacterActor)actor;

        if (this.begin ) {

            //TODO Do not move if the target is occupied  - but please dont break the bloody gameScreen
            //if (targetCell.getCell().isOccupied()) return true;

            this.stepx = Math.signum(targetx - actor.getX()) * 4;
            this.stepy = Math.signum(targety - actor.getY()) * 2;

            ((CharacterActor)actor).setState(CharacterActor.CHARACTER_STATE.MOVING);

            if (Math.signum(targetx - actor.getX()) != Math.signum(actor.getScaleX()))
                actor.setScaleX(-1 * actor.getScaleX());
            //System.out.println("[CharacterActor] Checking X "+actor.toString()+" "+targetx+" "+targety);
            this.begin = false;

            currentargetIndex = 1;
            path = CombatController.combat.getMap().getShortestPath(characterActor.getMapCell(), targetCell.getCell());

            if (path.size() == 1) currentargetIndex = 0;

            currenttargetx = map.getCell(path.get(currentargetIndex)).getX();
            currenttargety = map.getCell(path.get(currentargetIndex)).getY();


        }

        stateTime += delta;
        if (stateTime >= frameDuration) {
            stateTime=0;


            if (Math.round(actor.getX())==targetx & Math.round(actor.getY())==targety) {
                //Target reached

                actor.setBounds(targetx, targety, actor.getWidth(),actor.getHeight());

                //Assign final destination in the gameScreen logic
                characterActor.getCharacter().setCell(targetCell.getCell());
//                characterActor.getCharacter().setAvailableActions(characterActor.getCharacter().getAvailableActions()-1);

                //Stop moving
                characterActor.setState(CharacterActor.CHARACTER_STATE.IDLE);

               // System.out.println("[CharacterActor] Target reached "+characterActor);

                return true;

            } else {

                if (Math.round(actor.getX())==currenttargetx & Math.round(actor.getY())==currenttargety) {

                    currentargetIndex++;
                    currenttargetx = map.getCell(path.get(currentargetIndex)).getX();
                    currenttargety = map.getCell(path.get(currentargetIndex)).getY();

                } else {

                     //characterActor.setState(CharacterActor.CHARACTER_STATE.MOVING);

                    //Update position
                    if ((stepx > 0 & currenttargetx - actor.getX() > stepx) | (stepx < 0 & currenttargetx - actor.getX() < stepx))
                        actor.setX(actor.getX() + stepx);
                    else {
                        actor.setX(currenttargetx);
                        //System.out.println("[CharacterActor] MovingX "+actor.getX()+" "+targetx );
                    }

                    if ((stepy > 0 & currenttargety - actor.getY() > stepy) | (stepy < 0 & currenttargety - actor.getY() < stepy))
                        actor.setY(actor.getY() + stepy);
                    else {
                        actor.setY(currenttargety);
                        //System.out.println("[CharacterActor] MovingY "+actor.getY()+" "+targety );
                    }
                }

            }
        }

        return false;

    }



}
