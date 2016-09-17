package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.model.Map;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 28/01/2016.
 */
public class CharacterClickListener extends ClickListener {
    private CharacterActor actor;

    public CharacterClickListener(CharacterActor actor) {
        this.actor = actor;
    }

    @Override
    public void clicked(InputEvent evt, float x, float y) {

        //LOG.print("zindex "+actor.getZIndex()+" "+actor.getMapCell()+" "+actor.getMapCell().getMapCoordinates().y);

        CombatController stage = (CombatController)evt.getStage();

        //System.out.println("[Actor Clicked]"+x +","+y+ " has been clicked."+actor.getX()+"/"+actor.getWidth()+" Effects: "+actor.getCharacter().getEffects().size());
        if (actor.isDead() ) return;

        if (stage.getCombat().getGameStep().equals(Combat.GameStepType.SELECTION)
            || stage.getCombat().getGameStep().equals(Combat.GameStepType.ACTION_SELECTION)) {

            stage.setSelectedCharacter(actor);
            stage.setGameStep(Combat.GameStepType.ACTION_SELECTION);


        }

        //If the step was targeting, the clicked actor becomes the target.
        if (stage.getCombat().getGameStep().equals(Combat.GameStepType.TARGETING)
            && Map.distance(stage.getSelectedCharacter().getMapCell(),actor.getMapCell()) <= stage.getCurrentSelectedAbility().getRange()) {

            stage.executeCurrentAbility(actor);

            /*
            if (!stage.getSelectedCharacter().isActive()) {
                stage.setGameStep(Combat.GameStepType.SELECTION);
            } else {
                stage.setGameStep(Combat.GameStepType.ACTION_SELECTION);
            }
            */



        }


        if (stage.getSelectedCharacter() != null
                && stage.getCombat().getGameStep().equals(Combat.GameStepType.TARGETING)
                && evt.getButton() == Input.Buttons.RIGHT ) {
            stage.setGameStep(Combat.GameStepType.SELECTION);
        }

    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        CombatController stage = (CombatController)event.getStage();
        stage.showAreaOfEffect(actor.getMapCell());

        super.enter(event, actor.getX()+x, actor.getY()+y, pointer, fromActor);


    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        CombatController stage = (CombatController)event.getStage();
        stage.hideAreaOfEffect(actor.getMapCell());
        super.exit(event, x, y, pointer, toActor);

    }
}
