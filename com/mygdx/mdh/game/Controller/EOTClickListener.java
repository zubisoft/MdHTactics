package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.hud.AbilityButton;

/**
 * Created by zubisoft on 28/01/2016.
 */
public class EOTClickListener extends ClickListener {
    private AbilityButton abilityButton;

    public EOTClickListener() {

    }

    @Override
    public void clicked(InputEvent evt, float x, float y) {
        CombatController stage = (CombatController)evt.getStage();

        //stage.startBaddiesTurn();

    }
}
