package com.mygdx.mdh.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mygdx.mdh.game.controller.GameEventListener;
import com.mygdx.mdh.game.model.util.PersonaList;
import com.mygdx.mdh.game.util.CharacterDeserializer;
import com.mygdx.mdh.game.util.MissionDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zubisoft on 13/05/2016.
 */
public class Game {

    List <GameEventListener> listeners;

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
            jgen.writeNumberField("level", value.level);
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

    public void setPersonaList(PersonaList personaList) {
        this.personaList = personaList;
    }

    @JsonIgnore
    PersonaList personaList;


    public boolean isInParty (String characterId) {
        for (Character c: currentParty) {
            if (c.characterId.equals(characterId)) {
                return true;
            }
        }

        return false;

    }

    public boolean isInCollection (String characterId) {
        for (Character c: characterCollection) {
            if (c.characterId.equals(characterId)) {
                return true;
            }
        }

        return false;

    }

    public Game () {
        this.currentParty = new ArrayList<>();
        this.listeners = new ArrayList<>();

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
            mapper.writeValue(Gdx.files.internal("core/assets/data/games/autosave.txt").file(), this);
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

        emp.setPersonaList(new PersonaList(emp.getCharacterCollection()));

        return emp;
    }


    @JsonProperty("characterList")
    public void setCharacterList(List<String> charid) {
        this.characterCollection = new ArrayList<Character>();
        Character c;
        for (String baddy : charid) {
            c = Character.loadFromJSON(baddy);
            c.setFriendly(true);
            this.characterCollection.add(c);

        }

        personaList = new PersonaList(characterCollection);
    }

    public void addCharacterList(List<String> charid) {

        Character c;
        if (charid == null) return;

        for (String cid : charid) {
            c = Character.loadFromJSON(cid);
            c.setFriendly(true);
            if (!isInCollection(cid))
                this.characterCollection.add(c);

        }

        personaList = new PersonaList(characterCollection);
    }

    public PersonaList getPersonaList () {
        return personaList;
    }


    public void setCurrentParty(List<Character> party) {

        this.currentParty = new ArrayList<Character>();
        for(Character p: party) {
            for(Character c: characterCollection) {
                if (p.characterId.equals(c.characterId)) {
                    addCurrentParty(c);
                }
            }

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


        int i = 0;
        for (Mission m: currentCampaign.getCampaignMissions()) {
            if (m.getMissionId().equals(nextMission)) {
                m.setUnlocked(true);
                notifyMissionUnlocked(m);

            }
            if (m.getCurrentStars()>0) i++;
        }

        if (i==currentCampaign.getCampaignMissions().size()) {

            String nextCampaign = currentCampaign.getNextCampaignId();

            for (Campaign c: gameCampaign) {
                if (c.getCampaignId().equals(nextCampaign)) {

                    c.setUnlocked(true);

                }
            }
        }

        this.addCharacterList(mission.unlockedCharacters);

    }

    public void addListener (GameEventListener l) {
        listeners.add(l);
    }

    public void notifyMissionUnlocked (Mission m) {
        for (GameEventListener l: listeners) {
            l.onMissionUnlocked(m);
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

    public Campaign getNextCampaign() {
        int i = this.gameCampaign.indexOf(currentCampaign);
        if (i<0 || i+1>=gameCampaign.size() || !gameCampaign.get(i+1).isUnlocked()) return null;
        return gameCampaign.get(i+1);
    }

    public Campaign getPrevCampaign() {
        int i = this.gameCampaign.indexOf(currentCampaign);
        if (i==-1 || i-1<0 || !gameCampaign.get(i-1).isUnlocked()) return null;
        return gameCampaign.get(i-1);
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

    public boolean addCurrentParty(Character character) {
        if (currentParty.size()<MAX_PARTY) {
            character.setFriendly(true);
            this.currentParty.add(character);
            return true;
        }
        return false;

    }

    public void removeCurrentParty(Character removed) {
        this.currentParty.remove(removed);
    }


}
