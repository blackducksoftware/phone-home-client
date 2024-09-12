package com.blackduck.integration.phonehome;

import com.blackduck.integration.log.BufferedIntLogger;
import com.blackduck.integration.log.LogLevel;
import com.blackduck.integration.log.PrintStreamIntLogger;
import com.blackduck.integration.phonehome.exception.PhoneHomeException;
import com.blackduck.integration.phonehome.google.analytics.GoogleAnalyticsConstants;
import com.blackduck.integration.phonehome.request.PhoneHomeRequestBody;
import com.blackduck.integration.phonehome.request.PhoneHomeRequestBodyBuilder;
import com.google.gson.Gson;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PhoneHomeClientUnitTest {
    private Map<String, String> defaultEnvironmentVariables;
    private PhoneHomeClient defaultClient;

    public static final String TEST_GA4_MEASUREMENT_ID = "test-measurement-id";
    public static final String TEST_GA4_API_SECRET = "dummy-secret";

    @BeforeEach
    public void init() {
        PrintStreamIntLogger logger = new PrintStreamIntLogger(System.out, LogLevel.TRACE);
        logger.info("\n");
        logger.info("Test Class: PhoneHomeClientUnitTest");
        defaultEnvironmentVariables = new HashMap<>();
        defaultEnvironmentVariables.put(PhoneHomeClient.BLACKDUCK_PHONE_HOME_URL_OVERRIDE_VARIABLE, GoogleAnalyticsConstants.BASE_URL + GoogleAnalyticsConstants.DEBUG_ENDPOINT);
        defaultClient = new PhoneHomeClient(logger, HttpClientBuilder.create(), new Gson(), TEST_GA4_API_SECRET, TEST_GA4_MEASUREMENT_ID);
    }

    @Test
    public void callHomeIntegrationsTest() throws Exception {
        PhoneHomeRequestBody phoneHomeRequest = PhoneHomeRequestBodyBuilder
            .createForProduct(UniquePhoneHomeProduct.CODE_CENTER, "artifactId", "customerId", "hostName", "artifactVersion", "productVersion")
            .build();

        defaultClient.postPhoneHomeRequest(phoneHomeRequest, defaultEnvironmentVariables);
    }

    @Test
    public void callHomeSkip() throws Exception {
        BufferedIntLogger logger = new BufferedIntLogger();
        PhoneHomeClient clientWithTrackableLogger = new PhoneHomeClient(logger, HttpClientBuilder.create(), new Gson(), TEST_GA4_API_SECRET, TEST_GA4_MEASUREMENT_ID);

        PhoneHomeRequestBody phoneHomeRequest = PhoneHomeRequestBodyBuilder
            .createForProduct(UniquePhoneHomeProduct.CODE_CENTER, "artifactId", "customerId", "hostName", "artifactVersion", "productVersion")
            .build();

        defaultEnvironmentVariables.put(PhoneHomeClient.BLACKDUCK_SKIP_PHONE_HOME_VARIABLE, "true");

        clientWithTrackableLogger.postPhoneHomeRequest(phoneHomeRequest, defaultEnvironmentVariables);
        assertTrue(logger.getOutputString(LogLevel.DEBUG).contains("Skipping phone home"));

        defaultEnvironmentVariables.put(PhoneHomeClient.BLACKDUCK_SKIP_PHONE_HOME_VARIABLE, "false");

        clientWithTrackableLogger.postPhoneHomeRequest(phoneHomeRequest, defaultEnvironmentVariables);
        assertTrue(logger.getOutputString(LogLevel.DEBUG).contains("Phoning home to "));
        /* /debug/mp/collect endpoint returns 200 status code */
        assertTrue(logger.getOutputString(LogLevel.TRACE).contains("Response Code: 200"));
    }

    @Test
    public void testPhoneHomeException() {
        PhoneHomeException emptyException = new PhoneHomeException();
        PhoneHomeException exceptionException = new PhoneHomeException(emptyException);
        PhoneHomeException messageException = new PhoneHomeException("message");
        PhoneHomeException exceptionAndMessageException = new PhoneHomeException("message", emptyException);
        for (PhoneHomeException phoneHomeException : new PhoneHomeException[]{emptyException, exceptionException, messageException, exceptionAndMessageException}) {
            try {
                throw phoneHomeException;
            } catch (PhoneHomeException e) {
                // Do nothing
            }
        }
    }

    @Test
    public void validateBadPhoneHomeBackend() {
        PhoneHomeClient phClient = new PhoneHomeClient(null, (HttpClientBuilder) null, (Gson) null, (String) null, (String) null);
        try {
            phClient.postPhoneHomeRequest(null, Collections.emptyMap());
            fail("Phone home exception not thrown");
        } catch (PhoneHomeException e) {
            // Do nothing
        }
    }

}
