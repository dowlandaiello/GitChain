package com.dowlandaiello.gitchain.p2p;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.dowlandaiello.gitchain.common.CommonNet;

import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * Peer test is the main peer testing file.
 */
public class PeerTest {
    /**
     * Test peer constructor.
     */
    @Test
    public void TestPeer() {
        Peer peer = new Peer("/ipv4/127.0.0.1/tcp/3000"); // Init peer

        assertTrue("peer most not be null", peer != null); // Ensure not null
    }

    /**
     * Test peer constructor with given identity.
     */
    @Test
    public void TestPeerIdentity() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Create key pair
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }

        Peer peer = new Peer("/ipv4/127.0.0.1/tcp/3000", keyPair.getPublicKey()); // Init peer

        assertTrue("peer most not be null", peer != null); // Ensure not null
        assertTrue("peer public key must remain constant", peer.PublicKey == keyPair.getPublicKey()); // Ensure not null
    }

    /**
     * Test peer constructor with given identity and protocol.
     */
    public void TestPeerProtocol() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Create key pair
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }

        Peer peer = new Peer("/ipv4/127.0.0.1/tcp/3000", keyPair.getPublicKey(), CommonNet.Protocol.IPV4); // Init peer

        assertTrue("peer most not be null", peer != null); // Ensure not null
        assertTrue("peer public key must remain constant", peer.PublicKey == keyPair.getPublicKey()); // Ensure not null
    }
}