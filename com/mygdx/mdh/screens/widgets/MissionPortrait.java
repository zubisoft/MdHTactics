package com.mygdx.mdh.screens.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    Image portraitFrame =new Image(Assets.instance.guiElements.get("menus/charselection_portrait_frame"));
    public Image portrait;

    public Group getPicWithStars () {
        Group group = new Group();
        group.addActor(portraitFrame);
        Table t = new Table();
        t.setSize(125,25);
        t.padLeft(15);
        Actor a;
        for (int i = 0; i<5; i++) {
            a = new Image(Assets.instance.guiElements.get("menus/icon_d20_bordered"));
            a.setSize(25,25);

            a.setColor(Color.LIGHT_GRAY);
            if(mission!=null && i<mission.getCurrentStars()) a.setColor(Color.GOLD);
            t.add(a);
        }
        group.addActor(t);

        return group;
    }

    public MissionPortrait(Mission mission) {

        super();
        this.mission = mission;

        this.setSize(140,140);
        this.add(  new Image(Assets.instance.guiElements.get("menus/charselection_portrait")));

        if (mission != null) {
            System.out.println("icons/"+mission.getMissionMap().getCell(1,1));
            portrait = new Image(Assets.instance.maps.get("icons/"+mission.getMissionMap().getMapId()));

            portrait.setSize(140,140);
            portrait.scaleBy(0.002f);
            Container c = new Container(portrait);
            //c.padLeft(25);

            c.size(140,140);
            c.align(Align.center);
            this.add(c);
        }

        this.add( getPicWithStars() );

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
