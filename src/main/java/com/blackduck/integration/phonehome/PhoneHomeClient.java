/*
 * phone-home-client
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.phonehome;

import com.blackduck.integration.phonehome.google.analytics.GoogleAnalyticsRequestHelper;
import com.google.gson.Gson;
import com.synopsys.integration.log.IntLogger;
import com.blackduck.integration.phonehome.exception.PhoneHomeException;
import com.blackduck.integration.phonehome.google.analytics.MeasurementId;
import com.blackduck.integration.phonehome.request.PhoneHomeRequestBody;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Map;

public class PhoneHomeClient {
    public static final String BLACKDUCK_SKIP_PHONE_HOME_VARIABLE = "BLACKDUCK_SKIP_PHONE_HOME";
    public static final String BLACKDUCK_PHONE_HOME_URL_OVERRIDE_VARIABLE = "BLACKDUCK_PHONE_HOME_URL_OVERRIDE";

    private final HttpClientBuilder httpClientBuilder;
    private final IntLogger logger;
    private final Gson gson;
    private final MeasurementId measurementID;
    private final String apiSecret;

    public PhoneHomeClient(IntLogger logger, HttpClientBuilder httpClientBuilder, Gson gson, String apiSecret, String measurementID) {
        this(logger, httpClientBuilder, gson, apiSecret, new MeasurementId(measurementID));
    }

    public PhoneHomeClient(IntLogger logger, HttpClientBuilder httpClientBuilder, Gson gson, String apiSecret, MeasurementId measurementID) {
        this.httpClientBuilder = httpClientBuilder;
        this.logger = logger;
        this.gson = gson;
        this.apiSecret = apiSecret;
        this.measurementID = measurementID;
    }

    public void postPhoneHomeRequest(PhoneHomeRequestBody phoneHomeRequestBody, Map<String, String> environmentVariables) throws PhoneHomeException {
        if (skipPhoneHome(environmentVariables)) {
            logger.debug("Skipping phone home");
            return;
        }
        if (phoneHomeRequestBody == null) {
            throw new PhoneHomeException("The request body must not be null.");
        }
        String overrideUrl = checkOverridePhoneHomeUrl(environmentVariables);

        try (CloseableHttpClient client = httpClientBuilder.build()) {
            GoogleAnalyticsRequestHelper requestHelper = new GoogleAnalyticsRequestHelper(gson);

            if (overrideUrl != null) {
                logger.debug("Overriding Phone-Home URL: " + overrideUrl);
            }
            HttpUriRequest request = requestHelper.createRequest(phoneHomeRequestBody, overrideUrl, apiSecret, measurementID.getId());

            logger.debug("Phoning home to " + request.getURI());
            HttpResponse response = client.execute(request);
            logger.trace("Response Code: " + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            throw new PhoneHomeException(e.getMessage(), e);
        }
    }

    private boolean skipPhoneHome(Map<String, String> environmentVariables) {
        if (environmentVariables.containsKey(BLACKDUCK_SKIP_PHONE_HOME_VARIABLE)) {
            String valueString = environmentVariables.get(BLACKDUCK_SKIP_PHONE_HOME_VARIABLE);
            return BooleanUtils.toBoolean(valueString);
        }
        return false;
    }

    private String checkOverridePhoneHomeUrl(Map<String, String> environmentVariables) {
        return environmentVariables.get(BLACKDUCK_PHONE_HOME_URL_OVERRIDE_VARIABLE);
    }

}
