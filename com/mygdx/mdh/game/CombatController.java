package com.mygdx.mdh.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.controller.CharacterClickListener;
import com.mygdx.mdh.game.controller.TiledMapClickListener;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.hud.CombatHUD;
import com.mygdx.mdh.game.map.IsoMapActor;
import com.mygdx.mdh.game.map.IsoMapCellActor;

import java.util.*;


/**
 * Created by zubisoft on 28/01/2016.
 */
public class CombatController extends Stage {

    TiledMap tiledMap;
    public static Combat combat;
    java.util.List<CharacterActor> characterActors;
    public CombatHUD combatHUD;

    public CameraManager cameraManager;


    CharacterActor selectedCharacter;

    float stateTime;

    public IsoMapActor map;

    TextButton button;

    public Sprite background;


    public CombatController() {
        super();

        //Initialize game logic
        this.characterActors = new ArrayList<CharacterActor>();

        this.combat = new Combat();
        combat.populateSampleMap();

        map=new IsoMapActor();
        this.addActor(map);

        createActorsForLayer( combat );

        //Initialize HUD
        combatHUD = new CombatHUD(this);
        //this.addActor(combatHUD);

        Skin uiSkin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));
        //Table layer = new Table();
        //layer.right().bottom();
        // + play button

        //Initialize elements for graphic control
        stateTime = 0;

        cameraManager = new CameraManager();
        cameraManager.setPosition(new Vector2(map.getCellWidth()*64,map.getCellHeigth()*32));

        //Add event handling
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(combatHUD);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);


        Texture texture = new Texture(Gdx.files.internal("core/assets/graphics/background/Arena_Battle_Background.jpeg"));
        background = new Sprite(texture);

    }


    /** Initializes map cells and characters.*/
    private void createActorsForLayer(Combat encounter) {

        for (int x = 0; x < map.getCellWidth(); x++) {
            for (int y = 0; y < map.getCellHeigth(); y++) {

                //Add interactivity for the map tiles
                IsoMapCellActor actor = map.getCell(x,y);
                this.addActor(actor);
                EventListener eventListener = new TiledMapClickListener(actor);
                actor.addListener(eventListener);

            }
        }

        for ( Character character: encounter.getCharacters() ) {

            //Get the graphic location of the cell
            IsoMapCellActor m= map.getCell( character.getCellx(),character.getCelly() );
            Vector2 position = m.getPosition();

            //Place the character in that cell
            CharacterActor actor = new CharacterActor(character,position.x,position.y);
            actor.setOrigin(m.getWidth()/2-35,m.getHeight()/2-10);
            actor.setPosition(position.x,position.y);

            //Add interactivity for character
            this.addActor(actor);
            this.characterActors.add(actor);
            EventListener eventListener = new CharacterClickListener(actor);
            actor.addListener(eventListener);

            System.out.println("[CombatController] Adding actor "+actor.toString());

        }

    }

    /** If the player has no characters active, it is the end of his turn.*/
    public boolean isEndOfTurn () {
        for(CharacterActor a: characterActors) {
            if (a.getCharacter().isActive() & a.getCharacter().isFriendly()) return false;
        }
        return true;
    }

    /** If all player characters are dead, it´s game over.*/
    public boolean isGameOver () {
        for(CharacterActor a: characterActors) {
            if (!a.getCharacter().isDead() & a.getCharacter().isFriendly()) return false;
        }
        return true;
    }

    /** If all enemy characters are dead, it´s a victory*/
    public boolean isVictory () {
        for(CharacterActor a: characterActors) {
            if (!a.getCharacter().isDead() & !a.getCharacter().isFriendly()) return false;
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

        if (isGameOver()) {
          //  System.out.println("Game Over");
        }

        if (isVictory()) {
            //System.out.println("Victory");
        }

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
        if (this.selectedCharacter != null) this.selectedCharacter.setSelected(false);
        if (selectedCharacter != null)  selectedCharacter.setSelected(true);
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
            if (!c.getCharacter().isFriendly() & c.getCharacter().isActive() & c.isReady()) {
                IsoMapCellActor aux = map.getCell(5,8);
                c.moveToCell(aux.getX(), aux.getY());
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