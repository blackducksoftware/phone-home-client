/*
 * phone-home-client
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.phonehome.request;

import com.blackduck.integration.phonehome.UniquePhoneHomeProduct;
import com.blackduck.integration.util.NameVersion;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.blackduck.integration.phonehome.request.PhoneHomeRequestBody.MAX_META_DATA_CHARACTERS;
import static com.blackduck.integration.phonehome.request.PhoneHomeRequestBody.UNKNOWN_FIELD_VALUE;

public class PhoneHomeRequestBodyBuilder {
    private final String customerId;
    private final String hostName;
    private final NameVersion artifactInfo;
    private final UniquePhoneHomeProduct product;
    private final String productVersion;
    private final List<String> artifactModules = new ArrayList<>();
    private final Map<String, String> metaData = new HashMap<>();

    public static PhoneHomeRequestBodyBuilder createForBlackDuck(String integrationRepoName, String registrationId, String blackDuckServerUrl, @Nullable String integrationVersion, @Nullable String blackDuckVersion) {
        return createForProduct(UniquePhoneHomeProduct.BLACK_DUCK, integrationRepoName, registrationId, blackDuckServerUrl, integrationVersion, blackDuckVersion);
    }

    public static PhoneHomeRequestBodyBuilder createForCoverity(String integrationRepoName, String customerName, String cimServerUrl, @Nullable String integrationVersion, @Nullable String cimVersion) {
        return createForProduct(UniquePhoneHomeProduct.COVERITY, integrationRepoName, customerName, cimServerUrl, integrationVersion, cimVersion);
    }

    public static PhoneHomeRequestBodyBuilder createForPolaris(String integrationRepoName, String organizationName, String polarisServerUrl, @Nullable String integrationVersion, @Nullable String polarisVersion) {
        return createForProduct(UniquePhoneHomeProduct.POLARIS, integrationRepoName, organizationName, polarisServerUrl, integrationVersion, polarisVersion);
    }

    public static PhoneHomeRequestBodyBuilder createForProduct(UniquePhoneHomeProduct product, String artifactId, String customerId, String hostName, @Nullable String artifactVersion, @Nullable String productVersion) {
        artifactVersion = StringUtils.defaultIfEmpty(artifactVersion, UNKNOWN_FIELD_VALUE);
        productVersion = StringUtils.defaultIfEmpty(productVersion, UNKNOWN_FIELD_VALUE);

        NameVersion artifactInfo = new NameVersion(artifactId, artifactVersion);
        return new PhoneHomeRequestBodyBuilder(customerId, hostName, artifactInfo, product, productVersion);
    }

    public PhoneHomeRequestBodyBuilder(String customerId, String hostName, NameVersion artifactInfo, UniquePhoneHomeProduct product, String productVersion) {
        if (null == product || null == artifactInfo || StringUtils.isAnyBlank(customerId, hostName, artifactInfo.getName(), artifactInfo.getVersion(), product.getName(), productVersion)) {
            throw new IllegalArgumentException("The fields: customerId, hostName, artifactInfo, and product (with a non-blank name), and productVersion are all required.");
        }

        assert customerId != null;
        this.customerId = customerId;
        this.hostName = hostName;
        this.artifactInfo = artifactInfo;
        this.product = product;
        this.productVersion = productVersion;
    }

    public PhoneHomeRequestBody build() {
        return new PhoneHomeRequestBody(this);
    }

    /**
     * metaData map cannot exceed {@value PhoneHomeRequestBody#MAX_META_DATA_CHARACTERS}
     *
     * @return true if the data was successfully added, false if the new data would make the map exceed it's size limit
     */
    public boolean addToMetaData(String key, String value) {
        if (charactersInMetaDataMap(key, value) < MAX_META_DATA_CHARACTERS) {
            metaData.put(key, value);
            return true;
        }
        return false;
    }

    /**
     * metaData map cannot exceed {@value PhoneHomeRequestBody#MAX_META_DATA_CHARACTERS}
     *
     * @return true if the all the data was successfully added,
     * false if one or more of the entries entries would make the map exceed it's size limit
     */
    public boolean addAllToMetaData(Map<String, String> metadataMap) {
        return metadataMap.entrySet().stream()
                .allMatch(entry -> addToMetaData(entry.getKey(), entry.getValue()));
    }

    public void addArtifactModule(String artifactModule) {
        artifactModules.add(artifactModule);
    }

    public void addArtifactModules(String... artifactModules) {
        this.artifactModules.addAll(Arrays.asList(artifactModules));
    }

    private int charactersInMetaDataMap(String key, String value) {
        final int mapEntryWrappingCharacters = 6;
        String mapAsString = getMetaData().toString();
        return mapEntryWrappingCharacters + mapAsString.length() + key.length() + value.length();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getHostName() {
        return hostName;
    }

    public NameVersion getArtifactInfo() {
        return artifactInfo;
    }

    public UniquePhoneHomeProduct getProduct() {
        return product;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public List<String> getArtifactModules() {
        return artifactModules;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

}
