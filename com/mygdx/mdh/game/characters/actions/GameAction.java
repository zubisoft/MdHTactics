package com.mygdx.mdh.game.characters.actions;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by zubisoft on 24/03/2016.
 */
public abstract class GameAction {
    /** The actor this action is attached to, or null if it is not attached. */
    protected Actor actor;

    public void setActor (Actor actor) {
        this.actor = actor;
    }

    public abstract boolean act (float delta);
}
