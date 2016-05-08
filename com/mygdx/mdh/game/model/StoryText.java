package com.mygdx.mdh.game.model;

/**
 * Created by zubisoft on 07/05/2016.
 */
public class StoryText {
    public String text;
    public Character character;

    public StoryText (String text, String character) {
        this.text=text;
        this.character=Character.loadByName(character);

    }
}
