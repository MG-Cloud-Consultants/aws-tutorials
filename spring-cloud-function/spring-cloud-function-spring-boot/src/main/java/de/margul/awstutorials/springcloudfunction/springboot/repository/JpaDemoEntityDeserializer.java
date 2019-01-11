package de.margul.awstutorials.springcloudfunction.springboot.repository;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class JpaDemoEntityDeserializer extends JsonDeserializer<JpaDemoEntity> {

    @Override
    public JpaDemoEntity deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JpaDemoEntity entity = new JpaDemoEntity();
        if (node.has("name")) {
            entity.setName(node.get("name").asText());
        }
        if (node.has("description")) {
            entity.setDescription(node.get("description").asText());
        }

        return entity;
    }

}
