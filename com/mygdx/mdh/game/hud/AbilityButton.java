package com.mygdx.mdh.game.hud;

/**
 * Created by zubisoft on 29/01/2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.model.Ability;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by zubisoft on 28/01/2016.
 */

public class AbilityButton extends ImageButton {

    private Ability ability;
    Texture sprite;

    public AbilityButton(Ability ability) {
        super((new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal(ability.getPic()))))));
        this.ability = ability;

        this.setBounds(this.getX(),this.getY(), 50,50);


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