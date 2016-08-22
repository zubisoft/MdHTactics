package com.mygdx.mdh.game.hud;

/**
 * Created by zubisoft on 29/01/2016.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 28/01/2016.
 */

public class AbilityButton extends ImageButton {

    private Ability ability;

    public boolean isClickable() {
        return clickable;
    }



    boolean clickable = true;

    public AbilityButton(Ability ability) {

        super((new SpriteDrawable(new Sprite(Assets.instance.abilities.get(ability.getPic())))));
        this.ability = ability;

        this.setBounds(this.getX(),this.getY(), 50,50);



        //sprite = new Texture(Gdx.files.internal(ability.getPic()));

    }

    public Ability getAbility() {return ability;}



    @Override
    public void act(float delta) {
        super.act(delta);

        if (ability.getCurrentCooldown()>0 && clickable) {
            getImage().setColor(Color.GRAY);
            clickable = false;
        } else if (ability.getCurrentCooldown()==0 && !clickable) {
            getImage().setColor(Color.WHITE);
            clickable = true;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        super.draw(batch, parentAlpha);
        //batch.draw(sprite,getX(),getY(),50,50);
    }
}