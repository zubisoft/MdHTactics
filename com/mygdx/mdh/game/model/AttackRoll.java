package com.mygdx.mdh.game.model;

import com.mygdx.mdh.game.model.Roll;
/**
 * Created by zubisoft on 05/06/2016.
 */
public class AttackRoll   {

    Roll damageRoll;

    boolean hit=false;
    float hitChanceModifier=0;
    float hitChance=0.5f;
    double hitRoll;


    boolean directDamage;

    public AttackRoll (Roll.RollType type, int diceNumber, int diceSides, int modifier, float chance) {
        damageRoll = new Roll(type,diceNumber,diceSides,modifier);
        hitChance = chance;

        roll();
    }

    public Roll roll() {

        damageRoll.roll();
        hitRoll = Math.random();

        if (hitRoll >= hitChance-hitChanceModifier || isDirectDamage()) hit=true;
        else hit = false;

        return (hit?damageRoll:null);
    }

    public Roll getRolledDamage() {
        return (hit?damageRoll:null);
    }

    public boolean isDirectDamage() {
        return directDamage;
    }

    public void setDirectDamage(boolean directDamage) {
        this.directDamage = directDamage;
    }

    public boolean isHit() {

        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public float getHitChance() {
        return hitChance;
    }

    public void setHitChance(float hitChance) {
        this.hitChance = hitChance;
    }

    public float getHitChanceModifier() {
        return hitChanceModifier;
    }

    public void addHitChanceModifier(float x) {

        hitChanceModifier+=x;


            if (hitRoll >= hitChance-hitChanceModifier || isDirectDamage()) hit=true;
            else hit = false;




    }

    public void setHitChanceModifier(float hitChanceModifier) {
        this.hitChanceModifier = hitChanceModifier;
    }

    public String toString() {
        return "Attack [To Hit: "+hitChance+" Rolled:"+Math.ceil(hitRoll*100.0)/100+"+"+hitChanceModifier+" Result:"+hit+ "] Damage: "+damageRoll;
    }




}
