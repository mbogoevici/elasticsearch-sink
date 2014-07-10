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

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.springframework.xd.modules.sink.elasticsearch.ElasticSearchSinkOptionMetadata.HttpValidationGroup;
import static org.springframework.xd.modules.sink.elasticsearch.ElasticSearchSinkOptionMetadata.NodeValidationGroup;
import static org.springframework.xd.modules.sink.elasticsearch.ElasticSearchSinkOptionMetadata.SinkMode.http;
import static org.springframework.xd.modules.sink.elasticsearch.ElasticSearchSinkOptionMetadata.SinkMode.node;
import static org.springframework.xd.modules.sink.elasticsearch.ElasticSearchSinkOptionMetadata.SinkMode.transport;
import static org.springframework.xd.modules.sink.elasticsearch.ElasticSearchSinkOptionMetadata.TransportValidationGroup;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author: Marius Bogoevici
 */
public class TestElasticSearchSinkOptionMetadata {

    @Test
    public void testHttpModeActivation() throws Exception {

        ElasticSearchSinkOptionMetadata httpElasticSearchSinkOptionMetadata = new ElasticSearchSinkOptionMetadata();
        httpElasticSearchSinkOptionMetadata.setMode(http);

        Assert.assertThat(httpElasticSearchSinkOptionMetadata.profilesToActivate(), arrayContaining("http"));
        Assert.assertThat(httpElasticSearchSinkOptionMetadata.profilesToActivate(), arrayWithSize(1));

        Assert.assertThat(httpElasticSearchSinkOptionMetadata.groupsToValidate(), arrayContaining((Class)HttpValidationGroup.class));
        Assert.assertThat(httpElasticSearchSinkOptionMetadata.groupsToValidate(), arrayWithSize(1));

    }

    @Test
    public void testTransportModeActivation() throws Exception {

        ElasticSearchSinkOptionMetadata transportElasticSearchSinkOptionMetadata = new ElasticSearchSinkOptionMetadata();
        transportElasticSearchSinkOptionMetadata.setMode(transport);

        Assert.assertThat(transportElasticSearchSinkOptionMetadata.profilesToActivate(), arrayContaining("transport"));
        Assert.assertThat(transportElasticSearchSinkOptionMetadata.profilesToActivate(), arrayWithSize(1));

        Assert.assertThat(transportElasticSearchSinkOptionMetadata.groupsToValidate(), arrayContaining((Class)TransportValidationGroup.class));
        Assert.assertThat(transportElasticSearchSinkOptionMetadata.groupsToValidate(), arrayWithSize(1));
    }

    @Test
    public void testNodeModeActivation() throws Exception {

        ElasticSearchSinkOptionMetadata nodeElasticSearchSinkOptionMetadata = new ElasticSearchSinkOptionMetadata();
        nodeElasticSearchSinkOptionMetadata.setMode(node);

        Assert.assertThat(nodeElasticSearchSinkOptionMetadata.profilesToActivate(), arrayContaining("node"));
        Assert.assertThat(nodeElasticSearchSinkOptionMetadata.profilesToActivate(), arrayWithSize(1));

        Assert.assertThat(nodeElasticSearchSinkOptionMetadata.groupsToValidate(), arrayContaining((Class)NodeValidationGroup.class));
        Assert.assertThat(nodeElasticSearchSinkOptionMetadata.groupsToValidate(), arrayWithSize(1));
    }
}
