package com.mygdx.mdh.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.characters.actions.AnimatedAction;
import com.mygdx.mdh.game.characters.actions.IdleAnimatedAction;
import com.mygdx.mdh.game.characters.actions.MovementAnimatedAction;
import com.mygdx.mdh.game.model.Character;

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
    TextureRegion                   currentFrame;           // #7
    AnimatedAction currentAction;

    //Addtional graphic elements
    CharacterLifeBar lifebar;
    Label l;

    //Game Logic
    private Character character;

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


    //Logic management


    public enum CHARACTER_STATE {
        IDLE, MOVING, ABILITY1, ABILITY2
    }

    boolean ready;
    CHARACTER_STATE state;
    float stateTime;

    boolean selected;
    ShapeRenderer s;
    Sprite selectionCircle;



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
        lifebar.setX(startX);
        lifebar.setY(startY);


        loadAnimations();

        currentAction = new IdleAnimatedAction(0.025f, this);

        this.state=CHARACTER_STATE.IDLE;

        s = new ShapeRenderer();

        Texture texture = new Texture(Gdx.files.internal("core/assets/graphics/combatui/selected_character_stroke.png"));
        selectionCircle = new Sprite(texture);


    }



    public Character getCharacter() {return character;}


    public void update(float deltaTime) {

        stateTime +=deltaTime;

        lifebar.update();

        //Update character extra graphics
        if (l != null)  l.act(stateTime);

        switch(state) {
            case IDLE:      this.setReady(true); break;
            case MOVING:    this.setReady(false);break;
            case ABILITY1:  this.setReady(false);break;
            case ABILITY2:  this.setReady(false);break;

        }

        currentAction.update(stateTime);
        this.act(deltaTime);

    }


    public void draw (SpriteBatch batch) {

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);


        if (!character.isActive())  batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        //System.out.println("[Checking] "+CombatController.combat.getCurrentSelectedCharacter()+"=="+character);
        if (isSelected() ) {
            selectionCircle.setPosition(getX()+16,getY()+8);
            selectionCircle.setSize(100,50);
            selectionCircle.draw(batch,0.7f);
        }

        currentAction.draw(batch);

        lifebar.draw(batch);

        if (!character.isActive()) batch.setColor(1f, 1f, 1f, 1f);

        if (l != null)  l.draw(batch,1);

    /*
        this.setDebug(true);
        s.begin(ShapeRenderer.ShapeType.Line);
        s.setColor(new Color(0, 1, 0, 1));
        s.rect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
        //this.drawDebugBounds(s);
        s.end();
        */


    }

    public void loadAnimations () {
        walkSheet = new Texture(Gdx.files.internal(character.getPic())); // #9
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);              // #10
        TextureRegion[] frames = new TextureRegion[FRAME_COLS];
        int index = 0;

        for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[0][j];
        }

        walkAnimation = new Animation(0.15f, frames);      // #11

        frames = new TextureRegion[1];
        frames[0] = tmp[2][0];

        idleAnimation = new Animation(0.1f, frames);      // #11

        System.out.println("idle anim width "+frames[0].getRegionWidth()+" "+frames[0].getRegionHeight());

        frames = new TextureRegion[1];
        frames[0] = tmp[2][1];

        attackAnimation = new Animation(0.1f, frames);      // #11

        //stateTime = 0f;
    }

    public void moveToCell( float x, float y) {
        this.state=CHARACTER_STATE.MOVING;
        currentAction = new MovementAnimatedAction(0.1f, this, x, y);
/*
        System.out.println("[CharacterActor] Moving: "+this.character);
        System.out.println("[CharacterActor] New position: "+this.getX()+","+this.getY()+" Action "+ currentAction);
        */

    }



    public void getHit( int value ) {

        Skin uiSkin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));
        l=(new Label("Hit!", uiSkin, "default-font", Color.ORANGE));
        l.setPosition(getX()+60,getY()+getHeight());

        l.addAction(Actions.sequence(
                Actions.moveTo(getX()+60, getY()+getHeight()+50,1000, Interpolation.exp5Out)
                ,Actions.alpha(0,1000,Interpolation.fade)
        ));
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


    public Animation getIdleAnimation() {
        return idleAnimation;
    }

    public Animation getWalkAnimation() {
        return walkAnimation;
    }

    public AnimatedAction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(AnimatedAction currentAction) {
        this.currentAction = currentAction;
    }


    public String toString() {
        return "[Character: "+character.name+"] Cell("+character.getCellx()+","+character.getCelly()+") @ ["+this.getX()+","+getY()+ "] HP:"+character.getHealth()+ " AP:"+character.getAvailableActions();
    }
}