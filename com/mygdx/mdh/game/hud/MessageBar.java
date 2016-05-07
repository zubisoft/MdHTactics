package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 25/03/2016.
 */
public class MessageBar extends Actor {

    TextureRegion messageBarBG;

    Label la;
    Table t;

    CombatHUD combatHUD;

    boolean waitToShow = false;
    int duration;


    public MessageBar () {
        messageBarBG = Assets.instance.guiElements.get("message_bar_bg");

        this.setBounds(0,Constants.VIEWPORT_GUI_HEIGHT/2-100,Constants.VIEWPORT_GUI_WIDTH,200);
        this.setColor(new Color(0xd4be9000));

        t = new Table();
        t.setBounds(0,Constants.VIEWPORT_GUI_HEIGHT/2-100,Constants.VIEWPORT_GUI_WIDTH,200);
        t.align(Align.center);

        //TODO: this needs a proper layout
        la =(new Label("", Assets.uiSkin, "gradient-font", Color.WHITE));
        t.add(la);

    }

    public MessageBar (CombatHUD combatHUD) {
        messageBarBG = Assets.instance.guiElements.get("message_bar_bg");

        this.setBounds(0,Constants.VIEWPORT_GUI_HEIGHT/2-100,Constants.VIEWPORT_GUI_WIDTH,200);
        this.setColor(new Color(0xd4be9000));

        t = new Table();
        t.setBounds(0,Constants.VIEWPORT_GUI_HEIGHT/2-100,Constants.VIEWPORT_GUI_WIDTH,200);
        t.align(Align.center);

        //TODO: this needs a proper layout
        la =(new Label("", Assets.uiSkin, "gradient-font", Color.WHITE));
        t.add(la);

        this.combatHUD = combatHUD;




        /*
        la.setY(Constants.VIEWPORT_GUI_HEIGHT/2);
        la.setX(Constants.VIEWPORT_GUI_WIDTH/2-la.getWidth()/2);
        */

    }

    public void setMessage( String m) {
            la.setText(m);
    }

    public void show ()  {

            show(0);

    }

    public void show (int duration)  {
        waitToShow=true;
        this.duration = duration;

        combatHUD.controller.setCombatPaused(true);


    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (waitToShow) {


            this.addAction(Actions.sequence(
                    Actions.alpha(0),
                    Actions.alpha(0.8f,2.0f, Interpolation.fade),
                    Actions.delay(duration),
                    Actions.alpha(0f,2.0f, Interpolation.fade),
                    Actions.delay(0.5f)

            ));
            la.addAction(Actions.sequence(
                    Actions.alpha(0),
                    Actions.alpha(1,2.0f, Interpolation.fade),
                    Actions.delay(duration),
                    Actions.alpha(0f,2.0f, Interpolation.fade),
                    Actions.alpha(0),
                    Actions.delay(0.5f)
            ));
            waitToShow=false;
        }

        t.act(delta);

        if (!this.hasActions())  {
            combatHUD.controller.setCombatPaused(false);

        }
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {

        if(isVisible()) {

            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a);
            batch.draw(messageBarBG, getX(), getY(),getWidth(),getHeight());
            t.draw(batch,1.0f);
            batch.setColor(1f, 1f, 1f, 1f);
        }
    }
    //Get the current color before drawing (Useful to allow animating the color from actions)





}
