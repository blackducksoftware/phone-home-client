/*
 * phone-home-client
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome.google.analytics;

import com.google.gson.Gson;
import com.synopsys.integration.phonehome.request.PhoneHomeRequestBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoogleAnalyticsRequestTransformer {
    private final List<NameValuePair> parameters = new ArrayList<>();
    private final PhoneHomeRequestBody phoneHomeRequestBody;
    private final String trackingId;
    private final Gson gson;

    public GoogleAnalyticsRequestTransformer(Gson gson, String trackingId, PhoneHomeRequestBody phoneHomeRequestBody) {
        this.gson = gson;
        this.phoneHomeRequestBody = phoneHomeRequestBody;
        this.trackingId = trackingId;
    }

    public List<NameValuePair> getParameters() {
        addParameter(GoogleAnalyticsConstants.API_VERSION_KEY, "1");
        addParameter(GoogleAnalyticsConstants.HIT_TYPE_KEY, "pageview");

        String clientId = generateClientId(phoneHomeRequestBody.getCustomerId(), phoneHomeRequestBody.getHostName());
        addParameter(GoogleAnalyticsConstants.CLIENT_ID_KEY, clientId);
        addParameter(GoogleAnalyticsConstants.TRACKING_ID_KEY, trackingId);
        addParameter(GoogleAnalyticsConstants.DOCUMENT_PATH_KEY, "phone-home");

        // Phone Home Parameters
        addParameter(GoogleAnalyticsConstants.CUSTOMER_ID, phoneHomeRequestBody.getCustomerId());
        addParameter(GoogleAnalyticsConstants.HOST_NAME, phoneHomeRequestBody.getHostName());
        addParameter(GoogleAnalyticsConstants.ARTIFACT_ID, phoneHomeRequestBody.getArtifactId());
        addParameter(GoogleAnalyticsConstants.ARTIFACT_VERSION, phoneHomeRequestBody.getArtifactVersion());
        addParameter(GoogleAnalyticsConstants.PRODUCT_ID, phoneHomeRequestBody.getProductName());
        addParameter(GoogleAnalyticsConstants.PRODUCT_VERSION, phoneHomeRequestBody.getProductVersion());
        addParameter(GoogleAnalyticsConstants.META_DATA, gson.toJson(phoneHomeRequestBody.getMetaData()));

        return parameters;
    }

    private void addParameter(String key, String value) {
        NameValuePair parameter = new BasicNameValuePair(key, value);
        parameters.add(parameter);
    }

    private String generateClientId(String customerId, String hostName) {
        String clientId;
        if (!PhoneHomeRequestBody.UNKNOWN_FIELD_VALUE.equals(customerId)) {
            clientId = customerId;
        } else if (!PhoneHomeRequestBody.UNKNOWN_FIELD_VALUE.equals(hostName)) {
            clientId = hostName;
        } else {
            clientId = PhoneHomeRequestBody.UNKNOWN_FIELD_VALUE;
        }

        byte[] bytesFromString = clientId.getBytes();
        return UUID.nameUUIDFromBytes(bytesFromString).toString();
    }

}
