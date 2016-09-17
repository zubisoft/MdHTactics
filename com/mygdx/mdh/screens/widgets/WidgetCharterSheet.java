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

    Table mainLayout =  new Table();

    public void loadCharacter(Character c) {

        mainLayout.clear();

        mainLayout.setSize(430,600); //Default size
        mainLayout.align(Align.top);


        //mainLayout.add(new Container(new Image(Assets.instance.characters.get(c.getPic()).portrait)) ).size(50,50);

        Table characterInfo = new Table();
        //characterInfo.align(Align.center);
        characterInfo.setSize(430,75);

        characterInfo.add(new Label(c.getName(),Assets.uiSkin,"handwritten_black" )).expand().center().colspan(2).height(70);
        characterInfo.row();
        characterInfo.add(new Label("Level: "+c.getLevel(),Assets.uiSkin,"handwritten_black" )).align(Align.center).expand();
        characterInfo.add(new Label("Experience: "+c.getXp()+"/"+c.getNextLevelXP(),Assets.uiSkin,"handwritten_black" )).expand();

        mainLayout.add(characterInfo).size(430,120).colspan(2);


        //This is just a blank line
        mainLayout.add(new Label(" "+c.getLevel(),Assets.uiSkin,"handwritten_black" )).height(100);

        mainLayout.row();

        mainLayout.add(new Label("Attributes",Assets.uiSkin,"handwritten_black" ));
        mainLayout.add(new Label("Abilities",Assets.uiSkin,"handwritten_black" ));

        mainLayout.row();


        Table attributeBox = new Table();
        Image i =  new Image(Assets.instance.guiElements.get("menus/icon-attack"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(100,25);
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-actions"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(100,25);
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-defence"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(100,25);
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-movement"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(100,25);
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-health"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).size(100,25);
        attributeBox.row();

        mainLayout.add(attributeBox).padLeft(50).width(170);

        Table abilityBox = new Table();
        for (Ability a: c.getAbilities()) {
            abilityBox.add(abilityIconTextBox(a, new Image(Assets.instance.abilities.get(a.getPic())),""+a.getName())).size(50,25);
            abilityBox.row();
        }

        mainLayout.add(abilityBox).expand();





    }

    public WidgetCharterSheet(Character c) {
        this.setFillParent(true);
        this.align(Align.topLeft);
        loadCharacter(c);
        this.add(mainLayout).width(430);
        this.invalidate();
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
        t.add(new TextArea(text,Assets.uiSkin,"handwritten_black" )).size(150,25).padLeft(25).align(Align.bottom);
        i.addListener(new AbilityClickListener(a));
        return t;
    }


}
