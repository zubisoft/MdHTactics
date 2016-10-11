package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 13/04/2016.
 */
public class ScrollableBox extends Group {

    private ScrollPane box;
    private TextArea textArea;
    Stack c;


    public ScrollableBox() {

        textArea = new TextArea("",Assets.uiSkin, "default")
/*
        {
            public float getPrefHeight() {
                return getLines() * getStyle().font.getLineHeight();
            }


        }*/
        ;


        //textArea.setPrefRows(20);
        textArea.setFillParent(true);

        //textArea.setPrefRows(20);
        //textArea.setSize(300,200);
        //textArea.getStyle().font = Assets.uiSkin.getFont("handwritten_white");
        //textArea.setFillParent(true);

        //new Label("Unlocked mission\n"+m.getName(),Assets.uiSkin,"default" ))


        c = new Stack();
        c.add(textArea);
        //c.setSize(200,200);





        box = new ScrollPane(c);


        NinePatchDrawable tableBackground = new NinePatchDrawable(new NinePatch(Assets.instance.guiElements.get("menus/generic-box"),20,20,20,20));
        box.getStyle().background = tableBackground;

        SpriteDrawable txr = new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/knob")));

        box.getStyle().vScrollKnob = txr;
        box.getStyle().vScrollKnob.setMinHeight(10);
        box.getStyle().vScrollKnob.setMinWidth(10);

        txr = new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/scrollbar")));
        box.getStyle().vScroll = txr;
        box.getStyle().vScroll.setMinWidth(10);
        box.setFillParent(true);

        //box.debugAll();
       // c.debugAll();


        this.addActor(box);

    }


    @Override
    public void sizeChanged () {
        //c.setSize(getWidth(), getHeight());

        c.invalidate();
        box.invalidate();

    }


    public void setText(String text) {

                this.textArea.setText(text +"\n"

                );

System.out.println("asdf "+textArea.getText().split("\n").length);
        textArea.setPrefRows(textArea.getText().split("\n").length);
        textArea.invalidate();
        c.invalidate();
        box.invalidate();


    }

    public void addText(String text) {

        this.textArea.setText(textArea.getText()+text+"\n");

    }


}
