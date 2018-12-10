package de.margul.awstutorials.springcloudfunction.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.margul.awstutorials.springcloudfunction.logic.GetEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.StoreEntityFunction;

@SpringBootApplication
public class SpringCloudFunctionAwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudFunctionAwsApplication.class, args);
    }

    @Bean
    public GetEntityFunction getQueryFunction() {
        return new GetEntityFunction();
    }

    @Bean
    public StoreEntityFunction getStoreFunction() {
        return new StoreEntityFunction();
    }
}
