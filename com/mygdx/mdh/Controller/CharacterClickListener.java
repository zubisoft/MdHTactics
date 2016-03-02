package com.mygdx.mdh.Controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.View.CharacterActor;

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
        stage.setSelectedCharacter(actor);

        System.out.println("[Actor Clicked]"+x +","+y+ " has been clicked."+actor.getX()+"/"+actor.getWidth());

        if (stage.getCombat().getGameStep().equals("Selection")) {

            if (actor.getCharacter().isActive()) {
                System.out.println("[CharacterClickListener] Selected "+actor.getCharacter().getName());
                stage.combatHUD.showAbilityButtons(actor.getCharacter());
            } else {
                System.out.println("[CharacterClickListener] No actions left");
            }

            stage.getCombat().setGameStep("Action");

        }

        if (stage.getCombat().getGameStep().equals("Targeting")) {

            System.out.println("[CharacterClickListener] Targeted "+actor.getCharacter().getName());
            stage.getCombat().getCurrentSelectedAbility().apply(actor.getCharacter());
            actor.getHit(10);
            stage.getCombat().setGameStep("Selection");

        }

    }
}
