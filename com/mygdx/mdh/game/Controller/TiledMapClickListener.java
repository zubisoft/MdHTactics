package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.model.Combat;


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

        if (stage.getCombat().getGameStep().equals(Combat.GameStepType.ACTION_SELECTION)
                && stage.getSelectedCharacter().isActive()
                && event.getButton() == Input.Buttons.RIGHT
                && !actor.getCell().isOccupied()) {
            System.out.println("[Tile Clicked] Moving character to "+actor.getMapCoordinates());

            stage.map.removeHighlightCells();
            stage.getSelectedCharacter().moveToCell(actor);
            stage.getCombat().setGameStep(Combat.GameStepType.ACTION_SELECTION);

        }

        if ( event.getButton() == Input.Buttons.LEFT ) {
            stage.deselectCharacter();
            System.out.println("[Tile Clicked] "+actor.toString() );
            stage.getCombat().setGameStep(Combat.GameStepType.SELECTION);
        }



    }
}