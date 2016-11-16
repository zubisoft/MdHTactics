package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 13/04/2016.
 */
public class ScrollableBox extends Group {

    private ScrollPane box;
    private TextArea textArea;
    Container c;


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


        textArea.setPrefRows(100);

        textArea.setHeight(2000);
        textArea.setWidth(300);

        c = new Container(textArea);
        c.setHeight(textArea.getPrefHeight()+50);
        c.fillX();


        //c.setSize(200,200);

        Container c1 = new Container(c);
        c1.fillX();
        c1.pad(15);


        box = new ScrollPane(c1);


        NinePatchDrawable tableBackground = new NinePatchDrawable(new NinePatch(Assets.instance.guiElements.get("menus/generic-box"),8,8,8,8));
        box.getStyle().background = tableBackground;

        SpriteDrawable txr = new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/knob")));

        box.getStyle().vScrollKnob = txr;


        txr = new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/scrollbar")));
        box.getStyle().vScroll = txr;
        box.getStyle().vScroll.setMinWidth(20);
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
/*
        textArea.setPrefRows(textArea.getText().split("\n").length+1);
        textArea.invalidate();
        c.invalidate();
        box.invalidate();
        System.out.println(textArea.getPrefHeight()+" "+textArea.getHeight());*/

    }

    public void addText(String text) {

        this.textArea.setText(textArea.getText()+text+"\n");

    }


}
