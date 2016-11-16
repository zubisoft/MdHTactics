package com.mygdx.mdh.screens;

/**
 * Created by zubisoft on 20/03/2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.controller.CharacterChangeListener;
import com.mygdx.mdh.game.controller.GameEventListener;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Mission;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;
import com.mygdx.mdh.screens.widgets.UnlocksWindow;


public class CombatDebriefScreen extends AbstractGameScreen implements CharacterChangeListener, GameEventListener {


    Table unlocksTable = new Table();
    UnlocksWindow unlocksWindow;

    /**
     * Animation to resize the progress bar
     */
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

    /**
     * Progress Bar
     */
    private class XPBar extends Group {

        TextureRegion barBackground;
        TextureRegion barFill;

        Label la =(new Label("LEVEL UP!",Assets.uiSkin, "gradient-font",Color.WHITE));


        public XPBar() {

            setSize(200,33);

            barBackground = new TextureRegion(Assets.instance.guiElements.get("menus/progressbar_bg"));
            barFill = new TextureRegion(Assets.instance.guiElements.get("menus/progressbar_fill"));

            la.setY(60);
            la.getColor().a = 0;
            this.addActor(la);
        }


        /**
         * Updates character experience and shows progress in the bar.
         * Displays level up message when new levels are attained.
         * @param c
         * @param incrementalXP
         */
        public void setProgress (Character c, int incrementalXP) {

            int remainingXP = incrementalXP;

            barFill.setRegionWidth(Math.round(getWidth()*1.0f*c.getXp()/c.getNextLevelXP()));

            SequenceAction action = Actions.sequence();

            int level;

            while (remainingXP > 0 && c.getLevel() <= 20) {
                int auxXP = Math.min(remainingXP, c.getNextLevelXP()-c.getXp());
                remainingXP = remainingXP - auxXP;

                int newWidth = Math.round(getWidth()*1.0f*(c.getXp()+auxXP)/c.getNextLevelXP());
                if ( newWidth>getWidth() ) newWidth = (int)getWidth();

                level = c.getLevel();
                c.addXp(auxXP);

                action.addAction(resizeBarAction(newWidth,barFill.getRegionHeight(), 2, Interpolation.pow2Out) );

                if (remainingXP>0) action.addAction(resizeBarAction(0,barFill.getRegionHeight(), 0, Interpolation.pow2Out) );

                if (level < c.getLevel()) {
                    //Total duration of this animation mus be less than the bar progress animation
                    action.addAction(Actions.addAction(
                            Actions.sequence(
                                    Actions.moveTo(getX(), getY() + 60)
                                    , Actions.alpha(1, 0.3f, Interpolation.fade)
                                    , Actions.moveTo(getX(), getY() + 200, 1f, Interpolation.exp5Out)
                                    , Actions.delay(0.3f)
                                    , Actions.alpha(0, 0.3f, Interpolation.fade)
                            )  ,
                            la

                    ));

                }

            }
            this.addAction(action);
            //la.addAction(textAction);



        }

        public XPBarResize resizeBarAction (float x, float y, float duration, Interpolation interpolation) {
            XPBarResize action = new XPBarResize();
            action.setSize(x, y);
            action.setDuration(duration);
            action.setInterpolation(interpolation);
            return action;
        }


        @Override
        protected void positionChanged() {
            super.positionChanged();
            //la.setPosition(getX(),getY());

        }


        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.draw(barBackground       ,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),1,1,0);
            batch.draw(barFill             ,getX(),getY(),getOriginX(),getOriginY(),barFill.getRegionWidth(),getHeight(),1,1,0);

        }
    }

    /**
     * Button controller
     */
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
                gameScreen.game.saveGame();
                gameScreen.setScreen(screen, transition);
            }

            if (combatController.isGameOver() && combatController.isCombatFinished()) {
                ScreenTransition transition = ScreenTransitionFade.init(0.75f);
                CharSelectionScreen screen = new CharSelectionScreen(gameScreen);
                gameScreen.setScreen(screen, transition);
            }

        }
    }



    private enum ButtonType {
        CONTINUE
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


        int individualXP = combatController.getCombat().getExperience()/gameScreen.game.getCurrentParty().size();
        for (Character character: gameScreen.game.getCurrentParty()) {
            character.addListener(this);
            XPBar xpb = new XPBar();
            xpb.setProgress(character, individualXP);
            Container container = new Container(xpb);
            layout.add(container).size(200,33);

        }

        layout.row();


        //final Skin uiSkin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));
        //Label la = (new Label(""+combatController.getCombat().getExperience(), uiSkin, "default-font", Color.ORANGE));

        btnNew = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/btn-continue"))));
        //btnNew.setWidth(100);
        listener = new MenuClickListener(ButtonType.CONTINUE);
        btnNew.addListener(listener);

        buttonList = new Table();
        buttonList.pad(10);
        //buttonList.add(la);
        buttonList.add(btnNew).width(200);

        layout.add(buttonList).colspan(1);


        ScrollPane scroller = new ScrollPane(unlocksTable);
        scroller.getStyle().background =  new NinePatchDrawable(new NinePatch(Assets.instance.guiElements.get("menus/generic-box"),20,20,20,20));

        unlocksTable.pad(20);
        unlocksTable.setSize(400,200);

        unlocksTable.row();

        layout.add(scroller).colspan(2).size(400,200).align(Align.topLeft);

        Stack stack = new Stack();

        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(background);
        stack.add(layout);

        gameScreen.game.addListener(this);
        gameScreen.game.completeMission(gameScreen.game.getCurrentMission());



        /*
        btnNew.setPosition(400,500);
        btnLoad.setPosition(400,300);
        btnQuit.setPosition(400,100);
        */


        unlocksWindow = new UnlocksWindow(unlocksTable);
        stage.addActor(unlocksWindow);

        unlocksWindow.setPosition(
                500
                , 500
                );

        unlocksWindow.show();


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


    public  void onAbilityUnlock (Character c, Ability a) {
        //unlocksTable.add(new Label(c.getName()+" unlocked "+a.getName(),Assets.uiSkin,"handwritten_black" )).align(Align.center).expand().pad(20);
        //unlocksTable.row();

        Stack s = new Stack();

        Image pic = new Image(Assets.instance.characters.get(c.getPic()).portrait);
        s.add(pic);

        pic = new Image(Assets.instance.abilities.get(a.getPic()));
        Table aux = new Table();
        aux.right().bottom();
        aux.add(pic).size(30,30).align(Align.bottomRight);
        s.add(aux);

        unlocksTable.add(s).size(100,100);


    }

    public  void onMissionUnlocked (Mission m) {
        Table t = new Table ();
        t.add(new Image(Assets.instance.maps.get("icons/"+m.getMissionMap().getMapId()))).size(75,75);
        t.row();
        t.add(new Label("Unlocked mission\n"+m.getName(),Assets.uiSkin,"handwritten_black" )).align(Align.center);

        unlocksTable.add(t).size(500,200);

        //unlocksTable.row();
    }

}