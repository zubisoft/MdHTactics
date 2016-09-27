package com.mygdx.mdh.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.mdh.game.EffectManager;
import com.mygdx.mdh.game.characters.actions.*;
import com.mygdx.mdh.game.controller.CharacterChangeListener;
import com.mygdx.mdh.game.map.IsoMapActor;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.model.effects.EffectListener;
import com.mygdx.mdh.game.model.MapCell;
import com.mygdx.mdh.game.model.effects.EffectManagerListener;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zubisoft on 28/01/2016.
 */


public class CharacterActor extends Actor implements EffectManagerListener, EffectListener, CharacterChangeListener {

    private static final int        FRAME_COLS = 6;
    private static final int        FRAME_ROWS = 3;

    //Graphic Elements
    Animation                       idleAnimation;
    Animation                       attackAnimation;
    Animation                       walkAnimation;
    Animation                       hitAnimation;
    public TextureRegion            portrait;

    float offsetx=0;
    float offsety=0;


    ShapeRenderer s;
    Sprite selectionCircle;



    boolean highlighted;

    TextureRegion currentFrame;

    //Effects applied on the character
    public static Queue<EffectAction> effectActions = new Queue<>();
    public static Queue<GameAction> queueActions = new Queue<>();


    //Effects applied on the character
    List<Action> actionQueue;


    //Addtional graphic elements
    CharacterLifeBar lifebar;
    List<Label> messages;
    float stateTime;

    public CharacterMessenger getCharacterMessenger() {
        return characterMessenger;
    }

    CharacterMessenger characterMessenger;

    //Game Logic
    private Character character;



    public enum CHARACTER_STATE {
        IDLE, MOVING, ABILITY1, ABILITY2,HIT
    }


    boolean selected;

    CHARACTER_STATE state;



    //Auxiliar variables for hit detection
    private Pixmap pixmap;
    private float adjusted_x;
    private int pixel ;



    CHARACTER_STATE test = null;

    public Actor hit (float x, float y, boolean touchable) {

        if (actionInProgress()) return null; //No input allowed while main action in progress

        if (getScaleX()==-1) {
            adjusted_x = (getWidth()-(x+offsetx));
        } else {
            adjusted_x = x-offsetx;
        }

        if (super.hit(adjusted_x,y-offsety,touchable) != null) {

            currentFrame.getTexture().getTextureData().prepare();
            pixmap = currentFrame.getTexture().getTextureData().consumePixmap();


            if (getScaleX()==-1) {
                pixel = pixmap.getPixel((int) (currentFrame.getRegionX() + getWidth() - adjusted_x), (int) (currentFrame.getRegionY() + y - offsety));
            } else {
                pixel = pixmap.getPixel((int) (currentFrame.getRegionX() + adjusted_x), (int) (currentFrame.getRegionY() + y - offsety));
            }

            pixmap.dispose();

            //If not transparent, clicked
            if ((pixel & 0x000000ff) != 0) {
                return this;
            }

        }



        return null;

    }

    /**
     * Creates a new CharacterActor from the character specified, at the position (startX,startY)
     * @param character
     * @param startX
     * @param startY
     */
    public CharacterActor(Character character, float startX, float startY) {
        super();
        this.character = character;

        selected = false;

        this.setX(startX);
        this.setY(startY);
        this.setWidth(83);
        this.setHeight(125);

        this.setOriginX(getWidth()/2);

//TODO revisar esto
        if (character.isFriendly()) setScaleX(-1);


        lifebar = new CharacterLifeBar(this);
        //lifebar.setX(startX);
        //lifebar.setY(startY);


        loadAnimations();

        this.state=CHARACTER_STATE.IDLE;

        s = new ShapeRenderer();

        TextureRegion texture = Assets.instance.guiElements.get("character/CHAR-selection-circle");
        selectionCircle = new Sprite(texture);
        selectionCircle.setPosition(getX() + 20, getY() + 10);
        selectionCircle.setSize(100, 50);


       // effectActions = new ArrayList<>();
        actionQueue = new ArrayList<>();
        messages = new ArrayList<>();

        EffectManager.instance.addEffectListener(this);


        character.addListener(this);

         characterMessenger= new CharacterMessenger(this);


    }

    public static boolean actionInProgress () {
        //LOG.print("[CharacterActor] Effects running "+effectActions.size);
        return (queueActions.size>0)||(effectActions.size>0);
    }

    /**
     * Updates the CharacterActor according to the current status.
     * This implies updating the other attached actors: lifebar, messages and current animation keyframe
     * @param deltaTime
     */


    public void update(float deltaTime) {

        stateTime +=deltaTime;


        //Update character messages
        /*
        Iterator<Label> iterator = messages.iterator();
        while(iterator.hasNext()) iterator.next().act(stateTime);*/
        characterMessenger.update(deltaTime);



        if (test == null) test=state;
        if(!state.equals(test)) {
            test=state;
        }

        //Update the current frame
        switch(state) {
            case IDLE:
                currentFrame = getIdleAnimation().getKeyFrame(stateTime, true);
                break;
            case MOVING:
                currentFrame = getWalkAnimation().getKeyFrame(stateTime, true);
                break;
            case ABILITY1:
                currentFrame = attackAnimation.getKeyFrame(stateTime, true);
                break;
            case ABILITY2: break;
            case HIT: currentFrame = hitAnimation.getKeyFrame(stateTime, true); break;

        }

        //Update actions attached to this Actor

        this.act(deltaTime);
        lifebar.update(deltaTime);

        //Draw effects
        /*
        for (EffectAction effect: effectActions ) {
            effect.act(deltaTime);
        }
    */

        this.queuedActionsAct(deltaTime);
        this.effectActionsAct(deltaTime);

    }

    public void queuedActionsAct (float deltaTime) {
        if (queueActions.size == 0 ) return;

        if (queueActions.first().act(deltaTime) == true) {
            queueActions.removeFirst();
        }
    }

    public void effectActionsAct (float deltaTime) {
        if (effectActions.size == 0 ) return;

        if (effectActions.first().act(deltaTime) == true) {
            effectActions.removeFirst();
        }
    }


    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlight(Color c) {
        if (character.isDead()) return; //To avoid redrawing the character once it is dead
        this.setColor(c);
        this.highlighted = true;
    }

    public void removeHighlight() {
        if (character.isDead()) return;
        this.setColor(Color.WHITE);
        this.highlighted = false;
    }


    public void setOffset(float x, float y) {
        offsetx=x;
        offsety=y;

    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        this.setZIndex((int)this.getMapCell().getMapCoordinates().y);

        if(selectionCircle != null) {
            selectionCircle.setPosition(getX() + 20, getY() + 10);
            selectionCircle.setSize(100, 50);
        }
    }

    /**
     * Drawns the CharacterActor.
     * This implies drawing the other attached actors: lifebar, messages and selection cirle.
     * The character is also greyed when inactive.
     * @param batch
     */
    public void draw (SpriteBatch batch) {
        //Get the current color before drawing (Useful to allow animating the color from actions)
        batch.setColor(getColor());


        //Grey out if inactive
        if (!character.isActive() && !character.isDead() && character.isFriendly() && this.getState()==CHARACTER_STATE.IDLE )
            batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);

        //Draw selection circle when selected
        if (isSelected() ) {
            selectionCircle.draw(batch,0.7f);
        }


        //Draw current animation
        batch.draw(currentFrame
                ,getX()+offsetx
                ,getY()+offsety
                ,getOriginX()
                ,getOriginY()
                ,getWidth()
                ,getHeight()
                ,getScaleX()
                ,getScaleY()
                ,getRotation());

      //Draw effects
        //for (EffectAction effect: effectActions ) {
        if (effectActions.size>0)
            effectActions.first().draw(batch);
        //}

        //Draw attached actors
        lifebar.draw(batch,this.getColor().a);


        //Return color to normal
        if (!character.isActive()) batch.setColor(1f, 1f, 1f, 1f);

    }

    /**
     * Loads the animation from the spritesheet defined in the Character.
     */
    public void loadAnimations () {

        idleAnimation   = Assets.instance.characters.get(character.getPic()).idleAnimation;
        walkAnimation   = Assets.instance.characters.get(character.getPic()).walkAnimation;
        attackAnimation = Assets.instance.characters.get(character.getPic()).attackAnimation;
        hitAnimation = Assets.instance.characters.get(character.getPic()).hitAnimation;
        portrait   = Assets.instance.characters.get(character.getPic()).portrait;
    }

    /**
     * Adds a MovementAction that will move the actor to the specified point using the appropiate animation.
     */
    public void moveToCell(IsoMapCellActor newCell, IsoMapActor map) {

        if( IsoMapActor.distance(character.getCell().getCartesianCoordinates(),newCell.getCell().getCartesianCoordinates()) <= character.getMovement() ) {

            MovementAction movementAction = new MovementAction(0.025f);
            movementAction.setTargetCell(newCell, map);

            queueAction(movementAction);

        }

    }

    /**
     * Executes the actions that should happen when a turn starts.
     */
    public void turnStart () {
        character.startTurn();
    }


    /**
     * Adds an action to the action queue
     */
    public void useAbility( Ability a, CharacterActor target) {
        AttackAction action = new AttackAction(0.15f, a, target);

        this.queueAction(action);
    }

    public void useAbility( Ability a, List<CharacterActor> target) {
        AttackAction action = new AttackAction(0.15f, a, target);

        this.queueAction(action);
    }



    /**
     * Adds an message to the actor that will displayed for a short time.
     * Multiple messages can be displayed at the same time.
     * @param message
     */

    public void showMessage (String icon, String message, Color c) {
        if(message != null)
        characterMessenger.showMessage(icon, message,c);
    }



    public void showMessage (String message) {
        //showMessage (message, Color.WHITE);
        if(message != null)
        characterMessenger.showMessage(message, Color.WHITE);
    }

    /**
     * Execute an ability with this Character as target.
     * @param a
     */
    public void receiveAbility (Ability a) {
        if (a != null) {

            //if(IsoMapActor.distance(a.getSource().getCell(),this.getCharacter().getCell()) <= a.getRange())
                a.apply(this.getCharacter());


            this.showMessage(a.getMessage());
        }

    }





    /**
     * Queues an action so it is executed in order after all the previous actions have finished.
     * @param a Action to be queued.
     */
    public void queueAction (GameAction a) {
        a.setActor(this);
        //LOG.print(3,"[CharacterActor] Added Action "+a+" for "+this.getCharacter().getName());
        queueActions.addLast(a);

        if (! (a instanceof GameWaitAction) )
            this.getCharacter().setAvailableActions(this.getCharacter().getAvailableActions() - 1);
    }

    public void addEffectAction (EffectAction ea) {
        ea.setActor(this);
        this.effectActions.addLast(ea);
        //this.addAction(ea);
    }


    public void onEffectProcessed (Effect e) {
        if (e.getTarget()==this.getCharacter()) {
            //LOG.print(3,"[CharacterActor] "+character.hashCode()+" has been targeted for an effect."+e.hashCode(), LOG.ANSI_RED);
            e.addEffectListener(this);
            //this.showMessage(e.getIcon(),e.notification(), e.getColor());
        }

    }

    public void onEffectApply (Effect e) {
        if (e.getTarget()==this.getCharacter()) {
            e.addEffectListener(this);

            /*
            //TODO this is pretty much a workaround, we should be probably triggering this as an event from the effect
            if (e.getDuration()>0) showMessage(e.getIcon(),e.notification(), e.getColor());
            */
        }

    }




    public void onEffectTriggered (Effect e) {
        //LOG.print(3,"[CharacterActor] "+character.hashCode()+" effect triggered. "+e.getRolledDamage().getRolledDamage());

            EffectAction ea = new EffectAction(e, 0.15f);
            this.addEffectAction(ea);

        //this.queueAction(new GameWaitAction(2));

    }





    public void onAbilityUnlock (Ability a) {};

    public void onCharacterHit (int damage)  {
        //LOG.print(2,"[CharacterActor] Checking Death"+LOG.ANSI_RED);

        //TODO: some extra logic would be nice to consider the case when the actor is hitting himself
        if (damage <0) return; //Do not execute when healing

        this.addAction(new TakeDamageAction(1));
        if (character.isDead()) {
            LOG.print(1,"[CharacterActor] Dying:  "+character,LOG.ANSI_RED);
            this.addAction(
                    Actions.sequence(
                            Actions.color(new Color(1,0.2f,0.2f,0.5f),1,Interpolation.fade)
                            ,Actions.alpha(0,1,Interpolation.fade)

                    ));

            lifebar.addAction(
                    Actions.sequence(
                            Actions.color(new Color(1,0.2f,0.2f,0.5f),1,Interpolation.fade)
                            ,Actions.alpha(0,1,Interpolation.fade)

                    ));
        }

    }

    public void onCharacterActive (Character c)  {
    }

    public void onCharacterInactive (Character c)  {
    }


    public Character getCharacter() {return character;}

    public Animation getIdleAnimation() {
        return idleAnimation;
    }

    public Animation getWalkAnimation() {
        return walkAnimation;
    }


    public String toString() {
        return "[Character: "+character.name+"] Cell("+character.getCell()+") @ ["+this.getX()+","+getY()+ "] HP:"+character.getHealth()+ " AP:"+character.getAvailableActions();
    }

    public CHARACTER_STATE getState() {
        return state;
    }

    public void setState(CHARACTER_STATE state) {
        this.state = state;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isActive () {
        if (this.isDead()) return false;

        return character.isActive();
    }

    public boolean isFriendly() {
        return character.isFriendly();
    }

    public boolean isDead() {
        return character.isDead();
    }

    public MapCell getMapCell() {
        return character.getCell();
    }

}