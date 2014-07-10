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

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.xd.module.options.spi.ModuleOption;

/**
 * Module options for the {@code elasticsearch} sink module
 *
 * @author: Marius Bogoevici
 */
public class ElasticSearchSinkOptionMetadata {


    private String protocol = "http";

    private String hosts = "localhost:9200";

    private String index;

    private String type;

    @NotBlank
    public String getProtocol() {
        return protocol;
    }

    @NotBlank
    public String getHosts() {
        return hosts;
    }

    @NotBlank
    public String getIndex() {
        return index;
    }

    @NotBlank
    public String getType() {
        return type;
    }

    @ModuleOption("Protocol for inserting data to the ElasticSearch instance (e.g. http or https)")
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @ModuleOption("ElasticSearch hosts (comma-separated, use <host>:<port> format)")
    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    @ModuleOption("The index where data is inserted")
    public void setIndex(String index) {
        this.index = index;
    }

    @ModuleOption("The type of the inserted data")
    public void setType(String type) {
        this.type = type;
    }
}
