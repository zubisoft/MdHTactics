package com.mygdx.mdh.game.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.mygdx.mdh.game.model.Character;
import java.io.IOException;

/**
 * Created by zubisoft on 03/09/2016.
 */

public class CharacterDeserializer extends StdDeserializer<Character> {

    public CharacterDeserializer() {
        this(null);
    }

    public CharacterDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Character deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String characterId = node.get("characterId").asText();
        int xp = (Integer) ((IntNode) node.get("xp")).numberValue();
        //int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
        Character c = Character.loadFromJSON(characterId);
        c.setXp(xp);
        return c;
    }
}