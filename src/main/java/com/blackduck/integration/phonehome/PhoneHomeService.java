/*
 * phone-home-client
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.phonehome;

import com.blackduck.integration.log.IntLogger;
import com.blackduck.integration.phonehome.request.PhoneHomeRequestBody;
import com.blackduck.integration.util.NoThreadExecutorService;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class PhoneHomeService {
    private final IntLogger logger;
    private final PhoneHomeClient phoneHomeClient;
    private final ExecutorService executorService;

    public static PhoneHomeService createPhoneHomeService(IntLogger logger, PhoneHomeClient phoneHomeClient) {
        return new PhoneHomeService(logger, phoneHomeClient, new NoThreadExecutorService());
    }

    public static PhoneHomeService createAsynchronousPhoneHomeService(IntLogger logger, PhoneHomeClient phoneHomeClient, ExecutorService executorService) {
        return new PhoneHomeService(logger, phoneHomeClient, executorService);
    }

    public PhoneHomeService(IntLogger logger, PhoneHomeClient phoneHomeClient, ExecutorService executorService) {
        this.logger = logger;
        this.phoneHomeClient = phoneHomeClient;
        this.executorService = executorService;
    }

    public PhoneHomeResponse phoneHome(PhoneHomeRequestBody phoneHomeRequestBody) {
        return phoneHome(phoneHomeRequestBody, Collections.emptyMap());
    }

    public PhoneHomeResponse phoneHome(PhoneHomeRequestBody phoneHomeRequestBody, Map<String, String> environmentVariables) {
        Future<Boolean> phoneHomeTask = null;
        PhoneHomeCallable phoneHomeCallable = new PhoneHomeCallable(phoneHomeRequestBody, environmentVariables);
        try {
            phoneHomeTask = executorService.submit(phoneHomeCallable);
        } catch (Exception e) {
            logger.debug("Problem executing phone home asynchronously: " + e.getMessage(), e);
        }
        return PhoneHomeResponse.createAsynchronousResponse(phoneHomeTask);
    }

    private class PhoneHomeCallable implements Callable<Boolean> {
        private final PhoneHomeRequestBody phoneHomeRequestBody;
        private final Map<String, String> environmentVariables;

        public PhoneHomeCallable(PhoneHomeRequestBody phoneHomeRequestBody, Map<String, String> environmentVariables) {
            this.phoneHomeRequestBody = phoneHomeRequestBody;
            this.environmentVariables = environmentVariables;
        }

        @Override
        public Boolean call() {
            Boolean result = Boolean.FALSE;
            try {
                logger.debug("Starting phone home");
                phoneHomeClient.postPhoneHomeRequest(phoneHomeRequestBody, environmentVariables);
                result = Boolean.TRUE;
                logger.debug("Completed phone home");
            } catch (Exception ex) {
                logger.debug("Phone home error.", ex);
            }

            return result;
        }
    }

}
