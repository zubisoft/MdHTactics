package com.mygdx.mdh.Controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.View.MapCellActor;

/**
 * Created by zubisoft on 28/01/2016.
 */
public class TiledMapClickListener extends ClickListener {

    private MapCellActor actor;

    public TiledMapClickListener(MapCellActor actor) {
        super(-1); //Listen to all buttons
        this.actor = actor;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        CombatStage stage = (CombatStage)event.getStage();

        System.out.println("[Tile Clicked]"+actor.getX() +","+actor.getY()+ " has been clicked."+ this.getButton() );

        if (stage.getCombat().getGameStep().equals("Action") &&  stage.getSelectedCharacter().getCharacter().isActive() && event.getButton() == Input.Buttons.RIGHT ) {
            System.out.println("[Tile Clicked] Moving character to "+actor.getMapCell().getCellx()+","+actor.getMapCell().getCelly());
            stage.getSelectedCharacter().moveToCell(actor.getMapCell().getCellx(),actor.getMapCell().getCelly() );
        }

        if ( event.getButton() == Input.Buttons.LEFT ) {
            stage.setSelectedCharacter(null);
            stage.hideAbilityButtons();
            System.out.println("esconder" );
        }

        stage.getCombat().setGameStep("Selection");
    }
}