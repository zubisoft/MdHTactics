package com.mygdx.mdh.screens;

/**
 * Created by zubisoft on 20/03/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.CombatRenderer;
import com.mygdx.mdh.game.model.Game;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;
import com.mygdx.mdh.screens.widgets.Portrait;
import com.mygdx.mdh.screens.widgets.WidgetCharterSheet;


public class CharSelectionScreen extends AbstractGameScreen {

    private class PortraitClickListener extends ClickListener {

        Portrait portrait;

        PortraitClickListener(Portrait p) {
            this.portrait = p;
        }

        @Override
        public void clicked(InputEvent evt, float x, float y) {

            if (!portrait.isSelected() ) {
                if (gameScreen.game.addCurrentParty(portrait.getCharacter())) {
                    characterPortrait.setActor(new Image(Assets.instance.characters.get(portrait.getCharacter().getPic()).portrait));
                    characterSheet.loadCharacter(portrait.getCharacter());
                    portrait.setSelected(true);
                }
            } else {
                gameScreen.game.removeCurrentParty(portrait.getCharacter());
                portrait.setSelected(false);
            }





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

            ScreenTransition transition = ScreenTransitionFade.init(0.75f);


            switch (buttonType) {
                case CONTINUE:
                    gameScreen.setScreen(new MissionSelectionScreen(gameScreen), transition);
                    break;
            }

            //new StoryScreen(gameScreen)

        }
    }



    private Stage stage;

    PortraitClickListener listener;

    private static final String TAG = CharSelectionScreen.class.getName();

    CombatController combatController;
    CombatRenderer combatRenderer;

    Table layout;

    Image background;

    ImageButton btnContinue;




    SpriteBatch batch = new SpriteBatch();

    private boolean paused;

    public CharSelectionScreen(ScreenManager gameManager) {
        super(gameManager);
        //TODO remove this, this is for testing only
        if(gameScreen.game == null) gameScreen.setGame(Game.loadNewGame());
    }


    Container characterPortrait;
    WidgetCharterSheet characterSheet;



    public  void buildStage () {

        //background layout
        Table backgroundLayout = new Table();
        background = new Image(Assets.instance.guiElements.get("menus/mainmenu_bg"));
        backgroundLayout.add(background);


        //background layout
        Table charInfoBoxLayout = new Table();
        charInfoBoxLayout.setWidth(Constants.VIEWPORT_GUI_WIDTH/2);
        characterPortrait = new Container(new Image(Assets.instance.characters.get(gameScreen.game.getCharacterCollection().get(0).getPic()).portrait));
        charInfoBoxLayout.add(characterPortrait);
        charInfoBoxLayout.row();

        Stack s = new Stack();
        s.setSize(400,600);
        s.add(new Image(Assets.instance.guiElements.get("menus/charselection_infobox")));
        characterSheet = new WidgetCharterSheet(gameScreen.game.getCharacterCollection().get(0));
        s.add(characterSheet);
        charInfoBoxLayout.add(s);



        charInfoBoxLayout.row();
        btnContinue = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/mainmenu_top_button"))));
        btnContinue.addListener(new MenuClickListener(ButtonType.CONTINUE));
        charInfoBoxLayout.add(btnContinue);

        //portrait layout
        Table portraitsLayout = new Table();
        portraitsLayout.setWidth(Constants.VIEWPORT_GUI_WIDTH/2);

        Portrait[] portraits = new Portrait[12];

        for (int i=0; i<12; i++) {

            //portraits[i].add(buttons[i]);
            if (gameScreen.game.getCharacterCollection().size()>i) {

/*
                portraits[i].add( new Image(Assets.instance.guiElements.get("charselection_portrait")));

                Container c = new Container(new Image(Assets.instance.characters.get(gameScreen.game.getCharacterCollection().get(i).getPic()).portrait));
                c.padLeft(25);
                portraits[i].add(c);

                portraits[i].add( new Image(Assets.instance.guiElements.get("charselection_portrait_frame")));
*/

                portraits[i] = new Portrait(gameScreen.game.getCharacterCollection().get(i));
                listener = new PortraitClickListener(portraits[i]);
                portraits[i].addListener(listener);



            } else {
                portraits[i] = new Portrait(null);
            }



            //portraitsLayout.add(buttons[i]).pad(10);
            portraitsLayout.add(portraits[i]).pad(10);



            if(Math.floorMod(i+1,3)==0)   portraitsLayout.row();

        }

        layout = new Table();
        layout.add(charInfoBoxLayout);
        layout.add(portraitsLayout);

       // layout.setWidth(500);

        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(background);
        stack.add(layout);


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