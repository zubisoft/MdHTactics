package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
    private TextArea textArea = new TextArea("",Assets.uiSkin, "default");


    public ScrollableBox() {

        textArea.setAlignment(Align.topLeft);
        textArea.setPrefRows(25);
        textArea.getStyle().font = Assets.uiSkin.getFont("handwritten_black");

        Container c = new Container(textArea);
        c.pad(15).align(Align.topLeft);

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


        this.addActor(box);

    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        box.setPosition(x,y);
    }

    @Override
    public void setSize(float x, float y) {
        super.setSize(x, y);
        box.setSize(x,y);

    }

    public void setText(String text) {

        this.textArea.setText(text+"\n");


    }

    public void addText(String text) {

        this.textArea.setText(textArea.getText()+text+"\n");

    }


}
