package com.mygdx.mdh.screens.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.util.Assets;

import java.util.List;

/**
 * Created by zubisoft on 19/05/2016.
 */
public class Portrait extends Stack {


    Character character;

    public void setAlternateCharacters(List<Character> alternateCharacters) {
        this.alternateCharacters = alternateCharacters;
        if (character != null) {
            int i = 0;
            for (Character c: alternateCharacters) {
                if (character.characterId.equals(c.characterId)) {
                    altNum = i;
                }
                i++;
            }
        }
    }

    List<Character> alternateCharacters;
    int altNum = 0;

    boolean selected;
    Image portraitFrame = new Image(Assets.instance.guiElements.get("menus/charselection_portrait_frame"));
    Container c;

    public Portrait (Character character) {

        super();
        this.character = character;

        this.setSize(140,140);
        this.add( new Image(Assets.instance.guiElements.get("menus/charselection_portrait")));

        if (character != null) {
            c = new Container(new Image(Assets.instance.characters.get(character.getPic()).portrait));
            //c.padLeft(25);
            //c.setWidth(115);
            c.align(Align.center);
            this.add(c);
        }

        this.add( portraitFrame );

    }

    public void next() {
        if (alternateCharacters != null && altNum+1 < alternateCharacters.size()) {
            character = alternateCharacters.get(++altNum);
            this.c.setActor(new Image(Assets.instance.characters.get(character.getPic()).portrait));
        }
    }

    public void prev() {
        if (alternateCharacters != null && altNum-1 >= 0) {
            character = alternateCharacters.get(--altNum);
            this.c.setActor(new Image(Assets.instance.characters.get(character.getPic()).portrait));
        }
    }

    public boolean hasNext() {
        if (alternateCharacters != null && altNum+1 < alternateCharacters.size()) {
            return true;
        }
        return false;
    }

    public boolean hasPrev() {
        if (alternateCharacters != null && altNum-1 >= 0) {
            return true;
        }
        return false;
    }

    public void setHighlight(boolean selected) {
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
