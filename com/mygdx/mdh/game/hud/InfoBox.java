package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 13/04/2016.
 */
public class InfoBox extends Actor {
    private Table t,t1;
    TextArea textArea = new TextArea("",Assets.uiSkin, "default");

    public InfoBox () {

        TextureRegion tex = Assets.instance.guiElements.get("menus/info_box");
        NinePatchDrawable tableBackground = new NinePatchDrawable(new NinePatch(tex,20,20,20,20));

        t = new Table();
        t.setBackground(tableBackground);
        t.setSize(300+20,100);
        t.pad(20);



        textArea.setSize(300,100);

    }

    @Override
    public void setPosition(float x, float y) {
        t.setPosition(x-10,y-10);
        textArea.setPosition(x,y);

        super.setPosition(x, y);


    }

    public void setTextArea(String text) {

        if (text == null) return;

            int counter = 0;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == '\n') {
                    counter++;
                }
            }

        this.textArea.setText(text);


        textArea.setPrefRows(counter);
        textArea.setHeight(counter*textArea.getStyle().font.getLineHeight());
        t.setHeight(textArea.getHeight()+20);



    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.setColor(1f, 1f, 0.8f,  1.0f);
        t.draw(batch,1f);
        batch.setColor(1f, 1f, 1f,  1.0f);

        textArea.draw(batch,1.0f);
    }
}
