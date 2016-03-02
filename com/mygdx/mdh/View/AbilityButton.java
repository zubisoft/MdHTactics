package com.mygdx.mdh.View;

/**
 * Created by zubisoft on 29/01/2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.MDHTactics;
import com.mygdx.mdh.Model.Ability;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by zubisoft on 28/01/2016.
 */

public class AbilityButton extends ImageButton {

    private Ability ability;
    Texture sprite;

    public AbilityButton(Ability ability, int startX, int startY) {
        super((new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal(ability.getPic()))))));
        this.ability = ability;

        /*
        this.setX(startX);
        this.setY(startY);
        */
        this.setWidth(50);
        this.setHeight(50);
        //this.setBounds(this.getX(),this.getY(), this.getWidth(),this.getHeight());

        //sprite = new Texture(Gdx.files.internal(ability.getPic()));

    }

    public Ability getAbility() {return ability;}

    public Texture getSprite() {
        return sprite;
    }

    public void draw (SpriteBatch batch) {

        draw(batch,1.0f);

    }
}