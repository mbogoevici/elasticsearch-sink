/*
 * Copyright 2014 the original author or authors.
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

package org.springframework.xd.modules.sink.elasticsearch;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author: Marius Bogoevici
 */
public class MockUtils {

    public static final String SOURCE_INPUT = "{'text':'#WorldCup is cool','id':'1'}";

    public static Client mockingClientForIndexRequestsWithoutId() {
        // the request will not include an id
        Client mock = Mockito.mock(Client.class);
        IndexRequest indexRequest = getIndexRequestWithoutId();
        Mockito.when(mock.index(indexRequest)).thenReturn(Mockito.mock(ActionFuture.class));
        return mock;
    }

    public static Client mockingClientForIndexRequestsWithId() {
        // the request will not include an id
        Client mock = Mockito.mock(Client.class);
        IndexRequest indexRequest = getIndexRequestWithId();
        Mockito.when(mock.index(indexRequest)).thenReturn(Mockito.mock(ActionFuture.class));
        return mock;
    }

    public static RestTemplate mockingRestTemplate() {
        RestTemplate mock = Mockito.mock(RestTemplate.class);
        Mockito.when(
                mock.exchange("http://localhost:9200/twitter/tweet",
                       HttpMethod.POST, new HttpEntity<Object>(SOURCE_INPUT),
                        (Class<Object>)null)).thenReturn(new ResponseEntity<Object>(HttpStatus.OK));
        return mock;
    }

    public static IndexRequest getIndexRequestWithoutId() {
        IndexRequest indexRequest = new IndexRequest("twitter","tweet");
        indexRequest.source(SOURCE_INPUT.getBytes());
        return indexRequest;
    }

    public static IndexRequest getIndexRequestWithId() {
        IndexRequest indexRequest = new IndexRequest("twitter","tweet","1");
        indexRequest.source(SOURCE_INPUT.getBytes());
        return indexRequest;
    }
}
