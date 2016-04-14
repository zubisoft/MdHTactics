package com.mygdx.mdh.game.model;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.mdh.game.EffectManager;
import com.mygdx.mdh.game.controller.CharacterChangeListener;
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
    boolean active;
    boolean dead;

    //Position
    MapCell cell;
    int column;
    int row;

    //Abilities of the character
    List<Ability> abilities;

    //Effects currently applied on this character
    private List<Effect> effects;

    //event handling
    private List<CharacterChangeListener> listeners;




    //Graphic info
    String pic;

    //Auxiliary
    private List<Effect> effectsAux;


    public Character() {
        active=true;
        abilities = new ArrayList<Ability>();

        effects = new ArrayList<Effect> ();
        effectsAux = new ArrayList<Effect> ();
        listeners = new ArrayList<> ();
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
            availableActions = maxActions;
            this.setActive(true);

            if (effects.size() > 0) {
                for (Effect tmp : effects) {
                    //tmp.setTarget(this);
                    //Resolve immediate effects
                    LOG.print(4, "[Character] Starting turn effects: "+tmp.getEffectType());
                    //if(tmp.getGameSegment()== Effect.GameSegmentType.TURN_START)
                    EffectManager.instance.execute(tmp);
                    tmp.startTurn();
                }
                cleanEffects();
            }
        }
    }

    public void addListener(CharacterChangeListener cl) {
        listeners.add(cl);
    }

    public void hit(int damage) {
        setHealth(getHealth()-damage);
        for(CharacterChangeListener cl: listeners) {
            cl.onCharacterHit(damage);
        }

    }

    public int getHealth() { return health; }
    public void setHealth(int h) {
        health=h;
        if (health<=0) {
            dead=true;
            active=false;
        }
    }

    public int getAvailableActions() {return availableActions;}
    public void setAvailableActions(int a) {
        this.availableActions=a;
        if (this.availableActions<=0) this.setActive(false);
    }


    public int getAttack() { return attack; }
    public int getDefence() { return defence; }




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

        FileHandle file = Gdx.files.internal(name);
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

    public String toString() {
        return "[Character: "+name+"] HP:"+health+ " AP:"+availableActions;
    }


    public List<Effect> getEffectsByName(String name) {
        List<Effect> list = new ArrayList<>();

        for (Effect e: effects) {
            if (e.getName().equals(name))
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

    public void addEffect (Effect e) {
        LOG.print(4, "[Character] Adding Effect "+e.getEffectType()+" to "+getName()+" duration: "+e.getDuration());
        effects.add(e);

        for (Effect x: this.getEffects()) {
            LOG.print(4,"* "+x.toString()+"\n");
        }

    }

    public void addEffect (List<Effect> e) {
        LOG.print("[Character] Adding effect list");

       // if (e != null) effects.addAll(e);
        if (e==null ) return;


        for (Effect tmp: e) {
            tmp.setTarget(this);
            //Resolve immediate effects
            LOG.print("[Character] Calling effect manager");
            //TODO it might be necessary to create new effect objects every time an ability is applied?
            EffectManager.instance.apply(tmp);
        }



        cleanEffects();


    }

    private void cleanEffects () {
        if(effects.size()==0) return;

        Iterator<Effect> iterator = effects.iterator();

        while (iterator.hasNext()) {
            Effect tmp = iterator.next();
            if (tmp.getDuration()<=0) {
                LOG.print(4, "[Character] Remove Effect " + tmp.getEffectType() + " from " + getName());
                iterator.remove();
            }
        }
    }


    public boolean isActive() { return active; }

    public void setActive(boolean a) { this.active=a; }


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
        return movement;
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
} //Character

