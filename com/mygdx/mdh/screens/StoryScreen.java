package com.mygdx.mdh.screens;

/**
 * Created by zubisoft on 20/03/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.game.hud.StoryMessageBar;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.model.StoryText;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;


public class StoryScreen extends AbstractGameScreen {

    ScreenTransition transition = ScreenTransitionFade.init(0.75f);

    private class MenuClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent evt, float x, float y) {



            if(messageBar.hasMoreMessages()==false)
                switch(storyType) {
                    case INTRO: gameScreen.setScreen(new CombatScreen(gameScreen), transition); break;
                    case OUTRO: gameScreen.setScreen(new MissionSelectionScreen(gameScreen), transition); break;

                }
            else {
                messageBar.hide();
                messageBar.show();
            }

        }
    }


    private Stage stage;

    StoryMessageBar messageBar;

    MenuClickListener listener;

    private static final String TAG = StoryScreen.class.getName();


    public enum STORY_TYPE {
        INTRO, OUTRO
    }

    STORY_TYPE storyType;
    Image background;

    SpriteBatch batch = new SpriteBatch();

    private boolean paused;

    public StoryScreen(ScreenManager game, STORY_TYPE storyType) {
        super(game);

        messageBar = new StoryMessageBar();
        this.storyType = storyType;

    }

    public void setMessages (java.util.List<StoryText> textList) {
        messageBar.addMessages(textList);
    }

    public  void buildStage () {

        Table backgroundLayout = new Table();
        background = new Image(Assets.instance.guiElements.get("menus/mainmenu_bg"));
        backgroundLayout.add(background);

        listener = new MenuClickListener();


        Stack stack = new Stack();
        stage.addActor(stack);
        stage.addActor(messageBar);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(background);




        Label la =(new Label("Skip",Assets.uiSkin, "handwritten_white", Color.WHITE));
        Table t = new Table();
        t.setFillParent(true);
        t.bottom().right().pad(50);
        t.add(la);

        la.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent evt, float x, float y) {
                super.clicked(evt,x,y);
                gameScreen.setScreen(new CombatScreen(gameScreen), transition);
            }
        }
        );

        stack.add(t);

        background.addListener(listener);

        messageBar.show();


        /*
        btnNew.setPosition(400,500);
        btnLoad.setPosition(400,300);
        btnQuit.setPosition(400,100);
        */
    }






    @Override
    public void render(float deltaTime) {

        batch.begin();
/*
        background.draw(batch,0,0,1280,720);
        layout.draw(batch,1.0f);*/
        stage.act(deltaTime);


        stage.draw();

        /*
        btnNew.draw(batch,1.0f);
        btnLoad.draw(batch,1.0f);
        btnQuit.draw(batch,1.0f);
        */

        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage = new Stage();
        this.buildStage();
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {
        stage.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        // Only called on Android!
        paused = false;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

}