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
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleAnalyticsRequestHelper {
    private final Gson gson;

    public GoogleAnalyticsRequestHelper(Gson gson) {
        this.gson = gson;
    }

    public HttpPost createRequest(PhoneHomeRequestBody phoneHomeRequestBody, String url, String trackingId) throws UnsupportedEncodingException {
        GoogleAnalyticsRequestTransformer transformer = new GoogleAnalyticsRequestTransformer(gson, trackingId, phoneHomeRequestBody);
        List<NameValuePair> parameters = transformer.getParameters();
        AbstractHttpEntity entity;
        String requestUrl = url;

        // Not in the transformer because this will likely go away -- rotte 8/6/2019
        List<String> artifactModules = phoneHomeRequestBody.getArtifactModules();

        if (artifactModules == null || artifactModules.size() == 0) {
            if (StringUtils.isBlank(requestUrl)) {
                requestUrl = GoogleAnalyticsConstants.BASE_URL + GoogleAnalyticsConstants.COLLECT_ENDPOINT;
            }

            entity = new UrlEncodedFormEntity(parameters);
        } else {
            String requestString = artifactModules.stream()
                    .map(module -> createModuleParameters(parameters, module))
                    .map(moduleParameters -> URLEncodedUtils.format(moduleParameters, StandardCharsets.ISO_8859_1))
                    .collect(Collectors.joining("\n"));

            if (StringUtils.isBlank(requestUrl)) {
                requestUrl = GoogleAnalyticsConstants.BASE_URL + GoogleAnalyticsConstants.BATCH_ENDPOINT;
            }

            entity = new StringEntity(requestString, StandardCharsets.ISO_8859_1);
        }

        return this.createRequest(requestUrl, entity);

    }

    public HttpPost createRequest(String url, HttpEntity httpEntity) {
        HttpPost post = new HttpPost(url);

        post.setEntity(httpEntity);
        // TODO post.addHeader(HttpHeaders.ACCEPT, ContentType.TEXT_PLAIN.getMimeType());

        return post;
    }

    private List<NameValuePair> createModuleParameters(List<NameValuePair> parameters, String module) {
        NameValuePair parameter = new BasicNameValuePair(GoogleAnalyticsConstants.MODULE_ID, module);
        List<NameValuePair> newParameters = new ArrayList<>(parameters);
        newParameters.add(parameter);
        return newParameters;
    }

}
