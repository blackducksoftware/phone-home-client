/**
 * phone-home-client
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
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
package com.blackducksoftware.integration.phonehome;

import java.net.URL;
import java.util.concurrent.Callable;

import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.util.IntEnvironmentVariables;

public abstract class PhoneHomeCallable implements Callable<Boolean> {
    private final IntLogger logger;
    private final PhoneHomeClient client;
    private final URL productURL;
    private final String artifactId;
    private final String artifactVersion;
    private final IntEnvironmentVariables intEnvironmentVariables;

    public PhoneHomeCallable(final IntLogger logger, final PhoneHomeClient client, final URL productURL, final String artifactId, final String artifactVersion,
            final IntEnvironmentVariables intEnvironmentVariables) {
        this.logger = logger;
        this.client = client;
        this.productURL = productURL;
        this.artifactId = artifactId;
        this.artifactVersion = artifactVersion;
        this.intEnvironmentVariables = intEnvironmentVariables;
    }

    public abstract PhoneHomeRequestBody.Builder createPhoneHomeRequestBodyBuilder();

    /**
     * This will add the artifactId, artifactVersion and hostName to the PhoneHomeRequestBody.Builder as well as creating the PhoneHomeRequestBody.
     */
    public PhoneHomeRequestBody createPhoneHomeRequestBody() {
        final PhoneHomeRequestBody.Builder phoneHomeRequestBodyBuilder = createPhoneHomeRequestBodyBuilder();
        phoneHomeRequestBodyBuilder.setArtifactId(artifactId);
        phoneHomeRequestBodyBuilder.setArtifactVersion(artifactVersion);
        phoneHomeRequestBodyBuilder.setHostName(productURL.toString());
        return phoneHomeRequestBodyBuilder.build();
    }

    @Override
    public Boolean call() throws Exception {
        Boolean result = Boolean.FALSE;
        try {
            logger.debug("starting phone home");
            final PhoneHomeRequestBody phoneHomeRequestBody = createPhoneHomeRequestBody();
            client.postPhoneHomeRequest(phoneHomeRequestBody, intEnvironmentVariables.getVariables());
            result = Boolean.TRUE;
            logger.debug("completed phone home");
        } catch (final Exception ex) {
            logger.debug("Phone home error.", ex);
        }

        return result;
    }
}
