package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 14/05/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Campaign {



    String campaignId;
    String name;
    String description;
    boolean enabled;

    List<StoryText> introText;
    List<StoryText> storyText;

    List<Mission> campaignMissions;

    @JsonProperty("missionList")
    public void setMissionList(List<String> baddiesId) {
        this.campaignMissions = new ArrayList<Mission>();
        for(String baddy: baddiesId) {
            this.campaignMissions.add(Mission.loadFromJSON(baddy));
        }
    }


    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;

        Campaign c = loadFromJSON(campaignId);
        this.name=c.name;
        this.description=c.description;
        this.enabled=c.enabled;
        this.introText=c.introText;
        this.storyText=c.storyText;
        this.campaignMissions=c.campaignMissions;


    }

    public static Campaign loadFromJSON (String campaignId) {

        FileHandle file = Gdx.files.internal("core/assets/data/campaigns/"+campaignId+".txt");
        String jsonData = file.readString();

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        Campaign emp = new Campaign();


        try {

            emp = objectMapper.readValue(jsonData, Campaign.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        emp.campaignId = campaignId;

        //LOG.print(1,"campaign id "+emp.campaignId, LOG.ANSI_CYAN);


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

    public List<Mission> getCampaignMissions() {
        return campaignMissions;
    }

    public void setCampaignMissions(List<Mission> campaignMissions) {
        this.campaignMissions = campaignMissions;
    }
}
