package com.mygdx.mdh.game.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by zubisoft on 31/01/2016.
 */
public class LifeBar {

    ProgressBar bar;
    CharacterActor actor;

    public LifeBar (CharacterActor actor) {

        this.actor = actor;

        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        skin.add("green", new Texture(pixmap));

        pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        skin.add("red", new Texture(pixmap));


        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("red"), skin.newDrawable("green"));
        barStyle.knobBefore = barStyle.knob;
        bar = new ProgressBar(0, actor.getCharacter().getMaxHealth(), 1f, false, barStyle);
        bar.setValue(actor.getCharacter().getHealth());

        bar.setWidth(80);

    }

    public void draw(SpriteBatch batch) {
        //Get model data
        bar.setValue(actor.getCharacter().getHealth());

        //Update graphics
        bar.setX(actor.getX()+10);
        bar.setY(actor.getY());
        bar.setOriginX(actor.getOriginX());
        bar.setOriginY(actor.getOriginY());
        bar.draw(batch,1.0f);


    }


    public void setX (float x) {
        bar.setX(x);
    }

    public void setY (float x) {
        bar.setY(x);
    }

    public float getX (float x) {
        return bar.getX();
    }

    public float getY (float x) {
        return bar.getY();
    }

}
