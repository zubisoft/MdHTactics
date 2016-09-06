package com.mygdx.mdh.screens.widgets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.model.Character;

/**
 * Created by zubisoft on 05/09/2016.
 */
public class WidgetCharterSheet extends Table {

    public WidgetCharterSheet(Character c) {
        this.setSize(400,600); //Default size
        this.align(Align.left);
        this.pad(50);
        this.padTop(75);

        //this.add(new Container(new Image(Assets.instance.characters.get(c.getPic()).portrait)) ).size(50,50);

        Table characterInfo = new Table();
        characterInfo.setSize(350,50);
        characterInfo.add(new Label(c.getName(),Assets.uiSkin,"handwritten_black" )).align(Align.left);
        characterInfo.row();
        characterInfo.add(new Label("Level: "+c.getLevel(),Assets.uiSkin,"handwritten_black" )).align(Align.left);
        characterInfo.row();
        characterInfo.add(new Label("Experience: "+c.getXp()+"/"+c.getNextLevelXP(),Assets.uiSkin,"handwritten_black" )).align(Align.left);

        this.add(characterInfo).colspan(2).align(Align.left);

        this.row();

        this.add(new Label("Attributes",Assets.uiSkin,"handwritten_black" ));
        this.add(new Label("Abilities",Assets.uiSkin,"handwritten_black" ));

        this.row();


        Table attributeBox = new Table();
        attributeBox.add(horizontalIconTextBox(new Image(Assets.instance.abilities.get("btn-flame")),""+c.getAttack())).size(200,25);
        attributeBox.row();
        attributeBox.add(horizontalIconTextBox(new Image(Assets.instance.abilities.get("btn-flame")),""+c.getMaxActions())).size(200,25);
        attributeBox.row();
        attributeBox.add(horizontalIconTextBox(new Image(Assets.instance.abilities.get("btn-flame")),""+c.getDefence())).size(200,25);
        attributeBox.row();
        attributeBox.add(horizontalIconTextBox(new Image(Assets.instance.abilities.get("btn-flame")),""+c.getMovement())).size(200,25);
        attributeBox.row();
        attributeBox.add(horizontalIconTextBox(new Image(Assets.instance.abilities.get("btn-flame")),""+c.getMaxHealth())).size(200,25);
        attributeBox.row();

        this.add(attributeBox);

        Table abilityBox = new Table();
        for (Ability a: c.getAbilities()) {
            abilityBox.add(horizontalIconTextBox(new Image(Assets.instance.abilities.get(a.getPic())),""+a.getName())).size(200,25);
            abilityBox.row();
        }

        this.add(abilityBox);

    }



    public Table horizontalIconTextBox (Image i, String text) {
        Table t = new Table();
        t.add(i).size(25,25);
        t.add(new TextArea(text,Assets.uiSkin,"handwritten_black" )).size(175,25).padLeft(25);
        return t;
    }
}
