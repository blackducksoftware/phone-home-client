package com.synopsys.integration.phonehome;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class UniquePhoneHomeProductTest {
    @Test
    public void testCanCreateNewUniqueProduct() {
        UniquePhoneHomeProduct product = UniquePhoneHomeProduct.create("GLORY");
        assertNotNull(product);
        assertEquals("GLORY", product.getName());
    }

    @Test
    public void testCreatingDuplicateProductFails() {
        UniquePhoneHomeProduct product = UniquePhoneHomeProduct.create("HALLELUJAH");
        try {
            UniquePhoneHomeProduct duplicate = UniquePhoneHomeProduct.create("HALLELUJAH");
            fail("Should have thrown an Exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("HALLELUJAH"));
        }
    }

    @Test
    public void testCheckingForProduct() {
        assertTrue(UniquePhoneHomeProduct.isUsed("BLACK_DUCK"));
        assertFalse(UniquePhoneHomeProduct.isUsed("NOBODY_HAD_BETTER_USE_THIS_OR_SO_HELP_ME"));
    }

}
