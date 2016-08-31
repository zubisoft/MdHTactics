package com.mygdx.mdh.screens;

/**
 * Created by zubisoft on 20/03/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;


public class CombatDebriefScreen extends AbstractGameScreen {



    private class XPBarResize extends TemporalAction {

        private float startWidth;
        private float endWidth, endHeight;

        protected void begin () {
            XPBar xpb = (XPBar)target;
            startWidth = xpb.barFill.getRegionWidth();
            //startHeight = target.getHeight();
        }

        protected void update (float percent) {
            XPBar xpb = (XPBar)target;
            xpb.barFill.setRegionWidth( (int)(startWidth + (endWidth - startWidth) * percent));
            //xpb.barFill.setRegionHeight( (int)(startWidth + (endWidth - startWidth) * percent) );
            System.out.println("Newwidth: "+xpb.barFill.getRegionWidth());

        }

        public void setSize (float width, float height) {
            endWidth = width;
            endHeight = height;
        }

        public float getWidth () {
            return endWidth;
        }

        public void setWidth (float width) {
            endWidth = width;
        }

        public float getHeight () {
            return endHeight;
        }

        public void setHeight (float height) {
            endHeight = height;
        }


    }


    private class XPBar extends Actor {

        TextureRegion barBackground;
        TextureRegion barFill;

        public XPBar() {

            setSize(200,33);

            barBackground = new TextureRegion(Assets.instance.guiElements.get("menus/progressbar_bg"));
            barFill = new TextureRegion(Assets.instance.guiElements.get("menus/progressbar_fill"));

        }

        public void setProgress (Character c, int incrementalXP) {
            barFill.setRegionWidth(Math.round(getWidth()*1.0f*c.getXp()/c.getNextLevelXP()));
            int newWidth = Math.round(getWidth()*1.0f*(c.getXp()+incrementalXP)/c.getNextLevelXP());
            if ( newWidth>getWidth() ) newWidth = (int)getWidth();

            System.out.println("Progress: "+c.getXp()+"/"+c.getNextLevelXP()+"="+ 1.0f*c.getXp()/c.getNextLevelXP()+" new:"+1.0f*(c.getXp()+incrementalXP)/c.getNextLevelXP());
            System.out.println("Base: "+getWidth()+" Initial "+Math.round(getWidth()*1.0f*c.getXp()/c.getNextLevelXP())+" Final "+newWidth);

            this.addAction(resizeBarAction(newWidth,barFill.getRegionHeight(), 3, Interpolation.linear) );

        }

        public XPBarResize resizeBarAction (float x, float y, float duration, Interpolation interpolation) {
            XPBarResize action = new XPBarResize();
            action.setSize(x, y);
            action.setDuration(duration);
            action.setInterpolation(interpolation);
            return action;
        }

        public void draw(Batch batch, float parentAlpha) {
            batch.draw(barBackground       ,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),1,1,0);
            batch.draw(barFill             ,getX(),getY(),getOriginX(),getOriginY(),barFill.getRegionWidth(),getHeight(),1,1,0);
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


    CombatController combatController;

    Table buttonList;

    Image background;
    ImageButton btnNew;

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
            xpb.setProgress(character, combatController.getCombat().getExperience());
            Container container = new Container(xpb);
            layout.add(container).size(200,33);

        }

        layout.row();


        //final Skin uiSkin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));
        //Label la = (new Label(""+combatController.getCombat().getExperience(), uiSkin, "default-font", Color.ORANGE));

        btnNew = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/mainmenu_top_button"))));
        listener = new MenuClickListener(ButtonType.CONTINUE);
        btnNew.addListener(listener);

        buttonList = new Table();
        buttonList.pad(10);
        buttonList.setPosition(600,500);
        //buttonList.add(la);
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