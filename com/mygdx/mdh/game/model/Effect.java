package com.mygdx.mdh.game.model;


/**
 * Created by zubisoft on 08/03/2016.
 */
public class Effect {




    public enum EffectType {
        FIRE, MEDICAL, STUN
    }

    //Effect definition
    int duration;
    int damage;
    float chance;
    EffectType effectType;

    Character source;
    Character target;

    //File with the textures
    String pic;

    String outcome;


    public Effect () {}

    public Effect(String type) {
        //Some sample effects
        if (type=="FIRE") {
            damage = 10;
            duration = 2;
            effectType = EffectType.FIRE;
            pic="core/assets/graphics/effects/effect_burning.png";
        }

        if (type=="MEDICAL") {
            damage = -10;
            duration = 2;
            effectType = EffectType.MEDICAL;
            pic="core/assets/graphics/effects/effect_burning.png";
        }

    }

    public boolean apply() {

        System.out.println("burning"+target);
        if (target == null) return false;

        if (duration <= 0) return false;


        switch (effectType) {
            case FIRE:
                target.hit(damage);

                outcome = "Burning! " + damage + "HP";
                duration--;
                break;

            case MEDICAL:
                target.hit(damage);
                outcome = "Healed! " + damage + "HP";
                duration--;
                break;

            case STUN:
                if (Math.random() <= chance) {
                    target.setAvailableActions(target.getAvailableActions() - 1);
                    outcome = "Stunned!";
                }
                duration--;
                break;
        }
        return true;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public EffectType getEffecType() {

        return effectType;
    }

    public void setEffecType(EffectType effecType) {
        this.effectType = effecType;
    }


    public Character getSource() {
        return source;
    }

    public void setSource(Character source) {
        this.source = source;
    }

    public Character getTarget() {
        return target;
    }

    public void setTarget(Character target) {
        this.target = target;
    }


    public EffectType getEffectType() {
        return effectType;
    }

    public void setEffectType(EffectType effectType) {
        this.effectType = effectType;
    }

    public String getOutcome() {
        return outcome;
    }

}
