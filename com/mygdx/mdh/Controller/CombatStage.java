package com.mygdx.mdh.Controller;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.mdh.Controller.*;

import com.mygdx.mdh.Model.Ability;
import com.mygdx.mdh.Model.Character;
import com.mygdx.mdh.Model.Combat;
import com.mygdx.mdh.Model.MapCell;
import com.mygdx.mdh.View.AbilityButton;
import com.mygdx.mdh.View.CharacterActor;
import com.mygdx.mdh.View.MapCellActor;

import java.util.*;


/**
 * Created by zubisoft on 28/01/2016.
 */
public class CombatStage extends Stage {

    private TiledMap tiledMap;
    Combat combat;
    java.util.List<CharacterActor> characterActors;
    java.util.List<AbilityButton> abilityButtons;

    CharacterActor selectedCharacter;




    public CombatStage(TiledMap tiledMap, Combat combat) {
        super();

        this.tiledMap = tiledMap;
        this.combat=combat;
        this.characterActors = new ArrayList<CharacterActor>();

		/*
        for (MapLayer layer : tiledMap.getLayers()) {
            TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
            createActorsForLayer(tiledLayer);
        }
		*/
        this.abilityButtons = new ArrayList<AbilityButton>();
        createActorsForLayer( combat );


    }


    //private void createActorsForLayer(TiledMapTileLayer tiledLayer, Encounter encounter, HUD hud) {
    private void createActorsForLayer(Combat encounter) {

        TiledMapTileLayer tiledLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        for (int x = 0; x < tiledLayer.getWidth(); x++) {
            for (int y = 0; y < tiledLayer.getHeight(); y++) {
                //TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
                MapCellActor actor = new MapCellActor(tiledMap, new MapCell(x,y));
                this.addActor(actor);
                EventListener eventListener = new TiledMapClickListener(actor);
                actor.addListener(eventListener);
                //if (y<3 && x <3) { System.out.println("x,y"+x * tiledLayer.getTileWidth()+","+ y * tiledLayer.getTileHeight()+"realx,realy"+tiledLayer.getTileWidth()+","+tiledLayer.getTileHeight());}
            }
        }


        for ( Character character: encounter.getCharacters() ) {


            //Character character = new Character("Hagen",100,100,true);
            CharacterActor actor = new CharacterActor(character,character.getCellx(),character.getCelly());

            this.addActor(actor);
            this.characterActors.add(actor);

            System.out.println("[CombatStage] Adding actor "+character.getName()+". Bounds: ("+character.getCellx()*100+","+character.getCelly()*100+","+(character.getCellx()*100+100)+","+(character.getCelly()*100+100)+")");

            EventListener eventListener = new CharacterClickListener(actor);
            actor.addListener(eventListener);
        }

        /*
        for (ActionButton b : hud.getButtons()) {
            addActor(b);
            EventListener eventListener = new CharacterClickListener(b);
            b.addListener(eventListener);
        }
        */
    }

    public void showAbilityButtons(Character selectedCharacter) {

        int i = 0;
        for (Ability ability : selectedCharacter.getAbilities()) {
            AbilityButton actor = new AbilityButton(ability,300+i*70,20);


            this.abilityButtons.add(actor);
            this.addActor(actor);
            i = i+1;

            EventListener eventListener = new AbilityButtonClickListener(actor);
            actor.addListener(eventListener);
        }
    }

    public void hideAbilityButtons() {


        for (Actor actor :this.abilityButtons) {
            actor.remove();
        }
        this.abilityButtons.clear();


    }

    public Combat getCombat() {
        return combat;
    }

    public java.util.List<CharacterActor> getCharacterActors() {
        return characterActors;
    }


    public java.util.List<AbilityButton> getAbilityButtons() {
        return abilityButtons;
    }

    public CharacterActor getSelectedCharacter() {
        return selectedCharacter;
    }

    public void setSelectedCharacter(CharacterActor selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }
}