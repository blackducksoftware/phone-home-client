package com.blackduck.integration.phonehome.google.analytics;

import com.blackduck.integration.log.LogLevel;
import com.blackduck.integration.log.PrintStreamIntLogger;
import com.blackduck.integration.phonehome.request.PhoneHomeRequestBodyBuilder;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoogleAnalyticsRequestHelperTest {
    private static final PrintStreamIntLogger logger = new PrintStreamIntLogger(System.out, LogLevel.TRACE);
    public static final String TEST_GA4_MEASUREMENT_ID = "test-measurement-id";
    public static final String TEST_GA4_API_SECRET = "dummy-secret";

    @BeforeEach
    public void init() {
        logger.info("\n");
        logger.info("Test Class: GoogleAnalyticsRequestHelperTest");
    }

    @Test
    public void basicRequestTest() throws IOException, URISyntaxException {
        final String debugUrl = GoogleAnalyticsConstants.BASE_URL + GoogleAnalyticsConstants.COLLECT_ENDPOINT;

        PhoneHomeRequestBodyBuilder phoneHomeRequestBodyBuilder = PhoneHomeRequestBodyBuilder.createForBlackDuck("fake_artifact_id", "fake_customer_id", "fake_host_name", "fake_artifact_version", "fake_product_version");
        phoneHomeRequestBodyBuilder.addToMetaData("exampleMetaData_1", "data");
        phoneHomeRequestBodyBuilder.addToMetaData("exampleMetaData_2", "other Data");
        phoneHomeRequestBodyBuilder.addToMetaData("exampleMetaData_3", "special chars: !@#$%^&*()<>?,.;`~\\|{{}[]]-=_+");
        phoneHomeRequestBodyBuilder.addToMetaData("example meta data 4", "string \" with \"quotes\" \"    \" ");

        GoogleAnalyticsRequestHelper helper = new GoogleAnalyticsRequestHelper(new Gson());

        HttpPost request = helper.createRequest(phoneHomeRequestBodyBuilder.build(), debugUrl, TEST_GA4_API_SECRET, TEST_GA4_MEASUREMENT_ID);
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(request.getEntity().getContent()));

        String nextRequestLine;
        while ((nextRequestLine = requestReader.readLine()) != null) {
            logger.info(nextRequestLine);
        }

        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(request);

        int responseCode = response.getStatusLine().getStatusCode();
        logger.info("Response Code: " + responseCode);
        /* /mp/collect endpoint returns 204 status code */
        assertEquals(204, responseCode);
    }
}
