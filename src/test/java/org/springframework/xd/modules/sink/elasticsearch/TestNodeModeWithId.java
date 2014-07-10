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

import static org.springframework.xd.modules.sink.elasticsearch.MockUtils.SOURCE_INPUT;
import static org.springframework.xd.modules.sink.elasticsearch.MockUtils.getIndexRequestWithId;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: Marius Bogoevici
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:config/elasticsearch.xml","classpath:elasticsearch/sink/test-client-with-id.xml"})
@ActiveProfiles("node")
public class TestNodeModeWithId {

    @Autowired @Qualifier("input")
    MessageChannel outputChannel;

    @Autowired
    Client client;

    @Test
    public void testSinkPassthrough() throws Exception {
        outputChannel.send(MessageBuilder.withPayload(SOURCE_INPUT).build());
        Mockito.verify(client, Mockito.times(1)).index((IndexRequest) Mockito.argThat(new ReflectionEquals(getIndexRequestWithId())));
    }

}
