package de.margul.awstutorials.springcloudfunction.azure.repository;


import org.springframework.data.annotation.Id;

import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;

import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;

@Document(collection = "DemoEntity")
public class CosmosDemoEntity implements IDemoEntity {

    @Id
    private String name;
    private String description;

    @Override
    public String getName() {

        return name;
    }

    @Override
    public void setName(String name) {

        this.name = name;
    }

    @Override
    public String getDescription() {

        return this.description;
    }

    @Override
    public void setDescription(String description) {

        this.description = description;
    }
}
