package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.hud.AbilityButton;
import com.mygdx.mdh.game.hud.CombatHUD;
import com.mygdx.mdh.game.model.Combat;

/**
 * Created by zubisoft on 28/01/2016.
 */
public class AbilityButtonClickListener extends ClickListener {
    private AbilityButton abilityButton;

    public AbilityButtonClickListener(AbilityButton ability) {
        this.abilityButton = ability;
    }

    @Override
    public void clicked(InputEvent evt, float x, float y) {

        ((CombatHUD)evt.getStage()).controller.combat.setGameStep(Combat.GameStepType.TARGETING);
        ((CombatHUD)evt.getStage()).controller.setCurrentSelectedAbility(abilityButton.getAbility());

        System.out.println("[AbilityButtonCL] Selected "+abilityButton.getAbility().getType()+" ability from "+abilityButton.getAbility().getSource().getName());

    }
}
