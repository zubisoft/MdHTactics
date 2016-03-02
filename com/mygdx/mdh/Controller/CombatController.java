package com.mygdx.mdh.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
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
import com.mygdx.mdh.View.*;

import java.util.*;


/**
 * Created by zubisoft on 28/01/2016.
 */
public class CombatController extends Stage {

    TiledMap tiledMap;
    Combat combat;
    java.util.List<CharacterActor> characterActors;
    public CombatHUD combatHUD;

    public CameraManager cameraManager;


    CharacterActor selectedCharacter;

    float stateTime;

    public IsoMapActor map;


    public CombatController() {
        super();

        //Initialize map
        tiledMap = new TmxMapLoader().load("core/assets/sample.tmx");

        //Gdx.input.setInputProcessor(new CombatController(this));

        this.characterActors = new ArrayList<CharacterActor>();



        this.combat = new Combat();
        combat.populateSampleMap();


        Gdx.input.setInputProcessor(this);

        combatHUD = new CombatHUD(this);
        this.addActor(combatHUD);

        stateTime = 0;

        cameraManager = new CameraManager();

        map=new IsoMapActor();
        this.addActor(map);

        createActorsForLayer( combat );

        cameraManager.setPosition(new Vector2(map.getCellWidth()*64,map.getCellHeigth()*32));


    }

    public boolean isEndOfTurn () {
        for(CharacterActor a: characterActors) {
            if (a.getCharacter().isActive() & a.getCharacter().isFriendly()) return false;
        }
        return true;
    }

    public boolean  friendliesActive() {

        for(CharacterActor c: getCharacterActors()) {
            if(c.getCharacter().isFriendly() & c.getCharacter().isActive()) {
                return true;
            }
        }
        return false;

    }

    public boolean  baddiesActive() {

        for(CharacterActor c: getCharacterActors()) {
            if(!c.getCharacter().isFriendly() & c.getCharacter().isActive()) {
                return true;
            }
        }
        return false;

    }


    public void update() {
       // System.out.println(Gdx.graphics.getDeltaTime());
        stateTime = Gdx.graphics.getDeltaTime();           // #15

        for(CharacterActor a: characterActors) {
            a.update(stateTime);
        }

        if (!friendliesActive() & selectedCharacter != null) {
            if (selectedCharacter.isReady()) {

                CombatHUD.notificationText = "BADDIES";
                updateBaddies();
            }

        }

        if (!friendliesActive() & !baddiesActive()) {
            CombatHUD.notificationText = "START";
            this.startTurn();
        }

        /*
        if (friendliesActive()) {
            EOTButton = null;
            //System.out.println("[CombatController] Friendly");
        } else {
            showEndOfTurnButton();
            System.out.println("[CombatController] End Turn");
        }
        */
    }


    //private void createActorsForLayer(TiledMapTileLayer tiledLayer, Encounter encounter, HUD hud) {
    private void createActorsForLayer(Combat encounter) {

        /*
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

*/


        for (int x = 0; x < map.getCellWidth(); x++) {
            for (int y = 0; y < map.getCellHeigth(); y++) {
                //TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
                //MapCellActor actor = new MapCellActor(tiledMap, new MapCell(x,y));
                IsoMapCellActor actor = map.getCell(x,y);
                this.addActor(actor);
                EventListener eventListener = new TiledMapClickListener(actor);
                actor.addListener(eventListener);
                //if (y<3 && x <3) { System.out.println("x,y"+x * tiledLayer.getTileWidth()+","+ y * tiledLayer.getTileHeight()+"realx,realy"+tiledLayer.getTileWidth()+","+tiledLayer.getTileHeight());}
            }
        }

        for ( Character character: encounter.getCharacters() ) {


            //Character character = new Character("Hagen",100,100,true);
            IsoMapCellActor m= map.getCell( character.getCellx(),character.getCelly() );
            Vector2 position = m.getPosition();
            CharacterActor actor = new CharacterActor(character,position.x,position.y);
            actor.setOrigin(m.getWidth()/2-35,m.getHeight()/2-10);
            actor.setPosition(position.x,position.y);

            this.addActor(actor);
            this.characterActors.add(actor);

            System.out.println("[CombatController] Adding actor "+actor.toString());

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





    public Combat getCombat() {
        return combat;
    }

    public java.util.List<CharacterActor> getCharacterActors() {
        return characterActors;
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

    public void updateBaddies () {
        this.combat.setGameStep("Baddies");
        for (CharacterActor c : getCharacterActors()) {
            if (!c.getCharacter().isFriendly() & c.getCharacter().isActive()) {
                c.moveToCell(1, 1);
            }


        }
    }

    public void startTurn() {
        for(CharacterActor c: getCharacterActors()) {
            c.getCharacter().startTurn();
        }
    }


    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            cameraManager.move(-32,0);
        if(keycode == Input.Keys.RIGHT)
            cameraManager.move(32,0);
        if(keycode == Input.Keys.UP)
            cameraManager.move(0,-32);
        if(keycode == Input.Keys.DOWN)
            cameraManager.move(0,32);
        if(keycode == Input.Keys.NUM_1);
            //game.getTiledMap().getLayers().get(0).setVisible(!game.getTiledMap().getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2);
            //game.getTiledMap().getLayers().get(1).setVisible(!game.getTiledMap().getLayers().get(1).isVisible());
        return false;
    }

}