package com.mygdx.mdh.screens.widgets;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 05/09/2016.
 */
public class WidgetAbilitySheet extends Window {



    public void close() {
        this.getParent().removeActor(this);
    }

    public WidgetAbilitySheet(Ability a) {
        super("Ability",Assets.uiSkin);

        this.setSize(400,600); //Default size
        this.align(Align.left);
        //this.pad(50);
        this.setMovable(true);

        Label closeButton = new Label("[x]",Assets.uiSkin,"handwritten_black" );
        this.getTitleTable().add(closeButton).align(Align.right);
        closeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return super.touchDown(event, x, y, pointer, button);
            }
        });



        Effect mainEffect = a.getEffects().get(0);


        TextureRegion tex = Assets.instance.guiElements.get("menus/info_box");
        NinePatchDrawable tableBackground = new NinePatchDrawable(new NinePatch(tex,20,20,20,20));


        Image icon = new Image(Assets.instance.abilities.get(a.getPic()));
        icon.setSize(50,50);

        Table abilityInfo = new Table();
        abilityInfo.add(new Label(a.getName(),Assets.uiSkin,"handwritten_black" )).align(Align.left);
        abilityInfo.row();
        abilityInfo.add(new Label(mainEffect.getEffectType()+" "+mainEffect.getEffectClass() ,Assets.uiSkin,"handwritten_black" )).align(Align.left);
        abilityInfo.row();
        if (a.getRequiredLevel()>0)
            abilityInfo.add(new Label("Required Level:"+a.getRequiredLevel(),Assets.uiSkin,"handwritten_black" )).align(Align.left);

        Table mainLayout = new Table();
        mainLayout.add(icon).size(50,50);
        mainLayout.add(abilityInfo);
        mainLayout.row();


        mainLayout.add(new Label("Range", Assets.uiSkin, "handwritten_black"));
        mainLayout.add(new Label("" + a.getRange(), Assets.uiSkin, "handwritten_black"));
        mainLayout.row();

        if (a.getArea()>0) {
            mainLayout.add(new Label("Area",Assets.uiSkin,"handwritten_black" ));
            mainLayout.add(new Label(""+a.getArea(),Assets.uiSkin,"handwritten_black" ));
            mainLayout.row();
        }

        if (mainEffect.getChance()!=1.0f) {
        mainLayout.add(new Label("Chance",Assets.uiSkin,"handwritten_black" ));
        mainLayout.add(new Label(""+mainEffect.getChance(),Assets.uiSkin,"handwritten_black" ));
        mainLayout.row();
        }

        if (mainEffect.getDiceNumber()>0 || mainEffect.getModifier()>0) {
            mainLayout.add(new Label("Roll", Assets.uiSkin, "handwritten_black"));
            mainLayout.add(new Label(""
                    + (mainEffect.getDiceNumber() != 0 ? mainEffect.getDiceNumber() + "d" + mainEffect.getDiceSides() : "")
                    + (mainEffect.getModifier() > 0 ?  "+" : "")
                    + (mainEffect.getModifier() != 0 ?  mainEffect.getModifier() : "")
                    , Assets.uiSkin, "handwritten_black"));
            mainLayout.row();
        }

        if (mainEffect.getDuration()>0) {
            mainLayout.add(new Label("Duration", Assets.uiSkin, "handwritten_black"));
            mainLayout.add(new Label("" + mainEffect.getDuration(), Assets.uiSkin, "handwritten_black"));
            mainLayout.row();
        }

        if (a.getCooldown()>0){
            mainLayout.add(new Label("Cooldown", Assets.uiSkin, "handwritten_black"));
            mainLayout.add(new Label("" + a.getCooldown(), Assets.uiSkin, "handwritten_black"));
            mainLayout.row();
        }

        if (mainEffect.getHits()>1) {
            mainLayout.add(new Label("Hits", Assets.uiSkin, "handwritten_black"));
            mainLayout.add(new Label("" + mainEffect.getHits(), Assets.uiSkin, "handwritten_black"));
            mainLayout.row();
        }

        if (mainEffect.getStacking()>0) {
            mainLayout.add(new Label("Stacking", Assets.uiSkin, "handwritten_black"));
            mainLayout.add(new Label("" + mainEffect.getStacking(), Assets.uiSkin, "handwritten_black"));
            mainLayout.row();
        }

        if (mainEffect.getEffectSubType().size()>0) {
            mainLayout.add(new Label("Type", Assets.uiSkin, "handwritten_black"));
            mainLayout.add(new Label("" + mainEffect.getEffectSubType(), Assets.uiSkin, "handwritten_black"));
            mainLayout.row();
        }

        if (mainEffect.getConditionalEffects().size()>0) {
            mainLayout.add(new Label("Requirements", Assets.uiSkin, "handwritten_black"));
            mainLayout.add(new Label("" + mainEffect.getConditionalEffects(), Assets.uiSkin, "handwritten_black"));
            mainLayout.row();
        }

        //Other effects
        mainLayout.setBackground(tableBackground);

        this.add(mainLayout);

        this.pack();

    }


}
