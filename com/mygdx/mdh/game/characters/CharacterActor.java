package com.mygdx.mdh.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AfterAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.mdh.game.characters.actions.AttackAction;
import com.mygdx.mdh.game.characters.actions.EffectAction;
import com.mygdx.mdh.game.characters.actions.MovementAction;
import com.mygdx.mdh.game.map.IsoMapActor;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Effect;
import com.mygdx.mdh.game.util.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 28/01/2016.
 */


public class CharacterActor extends Actor {

    private static final int        FRAME_COLS = 6;         // #1
    private static final int        FRAME_ROWS = 3;         // #2

    //Graphic Elements
    Animation                       idleAnimation;          // #3
    Animation                       attackAnimation;          // #3
    Animation                       walkAnimation;          // #3
    Texture                         walkSheet;              // #4
    final Skin                      uiSkin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));

    ShapeRenderer s;
    Sprite selectionCircle;

    TextureRegion currentFrame;

    //Effects applied on the character
    List<EffectAction> effectActions;


    //Effects applied on the character
    List<Action> actionQueue;


    //Addtional graphic elements
    CharacterLifeBar lifebar;
    List<Label> messages;
    float stateTime;

    //Game Logic
    private Character character;

    public enum CHARACTER_STATE {
        IDLE, MOVING, ABILITY1, ABILITY2
    }


    boolean selected;
    boolean ready;
    CHARACTER_STATE state;


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
        this.setHeight(126);

        if (!character.isFriendly()) setScaleX(-1);


        lifebar = new CharacterLifeBar(this);
        //lifebar.setX(startX);
        //lifebar.setY(startY);


        loadAnimations();

        this.state=CHARACTER_STATE.IDLE;

        s = new ShapeRenderer();

        Texture texture = new Texture(Gdx.files.internal("core/assets/graphics/combatui/selected_character_stroke.png"));
        selectionCircle = new Sprite(texture);

        effectActions = new ArrayList<>();
        actionQueue = new ArrayList<>();
        messages = new ArrayList<>();

    }





    /**
     * Updates the CharacterActor according to the current status.
     * This implies updating the other attached actors: lifebar, messages and current animation keyframe
     * @param deltaTime
     */

    public void update(float deltaTime) {

        stateTime +=deltaTime;

        lifebar.update(deltaTime);

        //Update character messages
        for(Label l: messages) l.act(stateTime);

        //Update the current frame
        switch(state) {
            case IDLE:
                this.setReady(true);
                currentFrame = getIdleAnimation().getKeyFrame(stateTime, true);
                break;
            case MOVING:
                this.setReady(false);
                currentFrame = getWalkAnimation().getKeyFrame(stateTime, true);
                break;
            case ABILITY1:
                this.setReady(false);
                currentFrame = attackAnimation.getKeyFrame(stateTime, true);
                break;
            case ABILITY2:  this.setReady(false);break;

        }

        //Update actions attached to this Actor
        this.act(deltaTime);

    }

    /**
     * Drawns the CharacterActor.
     * This implies drawing the other attached actors: lifebar, messages and selection cirle.
     * The character is also greyed when inactive.
     * @param batch
     */
    public void draw (SpriteBatch batch) {
        //Get the current color before drawing (Useful to allow animating the color from actions)
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);

        //Grey out if inactive
        if (!character.isActive() & !character.isDead())  batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);

        //Draw selection circle when selected
        if (isSelected() ) {
            selectionCircle.setPosition(getX()+16,getY()+8);
            selectionCircle.setSize(100,50);
            selectionCircle.draw(batch,0.7f);
        }

        //Draw current animation
        batch.draw(currentFrame,getX()+getOriginX()
                ,getY()+getOriginY()
                ,getOriginX()
                ,getOriginY()
                ,getWidth()
                ,getHeight()
                ,getScaleX()
                ,getScaleY()
                ,getRotation());

        //Draw effects
        for (EffectAction effect: effectActions ) {
            effect.draw(batch);
        }

        //Draw attached actors
        lifebar.draw(batch);
        for(Label l: messages) l.draw(batch,1);

        //Return color to normal
        if (!character.isActive()) batch.setColor(1f, 1f, 1f, 1f);

    }

    /**
     * Loads the animation from the spritesheet defined in the Character.
     */
    public void loadAnimations () {

        idleAnimation   = Assets.instance.characters.get(character.getName()).idleAnimation;
        walkAnimation   = Assets.instance.characters.get(character.getName()).walkAnimation;
        attackAnimation = Assets.instance.characters.get(character.getName()).attackAnimation;
    }

    /**
     * Adds a MovementAction that will move the actor to the specified point using the appropiate animation.
     */
    public void moveToCell(IsoMapCellActor newCell) {


        if( IsoMapActor.distance(character.getCell().getMapCoordinates(),newCell.getCell().getMapCoordinates()) <= character.getMovement() ) {
            this.state = CHARACTER_STATE.MOVING;
            MovementAction movementAction = new MovementAction(0.025f);
            movementAction.setTargetCell(newCell);

            queueAction(movementAction);

        }

    }

    /**
     * Queues an action so it is executed in order after all the previous actions have finished.
     * @param a Action to be queued.
     */
    public void queueAction (Action a) {
        AfterAction action = new AfterAction();
        action.setAction(a);
        action.setTarget(this);
        this.addAction(action);
    }

    /**
     *
     */
    public void useAbility( ) {
        this.state=CHARACTER_STATE.ABILITY1;
        AttackAction movementAction = new AttackAction(0.15f);

        this.addAction(movementAction);

    }

    /**
     * Adds an message to the actor that will displayed for a short time.
     * Multiple messages can be displayed at the same time.
     * @param message
     */
    public void showMessage (String message) {
        Label la=(new Label(message, uiSkin, "default-font", Color.ORANGE));

        la.setPosition(getX()+60,getY()+getHeight()-messages.size()*15);


        messages.add(la);

        la.addAction(Actions.sequence(
                Actions.moveTo(getX()+60, getY()+getHeight()+50-messages.size()*15,1000, Interpolation.exp5Out)
                ,Actions.alpha(0,1000,Interpolation.fade)
        ));
    }

    /**
     * Execute an ability with this Character as target.
     * @param a
     */
    public void receiveAbility (Ability a) {
        if (a != null) {
            a.apply(this.getCharacter());
            this.showMessage(a.getMessage());
            for (Effect e : a.getEffects()) {
                EffectAction ea = new EffectAction(e, 0.15f);
                this.addEffectAction(ea);
            }
        }

        if (character.isDead()) {
            System.out.println("[CharacterActor] Dying:  "+character);
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

    /*
    public void getHit( int value ) {

        showMessage("Hit "+value+" HP");

        character.setHealth(character.getHealth()-50);

        if (character.isDead()) {
            System.out.println("[CharacterActor] Dying:  "+character);
            this.addAction(
                    Actions.sequence(
                             Actions.color(new Color(1,0.2f,0.2f,0.5f),1,Interpolation.fade)
                            ,Actions.alpha(0,1,Interpolation.fade)

            ));


        }

        System.out.println("[CharacterActor] Attacked "+character+" Dead? "+character.isDead());

    }
    */

    public Character getCharacter() {return character;}

    public void addEffectAction (EffectAction ea) {
        this.effectActions.add(ea);
        this.addAction(ea);
    }

    public Animation getIdleAnimation() {
        return idleAnimation;
    }

    public Animation getWalkAnimation() {
        return walkAnimation;
    }


    public String toString() {
        return "[Character: "+character.name+"] Cell("+character.getCell()+") @ ["+this.getX()+","+getY()+ "] HP:"+character.getHealth()+ " AP:"+character.getAvailableActions();
    }


    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
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
}