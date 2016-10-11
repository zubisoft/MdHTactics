package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mygdx.mdh.game.util.LOG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 13/05/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mission {


    public void setScore(int score) {
        this.score = score;
    }

    public enum MissionType { NORMAL, BOSS }


    String missionId;
    String name;
    String description;
    boolean enabled;


    //Generic mission concepts
    int maxStars;
    MissionType missionType;

    List<StoryText> introText;

    List<StoryText> outroText;
    List<StoryText> storyText;

    Map missionMap;
    List<Character> baddies;


    String nextMissionId;

    public List<String> getUnlockedCharacters() {
        return unlockedCharacters;
    }

    public void setUnlockedCharacters(List<String> unlockedCharacters) {
        this.unlockedCharacters = unlockedCharacters;
    }

    List<String> unlockedCharacters;

    //Specific instance concepts
    int currentStars;
    int score;
    boolean unlocked=false;


    public Mission () {
        currentStars =0;
    }


    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    @JsonProperty("mapId")
    public void setMapId(String mapId) {
        //LOG.print("Load mapId "+mapId);
        this.missionMap = Map.loadFromJSON(mapId);

    }

    @JsonProperty("baddiesId")
    public void setBaddiesId(List<String> baddiesId) {
        this.baddies = new ArrayList<Character>();
        for(String baddy: baddiesId) {
            this.baddies.add(Character.loadFromJSON(baddy));
        }
    }

    public List<StoryText> getOutroText() {
        return outroText;
    }

    public void setOutroText(List<StoryText> outroText) {
        this.outroText = outroText;
    }

    public static Mission loadFromJSON (String name) {

        FileHandle file = Gdx.files.internal("core/assets/data/missions/"+name+".txt");
        String jsonData = file.readString();


        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        Mission emp = new Mission();

        try {

            emp = objectMapper.readValue(jsonData, Mission.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        emp.missionId = name;
        //TODO: Dont forget, once the combat is created, to link characters and map.

        return emp;

    }

    //Generic getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxStars() {
        return maxStars;
    }

    public void setMaxStars(int maxStars) {
        this.maxStars = maxStars;
    }

    public MissionType getMissionType() {
        return missionType;
    }

    public void setMissionType(MissionType missionType) {
        this.missionType = missionType;
    }

    public List<StoryText> getIntroText() {
        return introText;
    }

    public void setIntroText(List<StoryText> introText) {
        this.introText = introText;
    }

    public List<StoryText> getStoryText() {
        return storyText;
    }

    public void setStoryText(List<StoryText> storyText) {
        this.storyText = storyText;
    }

    public Map getMissionMap() {
        return missionMap;
    }

    public void setMissionMap(Map missionMap) {
        this.missionMap = missionMap;
    }

    public List<Character> getBaddies() {
        return baddies;
    }

    public void setBaddies(List<Character> baddies) {
        this.baddies = baddies;
    }

    public int getCurrentStars() {
        return currentStars;
    }

    public void setCurrentStars(int currentStars) {
        this.currentStars = currentStars;
    }

    public int getScore() {
        return score;
    }


    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public String getNextMissionId() {
        return nextMissionId;
    }

    public void setNextMissionId(String nextMissionId) {
        this.nextMissionId = nextMissionId;
    }
}
