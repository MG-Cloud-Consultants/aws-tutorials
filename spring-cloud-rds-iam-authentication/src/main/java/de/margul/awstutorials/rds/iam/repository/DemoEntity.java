package de.margul.awstutorials.rds.iam.repository;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DemoEntity {

    @Id
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DemoEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public DemoEntity() {

    }
}
