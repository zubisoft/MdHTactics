package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.model.MapCell;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 09/04/2016.
 */
public class SummonEffect extends Effect  {

        String notification  = null;
        boolean initialized = false;


    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    Character character;


        public SummonEffect() {
            super();

            effectClass = EffectClass.SUMMON;
            effectType = EffectType.BUFF;
            color= Color.YELLOW;
            notification  = null;
            duration = 1;

            if (pic ==null) pic="effect_red";

        }

        @Override
        public void init() {

            if (!initialized) {
                super.init();

                character = new Character();
                character.copy(getSource());

                MapCell chosenCell = null;
                for (MapCell m: getSource().getCell().getMap().getCells(getSource().getCell(),1)) {
                    if (chosenCell==null && !m.isImpassable() && !m.isOccupied()) chosenCell = m;
                }

                if (chosenCell == null) character = null;
                else character.setCell(chosenCell);


                //clone self
                /*
                System.out.println(combat.getCharacterActors());
                */

                initialized = true;
                duration =0;
            }


        }


        public String notification() {
            return notification;
        }

    public SummonEffect copy () {

        SummonEffect e = new SummonEffect();
        e.copy(this);

        return e;
    }
    }


