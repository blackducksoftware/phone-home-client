/*
 * phone-home-client
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.phonehome.google.analytics;

public class MeasurementId {

    private final String id;

    public MeasurementId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
