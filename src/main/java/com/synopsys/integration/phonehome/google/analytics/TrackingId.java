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
