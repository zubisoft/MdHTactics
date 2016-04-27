package com.mygdx.mdh.game.controller;
import com.mygdx.mdh.game.model.Character;

/**
 * Created by zubisoft on 14/04/2016.
 */
public interface CharacterChangeListener {

    public void onCharacterHit (int damage) ;
    public void onCharacterActive (Character c);
    public void onCharacterInactive (Character c);

}
