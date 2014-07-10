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

import static org.springframework.xd.modules.sink.elasticsearch.ElasticSearchSinkOptionMetadata.SinkMode.transport;

import javax.validation.groups.Default;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.xd.module.options.spi.ModuleOption;
import org.springframework.xd.module.options.spi.ProfileNamesProvider;
import org.springframework.xd.module.options.spi.ValidationGroupsProvider;

/**
 * Module options for the {@code ElasticSearch} sink module
 *
 * @author: Marius Bogoevici
 */
public class ElasticSearchSinkOptionMetadata implements ProfileNamesProvider,ValidationGroupsProvider {

    // Supported sink modes

    public static enum SinkMode {
        http,
        transport,
        node;
    }


    // Validation groups for parameters

    protected interface HttpValidationGroup extends Default {

    }

    protected interface TransportValidationGroup extends Default {

    }

    protected interface NodeValidationGroup extends Default {

    }

    // Settings

    private String protocol = "http";

    private String hosts = "localhost:9300";

    private String index;

    private String type;

    private String idPath;

    private String clusterName;

    private SinkMode mode = transport;

    @NotBlank(groups = HttpValidationGroup.class)
    public String getProtocol() {
        return protocol;
    }

    @NotBlank(groups = {HttpValidationGroup.class,NodeValidationGroup.class})
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

    @NotBlank(groups = HttpValidationGroup.class)
    public String getHttpMethod() {
        return idPath != null ? "PUT" : "POST";
    }

    @NotBlank(groups = {TransportValidationGroup.class,NodeValidationGroup.class})
    public String getClusterName() {
        return clusterName;
    }

    public String getIdPath() {
        return idPath;
    }

    @ModuleOption("The ElasticSearch cluster")
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
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

    @ModuleOption("The sink mode (can be 'http','transport' or 'node')")
    public void setMode(SinkMode mode) {
        this.mode = mode;
    }

    @ModuleOption("The JSON path to be used as id when indexing")
    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }


    @Override
    public String[] profilesToActivate() {
        return new String[] { mode.toString() };
    }

    @Override
    public Class<?>[] groupsToValidate() {
        switch (mode) {
            case http: {
                return new Class[] { HttpValidationGroup.class };
            }
            case transport: {
                return new Class[] { TransportValidationGroup.class };
            }
            case node: {
                return new Class[] { NodeValidationGroup.class };
            }
            default: {
                throw new IllegalStateException("Invalid mode: " + mode);
            }
        }
    }

}
