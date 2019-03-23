package de.margul.awstutorials.springcloudfunction.azure.data;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class CosmosDemoEntityDeserializer extends JsonDeserializer<CosmosDemoEntity> {

    @Override
    public CosmosDemoEntity deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        CosmosDemoEntity entity = new CosmosDemoEntity();
        if (node.has("name")) {
            entity.setName(node.get("name").asText());
        }
        if (node.has("description")) {
            entity.setDescription(node.get("description").asText());
        }

        return entity;
    }

}
