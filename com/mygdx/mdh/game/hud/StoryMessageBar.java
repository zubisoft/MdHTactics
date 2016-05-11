package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.mdh.game.model.StoryText;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;

/**
 * Created by zubisoft on 25/03/2016.
 */
public class StoryMessageBar extends Actor {



    Table layout;

    TextArea la;
    Image portraitImage;




    boolean waitToShow = false;


    Queue<StoryText> story;


    public StoryMessageBar() {

        this.setColor(new Color(0xd4be9000));
        this.setVisible(false);

        //TODO: this needs a proper layout

        Stack textbox = new Stack();

        Image background = new Image( Assets.instance.guiElements.get("message_bar_bg" ));
        background.setColor(new Color(0xd4be90FF));
        background.setSize(Constants.VIEWPORT_GUI_WIDTH,200);
        background.setPosition(0,0);


        la =(new TextArea("", Assets.uiSkin, "gradient"));
        la.setSize(Constants.VIEWPORT_GUI_WIDTH,180);
        la.setPosition(0,0);
        la.setAlignment(Align.center);
        Table c = new Table();
        c.setSize(Constants.VIEWPORT_GUI_WIDTH,200);
        c.add(la).width(Constants.VIEWPORT_GUI_WIDTH);
        //c.pad(25);


        textbox.setSize(Constants.VIEWPORT_GUI_WIDTH,200);
        textbox.add(background);
        textbox.add(c);


        //portrait.align(Align.topLeft);
        portraitImage=new Image();
        portraitImage.scaleBy(1.0f);

        layout= new Table();
        layout.setPosition(0,Constants.VIEWPORT_GUI_HEIGHT/2-200);
        layout.setSize(Constants.VIEWPORT_GUI_WIDTH,400);
        layout.add(portraitImage).align(Align.bottomLeft);
        layout.row();
        layout.add(textbox).width(Constants.VIEWPORT_GUI_WIDTH);



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
        this.setVisible(true);
    }

    public void hide ()  {
        if(this.hasActions()) {
            layout.addAction(Actions.alpha(1.0f));
        }

        layout.addAction(Actions.sequence(
                Actions.alpha(0f,0.5f, Interpolation.fade),
                Actions.alpha(0)
                ));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (waitToShow && !layout.hasActions()) {
            portraitImage.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.characters.get(story.first().character.getPic()).portrait)));
            la.setText(story.removeFirst().text);

            layout.addAction(Actions.sequence(
                    Actions.alpha(0),
                    Actions.alpha(0.8f,1.0f, Interpolation.fade)


            ));

            waitToShow = false;
        }

        layout.act(delta);


    }

    @Override
    public void draw (Batch batch, float parentAlpha) {


        if(isVisible()) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a);
            layout.draw(batch, 1.0f);
            batch.setColor(1f, 1f, 1f, 1f);
        }


    }
    //Get the current color before drawing (Useful to allow animating the color from actions)





}
