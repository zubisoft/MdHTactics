package com.mygdx.mdh.screens;

/**
 * Created by zubisoft on 20/03/2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.CombatRenderer;
import com.mygdx.mdh.game.model.Game;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LoadGameScreen extends AbstractGameScreen {

    public enum ButtonType {
        LOAD,SAVE
    }

    private class MenuClickListener extends ClickListener {

        String filename;
        ButtonType type;

        public MenuClickListener (ButtonType type, String filename) {
            super();
            this.filename = filename;
            this.type=type;
        }

        @Override
        public void clicked(InputEvent evt, float x, float y) {

            ScreenTransition transition = ScreenTransitionFade.init(0.75f);

            if (type == ButtonType.LOAD) {
                if (filename != null) {
                    gameScreen.setGame(Game.loadGame(filename));
                    gameScreen.setScreen(new CharSelectionScreen(gameScreen), transition);
                }
            }

            if (type == ButtonType.SAVE) {
                if (filename != null) {
                    System.out.println(inputText.getText());
                }
            }

            //new StoryScreen(gameScreen)

        }
    }

    private Stage stage;

    MenuClickListener listener;

    private static final String TAG = LoadGameScreen.class.getName();

    CombatController combatController;
    CombatRenderer combatRenderer;

    Table buttonList;

    Image background;
    ImageButton btnNew;
    ImageButton btnLoad;
    ImageButton btnQuit;

    SpriteBatch batch = new SpriteBatch();

    private boolean paused;

    public boolean isShowSaveButton() {
        return showSaveButton;
    }

    public void setShowSaveButton(boolean showSaveButton) {
        this.showSaveButton = showSaveButton;
    }

    boolean showSaveButton = false;

    public LoadGameScreen(ScreenManager game) {
        super(game);


    }

    TextField inputText;

    final Skin uiSkin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));

    public  void buildStage () {

        Table backgroundLayout = new Table();
        background = new Image(Assets.instance.guiElements.get("menus/mainmenu_bg"));
        backgroundLayout.add(background);


        buttonList = new Table();
        buttonList.pad(10);
        buttonList.setSize(600,300);
        //buttonList.setPosition(600,500);







        FileHandle fh = Gdx.files.internal("core/assets/data/games");

        for (FileHandle f: fh.list() ) {
            if (!f.isDirectory()) {

                Label la = (new Label(f.name(),  Assets.uiSkin, "handwritten_black", Color.BLACK));
                listener = new MenuClickListener(ButtonType.LOAD,f.name());
                la.addListener(listener);
                buttonList.add(la).left();
                buttonList.row();
            }

        }



        ScrollPane box = new ScrollPane(buttonList);


        NinePatchDrawable tableBackground = new NinePatchDrawable(new NinePatch(Assets.instance.guiElements.get("menus/generic-box"),8,8,8,8));
        box.getStyle().background = tableBackground;


        SpriteDrawable txr = new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/knob")));
        box.getStyle().vScrollKnob = txr;
        box.setVariableSizeKnobs(false);
        box.setFadeScrollBars(false);

        txr = new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/scrollbar")));
        box.getStyle().vScroll = txr;
        box.getStyle().vScroll.setMinWidth(20);
        box.setFillParent(true);

        box.setSize(600,300);

        Table c = new Table();

        //c.setFillParent(true);
        c.add(box).size(600,300);

        Table x = new Table();

        if (showSaveButton) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            String prefix = "Savegame";
            if (gameScreen.game != null && gameScreen.game.getCurrentMission() != null) prefix = gameScreen.game.getCurrentMission().getName();

            inputText = new TextField(prefix+" "+dateFormat.format(date), Assets.uiSkin);
            inputText.getStyle().background = tableBackground;
            inputText.setSize(600,50);
            inputText.setCursorPosition(0);
            //stage.addActor(inputText);

            TextField b = new TextField("Save", Assets.uiSkin);
            b.setSize(100,50);

            MenuClickListener mc = new MenuClickListener(ButtonType.SAVE,"");
            b.addListener(mc);

            x.add(inputText).size(500, 50);
            x.add(b).size(100,50).pad(0);

            x.row();
        }




        x.add(c).size(600,300).colspan(2);



        Stack stack = new Stack();
        stage.addActor(stack);

        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(background);
        stack.add(x);


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