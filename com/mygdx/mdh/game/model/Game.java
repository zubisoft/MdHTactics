package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.mygdx.mdh.game.util.CharacterDeserializer;
import com.mygdx.mdh.game.util.LOG;
import com.mygdx.mdh.game.util.MissionDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 13/05/2016.
 */
public class Game {

    /**
     * Serializes the campaign as a campaign ID, which defines the template for the campaign
     */

    public class CampaignSerializer extends JsonSerializer<Campaign> {
        @Override
        public void serialize(Campaign value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeStringField("campaignId", value.campaignId);
            jgen.writeEndObject();
        }
    }

    /**
     * Serializes the campaign as a mission ID, which defines the template for the campaign,
     * but also stores the fields containing the mission status in the current game (i.e. the score)
     */
    public class MissionSerializer extends JsonSerializer<Mission> {
        @Override
        public void serialize(Mission value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeStringField("missionId", value.missionId);
            jgen.writeNumberField("currentStars", value.currentStars);
            jgen.writeBooleanField("unlocked", value.unlocked);
            jgen.writeEndObject();
        }
    }

    public class CharacterSerializer extends JsonSerializer<Character> {
        @Override
        public void serialize(Character value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            //TODO I should use the ID in the files, using the name only adds confusion
            jgen.writeStringField("characterId", value.characterId );
            jgen.writeNumberField("xp", value.xp);
            jgen.writeEndObject();
        }
    }



/*
    public class MapSerializer extends JsonSerializer<Map> {
        @Override
        public void serialize(Map value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeStringField("mapId", value.mapId);
            jgen.writeEndObject();
        }
    }

    public class StoryTextSerializer extends JsonSerializer<StoryText> {
        @Override
        public void serialize(StoryText value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeStringField("text", value.text);
            jgen.writeStringField("characterName", value.characterName);
            jgen.writeEndObject();
        }
    }
*/

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

    public void saveGame() {
        ObjectMapper mapper  = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        //module.addSerializer(Campaign.class, new CampaignSerializer());
        module.addSerializer(Mission.class, new MissionSerializer());
        module.addSerializer(Character.class, new CharacterSerializer());

        //module.addSerializer(StoryText.class, new StoryTextSerializer());
        mapper.registerModule(module);



        /*mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setVisibility (mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));*/

        try {
            mapper.writeValue(Gdx.files.internal("core/assets/data/games/savegame01.txt").file(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Game loadGame(String gameId) {
        FileHandle file = Gdx.files.internal("core/assets/data/games/"+gameId);
        String jsonData = file.readString();

        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Character.class, new CharacterDeserializer());
        module.addDeserializer(Mission.class, new MissionDeserializer());
        mapper.registerModule(module);

        Game emp = new Game();

        try {

            //System.out.println("Employee Object\n"+jsonData);
            emp = mapper.readValue(jsonData, Game.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return emp;
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
        currentCampaign = gameCampaign.get(0);
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


    public void completeMission (Mission mission) {
        String nextMission = mission.getNextMissionId();
        mission.setCurrentStars(mission.getCurrentStars()+1);

        System.out.println("Attempting to unlock mission "+nextMission);
        int i = 0;
        for (Mission m: currentCampaign.getCampaignMissions()) {
            if (m.getMissionId().equals(nextMission)) {
                m.setUnlocked(true);
                System.out.println("Unlocked "+mission.getMissionId());
            }
            i++;
        }

        if (i==currentCampaign.getCampaignMissions().size()) {
            String nextCampaign = currentCampaign.getNextCampaignId();
            for (Campaign c: gameCampaign) {
                if (c.getCampaignId().equals(nextCampaign)) {
                    c.setUnlocked(true);
                    System.out.println("Unlocked "+nextCampaign);
                }
            }
        }

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
        if (currentParty.size()<MAX_PARTY) {
            character.setFriendly(true);
            this.currentParty.add(character);
        }

    }

    public void removeCurrentParty(Character character) {
            this.currentParty.remove(character);
    }

}
