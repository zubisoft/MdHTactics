package com.mygdx.mdh.game.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.mygdx.mdh.game.model.Mission;

import java.io.IOException;

/**
 * Created by zubisoft on 03/09/2016.
 */

public class MissionDeserializer extends StdDeserializer<Mission> {

    public MissionDeserializer() {
        this(null);
    }

    public MissionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Mission deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String characterId = node.get("missionId").asText();
        int currentStars = (Integer) ((IntNode) node.get("currentStars")).numberValue();
        boolean unlocked = (Boolean)((BooleanNode) node.get("unlocked")).booleanValue();
        //int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
        Mission m = Mission.loadFromJSON(characterId);
        m.setCurrentStars(currentStars);
        m.setUnlocked(unlocked);

        return m;
    }
}