package com.mygdx.mdh.screens;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.mdh.game.util.Assets;

public abstract class AbstractGameScreen implements Screen {
    protected ScreenManager gameScreen;

    public AbstractGameScreen(ScreenManager game) {
        this.gameScreen = game;
    }

    public abstract void render(float delta);

    public abstract void resize(int width, int height);

    public abstract void show();

    public abstract void hide();

    public abstract void pause();

    public abstract InputProcessor getInputProcessor ();

    @Override
    public void resume() {
        Assets.instance.init(new AssetManager());
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }

}