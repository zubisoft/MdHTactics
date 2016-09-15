package com.mygdx.mdh.game.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by zubisoft on 15/05/2016.
 */
public class Orto {
    String name;
    String ortoId;

    @JsonIgnore
    public Orto () {}

    @JsonCreator
    public static Orto factory (@JsonProperty("ortoId") String ortoId) {

        String x = new String(ortoId);

        FileHandle file = Gdx.files.internal("core/assets/data/characters/"+x+".txt");
        String jsonData = file.readString();



        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        Orto emp = new Orto();

        try {

            //System.out.println("Employee Object\n"+jsonData);
            emp = objectMapper.readValue(jsonData, Orto.class);

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

    public String getOrtoId() {
        return ortoId;
    }

    public void setOrtoId(String ortoId) {
        this.ortoId = ortoId;
    }
}
