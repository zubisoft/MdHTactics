package com.mygdx.mdh.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mygdx.mdh.game.model.effects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 28/01/2016.
 */


public class Ability {


    AbilityType type;
    AbilityTarget targetType;
    /**
     * Main parameters of the ability
     */
    int range;
    int area;
    int cooldown = 0;
    int requiredLevel = 0;
    /**
     * Visual elements and description
     */
    String pic;
    String name;
    String message;
    /**
     * Effects that are applied by using the ability
     */
    List<Effect> effects;
    /**
     * Game handling attributes
     */
    @JsonIgnore
    Character source;
    @JsonIgnore
    Character target;
    boolean enabled = true;
    int currentCooldown = 0;


    public Ability() {
        effects = new ArrayList<Effect>();

        range = 1;

        area = 0;

        requiredLevel = 0;

        pic = "btn-flame";
    }

    public void copy(Ability b) {
        this.type = b.type;
        this.targetType = b.targetType;
        this.range = b.range;
        this.area = b.area;
        this.cooldown = b.cooldown;
        this.requiredLevel = b.requiredLevel;
        this.pic = b.pic;
        this.name = b.name;
        this.message = b.message;
        //this.effects=b.effects;
        for (Effect e : b.getEffects()) {

            System.out.println("Copying effect "+e.getName()+" "+e.getClass()+" "+(e.getClass() == SummonEffect.class)+" "+e.getEffectClass());

            //TODO esto hace llorar al niño jesus...
            if (e.getClass() == ShieldEffect.class) {
                ShieldEffect aux;
                aux = new ShieldEffect();
                aux.copy((ShieldEffect)e);
                this.effects.add(aux);
            }
            else if (e.getClass() == SummonEffect.class) {
                SummonEffect aux;
                aux = new SummonEffect();
                aux.copy((SummonEffect)e);
                this.effects.add(aux);
            }
            else if (e.getClass() == DamageEffect.class) {
                DamageEffect aux;
                aux = new DamageEffect();
                aux.copy((DamageEffect)e);
                this.effects.add(aux);
            }
            else if (e.getClass() == ProtectionEffect.class) {
                ProtectionEffect aux;
                aux = new ProtectionEffect();
                aux.copy((ProtectionEffect)e);
                this.effects.add(aux);
            }
            else if (e.getClass() == HealEffect.class) {
                HealEffect aux;
                aux = new HealEffect();
                aux.copy((HealEffect)e);
                this.effects.add(aux);
            }
            else if (e.getClass() == StunEffect.class) {
                StunEffect aux;
                aux = new StunEffect();
                aux.copy((StunEffect)e);
                this.effects.add(aux);
            }
            else if (e.getClass() == RemoverEffect.class) {
                RemoverEffect aux;
                aux = new RemoverEffect();
                aux.copy((RemoverEffect)e);
                this.effects.add(aux);
            }
            else if (e.getClass() == DamageModifierEffect.class) {
                DamageModifierEffect aux;
                aux = new DamageModifierEffect();
                aux.copy((DamageModifierEffect)e);
                this.effects.add(aux);
            }
            else if (e.getClass() == AttributeModifierEffect.class) {
                AttributeModifierEffect aux;
                aux = new AttributeModifierEffect();
                aux.copy((AttributeModifierEffect)e);
                this.effects.add(aux);
            }
            else {
                Effect aux;
                aux = new Effect();
                aux.copy(e);
                this.effects.add(aux);
            }

        }

        this.source = b.source;
        this.target = b.target;
        this.enabled = b.enabled;
        this.currentCooldown = b.currentCooldown;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public void setCurrentCooldown(int currentCooldown) {
        this.currentCooldown = currentCooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void apply(Character target) {


        //Use ability icon instead of just default
        for (Effect e : effects) {
            if (e.isDefaultIcon())
                e.setIcon(this.getPic());
        }

        //Apply effects
        if (effects != null)
            target.addEffect(effects);

        currentCooldown = cooldown;

        //source.setAvailableActions(source.getAvailableActions() - 1);

        //System.out.println("[Ability] Actions left: "+ source.getAvailableActions());
    }

    public Character getSource() {
        return source;
    }

    public void setSource(Character source) {
        this.source = source;
        for (Effect e : getEffects()) {
            e.setSource(source);
        }

    }

    public AbilityType getType() {
        return type;
    }

    public void setType(AbilityType type) {


        if (pic.equals("btn-flame")) {
            switch (type) {
                case DAMAGE:
                    pic = "btn-gladius";
                    break;
                case BUFF:
                    pic = "btn-shield";
                    break;
                case DEBUFF:
                    pic = "btn-demoralize";
                    break;
                case HEAL:
                    pic = "btn-transfusion";
                    break;
            }
        }

        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void addEffect(Effect e) {
        effects.add(e);
    }

    public Character getTarget() {
        return target;
    }

    public void setTarget(Character target) {
        this.target = target;
        if (effects.size() > 0) {
            for (Effect e : effects) e.setTarget(target);
        }
    }

    public String getMessage() {
        return message;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int r) {
        range = r;
    }

    public AbilityTarget getTargetType() {
        return targetType;
    }

    public void setTargetType(AbilityTarget targetType) {
        if (targetType == AbilityTarget.SELF) range = 0;

        this.targetType = targetType;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String toString() {
        return this.getName() + " " + this.getType() + " " + this.getRange();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * General type of the ability
     * This is mainly used to help the IA find the appropiate ability for each moment.
     */
    public enum AbilityType {
        DAMAGE, BUFF, DEBUFF, HEAL
    }

    /**
     * Type of target for the ability
     * This is the first filter that is used to check which character are affected.
     * The effects can further refine the target with their own target type.
     */
    public enum AbilityTarget {
        SELF,          //Target and source must be the same
        ALLIES,        //Target must be friendly
        ENEMIES,       //Target must be non friendly
        ALL            //Affects any target
    }

} //Ability
