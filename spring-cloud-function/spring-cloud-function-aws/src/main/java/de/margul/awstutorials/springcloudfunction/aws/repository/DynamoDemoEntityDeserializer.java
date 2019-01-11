package de.margul.awstutorials.springcloudfunction.aws.repository;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class DynamoDemoEntityDeserializer extends JsonDeserializer<DynamoDemoEntity> {

    @Override
    public DynamoDemoEntity deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        DynamoDemoEntity entity = new DynamoDemoEntity();
        if (node.has("name")) {
            entity.setName(node.get("name").asText());
        }
        if (node.has("description")) {
            entity.setDescription(node.get("description").asText());
        }

        return entity;
    }

}
