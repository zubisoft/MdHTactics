package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.util.LOG;


/**
 * Created by zubisoft on 28/01/2016.
 */
public class TiledMapClickListener extends ClickListener {

    private IsoMapCellActor actor;

    public TiledMapClickListener(IsoMapCellActor actor) {
        super(-1); //Listen to all buttons
        this.actor = actor;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        CombatController stage = (CombatController)event.getStage();

        System.out.println("[Tile Clicked]"+actor.getX() +","+actor.getY()+ " has been clicked."+ this.getButton() );

        if (stage.getSelectedCharacter() != null) {
            if (stage.getCombat().getGameStep().equals(Combat.GameStepType.ACTION_SELECTION)
                    && stage.getSelectedCharacter().isActive()
                    && event.getButton() == Input.Buttons.RIGHT
                    && !actor.getCell().isOccupied()) {
                System.out.println("[Tile Clicked] Moving character to " + actor.getMapCoordinates());

                stage.setGameStep(Combat.GameStepType.ACTION_SELECTION);
                stage.getSelectedCharacter().moveToCell(actor, stage.getMap());

            }
        }

        if ( event.getButton() == Input.Buttons.LEFT ) {

            if (stage.getCombat().getGameStep() == Combat.GameStepType.TARGETING)
                stage.executeCurrentAbility(actor);

            stage.setGameStep(Combat.GameStepType.SELECTION);
            stage.deselectCharacter();
            System.out.println("[Tile Clicked] "+actor.toString() );


            //debug
            //stage.getMap().highlightCells(actor,2, Color.BROWN);

        }

        /*
        if ( event.getButton() == Input.Buttons.RIGHT ) {
            stage.setGameStep(Combat.GameStepType.ACTION_SELECTION);
            //stage.deselectCharacter();
            System.out.println("[Tile Clicked] "+actor.toString() );
        }
        */

            if (stage.getSelectedCharacter() != null &&
                    stage.getCombat().getGameStep().equals(Combat.GameStepType.ACTION_SELECTION)
                    && !stage.getSelectedCharacter().isActive() ) {
                stage.setGameStep(Combat.GameStepType.SELECTION);
                stage.deselectCharacter();
            }

        if (stage.getSelectedCharacter() != null
                && stage.getCombat().getGameStep().equals(Combat.GameStepType.TARGETING)
                && event.getButton() == Input.Buttons.RIGHT ) {
            stage.setGameStep(Combat.GameStepType.SELECTION);
        }



    }


    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        CombatController stage = (CombatController)event.getStage();
        stage.showAreaOfEffect(actor.getCell());

        super.enter(event, actor.getX()+x, actor.getY()+y, pointer, fromActor);






    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        CombatController stage = (CombatController)event.getStage();
        stage.hideAreaOfEffect(actor.getCell());
        super.exit(event, x, y, pointer, toActor);

    }
}