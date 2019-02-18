package com.dowlandaiello.gitchain.config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * ChainConfigTest is the main ChainConfig testing file.
 * 
 * @author Dowland Aiello
 * @since 18.02.2019
 */
public class ChainConfigTest {
    /**
     * Test chain config constructor.
     */
    @Test
    public void TestChainConfig() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Generate a new key pair
        } catch (Exception e) { // Catch
            fail(e.getLocalizedMessage()); // Panic
        }

        Map<byte[], Float> alloc = new HashMap<byte[], Float>(); // Init hash map

        alloc.put(keyPair.getPublicKey().toByteArray(), 1000000f); // Set alloc

        ChainConfig chainConfig = new ChainConfig(alloc, 0, "test_chain"); // Initialize chain config

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null
    }
}