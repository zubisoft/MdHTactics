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
import com.mygdx.mdh.game.Test.EffectTest;
import com.mygdx.mdh.game.Test.Orto;
import com.mygdx.mdh.game.Test.User;
import com.mygdx.mdh.game.model.*;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.effects.DamageEffect;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.LOG;
import com.mygdx.mdh.screens.*;

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


        //new EffectTest();
        //if (1==1) return;

        //loadTest();
        //if (1==1) return;

           // Load assets
        Assets.instance.init(new AssetManager());
        // Load preferences for audio settings and start playing music
        //GamePreferences.instance.load();
        Gdx.graphics.setWindowedMode(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);


        loadTest ();

        //AudioManager.instance.play(Assets.instance.music.song01);
        // Start gameScreen at menu screen
        ScreenTransition transition = ScreenTransitionFade.init(1.0f);
        //setScreen(new CharSelectionScreen(this), transition);
        //setScreen(new CombatScreen(this), transition);

        setScreen(new MainMenuScreen(this), transition);
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

        //System.out.println(Game.loadFromJSON("DefaultGame").getGameCampaign().get(0).getCampaignMissions().get(1).getName());

        /*
        Character c1 = Character.loadFromJSON("zubi_filmmaker");
        Character c2 = Character.loadFromJSON("cultist");

        LOG.print(3, "Ability:" +c1.getAbilities().get(0), LOG.ANSI_GREEN);
        LOG.print(3, "Before:"+ c2.toString(), LOG.ANSI_GREEN);
        c1.getAbilities().get(0).apply(c2);
        LOG.print(3, "After:" +c2.toString(), LOG.ANSI_BLUE);


        LOG.print(3, "Ability:" +c1.getAbilities().get(0), LOG.ANSI_GREEN);
        LOG.print(3, "Before:"+ c2.toString(), LOG.ANSI_GREEN);
        c2.startTurn();
        LOG.print(3, "After:" +c2.toString(), LOG.ANSI_BLUE);



*/
        LOG.setLevel(2);

        testShields();
        testDamageEffects();
        testStunEffects();
        testAttributeModifier();
        testRemover();





    }

    public void testDamageEffects () {
        Character c1 = Character.loadFromJSON("zubi_filmmaker");
        Character c2 = Character.loadFromJSON("cultist");

        int[] damages = new int[1000];

        c2.setMaxHealth(100);
        for (int i=0; i<1000; i++) {
            c2.setHealth(100);
            c2.setDefence(1);

            c1.getAbilities().get(0).apply(c2);

            damages[i] = 100-c2.getHealth();
        }


        //On average we should see 6.75
        int test = 0;
        for (int i=0; i<1000; i++) {

            test+=damages[i];
        }

        if (test/1000.0f > 5.75 && test/1000.0f < 7.75)
            LOG.print(1, "[OK] Damage - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_RED);

    }


    public void testStunEffects () {
        Character c1 = Character.loadFromJSON("zubi_filmmaker");
        Character c2 = Character.loadFromJSON("cultist");

        int[] stuns = new int[1000];

        c2.setMaxHealth(100);
        c1.setAttack(20);

        for (int i=0; i<1000; i++) {
            c2.setHealth(c2.getMaxHealth());
            c2.setAvailableActions(2);
            c2.getEffects().clear();

            c1.getAbilities().get(0).apply(c2);
            c2.startTurn();

            stuns[i] = 2-c2.getAvailableActions();
        }


        //On average we should see 6.75
        int test = 0;
        for (int i=0; i<1000; i++) {

            test+=stuns[i];
        }


        if (test/1000.0f > 0.23 && test/1000.0f < 0.27)
            LOG.print(1, "[OK] Stun - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Stun - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_RED);


    }

    public void testShields()  {
        Character c1 = Character.loadFromJSON("zubi_starfleet");
        Character c2 = Character.loadFromJSON("test_dummy");

        int[] results = new int[1000];

        for (int i=0; i<1000; i++) {
            c1.setHealth(c1.getMaxHealth());
            c1.getEffects().clear();
            results[i] = c1.getHealth();

            c1.getAbilities().get(1).apply(c1); //shield
            c2.getAbilities().get(0).apply(c1); //attack
            results[i] -= c1.getHealth();


        }

        int total = 0;
        for (int i=0; i<1000; i++) {
            total += results[i];

        }

        if (total/1000f >= 5 && total/1000f <= 6)
        LOG.print(1, "[OK] Shields - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
        LOG.print(1, "[Error] Shields - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_RED);
    }


    public void testAttributeModifier()  {
        Character c1 = Character.loadFromJSON("zubi_filmmaker");
        Character c2 = Character.loadFromJSON("test_dummy");

        c1.setDefence(0);

        int[] results = new int[1000];

        c1.getAbilities().get(1).apply(c2); //apply attribute reduction

        if (c2.getMovement() == 1)
            LOG.print(2, "[OK] Attribute Mod (Movement) - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(2, "[OK] Attribute Mod (Movement)  - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_RED);

        for (int i=0; i<1000; i++) {
            c1.setHealth(c1.getMaxHealth());
            results[i] = c1.getHealth();
            c2.getAbilities().get(1).apply(c1); //attack
            results[i] -= c1.getHealth();
        }

        int total = 0;
        for (int i=0; i<1000; i++) {
            total += results[i];
        }

        //Without the modifier, should be 50% chance of 10
        if (total/1000f >= 2.5 && total/1000f <= 3.5)
        LOG.print(2, "[OK] Attribute Mod (Attack) - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
        LOG.print(2, "[OK] Attribute Mod (Attack) - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_RED);
    }


    public void testRemover()  {
        Character c1 = Character.loadFromJSON("zubi_starfleet");
        Character c2 = Character.loadFromJSON("cultist");
        Character c3 = Character.loadFromJSON("doomsayer");
        c1.setLevel(10);


        c1.getEffects().clear();
        c1.getAbilities().get(1).apply(c1); //buff
        c2.getAbilities().get(1).apply(c1); //debuff
        c3.getAbilities().get(1).apply(c1); //debuff
        c1.getAbilities().get(2).apply(c1); //remover


        if (c1.getEffects().size() == 1)
            LOG.print(2, "[OK] Remover - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(2, "[OK] Remover - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_RED);
    }


}

