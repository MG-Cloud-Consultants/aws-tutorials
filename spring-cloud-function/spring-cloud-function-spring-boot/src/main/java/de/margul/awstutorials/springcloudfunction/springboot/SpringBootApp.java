package de.margul.awstutorials.springcloudfunction.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.web.RequestProcessor;
import org.springframework.context.annotation.Bean;
import de.margul.awstutorials.springcloudfunction.logic.DeleteEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.GetEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.UpdateEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.CreateEntityFunction;

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
    public CreateEntityFunction createEntity() {
        return new CreateEntityFunction();
    }

    @Bean
    public UpdateEntityFunction updateEntity() {
        return new UpdateEntityFunction();
    }

    @Bean
    public DeleteEntityFunction deleteEntity() {
        return new DeleteEntityFunction();
    }

    @Bean
    public RestfulFunctionController restfulFunctionController(RequestProcessor processor) {
        return new RestfulFunctionController(processor);
    }

    @Bean
    public RestfulFunctionHandlerMapping functionHandlerMapping(FunctionCatalog catalog,
            RestfulFunctionController controller) {
        return new RestfulFunctionHandlerMapping(catalog, controller);
    }
}
