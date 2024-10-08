/*
 * phone-home-client
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.phonehome.google.analytics;

public class GoogleAnalyticsConstants {

    // Api Path(s)
    public static final String BASE_URL = "https://www.google-analytics.com";
    public static final String COLLECT_ENDPOINT = "/mp/collect";
    public static final String DEBUG_ENDPOINT = "/debug" + COLLECT_ENDPOINT;

    // Payload Data - Keys used in the request body's JSON
    public static final String REQUEST_CLIENT_ID_KEY = "client_id";
    public static final String REQUEST_EVENTS_KEY = "events";
    public static final String EVENT_NAME_KEY = "name";
    public static final String EVENT_NAME_VALUE = "bd_hit";
    public static final String EVENT_PARAMS_KEY = "params";


    // Payload Data - Custom Dimensions
    public static final String CUSTOMER_ID = "customer_id";
    public static final String ARTIFACT_ID = "artifact_id";
    public static final String ARTIFACT_VERSION = "artifact_version";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_VERSION = "product_version";
    public static final String META_DATA = "metadata";
    public static final String HOST_NAME = "host_name";
    public static final String MODULE_ID = "module_id";
    public static final String HIT_DATE = "hit_date";

}
