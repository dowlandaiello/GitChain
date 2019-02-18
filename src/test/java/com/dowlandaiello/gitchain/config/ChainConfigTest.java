package com.dowlandaiello.gitchain.config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.dowlandaiello.gitchain.common.CommonIO;

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

        Map<BigInteger, Float> alloc = new HashMap<BigInteger, Float>(); // Init hash map

        alloc.put(keyPair.getPublicKey(), 1000000f); // Set alloc

        ChainConfig chainConfig = new ChainConfig(alloc, 0, "test_chain", 10, 1f); // Initialize chain config

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null
    }

    /**
     * Test genesis.json read helper.
     */
    @Test
    public void TestReadChainConfig() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Generate a new key pair
        } catch (Exception e) { // Catch
            fail(e.getLocalizedMessage()); // Panic
        }

        CommonIO.GenesisPath = new File(CommonIO.ConfigPath+"/genesis_test.json").getAbsolutePath(); // Set genesis path

        Map<BigInteger, Float> alloc = new HashMap<BigInteger, Float>(); // Init hash map

        alloc.put(keyPair.getPublicKey(), 1000000f); // Set alloc

        ChainConfig chainConfig = new ChainConfig(alloc, 0, "test_chain", 10, 1f); // Initialize chain config

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null

        assertTrue("must write successfully", chainConfig.WriteChainConfig()); // Ensure success

        chainConfig = ChainConfig.ReadChainConfig(); // Read chain config

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null
    }

    /**
     * Test chain config constructor.
     */
    @Test
    public void TestWriteChainConfig() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Generate a new key pair
        } catch (Exception e) { // Catch
            fail(e.getLocalizedMessage()); // Panic
        }

        CommonIO.GenesisPath = new File(CommonIO.ConfigPath+"/genesis_test.json").getAbsolutePath(); // Set genesis path

        Map<BigInteger, Float> alloc = new HashMap<BigInteger, Float>(); // Init hash map

        alloc.put(keyPair.getPublicKey(), 1000000f); // Set alloc

        ChainConfig chainConfig = new ChainConfig(alloc, 0, "test_chain", 10, 1f); // Initialize chain config

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null

        assertTrue("must write successfully", chainConfig.WriteChainConfig()); // Ensure success
    }
}