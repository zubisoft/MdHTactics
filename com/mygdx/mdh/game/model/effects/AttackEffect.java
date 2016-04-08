package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.Gdx;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.Dice;

/**
 * Created by zubisoft on 30/03/2016.
 */
public class AttackEffect extends Effect {


    public void init() {

        if ( duration < 0 ) return;

        if(diceNumber>0) {
            rolledResult = Dice.roll(diceNumber, diceSides) + modifier;
            roll.setBaseRoll(rolledResult);
            roll.setModifier(modifier);
            roll.setPercentModifier(percentModifier);
            roll.setEffectType(Roll.RollType.ATTACK);
        }

        this.duration--;

        Gdx.app.debug("[AttackEffect]", "Rolled: "+roll.getTotalRoll());

    }

}
