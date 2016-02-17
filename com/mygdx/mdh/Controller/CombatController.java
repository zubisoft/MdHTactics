package com.mygdx.mdh.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
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
public class CombatController extends Stage {

    TiledMap tiledMap;
    Combat combat;
    java.util.List<CharacterActor> characterActors;
    java.util.List<AbilityButton> abilityButtons;
    ImageButton EOTButton;

    CharacterActor selectedCharacter;


    public CombatController() {
        super();

        //Initialize map
        tiledMap = new TmxMapLoader().load("core/assets/sample.tmx");

        //Gdx.input.setInputProcessor(new CombatController(this));

        this.characterActors = new ArrayList<CharacterActor>();
        this.abilityButtons = new ArrayList<AbilityButton>();

        this.combat = new Combat();
        combat.populateSampleMap();

        createActorsForLayer( combat );

        Gdx.input.setInputProcessor(this);


    }

    private boolean friendliesActive () {
        for(CharacterActor a: characterActors) {
            if (a.getCharacter().isActive() & a.getCharacter().isFriendly()) return true;
        }
        return false;
    }

    public void update() {
        if (friendliesActive()) {
            EOTButton = null;
            //System.out.println("[CombatController] Friendli");
        } else {
            showEndOfTurnButton();
            System.out.println("[CombatController] End Turn");
        }
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

            System.out.println("[CombatController] Adding actor "+character.getName()+". Bounds: ("+character.getCellx()*100+","+character.getCelly()*100+","+(character.getCellx()*100+100)+","+(character.getCelly()*100+100)+")");

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

    public void showEndOfTurnButton() {
        EOTButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("core/assets/btn-end_turn.png")))));
        EOTButton.setWidth(50);
        EOTButton.setHeight(50);
        EOTButton.setX(600);
        EOTButton.setY(50);

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

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    public ImageButton getEOTButton() {
        return EOTButton;
    }

/*
    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            game.getCamera().translate(-32,0);
        if(keycode == Input.Keys.RIGHT)
            game.getCamera().translate(32,0);
        if(keycode == Input.Keys.UP)
            game.getCamera().translate(0,-32);
        if(keycode == Input.Keys.DOWN)
            game.getCamera().translate(0,32);
        if(keycode == Input.Keys.NUM_1)
            game.getTiledMap().getLayers().get(0).setVisible(!game.getTiledMap().getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            game.getTiledMap().getLayers().get(1).setVisible(!game.getTiledMap().getLayers().get(1).isVisible());
        return false;
    }
    */
}