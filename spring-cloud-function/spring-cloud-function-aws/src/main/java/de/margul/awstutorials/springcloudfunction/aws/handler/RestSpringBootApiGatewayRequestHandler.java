/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.margul.awstutorials.springcloudfunction.aws.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import org.springframework.cloud.function.context.catalog.FunctionInspector;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;

/**
 * This class is a modification of
 * {@link org.springframework.cloud.function.adapter.aws.SpringBootApiGatewayRequestHandler},
 * implemented by Dave Syer and Oleg Zhurakousky and published under Apache 2.0
 * License.
 * 
 * Modifications are as follows: - Modified
 * {@link #convertEvent(APIGatewayProxyRequestEvent) convertEvent} method in the
 * way that is also can handle events without body (which is the case for GET
 * and DELETE requests) - Modified
 * {@link #getHeaders(APIGatewayProxyRequestEvent) getHeaders} method in the way
 * that now also extracts HTTP method, query as well as path parameters from the
 * request event) - Modified {@link #result(Object, Publisher<?>) result} in the
 * way that it returns an APIGatewayProxyResponseEvent, even if the called
 * function was a Consumer, i. e. didn't return any results
 */
public class RestSpringBootApiGatewayRequestHandler
        extends SpringBootRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FunctionInspector inspector;

    @Autowired
    ApplicationContext ctx;

    public RestSpringBootApiGatewayRequestHandler(Class<?> configurationClass) {
        super(configurationClass);
    }

    public RestSpringBootApiGatewayRequestHandler() {
        super();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        initialize();
        Object input = convertEvent(event);
        Publisher<?> output = apply(extract(input));
        return result(input, output);
    }

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
        if (event.getQueryStringParameters() != null) {
            headers.putAll(event.getQueryStringParameters());
        }
        if (event.getPathParameters() != null) {
            headers.putAll(event.getPathParameters());
        }
        headers.put("httpMethod", event.getHttpMethod());
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

    private APIGatewayProxyResponseEvent result(Object input, Publisher<?> output) {
        List<Object> result = new ArrayList<>();
        for (Object value : Flux.from(output).toIterable()) {
            result.add(value);
        }
        if (isSingleValue(input) && result.size() == 1) {
            return (APIGatewayProxyResponseEvent) convertOutput(result.get(0));
        }
        return (APIGatewayProxyResponseEvent) convertOutput(result);
    }

    private Flux<?> extract(Object input) {
        if (input instanceof Collection) {
            return Flux.fromIterable((Iterable<?>) input);
        }
        return Flux.just(input);
    }

    private boolean isSingleValue(Object input) {
        return !(input instanceof Collection);
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
