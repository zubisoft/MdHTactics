package com.mygdx.mdh.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.mdh.MDHTactics;
import com.mygdx.mdh.Model.Character;

/**
 * Created by zubisoft on 28/01/2016.
 */


public class CharacterActor extends Actor {

    private static final int        FRAME_COLS = 6;         // #1
    private static final int        FRAME_ROWS = 3;         // #2

    Animation                       idleAnimation;          // #3
    Animation                       walkAnimation;          // #3
    Texture                         walkSheet;              // #4

    TextureRegion                   currentFrame;           // #7
    float stateTime;

    private Character character;
    Texture sprite;
    LifeBar lifebar;

    AnimatedAction currentAction;


    public CharacterActor(Character character, int startX, int startY) {
        super();
        this.character = character;

        this.setX(startX*100);
        this.setY(startY*100);
        this.setWidth(100);
        this.setHeight(100);
        this.setBounds(this.getX(),this.getY(),this.getWidth(),this.getHeight());

        sprite = new Texture(Gdx.files.internal(character.getPic()));

        lifebar = new LifeBar(this);
        lifebar.setX(startX);
        lifebar.setY(startY);


        loadAnimations();

        currentAction = new IdleAnimatedAction (0.025f, this);

    }

    public Character getCharacter() {return character;}

    public Texture getSprite() {
        return sprite;
    }


    public void draw () {
        if (!character.isActive())  MDHTactics.batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);


        stateTime += Gdx.graphics.getDeltaTime();           // #15

        currentAction.update(stateTime);
        currentAction.draw(stateTime);

        lifebar.draw();
        if (!character.isActive()) MDHTactics.batch.setColor(1f, 1f, 1f, 1f);
    }

    public void loadAnimations () {
        walkSheet = new Texture(Gdx.files.internal(character.getPic())); // #9
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);              // #10
        TextureRegion[] frames = new TextureRegion[FRAME_COLS];
        int index = 0;

        for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[0][j];
        }

        walkAnimation = new Animation(0.1f, frames);      // #11

        frames = new TextureRegion[1];
        frames[0] = tmp[2][0];

        idleAnimation = new Animation(0.1f, frames);      // #11

        stateTime = 0f;
    }

    public void moveToCell( int x, int y) {


        character.setCellx(x);
        character.setCelly(y);
        character.setAvailableActions(character.getAvailableActions()-1);

        currentAction = new MovementAnimatedAction (0.1f, this, x*100, y*100);

        System.out.println("[CharacterActor] New position: "+this.getX()+","+this.getY());

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
}