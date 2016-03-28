package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;

/**
 * Created by zubisoft on 25/03/2016.
 */
public class MessageBar extends Actor {

    TextureRegion messageBarBG;

    Label la;


    public MessageBar () {
        messageBarBG = Assets.instance.guiElements.get("message_bar_bg");
        setVisible(false);
        this.setBounds(0,Constants.VIEWPORT_GUI_HEIGHT/2-100,Constants.VIEWPORT_GUI_WIDTH,200);
        this.setColor(new Color(0xd4be90ff));

        //TODO: this needs a proper layout
        la =(new Label("", Assets.uiSkin, "default-font", Color.BROWN));
        la.setFontScale(3.f);

        la.setY(Constants.VIEWPORT_GUI_HEIGHT/2);
        la.setX(Constants.VIEWPORT_GUI_WIDTH/2-la.getWidth()/2);

    }

    public void setMessage( String m) {
            la.setText(m);
    }

    public void show ()  {
        this.setVisible(true);
        this.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(0.8f,2.0f)
        ));
        la.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1,2.0f)
        ));
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {

        if(isVisible()) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a);
            batch.draw(messageBarBG, getX(), getY(),getWidth(),getHeight());
            la.draw(batch,1.0f);
            batch.setColor(1f, 1f, 1f, 1f);
        }
    }
    //Get the current color before drawing (Useful to allow animating the color from actions)





}
