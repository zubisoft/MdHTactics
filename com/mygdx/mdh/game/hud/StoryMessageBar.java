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
import com.badlogic.gdx.utils.Queue;
import com.mygdx.mdh.game.model.StoryText;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;

/**
 * Created by zubisoft on 25/03/2016.
 */
public class StoryMessageBar extends Actor {

    TextureRegion messageBarBG;

    Label la;
    Table t;

    CombatHUD combatHUD;

    boolean waitToShow = false;
    int duration;

    Queue<StoryText> story;


    public StoryMessageBar() {
        messageBarBG = Assets.instance.guiElements.get("message_bar_bg");

        this.setBounds(0,Constants.VIEWPORT_GUI_HEIGHT/2-100,Constants.VIEWPORT_GUI_WIDTH,200);
        this.setColor(new Color(0xd4be9000));

        t = new Table();
        t.setBounds(0,Constants.VIEWPORT_GUI_HEIGHT/2-100,Constants.VIEWPORT_GUI_WIDTH,200);
        t.align(Align.center);

        //TODO: this needs a proper layout
        la =(new Label("", Assets.uiSkin, "gradient-font", Color.WHITE));
        t.add(la);

        this.story = new Queue<>();

    }

    public boolean hasMoreMessages () {
        return story.size>=1;
    }


    public void addMessage( StoryText storyText) {

            story.addLast(storyText);
    }


    public void show ()  {



        waitToShow = true;


    }

    public void hide ()  {
        if(this.hasActions()) {
            this.addAction(Actions.alpha(1.0f));
        }

        this.addAction(Actions.sequence(
                Actions.alpha(0f,1.0f, Interpolation.fade),
                Actions.alpha(0)
                ));
        la.addAction(Actions.sequence(
                Actions.alpha(0f,1.0f, Interpolation.fade),
                Actions.alpha(0)
                ));

    }






    @Override
    public void act(float delta) {
        super.act(delta);

        if (waitToShow && !this.hasActions()) {
            la.setText(story.removeFirst().text);

            this.addAction(Actions.sequence(
                    Actions.delay(1.0f),
                    Actions.alpha(0),
                    Actions.alpha(0.8f,2.0f, Interpolation.fade)


            ));
            la.addAction(Actions.sequence(
                    Actions.delay(1.0f),
                    Actions.alpha(0),
                    Actions.alpha(1,2.0f, Interpolation.fade)
            ));


            waitToShow = false;
        }

        t.act(delta);


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
