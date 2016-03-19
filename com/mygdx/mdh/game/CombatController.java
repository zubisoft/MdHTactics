package com.mygdx.mdh.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
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
import com.mygdx.mdh.game.characters.actions.EffectAction;
import com.mygdx.mdh.game.controller.CharacterClickListener;
import com.mygdx.mdh.game.controller.CombatInputListener;
import com.mygdx.mdh.game.controller.TiledMapClickListener;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.hud.CombatHUD;
import com.mygdx.mdh.game.map.IsoMapActor;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.model.Effect;

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


    boolean baddiesBegin;


    public enum GameTurn {
        PLAYER, BADDIES
    }

    GameTurn gameTurn;


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
        multiplexer.addProcessor(new CombatInputListener(this));
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);


        Texture texture = new Texture(Gdx.files.internal("core/assets/graphics/background/arena_background.png"));
        background = new Sprite(texture);
        background.setPosition(0,-275);

        baddiesBegin=true;
        gameTurn = GameTurn.PLAYER;



/*
        for (CharacterActor ca: characterActors) {
            if(!ca.getCharacter().isFriendly()) ca.addEffectAction(new EffectAction(new Effect("FIRE"),0.15f));
        }
*/
    }


    /** Initializes map cells and characters.*/
    private void createActorsForLayer(Combat encounter) {


        for ( Character character: encounter.getCharacters() ) {

            //Get the graphic location of the cell
            IsoMapCellActor m= map.getCell( character.getCell().getMapCoordinates());
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

    public void playerTurnBegin () {
        gameTurn = GameTurn.PLAYER;

        CombatHUD.notificationText = "Player Start";
        for (CharacterActor c : getCharacterActors()) {
            if(c.getCharacter().isFriendly()) {
                c.getCharacter().startTurn();
            }
        }
    }

    public void baddiesTurnBegin () {

        gameTurn = GameTurn.BADDIES;

        /* Start turn for all characters */
        CombatHUD.notificationText = "Baddies Start";
        for (CharacterActor c : getCharacterActors()) {
            if(!c.getCharacter().isFriendly()) {
                c.getCharacter().startTurn();
            }
        }

        /* Execute orders */
        for (CharacterActor c : getCharacterActors()) {
            if (!c.getCharacter().isFriendly() & c.getCharacter().isActive() & c.isReady()) {
                IsoMapCellActor aux = map.getCell(1, 5);
                c.moveToCell(aux);
                aux = map.getCell(1, 6);

                c.moveToCell(aux);

                //aux = map.getCell(2,6);
                //c.moveToCell(aux);
            }
        }
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
            System.out.println("Game Over");
        }

        if (isVictory()) {
            System.out.println("Victory");
        }

        for(CharacterActor a: characterActors) {
            a.update(stateTime);
        }

        if (!friendliesActive() /*& selectedCharacter != null*/) {
            if (selectedCharacter.isReady()) {
                updateBaddies();
            }

        }


        if (!friendliesActive() & gameTurn==GameTurn.PLAYER ) {
            baddiesTurnBegin();
        }

        if (!baddiesActive() & gameTurn==GameTurn.BADDIES ) {
            playerTurnBegin();
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

        //this.startTurn("BADDIES");
/*
        if (baddiesBegin) {


            baddiesBegin = false;

        }
        */

    }

    public void startTurn(String player) {

        if (player.equals("FRIENDLIES")) {

        }

        if (player.equals("BADDIES")) {
            for (CharacterActor c : getCharacterActors()) {
                if(!c.getCharacter().isFriendly()) c.getCharacter().startTurn();
            }
        }

    }


    /**
     * Executes the current selected ability on the target.
     * @param target
     */
    public void executeCurrentAbility(CharacterActor target) {
        Ability a = combat.getCurrentSelectedAbility();
        target.receiveAbility(a);

        selectedCharacter.useAbility();

        if (target.getCharacter().isDead()) this.getActors().removeValue(target,true);

        combat.setGameStep(Combat.GameStepType.SELECTION);
    }


    public void showMovementTiles(CharacterActor actor) {

         map.highlightCells(new Color(0.0f,0.5f,1f,0.2f), map.getCell(actor.getCharacter().getCell().getMapCoordinates()), actor.getCharacter().getMovement());

    }


}