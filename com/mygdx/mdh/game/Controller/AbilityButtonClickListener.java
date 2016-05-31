package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.hud.AbilityButton;
import com.mygdx.mdh.game.hud.CombatHUD;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.util.LOG;

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

        ((CombatHUD)evt.getStage()).controller.setGameStep(Combat.GameStepType.TARGETING);
        ((CombatHUD)evt.getStage()).controller.setCurrentSelectedAbility(abilityButton.getAbility());

        System.out.println("[AbilityButtonCL] Selected "+abilityButton.getAbility().getType()+" ability from "+abilityButton.getAbility().getSource().getName());

    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (!abilityButton.isVisible()) return;

        String description=abilityButton.getAbility().getName()+"\n";
        for (Effect e: abilityButton.getAbility().getEffects())
            description += "- "+ e.getDescription()+"\n";
        CombatHUD.showInfo(description,500,125);


        super.enter(event, abilityButton.getX()+x, abilityButton.getY()+y, pointer, fromActor);

    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (!abilityButton.isVisible()) return;

        super.exit(event, x, y, pointer, toActor);
        CombatHUD.showInfo(null,0,0);
    }
}
