/*
 * phone-home-client
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome.google.analytics;

public class MeasurementId {

    private final String id;

    public MeasurementId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
