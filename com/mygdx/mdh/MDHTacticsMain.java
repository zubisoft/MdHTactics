package com.mygdx.mdh;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.LOG;
import com.mygdx.mdh.screens.MainMenuScreen;
import com.mygdx.mdh.screens.ScreenManager;

import com.mygdx.mdh.screens.GameScreen;
import com.mygdx.mdh.screens.Transitions.ScreenTransition;
import com.mygdx.mdh.screens.Transitions.ScreenTransitionFade;


public class MDHTacticsMain extends ScreenManager {
    private static final String TAG = MDHTacticsMain.class.getName();

    @Override
    public void create() {
        // Set LigGdx log level to DEBUG
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        LOG.setLevel(3);

           // Load assets
        Assets.instance.init(new AssetManager());
        // Load preferences for audio settings and start playing music
        //GamePreferences.instance.load();
        Gdx.graphics.setWindowedMode(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);

        //AudioManager.instance.play(Assets.instance.music.song01);
        // Start game at menu screen
        ScreenTransition transition = ScreenTransitionFade.init(2.0f);
        //setScreen(new GameScreen(this), transition);
        setScreen(new MainMenuScreen(this), transition);
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
}