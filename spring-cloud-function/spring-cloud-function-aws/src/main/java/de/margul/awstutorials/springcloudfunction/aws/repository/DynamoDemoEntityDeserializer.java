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
        String itemName = node.get("name").asText();

        DynamoDemoEntity entity = new DynamoDemoEntity();
        entity.setName(itemName);

        return entity;
    }

}
