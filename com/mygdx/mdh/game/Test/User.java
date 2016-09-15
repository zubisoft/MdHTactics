package com.mygdx.mdh.game.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by zubisoft on 15/05/2016.
 */
public class User {
    String name;
    Orto orto;

    public User () {

    }

    public User loadFromJSON (String mapId) {

        String x = new String(mapId);

        FileHandle file = Gdx.files.internal("core/assets/data/characters/"+x+".txt");
        String jsonData = file.readString();

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        User emp = new User();

        try {

            //System.out.println("Employee Object\n"+jsonData);
            emp = objectMapper.readValue(jsonData, User.class);

        } catch (Exception e) {
            e.printStackTrace();
        }



        return emp;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Orto getOrto() {
        return orto;
    }

    public void setOrto(Orto orto) {
        this.orto = orto;
    }
}
