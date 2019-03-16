package de.margul.awstutorials.springcloudfunction.azure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.microsoft.azure.spring.data.cosmosdb.config.AbstractDocumentDbConfiguration;
import com.microsoft.azure.spring.data.cosmosdb.config.DocumentDBConfig;
import com.microsoft.azure.spring.data.cosmosdb.repository.config.EnableDocumentDbRepositories;

import de.margul.awstutorials.springcloudfunction.azure.repository.CosmosDemoEntityDeserializer;
import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;

@Configuration
//@EnableDocumentDbRepositories
public class CosmosDBConfig {//extends AbstractDocumentDbConfiguration {

    //@Value("${azure.cosmosdb.uri}")
    private String uri = "uri";

    //@Value("${azure.cosmosdb.key}")
    private String key ="key";

    //@Value("${azure.cosmosdb.database}")
    private String dbName = "db";

    /*@Override
    public DocumentDBConfig getConfig() {
        return DocumentDBConfig.builder(uri, key, dbName).build();
    }*/


    @Bean
    public Module dynamoDemoEntityDeserializer() {

        SimpleModule module = new SimpleModule();
        module.addDeserializer(IDemoEntity.class, new CosmosDemoEntityDeserializer());
        return module;
    }
}
