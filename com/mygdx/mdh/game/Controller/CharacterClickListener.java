package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.model.Map;

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

        CombatController stage = (CombatController)evt.getStage();

        System.out.println("[Actor Clicked]"+x +","+y+ " has been clicked."+actor.getX()+"/"+actor.getWidth());
        if (actor.isDead() ) return;

        if (stage.getCombat().getGameStep().equals(Combat.GameStepType.SELECTION)
            || stage.getCombat().getGameStep().equals(Combat.GameStepType.ACTION_SELECTION)) {
            stage.setSelectedCharacter(actor);
            stage.getCombat().setGameStep(Combat.GameStepType.ACTION_SELECTION);

        }

        System.out.println("[CharacterClickListener] Distance to target "+Map.distance(stage.getSelectedCharacter().getMapCell(),actor.getMapCell()));
        //If the step was targeting, the clicked actor becomes the target.
        if (stage.getCombat().getGameStep().equals(Combat.GameStepType.TARGETING)
            && Map.distance(stage.getSelectedCharacter().getMapCell(),actor.getMapCell()) <= stage.getCurrentSelectedAbility().getRange()) {

            System.out.println("[CharacterClickListener] Targeted "+actor.getCharacter().getName());

            stage.executeCurrentAbility(actor);

            if (actor.getCharacter().isActive()) {
                stage.map.removeHighlightCells();
            } else {
                stage.deselectCharacter();
            }

        }

    }
}
