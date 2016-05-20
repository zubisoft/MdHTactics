package com.mygdx.mdh.screens.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Mission;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 19/05/2016.
 */
public class MissionPortrait extends Stack {


    Mission mission;

    boolean selected;
    Image portraitFrame = new Image(Assets.instance.guiElements.get("charselection_portrait_frame"));

    public MissionPortrait(Mission mission) {

        super();
        this.mission = mission;

        this.setSize(140,140);
        this.add( new Image(Assets.instance.guiElements.get("charselection_portrait")));

        if (mission != null) {

            Image i = new Image(Assets.instance.guiElements.get("missionselection_minimap"));
            i.setSize(140,140);
            i.scaleBy(0.002f);
            Container c = new Container(i);
            //c.padLeft(25);

            c.size(140,140);
            c.align(Align.center);
            this.add(c);
        }

        this.add( portraitFrame );

    }

    public void setSelected(boolean selected) {
        if (selected) portraitFrame.setColor(new com.badlogic.gdx.graphics.Color(1.0f, 0.84313726f, 0.0f, 1));
        else  portraitFrame.setColor(new com.badlogic.gdx.graphics.Color(1f, 1f, 1f, 1));

        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

}
