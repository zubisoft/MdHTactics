package com.mygdx.mdh.Model;

import java.util.*;

import java.nio.file.Files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    int cellx;
    int celly;

    List<Ability> abilities;

    //Graphic info
    String pic;


    public Character() {

        active=true;
        abilities = new ArrayList<Ability>();

    }
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
        friendly =startFriendly;
        pic = "core/assets/"+characterName+".png";

        //Add a couple of default abilities
        abilities = new ArrayList<Ability> ();
        abilities.add(new Ability(this,AbilityType.HEAL));
        abilities.add(new Ability(this,AbilityType.RANGED));

    }

    public void startTurn() {
        availableActions = maxActions;
        this.setActive(true);
    }

    public int getAttack() { return attack; }
    public int getDefence() { return defence; }
    public int getHealth() { return health; }
    public void setHealth(int h) { health=h; }
    public boolean isFriendly() { return friendly; }
    public String getPic() { return pic; }
    public List<Ability> getAbilities() { return abilities; }

    public int getAvailableActions() {return availableActions;}
    public void setAvailableActions(int a) {
        this.availableActions=a;
        if (this.availableActions<=0) this.setActive(false);
    }

    public boolean isActive() { return active; }

    public void setActive(boolean a) { this.active=a; }

    public int getCelly() {
        return celly;
    }

    public void setCelly(int celly) {
        this.celly = celly;
    }

    public int getCellx() {
        return cellx;
    }

    public void setCellx(int cellx) {
        this.cellx = cellx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        health=maxHealth;
        this.maxHealth = maxHealth;
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
        }
    }

    public void setPic(String pic) {
        System.out.println("Setting  "+pic); this.pic = pic;
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
        return "[Character: "+name+"] @ Cell("+cellx+","+celly+") HP:"+health+ " AP:"+availableActions;
    }

} //Character

