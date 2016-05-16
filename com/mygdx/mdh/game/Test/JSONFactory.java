package com.mygdx.mdh.game.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;


import java.io.IOException;

public class JSONFactory {

    /**
     * Treat all schemas as "seen" so that model schemas are never inlined.
     */
    /*
    public static class VisitorContextWithoutSchemaInlining extends VisitorContext {
        @Override
        public String addSeenSchemaUri(JavaType aSeenSchema) {
            return getSeenSchemaUri(aSeenSchema);
        }

        @Override
        public String getSeenSchemaUri(JavaType aSeenSchema) {
            return isModel(aSeenSchema) ? javaTypeToUrn(aSeenSchema) : null;
        }

        protected boolean isModel(JavaType type) {
            return type.getRawClass() != String.class
                    && !isBoxedPrimitive(type)
                    && !type.isPrimitive()
                    && !type.isMapLikeType()
                    && !type.isCollectionLikeType();
        }

        protected static boolean isBoxedPrimitive(JavaType type) {
            return type.getRawClass() == Boolean.class
                    || type.getRawClass() == Byte.class
                    || type.getRawClass() == Long.class
                    || type.getRawClass() == Integer.class
                    || type.getRawClass() == Short.class
                    || type.getRawClass() == Float.class
                    || type.getRawClass() == Double.class;
        }
    }

    public static class Animal {
        public String species;
    }

    public static class Zoo {
        public String name;
        public List<Animal> animals;
    }

    public static void readJSON(String arg) throws Exception {
        ObjectMapper m = new ObjectMapper();
        Class generateSchemaFor = Zoo.class;

        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        visitor.setVisitorContext(new VisitorContextWithoutSchemaInlining());

        m.acceptJsonFormatVisitor(m.constructType(generateSchemaFor), visitor);
        JsonSchema jsonSchema = visitor.finalSchema();

        System.out.println(m.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema));
    }


    public class ItemDeserializer extends JsonDeserializer<Item> {

        @Override
        public Item deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            JsonNode node = jp.getCodec().readTree(jp);
            int id = (Integer) ((IntNode) node.get("id")).numberValue();
            String itemName = node.get("itemName").asText();
            int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();

            return new Item(id, itemName, new User(userId, null));
        }
    }  */
}