/*
 * phone-home-client
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome.google.analytics;

public class GoogleAnalyticsConstants {
    // Google Tracking ID
    public static final String PRODUCTION_INTEGRATIONS_TRACKING_ID = "UA-116682967-1";
    public static final String TEST_INTEGRATIONS_TRACKING_ID = "UA-116682967-2";

    // Api Path(s)
    public static final String BASE_URL = "https://www.google-analytics.com";
    public static final String COLLECT_ENDPOINT = "/collect";
    public static final String BATCH_ENDPOINT = "/batch";
    public static final String DEBUG_ENDPOINT = "/debug" + COLLECT_ENDPOINT;

    // Payload Data - Required
    public static final String API_VERSION_KEY = "v";
    public static final String HIT_TYPE_KEY = "t";
    public static final String CLIENT_ID_KEY = "cid";
    public static final String USER_ID_KEY = "uid";
    public static final String TRACKING_ID_KEY = "tid";
    public static final String DOCUMENT_PATH_KEY = "dp";

    // Payload Data - Custom Dimensions
    public static final String CUSTOMER_ID = "cd1";
    public static final String ARTIFACT_ID = "cd2";
    public static final String ARTIFACT_VERSION = "cd3";
    public static final String PRODUCT_ID = "cd4";
    public static final String PRODUCT_VERSION = "cd5";
    public static final String META_DATA = "cd6";
    public static final String HOST_NAME = "cd7";
    public static final String MODULE_ID = "cd8";

}
