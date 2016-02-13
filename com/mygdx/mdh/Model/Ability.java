package com.mygdx.mdh.Model;

/**
 * Created by zubisoft on 28/01/2016.
 */



public class Ability {

    Character source;
    AbilityType type;
    String pic;
    String name;

    public Ability() {
    }

    public Ability(Character sourceCharacter, AbilityType actionType) {
        source = sourceCharacter;
        type = actionType;
        if (this.type == AbilityType.HEAL )
            this.pic="core/assets/btn-heal.png";
        if (this.type == AbilityType.MELEE )
            this.pic="core/assets/btn-attack.png";
        if (this.type == AbilityType.RANGED )
            this.pic="core/assets/btn-attack.png";
    }

    public void apply (Character target) {

        if (this.type == AbilityType.HEAL ) {
            System.out.println("[Ability] "+ source.name + " cura "+ target.name);
            target.setHealth( target.getHealth() + 2 );
        } else {
            System.out.println("[Ability] "+ source.name + " zumba "+ target.name);
            target.setHealth( target.getHealth() - 2);
        }

        source.setAvailableActions(source.getAvailableActions() - 1);
        System.out.println("[Ability] Actions left: "+ source.getAvailableActions());
    }

    public Character getSource() {
        return source;
    }

    public void setSource(Character source) {
        this.source = source;
    }

    public AbilityType getType() {
        return type;
    }

    public String getPic() {
        return pic;
    }

    public void setType(AbilityType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} //Ability
