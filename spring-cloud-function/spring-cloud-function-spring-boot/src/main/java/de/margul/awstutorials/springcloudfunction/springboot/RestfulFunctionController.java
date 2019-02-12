/*
 * Copyright 2016-2017 the original author or authors.
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
package de.margul.awstutorials.springcloudfunction.springboot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.function.web.RequestProcessor;
import org.springframework.cloud.function.web.RequestProcessor.FunctionWrapper;
import org.springframework.cloud.function.web.constants.WebRequestConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import reactor.core.publisher.Mono;

/**
 * This class is a modification of
 * {@link org.springframework.cloud.function.web.mvc.FunctionController},
 * implemented by Dave Syer and Mark Fisher and published under Apache 2.0
 * License.
 * 
 * Modifications are as follows:
 * - Added mappings for PUT and DELETE requests
 * - Removed mappings for streamings
 * - Modified {@link #get(WebRequest) get} method in the way that is also calls {@link org.springframework.cloud.function.web.RequestProcessor#post(FunctionWrapper, String, boolean) post}
 * - Modified {@link #wrapper(WebRequest) in a way that it now splits the request URL and considers all parts as path parameters
 */
@Component
public class RestfulFunctionController {

    public RestfulFunctionController(RequestProcessor processor) {
        this.processor = processor;
    }

    private RequestProcessor processor;

    @DeleteMapping(path = "/**")
    @ResponseBody
    public Mono<ResponseEntity<?>> delete(WebRequest request) {
        FunctionWrapper wrapper = wrapper(request);
        return processor.post(wrapper, null, false);
    }

    @GetMapping(path = "/**")
    @ResponseBody
    public Mono<ResponseEntity<?>> get(WebRequest request) {
        FunctionWrapper wrapper = wrapper(request);
        return processor.post(wrapper, null, false);
    }

    @PutMapping(path = "/**")
    @ResponseBody
    public Mono<ResponseEntity<?>> put(WebRequest request, @RequestBody(required = false) String body) {
        FunctionWrapper wrapper = wrapper(request);
        return processor.post(wrapper, body, false);
    }

    @PostMapping(path = "/**")
    @ResponseBody
    public Mono<ResponseEntity<?>> post(WebRequest request, @RequestBody(required = false) String body) {
        FunctionWrapper wrapper = wrapper(request);
        return processor.post(wrapper, body, false);
    }

    @Value("${spring.cloud.function.web.pathparametermappings}")
    private String[] pathParameterMappings;

    private FunctionWrapper wrapper(WebRequest request) {
        @SuppressWarnings("unchecked")
        Function<Publisher<?>, Publisher<?>> function = (Function<Publisher<?>, Publisher<?>>) request
                .getAttribute(WebRequestConstants.FUNCTION, WebRequest.SCOPE_REQUEST);
        @SuppressWarnings("unchecked")
        Consumer<Publisher<?>> consumer = (Consumer<Publisher<?>>) request.getAttribute(WebRequestConstants.CONSUMER,
                WebRequest.SCOPE_REQUEST);
        @SuppressWarnings("unchecked")
        Supplier<Publisher<?>> supplier = (Supplier<Publisher<?>>) request.getAttribute(WebRequestConstants.SUPPLIER,
                WebRequest.SCOPE_REQUEST);
        FunctionWrapper wrapper = RequestProcessor.wrapper(function, consumer, supplier);
        for (String key : request.getParameterMap().keySet()) {
            wrapper.params().addAll(key, Arrays.asList(request.getParameterValues(key)));
            
            // Add params also to headers (as we want to be query params to be available
            // later as headers of org.springframework.messaging.Message
            wrapper.headers().addAll(key, Arrays.asList(request.getParameterValues(key)));
        }
        for (Iterator<String> keys = request.getHeaderNames(); keys.hasNext();) {
            String key = keys.next();
            wrapper.headers().addAll(key, Arrays.asList(request.getHeaderValues(key)));
        }
        String argument = (String) request.getAttribute(WebRequestConstants.ARGUMENT, WebRequest.SCOPE_REQUEST);
        if (argument != null) {

            List<String> pathParameters = Arrays.asList(argument.split("/"));
            
            // Add path parameters as headers. The mapping between position in the request
            // URL and header name must be done as comma-separated list as the
            // spring.cloud.function.web.pathparametermappings property
            for (int i = 0; i < pathParameterMappings.length && i < pathParameters.size(); i++) {
                wrapper.headers().add(pathParameterMappings[i], pathParameters.get(i));
            }
        }
        return wrapper;
    }
}
