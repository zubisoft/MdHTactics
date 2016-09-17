package com.mygdx.mdh.game.controller;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;

/**
 * Created by zubisoft on 14/04/2016.
 */
public interface CharacterChangeListener {

    default  void onCharacterHit (int damage) {}
    default  void onCharacterActive (Character c) {}
    default  void onCharacterInactive (Character c) {}
    default  void onAbilityUnlock (Character c, Ability a) {}

}
