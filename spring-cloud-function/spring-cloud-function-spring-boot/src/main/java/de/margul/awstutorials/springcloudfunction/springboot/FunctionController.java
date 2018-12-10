package de.margul.awstutorials.springcloudfunction.springboot;

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

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.reactivestreams.Publisher;

import org.springframework.cloud.function.web.RequestProcessor;
import org.springframework.cloud.function.web.RequestProcessor.FunctionWrapper;
import org.springframework.cloud.function.web.constants.WebRequestConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import reactor.core.publisher.Mono;


@Component
public class FunctionController extends org.springframework.cloud.function.web.mvc.FunctionController{

    public FunctionController(RequestProcessor processor) {
super(processor);
        this.processor = processor;
    }

    private RequestProcessor processor;


    @GetMapping(path = "/**")
    @ResponseBody
    public Mono<ResponseEntity<?>> get(WebRequest request) {
        FunctionWrapper wrapper = wrapper(request);
        //return processor.get(wrapper);
        return processor.post(wrapper, null, false);
    }

    private FunctionWrapper wrapper(WebRequest request) {
        @SuppressWarnings("unchecked")
        Function<Publisher<?>, Publisher<?>> function = (Function<Publisher<?>, Publisher<?>>) request
                .getAttribute(WebRequestConstants.FUNCTION, WebRequest.SCOPE_REQUEST);
        @SuppressWarnings("unchecked")
        Consumer<Publisher<?>> consumer = (Consumer<Publisher<?>>) request
                .getAttribute(WebRequestConstants.CONSUMER, WebRequest.SCOPE_REQUEST);
        @SuppressWarnings("unchecked")
        Supplier<Publisher<?>> supplier = (Supplier<Publisher<?>>) request
                .getAttribute(WebRequestConstants.SUPPLIER, WebRequest.SCOPE_REQUEST);
        FunctionWrapper wrapper = RequestProcessor.wrapper(function, consumer, supplier);
        for (String key : request.getParameterMap().keySet()) {
            wrapper.params().addAll(key, Arrays.asList(request.getParameterValues(key)));
        }
        for (Iterator<String> keys = request.getHeaderNames(); keys.hasNext();) {
            String key = keys.next();
            wrapper.headers().addAll(key, Arrays.asList(request.getHeaderValues(key)));
        }
        String argument = (String) request.getAttribute(WebRequestConstants.ARGUMENT,
                WebRequest.SCOPE_REQUEST);
        if (argument != null) {
            //wrapper.argument("{\"name\":\"Detlef\"");
            wrapper.headers().add("name", argument);
        }
        return wrapper;
    }
}
