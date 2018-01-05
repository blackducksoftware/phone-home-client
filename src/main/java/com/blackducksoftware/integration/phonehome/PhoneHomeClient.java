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

import java.net.MalformedURLException;
import java.net.URL;

import com.blackducksoftware.integration.exception.IntegrationException;
import com.blackducksoftware.integration.hub.proxy.ProxyInfo;
import com.blackducksoftware.integration.hub.request.HubRequest;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.blackducksoftware.integration.hub.rest.UnauthenticatedRestConnectionBuilder;
import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.phonehome.exception.PhoneHomeException;

public class PhoneHomeClient {
    public static final String PHONE_HOME_BACKEND = "https://collect.blackducksoftware.com";
    private final IntLogger logger;
    private URL phoneHomeBackendUrl;
    private final int timeout;
    private final ProxyInfo proxyInfo;
    private final boolean alwaysTrustServerCertificate;

    public PhoneHomeClient(final IntLogger logger, final int timeout, final ProxyInfo proxyInfo, final boolean alwaysTrustServerCertificate) {
        this.logger = logger;
        try {
            this.phoneHomeBackendUrl = new URL(PHONE_HOME_BACKEND);
        } catch (final MalformedURLException e) {
            phoneHomeBackendUrl = null;
        }
        this.timeout = timeout;
        this.proxyInfo = proxyInfo;
        this.alwaysTrustServerCertificate = alwaysTrustServerCertificate;
    }

    public PhoneHomeClient(final IntLogger logger, final URL phoneHomeBackendUrl, final int timeout, final ProxyInfo proxyInfo, final boolean alwaysTrustServerCertificate) {
        this.logger = logger;
        this.phoneHomeBackendUrl = phoneHomeBackendUrl;
        this.timeout = timeout;
        this.proxyInfo = proxyInfo;
        this.alwaysTrustServerCertificate = alwaysTrustServerCertificate;
    }

    public void postPhoneHomeRequest(final PhoneHomeRequestBody phoneHomeRequestBody) throws PhoneHomeException {
        if (phoneHomeBackendUrl == null) {
            throw new PhoneHomeException("No phone home server found.");
        }
        logger.debug("Phoning home to " + phoneHomeBackendUrl);

        final UnauthenticatedRestConnectionBuilder builder = new UnauthenticatedRestConnectionBuilder();
        builder.setLogger(logger);
        builder.setBaseUrl(phoneHomeBackendUrl.toString());
        builder.setTimeout(timeout);
        builder.applyProxyInfo(proxyInfo);
        builder.setAlwaysTrustServerCertificate(alwaysTrustServerCertificate);
        final RestConnection restConnection = builder.build();
        final HubRequest request = new HubRequest(restConnection);
        try {
            request.executePost(restConnection.gson.toJson(phoneHomeRequestBody));
        } catch (final IntegrationException e) {
            throw new PhoneHomeException(e.getMessage(), e);
        }
    }

}
