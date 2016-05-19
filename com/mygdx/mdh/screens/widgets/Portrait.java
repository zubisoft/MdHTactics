package com.mygdx.mdh.screens.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.model.Character;
import javafx.scene.paint.Color;

/**
 * Created by zubisoft on 19/05/2016.
 */
public class Portrait extends Stack {


    Character character;

    boolean selected;
    Image portraitFrame = new Image(Assets.instance.guiElements.get("charselection_portrait_frame"));

    public Portrait (Character character) {

        super();
        this.character = character;

        this.setSize(140,140);
        this.add( new Image(Assets.instance.guiElements.get("charselection_portrait")));

        if (character != null) {
            Container c = new Container(new Image(Assets.instance.characters.get(character.getPic()).portrait));
            //c.padLeft(25);
            //c.setWidth(115);
            c.align(Align.center);
            this.add(c);
        }

        this.add( portraitFrame );

    }

    public void setSelected(boolean selected) {
        if (selected) portraitFrame.setColor(new com.badlogic.gdx.graphics.Color(1.0f, 0.84313726f, 0.0f, 1));
        else  portraitFrame.setColor(new com.badlogic.gdx.graphics.Color(1f, 1f, 1f, 1));

        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

}
