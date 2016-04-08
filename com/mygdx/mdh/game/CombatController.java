package com.mygdx.mdh.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.mdh.game.IA.StrategyManager;
import com.mygdx.mdh.game.controller.CharacterClickListener;
import com.mygdx.mdh.game.controller.CombatInputListener;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.hud.CombatHUD;
import com.mygdx.mdh.game.map.IsoMapActor;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.model.MapCell;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.LOG;
import com.mygdx.mdh.screens.ScreenManager;
import com.mygdx.mdh.game.model.Map;

import java.util.*;


/**
 * Created by zubisoft on 28/01/2016.
 */
public class CombatController extends Stage {

    public InputMultiplexer multiplexer;

    public static Combat combat;
    java.util.List<CharacterActor> characterActors;
    public CombatHUD combatHUD;

    public CameraManager cameraManager;

    CharacterActor selectedCharacter;
    MapCell selectedCharacterPosition = new MapCell();

    Ability currentSelectedAbility;

    float stateTime;

    public IsoMapActor map;


    public Sprite background;

    /**
     * Flow control variables used to define the logic to run code a single time within the render loop.
     */
    boolean baddiesBegin;
    boolean gameEnd;

    ScreenManager screenManager;

    public IsoMapActor getMap() {
        return map;
    }

    public void setMap(IsoMapActor map) {
        this.map = map;
    }


    public enum GameTurn {
        PLAYER, BADDIES
    }

    GameTurn gameTurn;


    StrategyManager strategyManager;

    public CombatController(ScreenManager screenManager)
    {
        super();
        this.screenManager = screenManager;

        //Initialize game logic
        this.characterActors = new ArrayList<CharacterActor>();

        this.combat = Combat.loadFromJSON("combat01");

        map=new IsoMapActor(combat.getMap());
        this.addActor(map);

        createActorsForLayer( combat );

        //Initialize HUD
        combatHUD = new CombatHUD(this);
        //this.addActor(combatHUD);


        //Initialize elements for graphic control
        stateTime = 0;

        cameraManager = new CameraManager();
        cameraManager.setPosition(new Vector2(map.getCellWidth()*64,map.getCellHeigth()*32));

        //Add event handling
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(combatHUD);
        multiplexer.addProcessor(new CombatInputListener(this));
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);


        background = new Sprite(Assets.instance.maps.get("map01"));
        background.setPosition(0,-275);

        baddiesBegin=true;
        gameEnd = false;
        gameTurn = GameTurn.PLAYER;

        strategyManager = new StrategyManager(this);
    }



    /** Initializes map cells and characters.*/
    private void createActorsForLayer(Combat encounter) {


        for ( Character character: encounter.getCharacters() ) {

            //Get the graphic location of the cell
            IsoMapCellActor m= map.getCell( character.getCell().getMapCoordinates());
            Vector2 position = m.getPosition();

            //Place the character in that cell
            CharacterActor actor = new CharacterActor(character,position.x,position.y);
            actor.setOffset(m.getWidth()/2-35,m.getHeight()/2-10);
            //actor.setPosition(512,139);

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
            if (a.isActive() & a.isFriendly()) return false;
        }
        return true;
    }

    /** If all player characters are dead, it´s game over.*/
    public boolean isGameOver () {
        for(CharacterActor a: characterActors) {
            if (!a.isDead() & a.isFriendly()) return false;
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

        LOG.print(1,"[CombatController] Player Turn Start", LOG.ANSI_YELLOW);

        for (CharacterActor c : getCharacterActors()) {
            if(c.getCharacter().isFriendly()) {
                c.turnStart();
                LOG.print(3,c.toString());
            }
        }
    }

    public void baddiesTurnBegin () {

        gameTurn = GameTurn.BADDIES;

        /* Start turn for all characters */
        LOG.print(1,"[CombatController] Baddies Turn Start", LOG.ANSI_YELLOW);



        for (CharacterActor c : getCharacterActors()) {
            if(!c.getCharacter().isFriendly()) {
                c.turnStart();
                LOG.print(3,c.toString());
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

    public void executeBaddiesTurn() {
        //If other characters have finished doing their stuff, take action
        if (CharacterActor.queueActions.size==0) {
            /* Execute orders */
            int i = 0;
            for (CharacterActor c : getCharacterActors()) {
                if (!c.getCharacter().isFriendly() & c.getCharacter().isActive() & !c.actionInProgress()) {

                    strategyManager.nextAction(i);

               }
                i++;
            }
        }
    }

    public void update(float deltaTime) {
       // System.out.println(Gdx.graphics.getDeltaTime());
        stateTime += deltaTime;          // #15

        combatHUD.update(deltaTime);

        if (isGameOver()) {
            if(!gameEnd) combatHUD.showMessageBar("Game Over");
            System.out.println("Game Over");
            gameEnd=true;
        }

        if (isVictory()) {
            if(!gameEnd) combatHUD.showMessageBar("Victory");
            System.out.println("Victory");
            gameEnd=true;
        }

        for(CharacterActor a: characterActors) {
            a.update(deltaTime);
        }


        if (!friendliesActive() && gameTurn==GameTurn.PLAYER ) {
            //Wait for player's action to finish
            if (!CharacterActor.actionInProgress()) {
                baddiesTurnBegin();
            }
        }

        if (!baddiesActive() && gameTurn==GameTurn.BADDIES ) {
            playerTurnBegin();
        }

        if (gameTurn==GameTurn.BADDIES) {
            executeBaddiesTurn();
        }

        //TODO Convertir esto en una llamada de evento que viene desde el character cuando cambia un atributo
        if (selectedCharacter != null)
            if(selectedCharacter.getCharacter().getCell()!=selectedCharacterPosition)
                if (selectedCharacter.getCharacter().isFriendly() & selectedCharacter.getCharacter().isActive()) {
                    setSelectedCharacter(selectedCharacter);
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

    /**
     * Selects a new character as the currently active character.
     * The previoius selected character becomes deselected.
     * @param selectedCharacter
     */
    public void setSelectedCharacter(CharacterActor selectedCharacter) {
        if (this.selectedCharacter != null) this.selectedCharacter.setSelected(false);

        if (selectedCharacter != null) {
            selectedCharacter.setSelected(true);
            if (selectedCharacter.getCharacter().isFriendly() & selectedCharacter.getCharacter().isActive()) {
                selectedCharacterPosition = selectedCharacter.getCharacter().getCell();
                combatHUD.showAbilityButtons(selectedCharacter.getCharacter());
                showMovementTiles(selectedCharacter);

                combatHUD.notificationText = "";
                for (Effect e: selectedCharacter.getCharacter().getEffects()) {
                    combatHUD.notificationText += "* "+e.toString()+"\n";
                }
            }

        }

        this.selectedCharacter = selectedCharacter;

    }

    public void deselectCharacter() {
        map.removeHighlightCells();
        combatHUD.hideAbilityButtons();

        if(selectedCharacter != null) {
            selectedCharacter.setSelected(false);
            selectedCharacterPosition = null;
            selectedCharacter = null;
        }
    }




    /**
     * Executes the current selected ability on a target.
     * @param target
     */
    public void executeCurrentAbility(CharacterActor target) {
        LOG.print(3,"[CombatController] Targeted:  "+target.getCharacter().getName());
        Ability a = getCurrentSelectedAbility();
        a.setTarget(target.getCharacter());
        target.receiveAbility(a);

        getCharacterActor(a.getSource()).useAbility();

        if (target.getCharacter().isDead()) this.getActors().removeValue(target,true);

        combat.setGameStep(Combat.GameStepType.SELECTION);
    }

    /**
     * Highlights the cells on the map where this character can move.
     * @param actor
     */
    public void showMovementTiles(CharacterActor actor) {

         map.highlightCells(map.getCell(actor.getCharacter().getCell().getMapCoordinates()), actor.getCharacter().getMovement());

    }

    public Ability getCurrentSelectedAbility() {
        return currentSelectedAbility;
    }

    public void setCurrentSelectedAbility(Ability currentSelectedAbility) {
        this.currentSelectedAbility = currentSelectedAbility;
    }


    /**
     * Returns the CharacterActor encapsulating a given character
     * @param character
     * @return
     */
    public CharacterActor getCharacterActor (Character character) {
        for(CharacterActor c: characterActors) {
            if(c.getCharacter() == character) return c;
        }
        return null;
    }

}