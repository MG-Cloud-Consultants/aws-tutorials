package de.margul.awstutorials.springcloudfunction.azure.handler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import de.margul.awstutorials.springcloudfunction.azure.data.CosmosDemoEntity;
import de.margul.awstutorials.springcloudfunction.logic.CreateEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.DeleteEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.GetEntityFunction;
import de.margul.awstutorials.springcloudfunction.logic.UpdateEntityFunction;

@SpringBootApplication
@ComponentScan(basePackages = { "de.margul.awstutorials.springcloudfunction.azure.repository" })
public class DemoFunctionHandler extends HttpAzureSpringBootRequestHandler<CosmosDemoEntity> {
    @FunctionName("getEntityFunction")
    public HttpResponseMessage executeGet(@HttpTrigger(name = "req", methods = {
            HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS, route = "entities/{name:regex(^[a-zA-Z0-9]*$)}") HttpRequestMessage<CosmosDemoEntity> request,
            ExecutionContext context, @BindingName("name") String name) {
        return handleRequest(request, context);
    }

    @FunctionName("deleteEntityFunction")
    public HttpResponseMessage executeDelete(@HttpTrigger(name = "req", methods = {
            HttpMethod.DELETE }, authLevel = AuthorizationLevel.ANONYMOUS, route = "entities/{name:regex(^[a-zA-Z0-9]*$)}") HttpRequestMessage<CosmosDemoEntity> request,
            ExecutionContext context, @BindingName("name") String name) {
        return handleRequest(request, context);
    }

    @FunctionName("createEntityFunction")
    public HttpResponseMessage executeCreate(@HttpTrigger(name = "req", methods = {
            HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS, route = "entities") HttpRequestMessage<CosmosDemoEntity> request,
            ExecutionContext context) {
        return handleRequest(request, context);
    }

    @FunctionName("updateEntityFunction")
    public HttpResponseMessage executeUpdate(@HttpTrigger(name = "req", methods = {
            HttpMethod.PUT }, authLevel = AuthorizationLevel.ANONYMOUS, route = "entities/{name:regex(^[a-zA-Z0-9]*$)}") HttpRequestMessage<CosmosDemoEntity> request,
            ExecutionContext context, @BindingName("name") String name) {
        return handleRequest(request, context);
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoFunctionHandler.class, args);
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
}
