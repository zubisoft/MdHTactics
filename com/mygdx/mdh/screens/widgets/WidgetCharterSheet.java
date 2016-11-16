package com.mygdx.mdh.screens.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.util.Assets;

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

        characterInfo.add(new Label(c.getName(),Assets.uiSkin,"handwritten_black" )).expand().center().colspan(2).height(50);
        characterInfo.row();
        characterInfo.add(new Label("Level: "+c.getLevel(),Assets.uiSkin,"handwritten_black" )).align(Align.center).expand();
        characterInfo.add(new Label("Experience: "+c.getXp()+"/"+c.getNextLevelXP(),Assets.uiSkin,"handwritten_black" )).expand();

        mainLayout.add(characterInfo).size(430,120).colspan(2);


        //This is just a blank line
        mainLayout.add(new Label(" "+c.getLevel(),Assets.uiSkin,"handwritten_black" )).height(100);

        mainLayout.row();

        mainLayout.add(new Label("Attributes",Assets.uiSkin,"handwritten_black" )).width(100).right();
        mainLayout.add(new Label("Abilities",Assets.uiSkin,"handwritten_black" )).expandX();

        mainLayout.row();


        Table attributeBox = new Table();
        attributeBox.center();
        Image i =  new Image(Assets.instance.guiElements.get("menus/icon-attack"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).expandX().fillX();
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-actions"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).expandX().fillX();
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-defence"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).expandX().fillX();
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-movement"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).expandX().fillX();
        attributeBox.row();
        i =  new Image(Assets.instance.guiElements.get("menus/icon-health"));
        i.setColor(Color.BROWN);
        attributeBox.add(horizontalIconTextBox(i,""+c.getAttack())).expandX().fillX();
        attributeBox.row();

        mainLayout.add(attributeBox).width(100);


        Table abilityBox = new Table();
        abilityBox.top().left();
        for (Ability a: c.getAbilities()) {
            abilityBox.add(abilityIconTextBox(a, new Image(Assets.instance.abilities.get(a.getPic())),""+a.getName())).expandX().fillX();
            abilityBox.row();
        }




        mainLayout.add(abilityBox).expand().fill();
        mainLayout.padLeft(25);


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
        t.add(i).size(25,25).expandX().center();
        t.add(new TextArea(text,Assets.uiSkin,"handwritten_black_big" )).size(25,25).expandX().center();


        return t;
    }


    public Table  abilityIconTextBox (Ability a, Image i, String text) {
        Table t = new Table();
        t.add(i).size(25,25).padLeft(15);
        t.add(new TextArea(text,Assets.uiSkin,"handwritten_black" )).expandX().padLeft(25).align(Align.bottom).expandX().fillX();
        i.addListener(new AbilityClickListener(a));
        return t;
    }


}
