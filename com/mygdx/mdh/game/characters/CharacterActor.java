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

    private static final int        FRAME_COLS = 6;         // #1
    private static final int        FRAME_ROWS = 3;         // #2

    //Graphic Elements
    Animation                       idleAnimation;          // #3
    Animation                       attackAnimation;          // #3
    Animation                       walkAnimation;          // #3
    public TextureRegion                        portrait;              // #4

    float offsetx=0;
    float offsety=0;

    public TextureRegion debugBorder;              // #4

    ShapeRenderer s;
    Sprite selectionCircle;

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

    CharacterMessenger characterMessenger;

    //Game Logic
    private Character character;



    public enum CHARACTER_STATE {
        IDLE, MOVING, ABILITY1, ABILITY2
    }


    boolean selected;

    CHARACTER_STATE state;


    public Actor hit (float x, float y, boolean touchable) {

        float adjusted_x;

        if (getScaleX()==-1) {
            adjusted_x = (getWidth()-(x+offsetx));
        } else {
            adjusted_x = x-offsetx;
        }

        if (super.hit(adjusted_x,y-offsety,touchable) != null) {

            currentFrame.getTexture().getTextureData().prepare();
            Pixmap pixmap = currentFrame.getTexture().getTextureData().consumePixmap();

            int pixel ;
            if (getScaleX()==-1) {
                pixel = pixmap.getPixel((int) (currentFrame.getRegionX() + getWidth() - adjusted_x), (int) (currentFrame.getRegionY() + y - offsety));
            } else {
                pixel = pixmap.getPixel((int) (currentFrame.getRegionX() + adjusted_x), (int) (currentFrame.getRegionY() + y - offsety));
            }


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

        Texture texture = new Texture(Gdx.files.internal("core/assets/graphics/combatui/selected_character_stroke.png"));
        selectionCircle = new Sprite(texture);

        debugBorder = new TextureRegion(new Texture(Gdx.files.internal("core/assets/graphics/combatui/character_square.png")));


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

        }

        //Update actions attached to this Actor

        this.act(deltaTime);

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
            LOG.print(3,"[CharacterActor] Removed Action "+queueActions.first());
            queueActions.removeFirst();
        }
    }

    public void effectActionsAct (float deltaTime) {
        if (effectActions.size == 0 ) return;

        if (effectActions.first().act(deltaTime) == true) {
            effectActions.removeFirst();
        }
    }


    public void setOffset(float x, float y) {
        offsetx=x;
        offsety=y;

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
        if (!character.isActive() && !character.isDead() && character.isFriendly() && !this.actionInProgress() )
            batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);

        //Draw selection circle when selected
        if (isSelected() ) {
            selectionCircle.setPosition(getX()+20,getY()+10);
            selectionCircle.setSize(100,50);
            selectionCircle.draw(batch,0.7f);
        }

        //Draw current animation

        batch.draw(currentFrame,getX()+offsetx
                ,getY()+offsety
                ,getOriginX()
                ,getOriginY()
                ,getWidth()
                ,getHeight()
                ,getScaleX()
                ,getScaleY()
                ,getRotation());

        /*
        batch.draw(debugBorder,getX()+offsetx
                ,getY()+offsety
                ,getOriginX()
                ,getOriginY()
                ,getWidth()
                ,getHeight()
                ,getScaleX()
                ,getScaleY()
                ,getRotation());
                */




        //Draw effects
        //for (EffectAction effect: effectActions ) {
        if (effectActions.size>0)
            effectActions.first().draw(batch);
        //}

        //Draw attached actors
        lifebar.draw(batch);
        //for(Label l: messages) l.draw(batch,1);
        characterMessenger.draw(batch);

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
        portrait   = Assets.instance.characters.get(character.getPic()).portrait;
    }

    /**
     * Adds a MovementAction that will move the actor to the specified point using the appropiate animation.
     */
    public void moveToCell(IsoMapCellActor newCell) {

        LOG.print("[CharacterActor] movetocell"+" "+newCell.getCell().hashCode());
        if( IsoMapActor.distance(character.getCell().getCartesianCoordinates(),newCell.getCell().getCartesianCoordinates()) <= character.getMovement() ) {

            MovementAction movementAction = new MovementAction(0.025f);
            movementAction.setTargetCell(newCell);

            queueAction(movementAction);

        }

    }

    /**
     * Executes the actions that should happen when a turn starts.
     */
    public void turnStart () {
        character.startTurn();
        //Execute start of turn effects
        /*
        for (Effect e : this.getCharacter().getEffects()) {
            LOG.print(3,"Turn start effect "+e.getDiceNumber()+e.getGameSegment());
            if (e.getGameSegment()== Effect.GameSegmentType.TURN_START) {

                EffectAction ea = new EffectAction(e, 0.15f);
                this.addEffectAction(ea);
            }
        }
        */
    }




    /**
     * Adds an action to the action queue
     */
    public void useAbility( Ability a, CharacterActor target) {
        AttackAction action = new AttackAction(0.15f, a, target);

        this.queueAction(action);
    }



    /**
     * Adds an message to the actor that will displayed for a short time.
     * Multiple messages can be displayed at the same time.
     * @param message
     */
    public void showMessage (String message, Color c) {
        /*
        Label la=(new Label(message, Assets.uiSkin, "text-font", Color.WHITE));

        la.setColor(c);

        la.setPosition(getX()+offsetx,getY()+getHeight()-messages.size()*15);

        messages.add(la);

        la.addAction(Actions.sequence(
                Actions.moveTo(getX()+offsetx, getY()+getHeight()+50-messages.size()*15,1000, Interpolation.exp5Out)
                ,Actions.delay(200)
                ,Actions.alpha(0,2000,Interpolation.fade)
                , new Action () {
                    public boolean act(float deltaTime) {

                        return true;
                    }

                }
             ));
             */
        characterMessenger.showMessage(message,c);
    }

    public void showMessage (String message) {
        //showMessage (message, Color.WHITE);
        characterMessenger.showMessage(message, Color.WHITE);
    }

    /**
     * Execute an ability with this Character as target.
     * @param a
     */
    public void receiveAbility (Ability a) {
        if (a != null) {

            /*
            for (Effect e : a.getTarget().getEffects()) {
                if (e.getGameSegment()== Effect.GameSegmentType.BEFORE_HIT && a.getSource()!=this.getCharacter()) {
                    EffectAction ea = new EffectAction(e, 0.15f);
                    this.addEffectAction(ea);
                }
            }
            */

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
        LOG.print(3,"[CharacterActor] Added Action "+a+" for "+this.getCharacter().getName());
        queueActions.addLast(a);
    }

    public void addEffectAction (EffectAction ea) {

        this.effectActions.addLast(ea);
        this.addAction(ea);
    }


    public void onEffectProcessed (Effect e) {
        if (e.getTarget()==this.getCharacter()) {
            LOG.print(3,"[CharacterActor] "+character.hashCode()+" has been targeted for an effect."+e.hashCode(), LOG.ANSI_RED);
            e.addEffectListener(this);

        }

    }

    public void onEffectTriggered (Effect e) {
        LOG.print(3,"[CharacterActor] "+character.hashCode()+" effect triggered. "+e.hashCode());

            EffectAction ea = new EffectAction(e, 0.15f);
            this.addEffectAction(ea);

            this.showMessage(e.notification(), e.getColor());

        //this.queueAction(new GameWaitAction(2));

    }



    public void onCharacterHit (int damage)  {
        LOG.print(2,"[CharacterActor] Checking Death"+LOG.ANSI_RED);
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

    public void takeDamage(int damage) {
        character.hit(damage);
        //TODO: animate when hit
    }

    public void substractAction () {
        character.setAvailableActions(character.getAvailableActions() - 1);
    }



}