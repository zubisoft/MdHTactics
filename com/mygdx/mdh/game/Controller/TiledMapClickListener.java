package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.map.IsoMapCellActor;


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

        if (stage.getCombat().getGameStep().equals("Action") &&  stage.getSelectedCharacter().getCharacter().isActive() && event.getButton() == Input.Buttons.RIGHT ) {
            System.out.println("[Tile Clicked] Moving character to "+actor.getMapCell().x+","+actor.getMapCell().y);

            stage.getSelectedCharacter().moveToCell(actor.getPosition().x, actor.getPosition().y);
        }

        if ( event.getButton() == Input.Buttons.LEFT ) {
            stage.setSelectedCharacter(null);
            stage.combatHUD.hideAbilityButtons();
            System.out.println("[Tile Clicked] "+actor.toString() );
        }

        stage.getCombat().setGameStep("Selection");
    }
}