package com.mygdx.mdh.screens;

/**
 * Created by zubisoft on 20/03/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.CombatRenderer;
import com.mygdx.mdh.game.hud.CombatHUD;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.LOG;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;


public class MainMenuScreen extends AbstractGameScreen {

    private class MenuClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent evt, float x, float y) {

            ScreenTransition transition = ScreenTransitionFade.init(0.75f);
            game.setScreen(new GameScreen(game), transition);

        }
    }

    private Stage stage;

    MenuClickListener listener;

    private static final String TAG = MainMenuScreen.class.getName();

    CombatController combatController;
    CombatRenderer combatRenderer;

    Table buttonList;

    Image background;
    ImageButton btnNew;
    ImageButton btnLoad;
    ImageButton btnQuit;

    SpriteBatch batch = new SpriteBatch();

    private boolean paused;

    public MainMenuScreen(ScreenManager game) {
        super(game);


    }

    public  void buildStage () {

        Table backgroundLayout = new Table();
        background = new Image(Assets.instance.guiElements.get("mainmenu_bg"));

        backgroundLayout.add(background);

        btnNew = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("mainmenu_top_button"))));
        btnLoad = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("mainmenu_loadbutton"))));
        btnQuit = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("mainmenu_quitbutton"))));

        listener = new MenuClickListener();

        btnNew.addListener(listener);
        btnLoad.addListener(listener);
        btnQuit.addListener(listener);

        buttonList = new Table();
        buttonList.pad(10);
        buttonList.setPosition(600,500);
        buttonList.add(btnNew);
        buttonList.row();
        buttonList.add(btnLoad);
        buttonList.row();
        buttonList.add(btnQuit);

        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(background);
        stack.add(buttonList);


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
        buttonList.draw(batch,1.0f);*/
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