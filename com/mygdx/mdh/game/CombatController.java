package com.mygdx.mdh.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.mygdx.mdh.game.IA.StrategyManager;
import com.mygdx.mdh.game.characters.actions.GameWaitAction;
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

import java.util.*;


/**
 * Created by zubisoft on 28/01/2016.
 */
public class CombatController extends Stage {

    public class ActorComparator implements Comparator < CharacterActor > {
        @Override
        public int compare(CharacterActor arg0, CharacterActor arg1) {
            if (arg0.getY() > arg1.getY()) {
                return -1;
            } else if (arg0.getY() == arg1.getY()) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public InputMultiplexer multiplexer;

    public static Combat combat;
    java.util.List<CharacterActor> characterActors;
    public CombatHUD combatHUD;

    public CameraManager cameraManager;

    private CharacterActor selectedCharacter;
    private MapCell selectedCharacterPosition = new MapCell();

    Ability currentSelectedAbility;

    float stateTime;
    boolean combatPaused;

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

    public boolean isCombatPaused() {
        return combatPaused;
    }

    public void setCombatPaused(boolean combatPaused) {
        this.combatPaused = combatPaused;
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

        //Initialize gameScreen logic
        this.characterActors = new ArrayList<CharacterActor>();

        if (screenManager.getGame() == null) {
            LOG.print(1,"[Combat Controller] Loading debug combat", LOG.ANSI_RED);
            this.combat = Combat.loadFromJSON("combat01");
        } else {
            //
            // this.combat = Combat.loadFromJSON("combat_minimal");

            combat = new Combat();
            combat.setMap(screenManager.getGame().getCurrentMission().getMissionMap());

            for (Character c : screenManager.getGame().getCurrentParty()) {
                combat.addCharacter(c);
                LOG.print("Added " + c);
            }

            for (Character c : screenManager.getGame().getCurrentMission().getBaddies()) {
                combat.addCharacter(c);
                LOG.print("Added " + c);
            }

            combat.initCharacterPositions();

            //Important step, link characters with the map
            /*
            int i = 0;
            for (Character c : combat.getCharacters()) {
                c.setRow(i);
                c.setColumn(i++);
                c.setCell(combat.getMap().getCell(c.getRow(), c.getColumn()));
            }
            */
        }


        map=new IsoMapActor(combat.getMap());
        this.addActor(map);


        background = new Sprite(Assets.instance.maps.get("map01"));
        background.setPosition(0,-275);

        createActorsForLayer( combat );

        //Initialize HUD
        combatHUD = new CombatHUD(this);



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

        //Initialize game logic
        baddiesBegin=true;
        gameEnd = false;
        gameTurn = GameTurn.PLAYER;


        //combat.initCharacterPositions (); //Randomizes char positions
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

            LOG.print(10,"[CombatController] Adding actor "+actor.toString());

        }

    }

    /** If the player has no characters active, it is the end of his turn.*/
    public boolean isEndOfTurn () {
        for(CharacterActor a: characterActors) {
            if (a.isActive() & a.isFriendly()) return false;
        }
        return true;
    }

    /** If all player characters are dead, it´s gameScreen over.*/
    public boolean isGameOver () {
        for(CharacterActor a: characterActors) {
            if (!a.isDead() && a.isFriendly()) return false;
        }



        return true;
    }

    /** If all enemy characters are dead, it´s a victory*/
    public boolean isVictory () {
        int xp = 0;
        for(CharacterActor a: characterActors) {
            if (!a.getCharacter().isFriendly()) {
                if (!a.getCharacter().isDead() ) return false;
                xp += a.getCharacter().getXp();
            }
        }
        combat.setExperience(xp);
        return true;
    }

    public void playerTurnBegin () {
        gameTurn = GameTurn.PLAYER;

        LOG.print(1,"[CombatController] Player Turn Start", LOG.ANSI_YELLOW);

        setGameStep(Combat.GameStepType.SELECTION);

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
                c.queueAction(new GameWaitAction(3f));
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

    public List<CharacterActor>  getFriendlies() {
        List<CharacterActor> list = new ArrayList<CharacterActor>();
        for(CharacterActor c: getCharacterActors()) {
            if(c.getCharacter().isFriendly() ) {
                list.add(c);
            }
        }
        return list;
    }

    public boolean  baddiesActive() {

        for(CharacterActor c: getCharacterActors()) {
            if(!c.getCharacter().isFriendly() & c.getCharacter().isActive()) {
                return true;
            }
        }

        return false;
    }

    public List<CharacterActor>  getBaddies() {
        List<CharacterActor> list = new ArrayList<CharacterActor>();
        for(CharacterActor c: getCharacterActors()) {
            if(!c.getCharacter().isFriendly() ) {
                list.add(c);
            }
        }
        return list;
    }



    public void executeBaddiesTurn() {
        //If other characters have finished doing their stuff, take action
        if (CharacterActor.queueActions.size==0) {
            /* Execute orders */
            int i = 0;
            for (CharacterActor c : getCharacterActors()) {
                if (!c.getCharacter().isFriendly() && c.getCharacter().isActive() && !c.actionInProgress()) {

                    getMap().removeHighlightCells();

                    strategyManager.nextAction(c.getCharacter());
                    c.queueAction(new GameWaitAction(2f));

               }
                i++;
            }
        }


    }

    public void enableInput(){
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void disableInput(){
        Gdx.input.setInputProcessor(null);
    }

    public boolean  characterActionsInProgress() {

        for(CharacterActor c: getCharacterActors()) {
            if(c.actionInProgress() || c.getState()!= CharacterActor.CHARACTER_STATE.IDLE) {
                return true;
            }
        }
        return false;

    }


    public boolean isCombatFinished() {
        return (gameEnd && !messageInProgress && !combatPaused);
    }

    boolean messageInProgress = false;

    public void update(float deltaTime) {
       // System.out.println(Gdx.graphics.getDeltaTime());
        stateTime += deltaTime;          // #15

        double x = System.currentTimeMillis();

        //System.out.println("A) Init");

        combatHUD.update(deltaTime);

        x =  System.currentTimeMillis()-x;
        //System.out.println("B) HUD "+x);

        if (CharacterActor.actionInProgress() && Gdx.input.getInputProcessor() !=null) {
            System.out.println("Disable");
            Gdx.input.setInputProcessor(null);
        } else if (!CharacterActor.actionInProgress() && Gdx.input.getInputProcessor() ==null ) {
            System.out.println("Enable");
            Gdx.input.setInputProcessor(multiplexer);
        }

        if (isGameOver()) {
            if(!gameEnd) {
                combatHUD.showMessageBar("Game Over");
                System.out.println("Game Over");

                for (CharacterActor a : characterActors) {
                    System.out.println(a + " " + a.isFriendly());
                }

                gameEnd = true;
            }
        }

        if (isVictory()) {
            if(!gameEnd) combatHUD.showMessageBar("Victory");
            System.out.println("Victory");
            gameEnd=true;
        }

        x =  System.currentTimeMillis()-x;
        //System.out.println("C) Victory checks "+x);

        if (!messageInProgress && !baddiesActive() && gameTurn==GameTurn.BADDIES && !characterActionsInProgress()) {

            LOG.print(3,"[Controller] Player Start",LOG.ANSI_BLUE);
            combatHUD.showMessageBar("Player Turn",3);
            messageInProgress = true;

        }

        if (!messageInProgress && !friendliesActive() && gameTurn==GameTurn.PLAYER  && !characterActionsInProgress() ) {

            LOG.print(3,"[Controller] Baddies Start - Effect Action Executing: "+CharacterActor.effectActions.size,LOG.ANSI_BLUE);
            combatHUD.showMessageBar("Enemy Turn",3);
            messageInProgress = true;

        }

        x =  System.currentTimeMillis()-x;
        //System.out.println("D) Messaging "+x);


        if (!combatPaused) {

            if (!baddiesActive() && gameTurn==GameTurn.BADDIES && !characterActionsInProgress()) {

                LOG.print(3,"[Controller] Player Start",LOG.ANSI_BLUE);
                playerTurnBegin();
                messageInProgress = false;
            }

            if (!friendliesActive() && gameTurn==GameTurn.PLAYER  && !characterActionsInProgress() ) {

                LOG.print(3,"[Controller] Baddies Start - Effect Action Executing: "+CharacterActor.effectActions.size,LOG.ANSI_BLUE);
                baddiesTurnBegin();
                messageInProgress = false;

            }

            if (gameTurn==GameTurn.BADDIES) {
                executeBaddiesTurn();
            }


            x =  System.currentTimeMillis()-x;
            //System.out.println("E) Game Turn handling "+x);


            //TODO Convertir esto en una llamada de evento que viene desde el character cuando cambia un atributo

            if (selectedCharacter != null) {
                if (selectedCharacter.getCharacter().getCell() != selectedCharacterPosition)
                    if (selectedCharacter.getCharacter().isFriendly() & selectedCharacter.getCharacter().isActive()) {
                        setSelectedCharacter(selectedCharacter);
                    }
            }


            x =  System.currentTimeMillis()-x;
            //System.out.println("F) Char Change "+x);

        }

        Collections.sort(characterActors, new ActorComparator());
        for(CharacterActor a: characterActors) {
            a.update(deltaTime);
        }

        x =  System.currentTimeMillis()-x;
        //System.out.println("G) Actor updating "+x);

       // map.act(deltaTime);
        this.act(deltaTime);

        x =  System.currentTimeMillis()-x;
        //System.out.println("H) Act "+x);

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
            setCurrentSelectedAbility(null);
            if (selectedCharacter.getCharacter().isFriendly() & selectedCharacter.getCharacter().isActive()) {
                selectedCharacterPosition = selectedCharacter.getCharacter().getCell();
                combatHUD.showAbilityButtons(selectedCharacter.getCharacter());
                showMovementTiles(selectedCharacter);
            }

            combatHUD.notificationText = "";
            for (Effect e: selectedCharacter.getCharacter().getEffects()) {
                combatHUD.notificationText += "* "+e.toString()+"\n";
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
            setCurrentSelectedAbility(null);
        }
    }

    /**
     * Executes the current selected ability on a target.
     * @param target
     */
    public void executeCurrentAbility(CharacterActor target) {
        LOG.print(3,"[CombatController] Targeted:  "+target.getCharacter().getName());

        Ability a = getCurrentSelectedAbility();
        //a.setTarget(target.getCharacter());

        //TODO probably better doing this in the action instead -- Not sure
        switch (a.getTargetType()) {
            case SELF:
                if(target.getCharacter() == a.getSource())
                    getCharacterActor(a.getSource()).useAbility(a, target);
                break;
            default:
                List<CharacterActor> characters = getCharacersInArea(target.getMapCell(),a.getArea(),a.getTargetType());
                if (characters.size() > 0)
                    getCharacterActor(a.getSource()).useAbility(a, characters);
                break;

        }

        //getCharacterActor(a.getSource()).useAbility(a, target);

        //TODO Probably better from an event?
        if (target.getCharacter().isDead()) this.getActors().removeValue(target,true);


        setGameStep(Combat.GameStepType.SELECTION);
    }



    /**
     * Executes the current selected ability on a target.
     * @param target
     */
    public void executeCurrentAbility(IsoMapCellActor target) {



        Ability a = getCurrentSelectedAbility();
        //a.setTarget(target.getCharacter());

        //If not in range ignore
        if (IsoMapActor.distance(a.getSource().getCell(),target.getCell()) > a.getRange()) return;


        CharacterActor source = getCharacterActor(a.getSource());
        boolean executed = true;

        switch (a.getTargetType()) {
            case SELF:
                source.useAbility(a, source);
                break;
           default:
               List<CharacterActor> characters =  getCharacersInArea(target.getCell(),a.getArea(),a.getTargetType());
               if (characters.size() > 0)
                       source.useAbility(a,characters);
                break;


        }

        //getCharacterActor(a.getSource()).useAbility(a, target);
        System.out.println("Executed? "+executed);

        if(executed) setGameStep(Combat.GameStepType.SELECTION);
    }

    public void setGameStep(Combat.GameStepType step) {
        System.out.println("Settting game step: "+step);
        switch (step) {
            case SELECTION:
                deselectCharacter();
                map.removeHighlightCells();
                map.removeBorders();
                break;
            case ACTION_SELECTION:
                setSelectedCharacter(selectedCharacter);
                map.removeBorders();
                break;
            case TARGETING:
                map.removeHighlightCells();
                break;

        }


        combat.setGameStep(step);
    }


    /**
     * Highlights the cells on the map where this character can move.
     * @param actor
     */
    public void showMovementTiles(CharacterActor actor) {
         map.highlightMovementCells(map.getCell(actor.getCharacter().getCell().getMapCoordinates()), actor.getCharacter().getMovement(),-1);
    }

    public Ability getCurrentSelectedAbility() {
        return currentSelectedAbility;
    }

    public void setCurrentSelectedAbility(Ability currentSelectedAbility) {
        this.currentSelectedAbility = currentSelectedAbility;

        if (selectedCharacter == null || currentSelectedAbility==null) {
            map.removeBorders();
            return;
        }

        if(selectedCharacter.isFriendly()) {
            map.outlineCells(
                    map.getCell(selectedCharacter.getCharacter().getCell().getMapCoordinates()),
                    selectedCharacter.getCharacter().getMovement(),
                    currentSelectedAbility.getRange());
        }

    }

    public void showAreaOfEffect(MapCell cell) {

        if (cell != null && currentSelectedAbility != null && selectedCharacter!= null)
            if (IsoMapActor.distance(cell,selectedCharacter.getMapCell()) <= currentSelectedAbility.getRange()) {
                map.highlightCells(
                        map.getCell(cell.getMapCoordinates()),
                        currentSelectedAbility.getArea(),
                        IsoMapActor.redHighlight);

                for (CharacterActor c: getCharacersInArea(cell,currentSelectedAbility.getArea())) {
                    c.setHighlight(Color.CORAL);
                }
            }
    }

    public void hideAreaOfEffect(MapCell cell) {

        if (cell != null && currentSelectedAbility != null && selectedCharacter!= null) {
            map.removeHighlightCells();
        }

        for (CharacterActor c : characterActors) {
            c.removeHighlight();
        }

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

    public List<CharacterActor> getCharacersInArea (MapCell center, int area) {
        return getCharacersInArea ( center,  area, Ability.AbilityTarget.ALL);
    }

    public List<CharacterActor> getCharacersInArea (MapCell center, int area, Ability.AbilityTarget targetType) {
        List<CharacterActor> list = new ArrayList<>();
        for(CharacterActor c: characterActors) {
            if (IsoMapActor.distance(center,c.getMapCell()) <= area) {
                if(gameTurn == GameTurn.PLAYER) {
                    switch (targetType) {
                        case ALLIES:
                            if (c.isFriendly()) list.add(c);
                            break;
                        case ENEMIES:
                            if (!c.isFriendly()) list.add(c);
                            break;
                        case ALL:
                            list.add(c);
                            break;
                        //case SELF:    break;
                    }
                }

                if(gameTurn == GameTurn.BADDIES) {
                    switch (targetType) {
                        case ALLIES:
                            if (!c.isFriendly()) list.add(c);
                            break;
                        case ENEMIES:
                            if (c.isFriendly()) list.add(c);
                            break;
                        case ALL:
                            list.add(c);
                            break;
                        //case SELF:    break;
                    }
                }

            }
        }

        return list;

    }



}