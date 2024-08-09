/*
 * phone-home-client
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome.google.analytics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synopsys.integration.phonehome.request.PhoneHomeRequestBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoogleAnalyticsRequestTransformer {
    private final List<NameValuePair> parameters = new ArrayList<>();
    private JsonObject payload = new JsonObject();
    private final PhoneHomeRequestBody phoneHomeRequestBody;
    private final String measurementId;
    private final String apiSecret;
    private final Gson gson;

    public GoogleAnalyticsRequestTransformer(Gson gson, String apiSecret, String measurementId, PhoneHomeRequestBody phoneHomeRequestBody) {
        this.gson = gson;
        this.phoneHomeRequestBody = phoneHomeRequestBody;
        this.apiSecret = apiSecret;
        this.measurementId = measurementId;
    }

    public List<NameValuePair> getParameters() {
        addParameter("api_secret", apiSecret);
        addParameter("measurement_id", measurementId);
        return parameters;
    }

    private String getFormattedHitDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return currentDate.format(dateTimeFormatter);
    }

    public JsonObject getPayload() {
        String clientId = generateClientId(phoneHomeRequestBody.getCustomerId(), phoneHomeRequestBody.getHostName());

        JsonObject eventParams = new JsonObject();
        eventParams.addProperty(GoogleAnalyticsConstants.CUSTOMER_ID, phoneHomeRequestBody.getCustomerId());
        eventParams.addProperty(GoogleAnalyticsConstants.ARTIFACT_ID, phoneHomeRequestBody.getArtifactId());
        eventParams.addProperty(GoogleAnalyticsConstants.ARTIFACT_VERSION, phoneHomeRequestBody.getArtifactVersion());
        eventParams.addProperty(GoogleAnalyticsConstants.HOST_NAME, phoneHomeRequestBody.getHostName());
        eventParams.addProperty(GoogleAnalyticsConstants.META_DATA, gson.toJson(phoneHomeRequestBody.getMetaData()));
        eventParams.addProperty(GoogleAnalyticsConstants.PRODUCT_ID, phoneHomeRequestBody.getProductName());
        eventParams.addProperty(GoogleAnalyticsConstants.PRODUCT_VERSION, phoneHomeRequestBody.getProductVersion());
        eventParams.addProperty(GoogleAnalyticsConstants.HIT_DATE, getFormattedHitDate());

        JsonObject eventObject = new JsonObject();
        eventObject.addProperty(GoogleAnalyticsConstants.EVENT_NAME_KEY, GoogleAnalyticsConstants.EVENT_NAME_VALUE);
        eventObject.add(GoogleAnalyticsConstants.EVENT_PARAMS_KEY, eventParams);

        JsonArray eventsArray = new JsonArray();
        eventsArray.add(eventObject);

        payload.addProperty(GoogleAnalyticsConstants.REQUEST_CLIENT_ID_KEY, clientId);
        payload.add(GoogleAnalyticsConstants.REQUEST_EVENTS_KEY, eventsArray);

        return payload;
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
