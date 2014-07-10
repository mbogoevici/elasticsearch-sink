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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.json.JsonPathUtils;
import org.springframework.util.Assert;

/**
 * A simple generator of URLs for posting data to ElasticSearch, in the form:
 * <p/>
 * {@code http://host:port/index/type}
 * <p/>
 * The main role of this class is to support the round-robin load-balancing multiple ElasticSearch nodes in a cluster.
 *
 * @author: Marius Bogoevici
 */
public class RoundRobinUrlGenerator implements InitializingBean {

    private static final int DEFAULT_PORT = 9200;

    private final AtomicInteger counter = new AtomicInteger(-1);

    private volatile String protocol;

    private volatile List<String> hosts;

    private volatile int hostsSize;

    private volatile String index;

    private volatile String type;

    private volatile String idPath;


    /**
     * Sets the protoc
     *
     * @param protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Sets the list of hosts that the HTTP client will try to connect to
     *
     * @param hostList
     */
    public void setHosts(String hostList) {
        Assert.notNull(hostList, "The list of hosts cannot be null");
        String[] parsedHostList = hostList.split(",");
        ArrayList<String> configuredHosts = new ArrayList<String>();
        for (String hostName : parsedHostList) {
            if (hostName.contains(":")) {
                // The host name contains port information
                configuredHosts.add(hostName);
            } else {
                // Append the default port to this host name
                configuredHosts.add(hostName + ":" + DEFAULT_PORT);
            }
        }
        this.hosts = Collections.unmodifiableList(configuredHosts);
        this.hostsSize = this.hosts.size();
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

    public String getUrl(String payload) throws Exception {
        if (idPath == null) {
            return protocol + "://" + getNextHost() + "/" + index + "/" + type;
        } else {
            return protocol + "://" + getNextHost() + "/" + index + "/" + type + "/" + JsonPathUtils.evaluate(payload, idPath);
        }
    }


    private String getNextHost() {
        // by this point, we can safely assume that there's at least one host definition,
        // or else the bean definition would fail
        if (hostsSize == 1) {
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
