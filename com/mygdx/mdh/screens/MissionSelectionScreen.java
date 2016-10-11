package com.mygdx.mdh.screens;

/**
 * Created by zubisoft on 20/03/2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.model.Campaign;
import com.mygdx.mdh.game.model.Mission;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.LOG;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;
import com.mygdx.mdh.screens.widgets.MissionPortrait;


public class MissionSelectionScreen extends AbstractGameScreen {

    private class PortraitClickListener extends ClickListener {

        MissionPortrait portrait;

        PortraitClickListener(MissionPortrait p) {
            this.portrait = p;
        }

        @Override
        public void clicked(InputEvent evt, float x, float y) {

            selectCurrentMission(portrait);

            /*
            Testing
            gameScreen.game.completeMission(portrait.getMission());
            selectCampaign(gameScreen.game.getCurrentCampaign());
            */

            /*
            System.out.println(gameScreen.game.getCurrentParty());
            System.out.println(gameScreen.game.getCurrentMission().getMissionMap());
            System.out.println(gameScreen.game.getCurrentMission().getBaddies());
            */

        }
    }


    private enum ButtonType {
        CONTINUE, PREV, NEXT
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
                    StoryScreen screen = new StoryScreen(gameScreen, StoryScreen.STORY_TYPE.INTRO);
                    screen.setMessages(currentSelectedMission.getMission().getIntroText());
                    LOG.print(1,"texto"+currentSelectedMission.getMission().getIntroText(),LOG.ANSI_GREEN);
                    gameScreen.setScreen(screen, transition);
                    break;
                case NEXT:
                    selectCampaign(gameScreen.game.getNextCampaign());
                    break;
                case PREV:
                    selectCampaign(gameScreen.game.getPrevCampaign());
                    break;
            }

            //new StoryScreen(gameScreen)

        }
    }



    private Stage stage;

    /* Screen background */
    Image background;
    Table layout;

    MissionPortrait currentSelectedMission;
    PortraitClickListener listener;

    TextArea title;
    TextArea description;
    Image portrait;

    Table portraitsLayout;
    Table charInfoBoxLayout;
    Table campaignTitleLayout;

    private boolean paused;

    public MissionSelectionScreen(ScreenManager gameManager) {

        super(gameManager);


    }

    public  void buildStage () {

        background = new Image(Assets.instance.guiElements.get("menus/mainmenu_bg"));

        //Elements
        layout = new Table();

        createCampaignTitleLayout ();

        charInfoBoxLayout = new Table();
        createMissionInfoLayout();

        portraitsLayout = new Table();
        //createMissionLayout(gameScreen.game.getCurrentCampaign());
        selectCampaign(gameScreen.game.getCurrentCampaign());

        //Structure
        layout.add(campaignTitleLayout).colspan(2).expand();
        layout.row();
        layout.add(charInfoBoxLayout).width(Constants.VIEWPORT_GUI_WIDTH/2).expand();
        layout.add(portraitsLayout);

        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(background);
        stack.add(layout);



    }

    Image leftArrow, rightArrow;
    Label campaignTitle;

    public void createCampaignTitleLayout () {
        campaignTitleLayout= new Table();

        leftArrow = new Image(Assets.instance.guiElements.get("menus/btn-arrow"));
        leftArrow.setScaleX(-1);
        campaignTitleLayout.add(leftArrow);

        campaignTitle =(new Label("The Museum of Madness", Assets.uiSkin, "gradient-font", Color.WHITE));
        campaignTitleLayout.add(campaignTitle);

        rightArrow = new Image(Assets.instance.guiElements.get("menus/btn-arrow"));
        campaignTitleLayout.add(rightArrow);

        rightArrow.addListener(new MenuClickListener(ButtonType.NEXT));
        leftArrow.addListener(new MenuClickListener( ButtonType.PREV));

    }

    public void createMissionInfoLayout () {

        //Mission Information Area
        title = new TextArea("Mission Title",Assets.uiSkin,"mdh_menu_infobox_title" );
        title.setAlignment(Align.center);

        description = new TextArea("First of all, there are no ads poping on this one, which was annoying as hell.",Assets.uiSkin,"mdh_menu_infobox_title" );
        description.setAlignment(Align.center);

        portrait = new Image(Assets.instance.characters.get("zubi").portrait);


        //Mission Info Box
        Stack missionInfoBox = new Stack();

        Table c = new Table();
        c.top();
        c.setSize(450,200);
        c.add(title).center().height(50).pad(30);
        c.row();
        c.add(description).size(400,150).center().padLeft(40);

        missionInfoBox.add(new Image(Assets.instance.guiElements.get("menus/charselection_infobox")));
        missionInfoBox.add(c);


        charInfoBoxLayout.setWidth(Constants.VIEWPORT_GUI_WIDTH/2);
        charInfoBoxLayout.add(portrait).size(150,150);
        charInfoBoxLayout.row();
        charInfoBoxLayout.add(missionInfoBox);
        charInfoBoxLayout.row();
        ImageButton btnContinue = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.guiElements.get("menus/btn-continue"))));
        btnContinue.addListener(new MenuClickListener(ButtonType.CONTINUE));
        charInfoBoxLayout.add(btnContinue).height(75).expand();

    }


    public void createMissionLayout (Campaign c) {

        portraitsLayout.clear();

        //Mission Selection Area
        portraitsLayout.setWidth(Constants.VIEWPORT_GUI_WIDTH/2);

        MissionPortrait[] portraits = new MissionPortrait[12];

        int lastUnlockedMission = 0;
        for (int i=0; i<9; i++) {

            //portraits[i].add(buttons[i]);
            if (c.getCampaignMissions().size()>i
                    && c.getCampaignMissions().get(i).isUnlocked()) {

                portraits[i] = new MissionPortrait(c.getCampaignMissions().get(i));
                listener = new PortraitClickListener(portraits[i]);
                portraits[i].addListener(listener);
                lastUnlockedMission=i;


            } else {
                portraits[i] = new MissionPortrait(null);
            }

            portraitsLayout.add(portraits[i]).pad(10);

            //Display 3 missions per row
            if(Math.floorMod(i+1,3)==0)   portraitsLayout.row();

        }


        //Initialize mission description with the first mission available
        selectCurrentMission(portraits[lastUnlockedMission]);


    }


    public void selectCampaign (Campaign c) {

        gameScreen.game.setCurrentCampaign(c);

        System.out.println(c.getName());
        campaignTitle.setText(c.getName());

        if (gameScreen.game.getNextCampaign() != null ) {
            rightArrow.setVisible(true);

        } else
            rightArrow.setVisible(false);

        if (gameScreen.game.getPrevCampaign() != null ) {
            leftArrow.setVisible(true);

        } else
            leftArrow.setVisible(false);

        createMissionLayout(c);

        layout.invalidate();

    }

    public void selectCurrentMission(MissionPortrait mission) {

        //Deselect previous
        if (currentSelectedMission != null) currentSelectedMission.setSelected(false);

        //Select current
        currentSelectedMission = mission;
        mission.setSelected(true);

        //TODO the campaign must be also set accordingly
        gameScreen.game.setCurrentMission(currentSelectedMission.getMission());

        title.setText(mission.getMission().getName());
        description.setText(mission.getMission().getDescription());
        portrait.setDrawable(mission.portrait.getDrawable());
    }

    @Override
    public void render(float deltaTime) {

        gameScreen.batch.begin();

        stage.act(deltaTime);
        stage.draw();

        gameScreen.batch.end();

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