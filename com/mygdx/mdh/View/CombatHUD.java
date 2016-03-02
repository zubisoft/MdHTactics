package com.mygdx.mdh.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.Controller.AbilityButtonClickListener;
import com.mygdx.mdh.Controller.CombatController;
import com.mygdx.mdh.Model.Ability;
import com.mygdx.mdh.Model.Character;

import java.util.ArrayList;

/**
 * Created by zubisoft on 11/02/2016.
 */
public class CombatHUD extends Group {

    java.util.List<AbilityButton> abilityButtons;
    ImageButton EOTButton;

    CombatController controller;

    Table hudTableLayout;

    public static String notificationText;


    public CombatHUD (CombatController controller) {

        this.controller = controller;

        abilityButtons = new ArrayList<>();

        EOTButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("core/assets/btn-end_turn.png")))));
        EOTButton.setWidth(50);
        EOTButton.setHeight(50);
        EOTButton.setX(800);
        EOTButton.setY(50);

        notificationText="Hello World";

    }


    public void showAbilityButtons(Character selectedCharacter) {


        Skin skin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));
        Label nameLabel = new Label("Name:", skin);


        hudTableLayout = new Table();
        hudTableLayout.center().bottom();
        hudTableLayout.pad(10);
        hudTableLayout.setPosition(400,50);




        int i = 0;
        for (Ability ability : selectedCharacter.getAbilities()) {
            AbilityButton actor = new AbilityButton(ability,300+i*70,20);

            hudTableLayout.add(actor).size(50,50).pad(2);

            /*
            this.abilityButtons.add(actor);
this.addActor(actor);
            i = i+1;
            */

            EventListener eventListener = new AbilityButtonClickListener(actor);
            actor.addListener(eventListener);
        }

        this.addActor(hudTableLayout);
    }

    public void hideAbilityButtons() {


        for (Actor actor :this.abilityButtons) {
            actor.remove();
        }
        this.abilityButtons.clear();


    }


    public java.util.List<AbilityButton> getAbilityButtons() {
        return abilityButtons;
    }
    public ImageButton getEOTButton() {
        return EOTButton;
    }


    public void update  (float stateTime) {

    }

    public void render (SpriteBatch batch) {
        Texture sprite = new Texture(Gdx.files.internal("core/assets/HUD-background.png"));
        batch.setColor(1f, 1f, 1f,  0.8f);
        batch.draw(sprite,450,0);
        batch.setColor(1f, 1f, 1f, 1f);

        BitmapFont font = new BitmapFont();
        font.draw(batch, notificationText, 450, 100);

        //Draw current character abilities
        if (controller.getSelectedCharacter() != null ) {
            /*
            for (AbilityButton a : this.getAbilityButtons()) {
                a.draw(batch);
            }
            */
            if (controller.getSelectedCharacter().isReady())
                hudTableLayout.draw(batch,1.0f);

        }


        if (controller.isEndOfTurn()) {

            this.getEOTButton().draw(batch, 1.0f);

        }
    }

}
