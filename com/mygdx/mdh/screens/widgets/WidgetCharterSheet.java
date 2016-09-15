package com.mygdx.mdh.screens.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.model.Character;

/**
 * Created by zubisoft on 05/09/2016.
 */
public class WidgetCharterSheet extends Table {

    class AbilityClickListener extends ClickListener {
        Ability ability;

        public AbilityClickListener(Ability ability) {
            this.ability = ability;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);
            addWidget(new WidgetAbilitySheet(ability));
            return true;
        }
    }

    public void addWidget (Table t) {
        addActor(t);
    }

    Stack s = new Stack();

    public void loadCharacter(Character c) {

        s.clear();
        s.setSize(400,60);

        Table mainLayout = new Table();

        mainLayout.setSize(400,600); //Default size
        mainLayout.align(Align.top);
        mainLayout.padLeft(50);


        //mainLayout.add(new Container(new Image(Assets.instance.characters.get(c.getPic()).portrait)) ).size(50,50);

        Table characterInfo = new Table();
        characterInfo.setSize(350,100);
        characterInfo.align(Align.topLeft);
        characterInfo.add(new Label(c.getName(),Assets.uiSkin,"handwritten_black" )).padLeft(100).align(Align.top).colspan(2).height(20);
        characterInfo.row();
        characterInfo.add(new Label("Level: "+c.getLevel(),Assets.uiSkin,"handwritten_black" )).align(Align.left);
        //characterInfo.row();
        characterInfo.add(new Label("Experience: "+c.getXp()+"/"+c.getNextLevelXP(),Assets.uiSkin,"handwritten_black" )).align(Align.left);

        mainLayout.add(characterInfo).colspan(2).align(Align.topLeft);
/*
        //This is just a blank line
        mainLayout.add(new Label(" "+c.getLevel(),Assets.uiSkin,"handwritten_black" )).height(100);

        mainLayout.row();

        mainLayout.add(new Label("Attributes",Assets.uiSkin,"handwritten_black" ));
        mainLayout.add(new Label("Abilities",Assets.uiSkin,"handwritten_black" ));

        mainLayout.row();


        Table attributeBox = new Table();
        Image i =  new Image(Assets.instance.guiElements.get("menus/icon-attack"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(200,25);
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-actions"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(200,25);
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-defence"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(200,25);
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-movement"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(200,25);
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-health"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(200,25);
        attributeBox.row();

        mainLayout.add(attributeBox);

        Table abilityBox = new Table();
        for (Ability a: c.getAbilities()) {
            abilityBox.add(abilityIconTextBox(a, new Image(Assets.instance.abilities.get(a.getPic())),""+a.getName())).size(200,25);
            abilityBox.row();
        }

        mainLayout.add(abilityBox);
*/
        s.add(mainLayout);

        this.add(s);

    }

    public WidgetCharterSheet(Character c) {

        loadCharacter(c);
    }



    public Table horizontalIconTextBox (Image i, String text) {
        Table t = new Table();
        t.add(i).size(25,25);
        t.add(new TextArea(text,Assets.uiSkin,"handwritten_black" )).size(175,25).padLeft(25);

        return t;
    }


    public Table  abilityIconTextBox (Ability a, Image i, String text) {
        Table t = new Table();
        t.add(i).size(25,25);
        t.add(new TextArea(text,Assets.uiSkin,"handwritten_black" )).size(175,25).padLeft(25).align(Align.bottom);
        i.addListener(new AbilityClickListener(a));
        return t;
    }


}
