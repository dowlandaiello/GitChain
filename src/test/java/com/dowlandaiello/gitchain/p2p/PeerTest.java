package com.dowlandaiello.gitchain.p2p;

import static org.junit.Assert.assertTrue;

import com.dowlandaiello.gitchain.account.Account;
import com.dowlandaiello.gitchain.common.CommonNet;

import org.junit.Test;

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

        assertTrue("peer must not be null", peer != null); // Ensure not null
    }

    /**
     * Test peer constructor with given identity.
     */
    @Test
    public void TestPeerIdentity() {
        Account account = new Account(); // Initialize account

        Peer peer = new Peer("/ipv4/127.0.0.1/tcp/3000", account.PublicKey); // Init peer

        assertTrue("peer most not be null", peer != null); // Ensure not null
        assertTrue("peer public key must remain constant", peer.PublicKey == account.PublicKey); // Ensure not null
    }

    /**
     * Test peer constructor with given identity and protocol.
     */
    @Test
    public void TestPeerProtocol() {
        Account account = new Account(); // Initialize account

        Peer peer = new Peer("/ipv4/127.0.0.1/tcp/3000", account.PublicKey, CommonNet.Protocol.IPV4); // Init peer

        assertTrue("peer most not be null", peer != null); // Ensure not null
        assertTrue("peer public key must remain constant", peer.PublicKey == account.PublicKey); // Ensure not null
    }
}