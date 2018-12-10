package de.margul.awstutorials.springcloudfunction.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.web.RequestProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import de.margul.awstutorials.springcloudfunction.logic.GetEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.StoreEntityFunction;

@SpringBootApplication
public class SpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }

    @Bean
    public GetEntityFunction getEntity() {
        return new GetEntityFunction();
    }

    @Bean
    public StoreEntityFunction storeEntity() {
        return new StoreEntityFunction();
    }

    @Bean
    @Autowired
    @Primary
    public org.springframework.cloud.function.web.mvc.FunctionController getFunctionController(
            RequestProcessor processor) {
        return new FunctionController(processor);
    }
}
