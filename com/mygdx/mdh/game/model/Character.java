package com.mygdx.mdh.game.model;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.mdh.game.EffectManager;
import com.mygdx.mdh.game.controller.CharacterChangeListener;
import com.mygdx.mdh.game.model.effects.AttributeModifierEffect;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 27/01/2016.
 */


public class Character  {

    //Core attributes
    public String name;
    int attack;
    int defence;
    int health;
    int maxHealth;
    int movement;
    int maxActions;
    int availableActions;
    boolean friendly;
    boolean dead;
    int xp;
    int level;

    //Position
    @JsonIgnore MapCell cell;
    @JsonIgnore int column;
    @JsonIgnore int row;

    //Abilities of the character
    List<Ability> abilities;

    //Effects currently applied on this character
    @JsonIgnore private List<Effect> effects;

    //event handling
    @JsonIgnore private List<CharacterChangeListener> listeners;




    //Graphic info
    String pic;

    //Auxiliary
    @JsonIgnore private List<Effect> effectsAux;


    public Character() {

        abilities = new ArrayList<Ability>();

        effects = new ArrayList<Effect> ();
        effectsAux = new ArrayList<Effect> ();
        listeners = new ArrayList<> ();

        maxActions = 2;
        availableActions=2;
        xp =0;
        level=0;
    }

    /*
    public Character(String characterName, boolean startFriendly) {

        //Normally we would load this from JSON or similar
        name = characterName;
        attack=12;
        defence=10;
        health=10;
        maxHealth=10;
        movement = 3;
        maxActions = 2;
        availableActions=maxActions;
        active=true;
        dead=false;
        friendly =startFriendly;
        pic = "core/assets/"+characterName+".png";

        //Add a couple of default abilities
        abilities = new ArrayList<Ability> ();
        abilities.add(new Ability(this, Ability.AbilityType.HEAL));
        abilities.add(new Ability(this, Ability.AbilityType.RANGED));

        //Add a list for active effects
        effects = new ArrayList<Effect> ();

    }*/

    public void startTurn() {

        if (!isDead()) {
            setAvailableActions(maxActions);
            //this.setActive(true);

            for (Ability a: getAbilities()) {
                if (a.getCurrentCooldown()>0) a.setCurrentCooldown(a.getCurrentCooldown()-1);
            }

            if (effects.size() > 0) {
                cleanEffects();

                for (Effect tmp : effects) {
                    //tmp.setTarget(this);
                    //Resolve immediate effects
                    LOG.print(4, "[Character] Starting turn effects: "+tmp.getEffectClass());
                    //if(tmp.getGameSegment()== Effect.GameSegmentType.TURN_START)
                    EffectManager.instance.execute(tmp);
                    tmp.startTurn();
                }

            }
        }
    }

    public void addListener(CharacterChangeListener cl) {
        listeners.add(cl);
    }

    public void hit(int damage) {

        //Do not allow to surpass max hitpoints when healed
        if (damage<0 && (getHealth()-damage)>maxHealth)
            health=maxHealth;

        if (getHealth()-damage > maxHealth) setHealth(maxHealth);
        else setHealth(getHealth()-damage);

        for(CharacterChangeListener cl: listeners) {
            cl.onCharacterHit(damage);
        }

    }

    public int getHealth() { return health; }
    public void setHealth(int h) {
        health=h;
        if (health<=0) {
            dead=true;
            availableActions=-1;
            if(cell!= null) cell.setOccupied(null);
        }
    }



    public int getAvailableActions() {
        /*
        int modifier=0;

        //TODO Super inefficient, maybe better when adding/removing effects and keeping an aux variable
        for (Effect e:getEffects()) {
            if (e.getEffectClass()== Effect.EffectClass.ATTRIBUTE_MODIFIER) {
                if (((AttributeModifierEffect)e).getAttributeType().contains(AttributeModifierEffect.EffectTargetType.ACTIONS)) {
                    modifier += ((AttributeModifierEffect) e).getAttributeModifier();
                }
            }
        }

        if (modifier>0) cleanEffects();
        */

        return availableActions /*+modifier*/ ;
    }


    public void setAvailableActions(int a) {
        if (this.availableActions<=0 && a>0) {
            for(CharacterChangeListener cl: listeners) {
                cl.onCharacterActive(this);
            }
        }

        this.availableActions=a;

        if(availableActions<=0) {
            for (CharacterChangeListener cl : listeners) {
                cl.onCharacterInactive(this);
            }
        }

    }


    public int getAttack() {
        int modifier=0;

        //TODO Super inefficient, maybe better when adding/removing effects and keeping an aux variable
        for (Effect e:getEffects()) {
            if (e.getEffectClass()== Effect.EffectClass.ATTRIBUTE_MODIFIER) {
                if (((AttributeModifierEffect)e).getAttributeType().contains(AttributeModifierEffect.EffectTargetType.ATTACK))
                    modifier += ((AttributeModifierEffect)e).getAttributeModifier();
            }

        }
        return defence+modifier;
    }
    public int getDefence() {
        int modifier=0;

        //TODO Super inefficient, maybe better when adding/removing effects and keeping an aux variable
        for (Effect e:getEffects()) {
            if (e.getEffectClass()== Effect.EffectClass.ATTRIBUTE_MODIFIER) {
                if (((AttributeModifierEffect)e).getAttributeType().contains(AttributeModifierEffect.EffectTargetType.DEFENCE))
                    modifier += ((AttributeModifierEffect)e).getAttributeModifier();
            }

        }
        return defence+modifier;
    }




    public boolean isFriendly() { return friendly; }
    public String getPic() { return pic; }
    public List<Ability> getAbilities() { return abilities; }




    public void setMaxHealth(int maxHealth) {
        health=maxHealth;
        this.maxHealth = maxHealth;
    }



    public int getMaxActions() {
        return maxActions;
    }

    public void setMaxActions(int maxActions) {
        availableActions=maxActions;
        this.maxActions = maxActions;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }


    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
        for (Ability a: abilities) {
            a.setSource(this);
            for(Effect e: a.getEffects()) {
                e.setSource(this);
            }
        }
    }

    public void setPic(String pic) {
         this.pic = pic;
    }

    public List<Ability> addAbility(Ability ab) {
        abilities.add(ab);
        return abilities;
    }



    public static Character loadFromJSON (String name) {

        FileHandle file = Gdx.files.internal("core/assets/data/characters/" + name+ ".txt");
        String jsonData = file.readString();


        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        Character emp = new Character();

        try {

            //System.out.println("Employee Object\n"+jsonData);
            emp = objectMapper.readValue(jsonData, Character.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return emp;

    }

    public static Character loadByName (String name) {
        return Character.loadFromJSON(name);
    }

    public String toString() {
        return "[Character: "+name+"] HP:"+health+ " AP:"+availableActions;
    }


    public List<Effect> getEffectsByNameAndClass(String name, Effect.EffectClass type) {
        List<Effect> list = new ArrayList<>();

        for (Effect e: effects) {
            if ((e.getName()+e.getEffectClass()).equals(name+type))
                list.add(e);
        }

        return list;
    }


    /******************** Generic getters and setters ********************/
    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    /**
     Simply adds an effect to the effect list, as is.
     This method is called once all the relevant modifiers are applied to the effect.
     **/
    public void addEffect (Effect e) {
        effects.add(e);
    }

    /**
     * This method adds a list of effects to a character after processing all the relevant modifiers.
     * The target is set to this character, and then sent to the effect manager for processing, where
     * source and target modifiers are applied before ultimately adding the effect.
     *
     * Immediate effects will be triggered.
     *
     * @param e
     */
    public void addEffect (List<Effect> e) {
        LOG.print("[Character] Adding effect list");

       // if (e != null) effects.addAll(e);
        if (e==null ) return;

        int i=0;


        for (Effect tmp: e) {
            tmp.setTarget(this);
            //Resolve immediate effects
            //LOG.print("[Character] Calling effect manager");



            //TODO If first attack fails, further effects are not applied - MUY CHAPUCERO
            if (!EffectManager.instance.apply(tmp) && i==0) {
                //System.out.println("breaking");
                break;
            }

            i++;

        }
        cleanEffects();






    }

    public void cleanEffects () {
        if(effects.size()==0) return;

        Iterator<Effect> iterator = effects.iterator();

        while (iterator.hasNext()) {
            Effect tmp = iterator.next();
            if (tmp.getDuration()<=0) {
                LOG.print(4, "[Character] Remove Effect " + tmp.getEffectClass() + " from " + getName());
                iterator.remove();
            }
        }
    }


    public boolean isActive() { return getAvailableActions()>0; }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getMovement() {
        int modifier=0;

        //TODO Super inefficient, maybe better when adding/removing effects and keeping an aux variable
        for (Effect e:getEffects()) {
            if (e.getEffectClass()== Effect.EffectClass.ATTRIBUTE_MODIFIER) {
                if (((AttributeModifierEffect)e).getAttributeType().contains(AttributeModifierEffect.EffectTargetType.MOVEMENT))
                    modifier += ((AttributeModifierEffect)e).getAttributeModifier();
            }

        }

        return movement+modifier;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }


    public MapCell getCell() {
        return cell;
    }

    public void setCell(MapCell cell) {
        if(this.cell !=null)  this.cell.setOccupied(null);
        this.cell = cell;
        this.cell.setOccupied(this);
        this.column = (int)cell.getMapCoordinates().x;
        this.row = (int)cell.getMapCoordinates().y;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean hasEffect(Effect effect) {
        for (Effect e:getEffects()) {
            if (e.getName().equals(effect.getName())) return true;
         }
        return false;
    }



    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }



} //Character

