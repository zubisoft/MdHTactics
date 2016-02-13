package com.mygdx.mdh.Controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.View.AbilityButton;

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
        CombatStage stage = (CombatStage)evt.getStage();

        stage.getCombat().setGameStep("Targeting");
        stage.getCombat().setCurrentSelectedAbility(abilityButton.getAbility());

        System.out.println("[AbilityButtonCL] Selected "+abilityButton.getAbility().getType()+" ability from "+abilityButton.getAbility().getSource().getName());


    }
}
