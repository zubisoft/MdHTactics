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

    final int MAX_PARTY = 3;

    String name;

    List<Character> characterCollection;
    List<Campaign> gameCampaign;

    List<Character> currentParty;


    Campaign currentCampaign;


    Mission currentMission;

    public Game () {
        this.currentParty = new ArrayList<>();

    }

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

    public static Game loadNewGame () {
        return loadFromJSON ("DefaultGame");

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


    public Campaign getCurrentCampaign() {
        if (currentCampaign == null)  currentCampaign=gameCampaign.get(0);
        return currentCampaign;
    }

    public Mission getCurrentMission() {
        if (currentMission==null) currentMission=getCurrentCampaign().getCampaignMissions().get(0);
        return currentMission;
    }




    //Generic getters and setters

    public void setCurrentMission(Mission currentMission) {
        this.currentMission = currentMission;
    }
    public void setCurrentCampaign(Campaign currentCampaign) {
        this.currentCampaign = currentCampaign;
    }

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

    public List<Character> getCurrentParty() {
        return currentParty;
    }

    public void addCurrentParty(Character character) {
        if (currentParty.size()<MAX_PARTY)
            this.currentParty.add(character);
    }

    public void removeCurrentParty(Character character) {
            this.currentParty.remove(character);
    }

}
