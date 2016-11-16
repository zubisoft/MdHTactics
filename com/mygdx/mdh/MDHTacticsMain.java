package com.mygdx.mdh;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.LOG;
import com.mygdx.mdh.screens.MainMenuScreen;
import com.mygdx.mdh.screens.ScreenManager;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;


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


        //EffectTester.runTests();

        //AudioManager.instance.play(Assets.instance.music.song01);
        // Start gameScreen at menu screen
        ScreenTransition transition = ScreenTransitionFade.init(1.0f);
        //setScreen(new CharSelectionScreen(this), transition);
        //setScreen(new CombatScreen(this), transition);

        setScreen(new MainMenuScreen(this), transition);
    }







    }

