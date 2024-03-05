/*
 * phone-home-client
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome.google.analytics;

public class MeasurementID {
    public static final MeasurementID PRODUCTION = new MeasurementID(GoogleAnalyticsConstants.PRODUCTION_GA4_MEASUREMENT_ID);
    public static final MeasurementID TEST = new MeasurementID(GoogleAnalyticsConstants.TEST_GA4_MEASUREMENT_ID);

    private final String id;

    public MeasurementID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
