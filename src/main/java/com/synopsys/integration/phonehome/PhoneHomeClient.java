/*
 * phone-home-client
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome;

import com.google.gson.Gson;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.phonehome.exception.PhoneHomeException;
import com.synopsys.integration.phonehome.google.analytics.GoogleAnalyticsRequestHelper;
import com.synopsys.integration.phonehome.google.analytics.MeasurementID;
import com.synopsys.integration.phonehome.request.PhoneHomeRequestBody;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Map;

public class PhoneHomeClient {
    public static final String BLACKDUCK_SKIP_PHONE_HOME_VARIABLE = "BLACKDUCK_SKIP_PHONE_HOME";
    public static final String BLACKDUCK_PHONE_HOME_URL_OVERRIDE_VARIABLE = "BLACKDUCK_PHONE_HOME_URL_OVERRIDE";
    public static final String SKIP_PHONE_HOME_VARIABLE = "SYNOPSYS_SKIP_PHONE_HOME";
    public static final String PHONE_HOME_URL_OVERRIDE_VARIABLE = "SYNOPSYS_PHONE_HOME_URL_OVERRIDE";

    private final HttpClientBuilder httpClientBuilder;
    private final IntLogger logger;
    private final Gson gson;
    private final MeasurementID measurementID;

    public PhoneHomeClient(IntLogger logger, HttpClientBuilder httpClientBuilder, Gson gson, String measurementID) {
        this(logger, httpClientBuilder, gson, new MeasurementID(measurementID));
    }

    public PhoneHomeClient(IntLogger logger, HttpClientBuilder httpClientBuilder, Gson gson, MeasurementID measurementID) {
        this.httpClientBuilder = httpClientBuilder;
        this.logger = logger;
        this.gson = gson;
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
            HttpUriRequest request = requestHelper.createRequest(phoneHomeRequestBody, overrideUrl, measurementID.getId());

            logger.debug("Phoning home to " + request.getURI());
            HttpResponse response = client.execute(request);
            logger.trace("Response Code: " + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            throw new PhoneHomeException(e.getMessage(), e);
        }
    }

    private boolean skipPhoneHome(Map<String, String> environmentVariables) {
        if (environmentVariables.containsKey(SKIP_PHONE_HOME_VARIABLE) || environmentVariables.containsKey(BLACKDUCK_SKIP_PHONE_HOME_VARIABLE)) {
            String valueString = environmentVariables.get(SKIP_PHONE_HOME_VARIABLE);
            if (StringUtils.isBlank(valueString)) {
                valueString = environmentVariables.get(BLACKDUCK_SKIP_PHONE_HOME_VARIABLE);
            }
            return BooleanUtils.toBoolean(valueString);
        }
        return false;
    }

    private String checkOverridePhoneHomeUrl(Map<String, String> environmentVariables) {
        String overrideUrl;

        overrideUrl = environmentVariables.get(PHONE_HOME_URL_OVERRIDE_VARIABLE);
        if (StringUtils.isBlank(overrideUrl)) {
            overrideUrl = environmentVariables.get(BLACKDUCK_PHONE_HOME_URL_OVERRIDE_VARIABLE);
        }

        return overrideUrl;
    }

}
