/**
 * Phone Home Client
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.phonehome.enums;

import com.blackducksoftware.integration.validator.FieldEnum;

public enum PhoneHomeRequestFieldEnum implements FieldEnum {
    BLACKDUCKNAME("blackDuckName"),
    BLACKDUCKVERSION("blackDuckVersion"),
    PLUGINVERSION("pluginVersion"),
    REGID("regId"),
    THIRDPARTYNAME("thirdPartyName"),
    THIRDPARTYVERSION("thirdPartyVersion");

    private String key;

    private PhoneHomeRequestFieldEnum(final String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}