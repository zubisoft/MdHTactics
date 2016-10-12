package com.mygdx.mdh.game.model.effects;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.mdh.game.model.AttackRoll;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by zubisoft on 29/03/2016.
 */
public class DamageEffect extends Effect {

    String notification;
    /**
     * If true, the default attack chance is ignored and the damage is always applied.
     */
    boolean directDamage = false;


    EnumSet<Character.CHARACTER_TAGS> conditionalType;


    public List<AttackRoll> getDamageRolls() {
        return damageRolls;
    }

    public void setDamageRolls(List<AttackRoll> damageRolls) {
        this.damageRolls = damageRolls;
    }

    List<AttackRoll> damageRolls;

    public DamageEffect () {
        super();

        effectClass = EffectClass.DAMAGE;
        effectType = EffectType.DAMAGE;
        color= Color.RED;

        damageRolls = new ArrayList<>();

        conditionalType = EnumSet.noneOf(Character.CHARACTER_TAGS.class);

        if (pic ==null) {
            if (effectSubType.contains(EffectSubType.ELECTRIC)) pic = "effect_electric";
            else if (effectSubType.contains(EffectSubType.FIRE)) pic = "effect_burning";
            else pic="effect_gunshot";
        }
    }

    public DamageEffect copy () {
        DamageEffect e = new DamageEffect();
        e.copy(this);
        e.directDamage = directDamage;
        e.conditionalType = conditionalType;


        return e;
    }

    /**
     *  To hit chance calculation: d20 + attack >= 10 + defence
     *  */
    public void setAttackChance () {
        setChance((10.0f-source.getAttack()+target.getDefence())/20);
    }

    /**
     *  Direct damage - Ignore attach chance and always hits
     *  */
    public boolean isDirectDamage() {
        return directDamage;
    }

    public void setDirectDamage(boolean directDamage) {
        this.directDamage = directDamage;
    }


    /**
     * Calculates the attack chance and rolls the dice for attack and damage.
     * Since everything is initialized here, it allows modifications on the rolls before applying the damage.
     */
    @Override
    public void init() {
        super.init();
        this.setAttackChance();

        for (int i=0; i< hits; i++) {
            AttackRoll r = new AttackRoll(Roll.RollType.DAMAGE,diceNumber,diceSides,modifier,chance);
            r.setHitChanceModifier(chanceModifier);
            r.setDirectDamage(directDamage);
            r.roll();
            damageRolls.add(r);
        }

        //For those effects that have a chance to happen
        //if ( Math.random() > chance) return ;

    }


    public void addChanceModifier(float chanceModifier) {
        this.chanceModifier += chanceModifier;
        for (int i=0; i< hits; i++) {
            damageRolls.get(i).addHitChanceModifier(chanceModifier);
        }
    }


    public void execute () {
        super.execute();

        setFailed(true);

        if (!conditionalType.isEmpty() && Collections.disjoint(conditionalType,target.getTags()) ) return;

            for (int i=0; i<hits;i++) {
                //double attackRoll = Math.random();
                //LOG.print(2, "[DamageEffect] Rolled: " + attackRoll+" Needed: "+(chance-chanceModifier), LOG.ANSI_RED);

                LOG.print(3,"[DamageEffect] Attack roll"+damageRolls.get(i)+" attack score"+source, LOG.ANSI_RED);
                if ( damageRolls.get(i).isHit()  ) {

                    int rolledResult;

                    //A damage roll can never be negative - Use heal for that
                    rolledResult = Math.max(damageRolls.get(i).getRolledDamage().getRoll(), 0);

                    if (rolledResult>0) {
                        notification=(damageRolls.get(i).isCritical()?"Critic!":"")+" -"+rolledResult+" HP";
                        target.hit(rolledResult);
                    } else {
                        notification="No Damage!";
                    }

                    setFailed(false);


                } else {
                    notification= "Miss!";

                }

                effectTriggered();
            }



    }

    public String notification() {

        return notification;

    }



    public String getDescription () {
        String description;
        if (gameSegment == GameSegmentType.IMMEDIATE) {
             description = "Attack ";

            if (hits>1)             description += hits+" times ";
                                    description += "for ";
            if (diceNumber!=0 )      description += ""+diceNumber+"d"+diceSides+(modifier>0?"+":"");
            if (modifier!=0 )        description += modifier;
            if (isDirectDamage())   description += " direct";
            /*for (EffectSubType est: getEffectSubType())
                                    description += " " + est.name().toLowerCase();*/
                                    description += " damage";
            if (duration>0)         description += " ("+duration+" rounds)";

        } else {
            description = "Applies "+getName();
        }

        return description;


    }




}
