package de.margul.awstutorials.springcloudfunction.azure.handler;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;

import reactor.core.publisher.Flux;

public class HttpAzureSpringBootRequestHandler<I>
        extends AzureSpringBootRequestHandler<HttpRequestMessage<I>, HttpResponseMessage> {

    private HttpRequestMessage<I> input;

    public HttpAzureSpringBootRequestHandler(Class<?> configurationClass) {
        super(configurationClass);
    }

    public HttpAzureSpringBootRequestHandler() {
        super();
    }

    @Override
    public HttpResponseMessage handleRequest(HttpRequestMessage<I> input, ExecutionContext context) {
        String name = null;
        this.input = input;
        try {
            if (context != null) {
                name = context.getFunctionName();
                context.getLogger().info("Handler processing a request for: " + name);
            }
            initialize(context);

            Function<Publisher<?>, Publisher<?>> function = lookup(name);
            Publisher<?> events = extract(function, convertEvent(input));
            Publisher<?> output = function.apply(events);
            return result(function, input, output);
        } catch (Throwable ex) {
            if (context != null) {
                context.getLogger().throwing(getClass().getName(), "handle", ex);
            }
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            }
            if (ex instanceof Error) {
                throw (Error) ex;
            }
            throw new UndeclaredThrowableException(ex);
        } finally {
            if (context != null) {
                context.getLogger().fine("Handler processed a request for: " + name);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Object convertEvent(HttpRequestMessage<I> event) {

        if (event.getBody() != null) {
            return new GenericMessage<I>(event.getBody(), getHeaders(event));
        } else {
            return new GenericMessage<Optional>(Optional.empty(), getHeaders(event));
        }
    }

    private MessageHeaders getHeaders(HttpRequestMessage<I> event) {
        Map<String, Object> headers = new HashMap<String, Object>();
        if (event.getHeaders() != null) {
            headers.putAll(event.getHeaders());
        }
        if (event.getQueryParameters() != null) {
            headers.putAll(event.getQueryParameters());
        }
        headers.put("httpMethod", event.getHttpMethod());
        headers.put("request", event);
        return new MessageHeaders(headers);
    }

    private Flux<?> extract(Function<?, ?> function, Object input) {
        // if (!isSingleInput(function, input)) {
        if (input instanceof Collection) {
            return Flux.fromIterable((Iterable<?>) input);
        }
        // }
        return Flux.just(input);
    }

    private HttpResponseMessage result(Function<?, ?> function, Object input, Publisher<?> output) {

        List<Object> result = new ArrayList<>();
        for (Object value : Flux.from(output).toIterable()) {
            result.add(convertOutput(value));
        }
        if (isSingleInput(function, input) && result.size() == 1) {
            HttpResponseMessage value = (HttpResponseMessage) result.get(0);
            return value;
        }
        if (isSingleOutput(function, input) && result.size() == 1) {
            HttpResponseMessage value = (HttpResponseMessage) result.get(0);
            return value;
        }
        return this.input.createResponseBuilder(com.microsoft.azure.functions.HttpStatus.OK).build();
    }

    @Override
    protected HttpResponseMessage convertOutput(Object output) {
        if (functionReturnsMessage(output)) {

            Message<?> message = (Message<?>) output;
            return this.input.createResponseBuilder(com.microsoft.azure.functions.HttpStatus.OK)
                    .body(message.getPayload()).build();
        } else {

            return this.input.createResponseBuilder(com.microsoft.azure.functions.HttpStatus.OK).body(output).build();

        }
    }

    private boolean functionReturnsMessage(Object output) {
        return output instanceof Message;
    }
}
