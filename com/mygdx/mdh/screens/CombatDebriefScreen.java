package com.mygdx.mdh.screens;

/**
 * Created by zubisoft on 20/03/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.CombatRenderer;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Game;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;


public class CombatDebriefScreen extends AbstractGameScreen {

    TextureRegion barBackground;
    TextureRegion barFill;

    private class XPBar extends Actor {
        public XPBar() {

            setSize(200,50);

            barBackground = new TextureRegion(Assets.instance.guiElements.get("character/CHAR-healthbar-bg"));
            barFill = new TextureRegion(Assets.instance.guiElements.get("character/CHAR-healthbar-fill"));
            //
        }

        public void setProgress (int xp, int levelXp) {
            barFill.setRegionWidth(Math.round(getWidth()*1.0f*xp/levelXp));
         }

        public void draw(Batch batch, float parentAlpha) {
            System.out.println("draw");
            batch.draw(barBackground       ,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),1,1,0);
            batch.draw(barFill             ,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),1,1,0);
        }
    }

    private enum ButtonType {
        CONTINUE
    }

    private class MenuClickListener extends ClickListener {

        ButtonType buttonType;

        public MenuClickListener (ButtonType type) {
            super();
            this.buttonType = type;

        }

        @Override
        public void clicked(InputEvent evt, float x, float y) {

            if (combatController.isVictory() && combatController.isCombatFinished()) {
                ScreenTransition transition = ScreenTransitionFade.init(0.75f);
                StoryScreen screen = new StoryScreen(gameScreen, StoryScreen.STORY_TYPE.OUTRO);
                screen.setMessages(gameScreen.getGame().getCurrentMission().getOutroText());
                gameScreen.setScreen(screen, transition);
            }

            if (combatController.isGameOver() && combatController.isCombatFinished()) {
                ScreenTransition transition = ScreenTransitionFade.init(0.75f);
                MissionSelectionScreen screen = new MissionSelectionScreen(gameScreen);
                gameScreen.setScreen(screen, transition);
            }

        }
    }

    private Stage stage;

    MenuClickListener listener;

    private static final String TAG = CombatDebriefScreen.class.getName();

    CombatController combatController;
    CombatRenderer combatRenderer;

    Table buttonList;

    Image background;
    ImageButton btnNew;
    ImageButton btnLoad;
    ImageButton btnQuit;

    SpriteBatch batch = new SpriteBatch();


    private boolean paused;

    public CombatDebriefScreen(ScreenManager game, CombatController combatController) {
        super(game);
        this.combatController = combatController;

    }

    public  void buildStage () {


        background = new Image(Assets.instance.guiElements.get("menus/mainmenu_bg"));

        Table layout = new Table();

        for (Character character: gameScreen.game.getCurrentParty()) {
            Container container = new Container(new Image(Assets.instance.characters.get(character.getPic()).portrait));
            layout.add(container).size(200,200);
        }

        layout.row();

        for (Character character: gameScreen.game.getCurrentParty()) {
            XPBar xpb = new XPBar();
            xpb.setProgress(character.getXp(),(character.getLevel()+1)*500);
            Container container = new Container(xpb);
            layout.add(container).size(200,50);
            xpb.addAction(Actions.sizeTo(100f,50f, 3, Interpolation.linear)
            );
        }

        layout.row();


        final Skin uiSkin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));
        Label la = (new Label(""+combatController.getCombat().getExperience(), uiSkin, "default-font", Color.ORANGE));

        btnNew = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/mainmenu_top_button"))));
        listener = new MenuClickListener(ButtonType.CONTINUE);
        btnNew.addListener(listener);

        buttonList = new Table();
        buttonList.pad(10);
        buttonList.setPosition(600,500);
        buttonList.add(la);
        buttonList.row();
        buttonList.add(btnNew);

        layout.add(buttonList).colspan(3);

        Stack stack = new Stack();

        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(background);
        stack.add(layout);



        gameScreen.game.completeMission(gameScreen.game.getCurrentMission());


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