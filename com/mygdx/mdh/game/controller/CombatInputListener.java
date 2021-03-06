package com.mygdx.mdh.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.model.Character;

/**
 * Created by zubisoft on 18/03/2016.
 */
public class CombatInputListener extends InputAdapter {

    Vector2 lastTouch = new Vector2();
    CombatController combatController;

    public  CombatInputListener (CombatController c) {
        combatController=c;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouch.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 newTouch = new Vector2(screenX, screenY);
        // delta will now hold the difference between the last and the current touch positions
        // delta.x > 0 means the touch moved to the right, delta.x < 0 means a move to the left
        Vector2 delta = newTouch.cpy().sub(lastTouch);
        lastTouch = newTouch;

        combatController.cameraManager.move(-(int)delta.x, (int)delta.y);

        return false;
    }



    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            combatController.cameraManager.move(-32,0);
        if(keycode == Input.Keys.RIGHT)
            combatController.cameraManager.move(32,0);
        if(keycode == Input.Keys.UP)
            combatController.cameraManager.move(0,-32);
        if(keycode == Input.Keys.DOWN)
            combatController.cameraManager.move(0,32);
        if(keycode == Input.Keys.NUM_1);
        //gameScreen.getTiledMap().getLayers().get(0).setVisible(!gameScreen.getTiledMap().getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2);
        //gameScreen.getTiledMap().getLayers().get(1).setVisible(!gameScreen.getTiledMap().getLayers().get(1).isVisible());

        if(keycode==Input.Keys.F9) {
            for (Character c : CombatController.combat.getCharacters()) {
                if (!c.isFriendly()) c.setHealth(0);
            }
        }


        if(keycode==Input.Keys.F10) {
            for (Character c : CombatController.combat.getCharacters()) {
                c.setAvailableActions(0);
            }
        }

        if(keycode==Input.Keys.F11) {
            for (Character c : CombatController.combat.getCharacters()) {
                if (c.isFriendly()) {
                    //c.setAvailableActions(5);
                    //c.setMovement(10);
                    c.setLevel(5);
                }
            }
        }


        return false;
    }

}
