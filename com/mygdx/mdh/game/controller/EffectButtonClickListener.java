package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.hud.AbilityButton;
import com.mygdx.mdh.game.hud.CombatHUD;
import com.mygdx.mdh.game.hud.EffectButton;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 28/01/2016.
 */
public class EffectButtonClickListener extends ClickListener {
    private EffectButton effect;


    public EffectButtonClickListener(EffectButton effect) {
        this.effect = effect;
    }



    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        String description = effect.getEffect().getName()+"\n";
        description += "- "+ effect.getEffect().getDescription()+"\n";
        CombatHUD.showInfo(description,25,150);


        super.enter(event, effect.getX()+x, effect.getY()+y, pointer, fromActor);

    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        CombatHUD.showInfo(null,0,0);
    }
}
