package com.mygdx.mdh.game.hud;

/**
 * Created by zubisoft on 29/01/2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 28/01/2016.
 */

public class EffectButton extends ImageButton {

    private Effect effect;
    Texture sprite;

    public EffectButton(Effect effect) {
        super((new SpriteDrawable(new Sprite(Assets.instance.effects.get("icons/"+effect.getIcon())))));
        this.effect = effect;

        this.setBounds(this.getX(),this.getY(), 20,20);


        //sprite = new Texture(Gdx.files.internal(ability.getPic()));

    }

    public Effect getEffect() {return effect;}

    public Texture getSprite() {
        return sprite;
    }

    public void draw (SpriteBatch batch) {
        LOG.print(1,"draw", LOG.ANSI_GREEN);
        draw(batch,1.0f);

    }
}