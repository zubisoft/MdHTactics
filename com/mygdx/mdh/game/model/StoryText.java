package com.mygdx.mdh.game.model;

/**
 * Created by zubisoft on 07/05/2016.
 */
public class StoryText {
    public String text;
    public Character character;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setCharacter(String characterId) {
        this.character = Character.loadFromJSON(characterId);
    }

    public StoryText() {

    }


    public StoryText (String text, String character) {
        this.text=text;
        this.character=Character.loadByName(character);

    }
}
