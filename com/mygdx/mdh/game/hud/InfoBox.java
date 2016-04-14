package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 13/04/2016.
 */
public class InfoBox extends Actor {
    private Table t,t1;
    Label text = new Label("",Assets.uiSkin, "default-font", Color.WHITE);

    public InfoBox () {

        TextureRegion tex = Assets.instance.guiElements.get("info_box");
        NinePatchDrawable tableBackground = new NinePatchDrawable(new NinePatch(tex,20,20,10,10));

        t = new Table();
        t.setBackground(tableBackground);
        t.setBounds(100,100,300,100);

        t1 = new Table();
        t1.setBounds(100,100,100,100);
        t1.align(Align.left);
        t1.pad(10);
        t1.add(text);

    }

    @Override
    public void setPosition(float x, float y) {
        t.setPosition(x,y);
        t1.setPosition(x,y);

        super.setPosition(x, y);


    }

    public void setText(String text) {
        this.text.setText(text);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.setColor(1f, 1f, 0.8f,  1.0f);
        t.draw(batch,0.8f);
        batch.setColor(1f, 1f, 1f,  1.0f);

        t1.draw(batch,1.0f);
    }
}
