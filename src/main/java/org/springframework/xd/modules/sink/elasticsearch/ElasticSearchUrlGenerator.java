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

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple URL generator for posting data to ElasticSearch
 *
 * @author: Marius Bogoevici
 */
public class ElasticSearchUrlGenerator implements InitializingBean {

    private static final int DEFAULT_PORT = 9200;

    private final AtomicInteger counter = new AtomicInteger(-1);

    private volatile String protocol;

    private volatile List<String> hosts;

    private volatile String index;

    private volatile String type;

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHosts(String hostList) {
        Assert.notNull(hostList, "The list of hostList cannot be null");
        String[] hostsAsSplitArray = hostList.split(",");
        ArrayList<String> configuredHosts = new ArrayList<String>();
        for (String hostName : hostsAsSplitArray) {
            if (hostName.contains(":")) {
                // The host name contains port information
                configuredHosts.add(hostName);
            } else {
                // Append the default port to this host name
                configuredHosts.add(hostName + ":" + DEFAULT_PORT);
            }
        }
        this.hosts = Collections.unmodifiableList(configuredHosts);
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(protocol, "Protocol cannot be null");
        if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
            throw new BeanCreationException("Protocol must be either 'http' or 'https'");
        }
        if (hosts == null || hosts.size() == 0) {
            throw new BeanCreationException("At least one host must be configured");
        }
        if (index == null || "".equals(index)) {
            throw new BeanCreationException("An ElasticSearch index must be configured");
        }
        if (type == null || "".equals(type)) {
            throw new BeanCreationException("An ElasticSearch type must be configured");
        }
    }

    public String getUrl() {
        return protocol + "://" + getNextHost() + "/" + index + "/" + type;
    }


    private String getNextHost() {
        // we can safely assume a
        if (hosts.size() == 1) {
            return hosts.get(0);
        } else {
            int nextCounter = counter.incrementAndGet() % hosts.size();
            if (nextCounter < 0) {
                // correct the value for negative modulus - i.e. on overflow
                nextCounter += hosts.size();
            }
            return hosts.get(nextCounter);
        }
    }
}
