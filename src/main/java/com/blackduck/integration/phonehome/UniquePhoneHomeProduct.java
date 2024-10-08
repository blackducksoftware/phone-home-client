/*
 * phone-home-client
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.phonehome;

import com.blackduck.integration.util.Stringable;

import java.util.HashSet;
import java.util.Set;

public final class UniquePhoneHomeProduct extends Stringable {
    private static final Set<String> USED_NAMES = new HashSet<>();

    public static final UniquePhoneHomeProduct BLACK_DUCK = create("BLACK_DUCK");
    public static final UniquePhoneHomeProduct CODE_CENTER = create("CODE_CENTER");
    public static final UniquePhoneHomeProduct COVERITY = create("COVERITY");
    public static final UniquePhoneHomeProduct POLARIS = create("POLARIS");
    public static final UniquePhoneHomeProduct PROTEX = create("PROTEX");

    public static UniquePhoneHomeProduct create(String name) {
        if (!USED_NAMES.add(name)) {
            throw new IllegalArgumentException(String.format("The product name '%s' is already defined - using it again could cause unintended collisions in the data.", name));
        }
        return new UniquePhoneHomeProduct(name);
    }

    public static boolean isUsed(String name) {
        return USED_NAMES.contains(name);
    }

    private String name;

    private UniquePhoneHomeProduct(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
