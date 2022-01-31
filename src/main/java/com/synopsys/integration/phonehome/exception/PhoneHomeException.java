/*
 * phone-home-client
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome.exception;

public class PhoneHomeException extends Exception {
    private static final long serialVersionUID = 678249589814131943L;

    public PhoneHomeException() {
        super();
    }

    public PhoneHomeException(final String message) {
        super(message);
    }

    public PhoneHomeException(final Throwable cause) {
        super(cause);
    }

    public PhoneHomeException(final String message, final Throwable e) {
        super(message, e);
    }

}
