package com.mygdx.mdh;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.IntNode;
import com.mygdx.mdh.game.Test.Orto;
import com.mygdx.mdh.game.Test.User;
import com.mygdx.mdh.game.model.*;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.LOG;
import com.mygdx.mdh.screens.MainMenuScreen;
import com.mygdx.mdh.screens.ScreenManager;

import com.mygdx.mdh.screens.GameScreen;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;

import java.io.IOException;


public class MDHTacticsMain extends ScreenManager {
    private static final String TAG = MDHTacticsMain.class.getName();

    @Override
    public void create() {
        // Set LigGdx log level to DEBUG
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        LOG.setLevel(3);

        loadTest();
        if (1==1) return;

           // Load assets
        Assets.instance.init(new AssetManager());
        // Load preferences for audio settings and start playing music
        //GamePreferences.instance.load();
        Gdx.graphics.setWindowedMode(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);



        //AudioManager.instance.play(Assets.instance.music.song01);
        // Start game at menu screen
        ScreenTransition transition = ScreenTransitionFade.init(2.0f);
        setScreen(new GameScreen(this), transition);
        //setScreen(new MainMenuScreen(this), transition);
    }

    public void combatTest () {
        Combat testCombat = Combat.loadFromJSON("combat01");
        Character zubi = testCombat.getCharacters().get(0);
        Character hagen = testCombat.getCharacters().get(1);

        //Hit Hagen
        zubi.getAbilities().get(0).apply(hagen);

        //Activate shield
        zubi.getAbilities().get(1).apply(zubi);

        //Hit Zubi
        hagen.getAbilities().get(0).apply(zubi);

        //Hit Zubi
        hagen.getAbilities().get(0).apply(zubi);

        zubi.startTurn();

    }




    public void loadTest () {


        /*
        User u = new User();
        User u2 = u.loadFromJSON("test");

        System.out.println(u2.getOrto().getName());
        */
/*
        FileHandle file = Gdx.files.internal("core/assets/data/missions/test.txt");
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Map.class, new MapDeserializer());
        objectMapper.registerModule(module);

        JsonNode x=null;
        try { x*/


//Map m = new Map("map_C01M01");

        //Map.loadFromJSON("map_C01M01");
        //Mission m = Mission.loadFromJSON("test");
        /*
        Mission m = Mission.loadFromJSON("mission_C01M01");
        System.out.println(m.getMissionMap().getCell(1,1).getCellType());
        System.out.println(m.getBaddies().get(0).getName());
        */
        //Mission teschar= new Mission();
       // teschar.setMissionId("mission_C01M01");

        System.out.println(Game.loadFromJSON("DefaultGame").getGameCampaign().get(0).getCampaignMissions().get(1).getName());

    }
}