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

import java.util.Collection;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.json.JsonPathUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;

/**
 * MessageHandler implementation that sends data to an ElasticSearch index.
 *
 * @author: Marius Bogoevici
 */
public class ElasticSearchSinkMessageHandler extends AbstractMessageHandler {


    private volatile Client client;

    private volatile String index;

    private volatile String type;

    private volatile String idPath;


    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * The ElasticSearch index where the data will be stored
     *
     * @param index - the name of the index, e.g. 'twitter'
     */
    public void setIndex(String index) {
        this.index = index;
    }


    /**
     * The ElasticSearch type of the stored data
     *
     * @param type - the name of the type, e.g. 'tweet'
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * The JSONPath expression that is used to infer the id of the inserted document
     *
     * @param idPath - JSONPath expression, e.g. '$.id'
     */
    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }

    @Override
    protected void handleMessageInternal(Message<?> message) throws Exception {
        // currently, accepting only Strings as payloads
        if (message.getPayload() instanceof String) {
            IndexRequest request;
            if (idPath == null) {
                request = new IndexRequest(index, type);
            }
            else {
                Object extractedId = JsonPathUtils.evaluate(message.getPayload(), idPath);
                if (!(extractedId instanceof Collection)) {
                    request = new IndexRequest(index, type, extractedId.toString());
                } else {
                    throw new MessageHandlingException(message, "The id must be a single value");
                }
            }
            // TODO: make the charset configurable
            request.source(((String)message.getPayload()).getBytes());
            client.index(request);
        } else {
            throw new MessageHandlingException(message, "Only String payloads are currently supported");
        }
    }

}
