package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.characters.actions.EffectAction;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.model.Effect;

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
        if (actor.getCharacter().isDead() ) return;

        if (stage.getCombat().getGameStep().equals(Combat.GameStepType.SELECTION)) {
            stage.setSelectedCharacter(actor);

            if (!actor.getCharacter().isFriendly()) {
                System.out.println("[CharacterClickListener] Clicked maluto");
                return;
            }

                if (actor.getCharacter().isActive()) {
                    System.out.println("[CharacterClickListener] Selected "+actor.getCharacter().getName());
                    stage.combatHUD.showAbilityButtons(actor.getCharacter());
                    stage.showMovementTiles(actor);
                } else {
                    System.out.println("[CharacterClickListener] No actions left");
                }


            stage.getCombat().setGameStep(Combat.GameStepType.ACTION_SELECTION);

        }

        //If the step was targeting, the clicked actor becomes the target.
        if (stage.getCombat().getGameStep().equals(Combat.GameStepType.TARGETING)) {

            System.out.println("[CharacterClickListener] Targeted "+actor.getCharacter().getName());

            stage.executeCurrentAbility(actor);

            stage.map.removeHighlightCells();

        }

    }
}
