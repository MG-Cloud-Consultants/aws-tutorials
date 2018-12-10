package de.margul.awstutorials.springcloudfunction.aws.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import org.springframework.cloud.function.context.catalog.FunctionInspector;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestSpringBootApiGatewayRequestHandler
        extends SpringBootRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FunctionInspector inspector;

    public RestSpringBootApiGatewayRequestHandler(Class<?> configurationClass) {
        super(configurationClass);
    }

    public RestSpringBootApiGatewayRequestHandler() {
        super();
    }

    @Override
    protected Object convertEvent(APIGatewayProxyRequestEvent event) {

        Object body = "";
        if (event.getBody() != null) {
            body = deserializeBody(event.getBody());
        }

        if (functionAcceptsMessage()) {
            return new GenericMessage<Object>(body, getHeaders(event));
        } else {
            return body;
        }
    }

    private boolean functionAcceptsMessage() {
        return inspector.isMessage(function());
    }

    private Object deserializeBody(String json) {
        try {
            return mapper.readValue(json, getInputType());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot convert event", e);
        }
    }

    private MessageHeaders getHeaders(APIGatewayProxyRequestEvent event) {
        Map<String, Object> headers = new HashMap<String, Object>();
        if (event.getHeaders() != null) {
            headers.putAll(event.getHeaders());
        }
        headers.put("httpMethod", event.getHttpMethod());
        if (event.getQueryStringParameters() != null) {
            headers.putAll(event.getQueryStringParameters());
        }
        if (event.getPathParameters() != null) {
            headers.putAll(event.getPathParameters());
        }
        headers.put("request", event);
        return new MessageHeaders(headers);
    }

    @Override
    protected APIGatewayProxyResponseEvent convertOutput(Object output) {
        if (functionReturnsMessage(output)) {
            Message<?> message = (Message<?>) output;
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode((Integer) message.getHeaders().getOrDefault("statuscode", HttpStatus.OK.value()))
                    .withHeaders(toResponseHeaders(message.getHeaders())).withBody(serializeBody(message.getPayload()));
        } else {
            return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatus.OK.value())
                    .withBody(serializeBody(output));

        }
    }

    private boolean functionReturnsMessage(Object output) {
        return output instanceof Message;
    }

    private Map<String, String> toResponseHeaders(MessageHeaders messageHeaders) {
        Map<String, String> responseHeaders = new HashMap<String, String>();
        messageHeaders.forEach((key, value) -> responseHeaders.put(key, value.toString()));
        return responseHeaders;
    }

    private String serializeBody(Object body) {
        try {
            return mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot convert output", e);
        }
    }

}
