package com.dowlandaiello.gitchain.p2p;

import java.math.BigInteger;

import com.dowlandaiello.gitchain.account.Account;
import com.dowlandaiello.gitchain.common.CommonNet;

/**
 * Peer is a data type representing a node's public identity on a network.
 */
public class Peer {
    /* Peer public key used for peer identification (separate from coin public key) */
    public BigInteger PublicKey;

    /* Peer IP protocol */
    public CommonNet.Protocol Protocol;

    /* Addr to connect to peer */
    public String ConnectionAddr;

    /**
     * Initialize a new peer with a given connection address, generating a new key pair for the peer.
     */
    public Peer(String connectionAddr) {
        Account account = new Account(); // Initialize account

        account.WriteP2PIdentityToMemory(); // Write p2p identity to memory

        this.PublicKey = account.PublicKey; // Set public key
        this.ConnectionAddr = connectionAddr; // Set connection addr

        if (connectionAddr.contains("ipv6")) { // Check IPV6
            this.Protocol = CommonNet.Protocol.IPV6; // Set IPV6
        } else {
            this.Protocol = CommonNet.Protocol.IPV4; // Set IPV4
        }
    }

    /**
     * Initialize a new peer with a given connection address and elliptic curve identity.
     */
    public Peer(String connectionAddr, BigInteger publicKey) {
        this.PublicKey = publicKey; // Set public key
        this.ConnectionAddr = connectionAddr; // Set connection addr

        if (connectionAddr.contains("ipv6")) { // Check IPV6
            this.Protocol = CommonNet.Protocol.IPV6; // Set IPV6
        } else {
            this.Protocol = CommonNet.Protocol.IPV4; // Set IPV4
        }
    }

    /**
     * Initialize a new peer with a given connection address, identity, and protocol.
     */
    public Peer(String connectionAddr, BigInteger publicKey, CommonNet.Protocol protocol) {
        this.PublicKey = publicKey; // Set public key
        this.ConnectionAddr = connectionAddr; // Set connection address
        this.Protocol = protocol; // Set protocol
    }
}