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

/**
 * Created by zubisoft on 05/03/2016.
 */
public class CharacterLifeBar extends Actor {

    static final float BARWIDTH = 60f;
    static final float BARHEIGHT = 10f;


    Sprite barBackground;
    TextureRegion barFill;

    CharacterActor actor;



    public CharacterLifeBar (CharacterActor actor) {

        this.actor = actor;

        Texture texture = new Texture(Gdx.files.internal("core/assets/graphics/combatui/healthbar_bg_small.png"));
        barBackground = new Sprite(texture);
        barBackground.setSize(BARWIDTH, BARHEIGHT);

        texture = new Texture(Gdx.files.internal("core/assets/graphics/combatui/healthbar_fill_small.png"));

        barFill = new TextureRegion(texture);
                //, 0, 0, BARWIDTH, BARHEIGHT);

    }

    public void update () {
        barBackground.setX(actor.getX()+actor.getOriginX());
        barBackground.setY(actor.getY()+actor.getOriginY());

        barFill.setRegionWidth(Math.round(BARWIDTH*actor.getCharacter().getHealth()/actor.getCharacter().getMaxHealth()));
    }

    public void draw(SpriteBatch batch) {
        //Update graphics

        barBackground.draw(batch,1.0f);

        batch.draw(barFill,actor.getX()+actor.getOriginX(),actor.getY()+actor.getOriginY());

    }


    public void setX (float x) {
        barBackground.setX(x);
    }

    public void setY (float x) {
        barBackground.setY(x);
    }
}
