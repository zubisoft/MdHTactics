package com.mygdx.mdh.game.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by zubisoft on 24/04/2016.
 */
public class CharacterMessenger extends Actor {


    class RemoveFromListAction extends Action {

        List list;
        Object item;

        public RemoveFromListAction(List l) {

            this.list = l;

        }

        public boolean act (float deltaTime) {

            list.remove(0);

            return true;
        }
    }

    List<Label> messages;
    List<Label> messagesAux;
    CharacterActor actor;

    public CharacterMessenger (CharacterActor actor ) {
        this.actor=actor;
        messages = new ArrayList<>();
        messagesAux= new ArrayList<>();
    }
    /**
     * Adds an message to the actor that will displayed for a short time.
     * Multiple messages can be displayed at the same time.
     * @param message
     */
    public void showMessage (String message, Color c) {
        Label la=(new Label(message, Assets.uiSkin, "text-font", Color.WHITE));

        la.setColor(c);

        la.setPosition(actor.getX()+actor.offsetx,actor.getY()+actor.getHeight()-messages.size()*15);

        messages.add(la);

        LOG.print("[Messenger] LA hash: "+la.hashCode());
        la.addAction(Actions.sequence(
                Actions.moveTo(actor.getX()+actor.offsetx, actor.getY()+actor.getHeight()+50-messages.size()*15,1000, Interpolation.exp5Out)
                ,Actions.delay(200)
                ,Actions.alpha(0,2000,Interpolation.fade)
                ,new RemoveFromListAction(messages )
        ));
    }

    public void showMessage (String message) {
        showMessage (message, Color.WHITE);
    }

    public void update (float stateTime) {
        //Update character messages
        messagesAux.clear();
        messagesAux.addAll(messages);

        Iterator<Label> iterator = messagesAux.iterator();
        while(iterator.hasNext()) iterator.next().act(stateTime);

    }


    public void draw (SpriteBatch batch) {
        for(Label l: messages) l.draw(batch,1);
    }
}
