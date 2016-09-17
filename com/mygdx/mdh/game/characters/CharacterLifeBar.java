package com.mygdx.mdh.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.mdh.game.controller.CharacterChangeListener;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 05/03/2016.
 */
public class CharacterLifeBar extends Actor implements CharacterChangeListener {

    static final float BARWIDTH = 60f;
    static final float BARHEIGHT = 10f;


    TextureRegion barBackground;
    TextureRegion barFill;

    CharacterActor actor;


    public void onCharacterHit (int damage) { };
    public void onCharacterActive (Character c) {};
    public void onCharacterInactive (Character c) {};
    public void onAbilityUnlock (Ability a) {};


    public CharacterLifeBar (CharacterActor actor) {

        this.actor = actor;

        barBackground = new TextureRegion(Assets.instance.guiElements.get("character/CHAR-healthbar-bg"));

        barFill = new TextureRegion(Assets.instance.guiElements.get("character/CHAR-healthbar-fill"));

    }

    public void update (float delta) {
        //barBackground.setX(actor.getX()+actor.getOriginX());
        //barBackground.setY(actor.getY()+actor.getOriginY());

        barFill.setRegionWidth(Math.round(BARWIDTH*actor.getCharacter().getHealth()/actor.getCharacter().getMaxHealth()));
        this.act(delta);


    }

    public void draw(SpriteBatch batch, float parentAlpha) {

        //Update graphics
        batch.setColor(getColor()); //Ignoring parent alpha here really


        //barBackground.draw(batch,1.0f);

        batch.draw(barBackground
                ,actor.getX()+actor.offsetx+Math.round(actor.getWidth()/2-BARWIDTH/2)
                ,actor.getY()+actor.offsety-10);
        batch.draw(barFill
                ,actor.getX()+actor.offsetx+Math.round(actor.getWidth()/2-BARWIDTH/2)
                ,actor.getY()+actor.offsety-10);

        //Return color to normal
        batch.setColor(1f, 1f, 1f, 1f);
    }



}
