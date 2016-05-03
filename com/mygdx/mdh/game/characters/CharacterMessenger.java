package com.mygdx.mdh.game.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
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
    List<ImageButton> icons;
    List<ImageButton> iconsAux;

    CharacterActor actor;

    public CharacterMessenger (CharacterActor actor ) {
        this.actor=actor;
        messages = new ArrayList<>();
        messagesAux= new ArrayList<>();

        icons = new ArrayList<>();
        iconsAux= new ArrayList<>();
    }







    /**
     * Adds an message to the actor that will displayed for a short time.
     * Multiple messages can be displayed at the same time.
     * @param message
     */
    public void showMessage (String iconName, String message, Color c) {


        Label la=(new Label(message, Assets.uiSkin, "text-font", Color.WHITE));
        la.setColor(c);
        la.setPosition(actor.getX()+actor.offsetx,actor.getY()+actor.getHeight()-messages.size()*15);

        messages.add(la);


        LOG.print("[Messenger] LA hash: "+la.hashCode());
        la.addAction(Actions.sequence(
                Actions.moveTo(actor.getX()+actor.offsetx, actor.getY()+actor.getHeight()+50-messages.size()*15,1, Interpolation.exp5Out)
                ,Actions.delay(1)
                ,Actions.alpha(0,2,Interpolation.fade)
                ,new RemoveFromListAction(messages )
        ));

        //TODO put label and image together in a minitable - also fix z indices
        ImageButton icon = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.effects.get("icons/"+iconName))));
        icon.setSize(20,20);
        icon.setZIndex(20);
        icon.setPosition(actor.getX()+actor.offsetx-22,actor.getY()+actor.getHeight()-messages.size()*15);
        icons.add(icon);

        icon.addAction(Actions.sequence(
                Actions.moveTo(actor.getX()+actor.offsetx-22, actor.getY()+actor.getHeight()+50-messages.size()*15,1, Interpolation.exp5Out)
                ,Actions.delay(1)
                ,Actions.alpha(0,2,Interpolation.fade)
                ,new RemoveFromListAction(icons )
        ));
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
                Actions.moveTo(actor.getX()+actor.offsetx, actor.getY()+actor.getHeight()+50-messages.size()*15,1, Interpolation.exp5Out)
                ,Actions.delay(1)
                ,Actions.alpha(0,2,Interpolation.fade)
                ,new RemoveFromListAction(messages )
        ));
    }

    public void showMessage (String message) {
        showMessage (message, Color.WHITE);
    }

    public void update (float deltaTime) {
        //Update character messages
        messagesAux.clear();
        messagesAux.addAll(messages);

        Iterator<Label> iterator = messagesAux.iterator();
        while(iterator.hasNext()) iterator.next().act(deltaTime);

        iconsAux.clear();
        iconsAux.addAll(icons);

        Iterator<ImageButton> iterator2 = iconsAux.iterator();
        while(iterator2.hasNext()) iterator2.next().act(deltaTime);

    }


    public void draw (SpriteBatch batch) {

        for(Label l: messages) l.draw(batch,1);
        for(ImageButton i: icons) i.draw(batch,1);



    }
}
