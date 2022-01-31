/*
 * phone-home-client
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.phonehome;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PhoneHomeResponse {
    private final Future<Boolean> phoneHomeTask;
    private final Boolean result;

    private PhoneHomeResponse(Future<Boolean> phoneHomeTask, Boolean result) {
        this.phoneHomeTask = phoneHomeTask;
        this.result = result;
    }

    public static PhoneHomeResponse createResponse(Boolean result) {
        return new PhoneHomeResponse(null, result);
    }

    public static PhoneHomeResponse createAsynchronousResponse(Future<Boolean> phoneHomeTask) {
        return new PhoneHomeResponse(phoneHomeTask, Boolean.FALSE);
    }

    /**
     * If Phone Home was called asynchronously: If the result is available, it will be returned, otherwise the task will be cancelled and this will return Boolean.FALSE.
     * If Phone Home was called synchronously: The result of the synchronous call will be returned.
     */
    public Boolean getImmediateResult() {
        return awaitResult(0L);
    }

    /**
     * @param timeoutInSeconds time to wait before cancelling the task
     */
    public Boolean awaitResult(long timeoutInSeconds) {
        if (phoneHomeTask != null) {
            try {
                return phoneHomeTask.get(timeoutInSeconds, TimeUnit.SECONDS);
            } catch (ExecutionException | TimeoutException e) {
                return Boolean.FALSE;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Boolean.FALSE;
            } finally {
                endPhoneHome();
            }
        }
        return result;
    }

    public boolean isDone() {
        if (phoneHomeTask != null) {
            return phoneHomeTask.isDone();
        }
        return true;
    }

    private boolean endPhoneHome() {
        if (phoneHomeTask != null) {
            if (!phoneHomeTask.isDone()) {
                return phoneHomeTask.cancel(true);
            }
        }
        return false;
    }

}
