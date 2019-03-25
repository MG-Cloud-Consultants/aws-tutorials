package de.margul.awstutorials.springcloudfunction.azure.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.microsoft.azure.spring.data.cosmosdb.config.AbstractDocumentDbConfiguration;
import com.microsoft.azure.spring.data.cosmosdb.config.DocumentDBConfig;
import com.microsoft.azure.spring.data.cosmosdb.repository.config.EnableDocumentDbRepositories;

import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;

@Configuration
@EnableDocumentDbRepositories
public class CosmosDBConfig extends AbstractDocumentDbConfiguration {

    @Override
    public DocumentDBConfig getConfig() {
        return DocumentDBConfig.builder(System.getenv("CosmosDB_Endpoint"), System.getenv("CosmosDB_Key"),
                System.getenv("CosmosDB_Collection")).build();
    }

    @Bean
    public Module dynamoDemoEntityDeserializer() {

        SimpleModule module = new SimpleModule();
        module.addDeserializer(IDemoEntity.class, new CosmosDemoEntityDeserializer());
        return module;
    }
}
