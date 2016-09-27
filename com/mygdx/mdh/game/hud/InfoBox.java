package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 13/04/2016.
 */
public class InfoBox extends Actor {
    private Table t,t1;
    TextArea textArea = new TextArea("",Assets.uiSkin, "default");

    static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/skin/Alyssa_Kayla.ttf"));
    BitmapFont font15;

    public InfoBox () {

        TextureRegion tex = Assets.instance.guiElements.get("menus/info_box");
        NinePatchDrawable tableBackground = new NinePatchDrawable(new NinePatch(tex,20,20,20,20));

        t = new Table();
        t.setBackground(tableBackground);
        t.setSize(300+20,100);
        t.pad(20);


        textArea.setSize(300,100);


        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        parameter.borderColor=Color.BLACK;
        parameter.borderWidth=0.2f;
        parameter.color = Color.BLACK;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
         font15 = generator.generateFont(parameter);


        textArea.getStyle().font = font15;

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

        String[] split = text.split("\n");
        int maxLen = 0;
        for (String s:split) {
            maxLen=(s.length()>maxLen?s.length():maxLen);
        }

        textArea.setWidth(maxLen*10);
        t.setWidth(textArea.getWidth()+20);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.setColor(1f, 1f, 0.8f,  1.0f);
        t.draw(batch,1f);
        batch.setColor(1f, 1f, 1f,  1.0f);

        //FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/skin/ArchitectsDaughter.ttf"));



        //font15.draw(batch, "MECAGUEN LA leche dona rogelia", t.getX()+20, t.getY()+20);

        textArea.draw(batch,1.0f);
    }
}
