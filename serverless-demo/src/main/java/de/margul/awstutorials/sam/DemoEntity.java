package de.margul.awstutorials.sam;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoEntity {

    private int id;

    private String description;

    public DemoEntity() {

    }

    public DemoEntity(String json) {
        super();

        try {
            ObjectMapper mapper = new ObjectMapper();
            DemoEntity entity = mapper.readValue(json, DemoEntity.class);
            this.id = entity.getId();
            this.description = entity.getDescription();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse body: ", e);
        }
    }

    public DemoEntity(int id, String description) {
        super();
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to serialize: ", e);
        }
    }
}
