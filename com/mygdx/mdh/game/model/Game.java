package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 13/05/2016.
 */
public class Game {

    String name;

    List<Character> characterCollection;
    List<Campaign> gameCampaign;


    @JsonProperty("characterList")
    public void setCharacterList(List<String> baddiesId) {
        this.characterCollection = new ArrayList<Character>();
        for(String baddy: baddiesId) {
            this.characterCollection.add(Character.loadFromJSON(baddy));
        }

    }

    @JsonProperty("campaignList")
    public void setCampaignList(List<String> baddiesId) {
        this.gameCampaign = new ArrayList<Campaign>();
        for(String baddy: baddiesId) {
            this.gameCampaign.add(Campaign.loadFromJSON(baddy));
        }
    }

    public static Game loadFromJSON (String name) {

        FileHandle file = Gdx.files.internal("core/assets/data/games/"+name+".txt");
        String jsonData = file.readString();

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        Game emp = new Game();

        try {

            emp = objectMapper.readValue(jsonData, Game.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return emp;

    }

    //Generic getters and setters

    public List<Character> getCharacterCollection() {
        return characterCollection;
    }

    public void setCharacterCollection(List<Character> characterCollection) {
        this.characterCollection = characterCollection;
    }

    public List<Campaign> getGameCampaign() {
        return gameCampaign;
    }

    public void setGameCampaign(List<Campaign> gameCampaign) {
        this.gameCampaign = gameCampaign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
