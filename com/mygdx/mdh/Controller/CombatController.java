package com.mygdx.mdh.Controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.mdh.MDHTactics;

/**
 * Created by zubisoft on 27/01/2016.
 */
public class CombatController implements InputProcessor {
    MDHTactics game;

    public CombatController (MDHTactics game) {
        this.game=game;
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            game.getCamera().translate(-32,0);
        if(keycode == Input.Keys.RIGHT)
            game.getCamera().translate(32,0);
        if(keycode == Input.Keys.UP)
            game.getCamera().translate(0,-32);
        if(keycode == Input.Keys.DOWN)
            game.getCamera().translate(0,32);
        if(keycode == Input.Keys.NUM_1)
            game.getTiledMap().getLayers().get(0).setVisible(!game.getTiledMap().getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            game.getTiledMap().getLayers().get(1).setVisible(!game.getTiledMap().getLayers().get(1).isVisible());
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
