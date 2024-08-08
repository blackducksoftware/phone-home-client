/*
 * phone-home-client
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome.google.analytics;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.synopsys.integration.phonehome.request.PhoneHomeRequestBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GoogleAnalyticsRequestHelper {
    private final Gson gson;

    public GoogleAnalyticsRequestHelper(Gson gson) {
        this.gson = gson;
    }

    public HttpPost createRequest(PhoneHomeRequestBody phoneHomeRequestBody, String overrideUrl, String apiSecret, String measurementId) throws UnsupportedEncodingException, URISyntaxException {

        // Determine the request endpoint
        String requestUrl = overrideUrl;
        if (StringUtils.isBlank(requestUrl)) {
            requestUrl = GoogleAnalyticsConstants.BASE_URL + GoogleAnalyticsConstants.COLLECT_ENDPOINT;
        }
        URIBuilder uriBuilder = new URIBuilder(requestUrl);

        // This transformer class provides access to manipulate/retrieve the query parameters and payload for requests to GA4
        GoogleAnalyticsRequestTransformer transformer = new GoogleAnalyticsRequestTransformer(gson, apiSecret, measurementId, phoneHomeRequestBody);

        // Build the GA4 server URI with the required query parameters
        List<NameValuePair> parameters = transformer.getParameters();
        uriBuilder.addParameters(parameters);

        // Create HTTP POST for the built URI
        HttpPost httpPost = new HttpPost(uriBuilder.build());

        // Set content type
        httpPost.setHeader("Content-Type", "application/json");

        // Build the POST request payload as a StringEntity and set this entity for the request
        JsonObject payloadJson = transformer.getPayload();
        StringEntity payload = new StringEntity(payloadJson.toString());
        httpPost.setEntity(payload);

        return httpPost;
    }

    private List<NameValuePair> createModuleParameters(List<NameValuePair> parameters, String module) {
        NameValuePair parameter = new BasicNameValuePair(GoogleAnalyticsConstants.MODULE_ID, module);
        List<NameValuePair> newParameters = new ArrayList<>(parameters);
        newParameters.add(parameter);
        return newParameters;
    }

}
