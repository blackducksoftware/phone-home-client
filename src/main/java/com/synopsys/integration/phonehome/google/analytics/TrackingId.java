/*
 * phone-home-client
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome.google.analytics;

public class TrackingId {
    public static final TrackingId PRODUCTION = new TrackingId(GoogleAnalyticsConstants.PRODUCTION_INTEGRATIONS_TRACKING_ID);
    public static final TrackingId TEST = new TrackingId(GoogleAnalyticsConstants.TEST_INTEGRATIONS_TRACKING_ID);

    private final String id;

    public TrackingId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
