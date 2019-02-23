package com.dowlandaiello.gitchain.p2p;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.config.ChainConfig;

import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * DhtTest is the main Dht testing file.
 */
public class DhtTest {
    /**
     * Test DHT constructor
     */
    @Test
    public void TestDht() {
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

        Peer bootstrapPeer = new Peer("/ipv4/127.0.0.1/tcp/3000"); // Init peer

        Dht dht = new Dht(chainConfig, bootstrapPeer); // Initialize DHT

        assertTrue("dht must not be null", dht != null); // Ensure DHT is not null
    }

    /**
     * Test DHT bootstrap helper.
     */
    @Test
    public void TestBootstrap() {
        assertTrue("must remove test cache",
                CommonIO.DeleteDirectoryContents(new java.io.File(CommonIO.DHTPath + "/" + "test_chain"))); // Delete

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

        Peer bootstrapPeer = new Peer("/ipv4/127.0.0.1/tcp/3048"); // Init peer

        Dht dht = new Dht(chainConfig, bootstrapPeer); // Initialize DHT

        dht.WriteToMemory(); // Write DHT to persistent memory

        dht.StartServing(); // Start DHT server

        assertTrue("dht must not be null", dht != null); // Ensure DHT is not null

        Dht bootstrappedDht = Dht.Bootstrap(bootstrapPeer.ConnectionAddr, "test_chain"); // Bootstrap peer

        assertTrue("dht must not be null", bootstrappedDht != null); // Ensure DHT is not null
        assertTrue("dht instances must be equivalent", Arrays.equals(dht.Bytes(), bootstrappedDht.Bytes())); // Ensure
                                                                                                             // DHT
                                                                                                             // instances
                                                                                                             // equivalent

        dht.StopServing(); // Stop DHT server

        assertTrue("must close db", dht.CloseNodeDB()); // Close db

        assertTrue("must remove test cache",
                CommonIO.DeleteDirectoryContents(new java.io.File(CommonIO.DHTPath + "/" + "test_chain"))); // Delete
    }

    /**
     * Test DHT join network helper.
     */
    @Test
    public void TestJoinNetwork() {
        if (Dht.WorkingNodeDB != null) { // Check node db already existent
            try {
                Dht.WorkingNodeDB.close(); // Close node db
                Dht.WorkingNodeDB = null; // Remove node db
            } catch (IOException e) { // Catch
                fail(e.getLocalizedMessage()); // Panic
            }
        }

        assertTrue("must remove test cache",
                CommonIO.DeleteDirectoryContents(new java.io.File(CommonIO.DHTPath + "/" + "test_chain"))); // Delete

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

        Peer bootstrapPeer = new Peer("/ipv4/127.0.0.1/tcp/3048"); // Init peer

        Dht dht = new Dht(chainConfig, bootstrapPeer); // Initialize DHT

        dht.WriteToMemory(); // Write DHT to persistent memory

        dht.StartServing(); // Start DHT server

        assertTrue("dht must not be null", dht != null); // Ensure DHT is not null

        Dht bootstrappedDht = Dht.Bootstrap(bootstrapPeer.ConnectionAddr, "test_chain"); // Bootstrap peer

        assertTrue("dht must not be null", bootstrappedDht != null); // Ensure DHT is not null
        assertTrue("dht instances must be equivalent", Arrays.equals(dht.Bytes(), bootstrappedDht.Bytes())); // Ensure
                                                                                                             // DHT
                                                                                                             // instances
                                                                                                             // equivalent

        assertTrue("must be able to join network", bootstrappedDht.JoinNetwork()); // Attempt to join network

        dht.StopServing(); // Stop DHT server

        assertTrue("must close db", dht.CloseNodeDB()); // Close db

        assertTrue("must remove test cache",
                CommonIO.DeleteDirectoryContents(new java.io.File(CommonIO.DHTPath + "/" + "test_chain"))); // Delete
    }
}