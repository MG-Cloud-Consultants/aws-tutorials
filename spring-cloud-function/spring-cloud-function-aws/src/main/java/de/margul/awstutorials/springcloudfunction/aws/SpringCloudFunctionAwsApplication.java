package de.margul.awstutorials.springcloudfunction.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.margul.awstutorials.springcloudfunction.logic.DeleteEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.GetEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;
import de.margul.awstutorials.springcloudfunction.logic.UpdateEntityFunction;
import de.margul.awstutorials.springcloudfunction.aws.repository.DynamoDemoEntityDeserializer;
import de.margul.awstutorials.springcloudfunction.logic.CreateEntityFunction;

@SpringBootApplication
public class SpringCloudFunctionAwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudFunctionAwsApplication.class, args);
    }

    @Bean
    public GetEntityFunction getEntityFunction() {
        return new GetEntityFunction();
    }

    @Bean
    public CreateEntityFunction createEntityFunction() {
        return new CreateEntityFunction();
    }

    @Bean
    public UpdateEntityFunction updateEntityFunction() {
        return new UpdateEntityFunction();
    }

    @Bean
    public DeleteEntityFunction deleteEntityFunction() {
        return new DeleteEntityFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper defaultObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(IDemoEntity.class, new DynamoDemoEntityDeserializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(module);
        return mapper;
    }
}
