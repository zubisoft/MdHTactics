package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.controller.AbilityButtonClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;

import java.util.ArrayList;

/**
 * Created by zubisoft on 11/02/2016.
 */
public class CombatHUD extends Stage {

    java.util.List<AbilityButton> abilityButtons;
    ImageButton EOTButton;
    MessageBar messageBar;

    public CombatController controller;

    Table hudTableLayout;

    public static String notificationText;


    Texture sprite = new Texture(Gdx.files.internal("core/assets/HUD-background.png"));
    Texture abilityBarSprite = new Texture(Gdx.files.internal("core/assets/HUD-bar.png"));
    Texture characterHUDSprite = new Texture(Gdx.files.internal("core/assets/graphics/combatui/character_hud.png"));


    public CombatHUD (CombatController controller) {

        this.controller = controller;

        abilityButtons = new ArrayList<>();

        EOTButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("core/assets/btn-end_turn.png")))));
        EOTButton.setWidth(50);
        EOTButton.setHeight(50);
        EOTButton.setX(1000);
        EOTButton.setY(50);

        notificationText="Hello World";

        messageBar = new MessageBar();
        this.addActor(messageBar);


    }


    public void showAbilityButtons(Character selectedCharacter) {

        Skin skin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));

        hudTableLayout = new Table();
        hudTableLayout.center().bottom();
        hudTableLayout.pad(10);
        hudTableLayout.setPosition(480,40);
        hudTableLayout.setSize(200,50);




        int i = 0;
        for (Ability ability : selectedCharacter.getAbilities()) {
            AbilityButton actor = new AbilityButton(ability);

            hudTableLayout.add(actor).size(40,40).pad(2);

            EventListener eventListener = new AbilityButtonClickListener(actor);
            actor.addListener(eventListener);
            //this.addActor(actor);
        }


        this.addActor(hudTableLayout);


    }

    public void hideAbilityButtons() {


        for (Actor actor :this.abilityButtons) {
            actor.remove();
        }
        this.abilityButtons.clear();


    }

    public void showMessageBar (String message) {
        messageBar.setMessage(message);
        messageBar.show();

    }


    public java.util.List<AbilityButton> getAbilityButtons() {
        return abilityButtons;
    }
    public ImageButton getEOTButton() {
        return EOTButton;
    }


    public void update  (float deltaTime) {
        messageBar.act(deltaTime);
    }

    public void render (SpriteBatch batch) {

        batch.setColor(1f, 1f, 1f,  1f);

        batch.draw(sprite,950,0);
        batch.setColor(1f, 1f, 1f, 1f);

        BitmapFont font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.draw(batch, notificationText, 1000, 100);

        //Draw current character abilities
        if (controller.getSelectedCharacter() != null ) {


            batch.draw(characterHUDSprite,0,0);
            batch.draw(controller.getSelectedCharacter().portrait,15,15);
            /*
            for (AbilityButton a : this.getAbilityButtons()) {
                a.draw(batch);
            }
            */
            if (controller.getSelectedCharacter().isReady())
                batch.draw(abilityBarSprite,500,0);
                hudTableLayout.draw(batch,1.0f);

        }

        messageBar.draw(batch,1.0f);

/*
        if (controller.isEndOfTurn()) {

            this.getEOTButton().draw(batch, 1.0f);

        }
        */
    }

}
