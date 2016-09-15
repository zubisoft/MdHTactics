package com.mygdx.mdh.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by zubisoft on 24/04/2016.
 */
public class CharacterMessenger extends Group {


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

        Label la=(new Label(message, Assets.uiSkin, "font15", Color.WHITE));

        la.setColor(c);
        la.setPosition(actor.getX()+actor.offsetx,actor.getY()+actor.getHeight()-messages.size()*15);

        messages.add(la);



/*
        la.addAction(Actions.sequence(
                Actions.moveTo(actor.getX()+actor.offsetx, actor.getY()+actor.getHeight()+50-messages.size()*15,1, Interpolation.exp5Out)
                ,Actions.delay(1)
                ,Actions.alpha(0,2,Interpolation.fade)
                ,new RemoveFromListAction(messages )
        ));
        */

        //TODO probably  we can place this logic in a better place
        ImageButton icon;
        if (Assets.instance.effects.get("icons/"+iconName) != null) {
            icon = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.effects.get("icons/" + iconName))));
        } else {
            icon = new ImageButton(new SpriteDrawable(new Sprite(Assets.instance.abilities.get( iconName))));
        }
        icon.setSize(20,20);
        icon.setZIndex(20);
        icon.setPosition(actor.getX()+actor.offsetx-22,actor.getY()+actor.getHeight()-messages.size()*15);
        icons.add(icon);

/*

        icon.addAction(Actions.sequence(
                Actions.moveTo(actor.getX()+actor.offsetx-22, actor.getY()+actor.getHeight()+50-messages.size()*15,1, Interpolation.exp5Out)
                ,Actions.delay(1)
                ,Actions.alpha(0,2,Interpolation.fade)
                ,new RemoveFromListAction(icons )
        ));*/

        Table layout = new Table();
        layout.setZIndex(1000);
        layout.add(icon).size(20,20);
        layout.add(la).height(20).padLeft(5);

        layout.setPosition(actor.getX()+actor.offsetx,actor.getY()+actor.getHeight()-messages.size()*15);
        this.addActor(layout);
        layout.addAction(Actions.sequence(
                Actions.moveTo(actor.getX()+actor.offsetx, actor.getY()+actor.getHeight()+50-messages.size()*15,1, Interpolation.exp5Out)
                ,Actions.delay(1)
                ,Actions.alpha(0,2,Interpolation.fade)
                ,new RemoveFromListAction(icons )
                ,new RemoveFromListAction(messages )
        ));

    }


    /**
     * Adds an message to the actor that will displayed for a short time.
     * Multiple messages can be displayed at the same time.
     * @param message
     */
    public void showMessage (String message, Color c) {
        Label la=(new Label(message, Assets.uiSkin, "font15", Color.WHITE));

        la.setColor(c);

        la.setPosition(actor.getX()+actor.offsetx,actor.getY()+actor.getHeight()-messages.size()*15);

        messages.add(la);

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
        /*
        messagesAux.clear();
        messagesAux.addAll(messages);

        Iterator<Label> iterator = messagesAux.iterator();
        while(iterator.hasNext()) iterator.next().act(deltaTime);

        iconsAux.clear();
        iconsAux.addAll(icons);

        Iterator<ImageButton> iterator2 = iconsAux.iterator();
        while(iterator2.hasNext()) iterator2.next().act(deltaTime);
        */
        this.act(deltaTime);

    }

/*
    public void draw (SpriteBatch batch) {

        for(Label l: messages) l.draw(batch,1);
        for(ImageButton i: icons) i.draw(batch,1);



    }*/
}
