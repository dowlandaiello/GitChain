package com.dowlandaiello.gitchain.p2p;

import java.io.Serializable;
import java.math.BigInteger;

import com.dowlandaiello.gitchain.account.Account;
import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.common.CommonNet;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Peer is a data type representing a node's public identity on a network.
 */
public class Peer implements Serializable {
    /* lol serialization */
    static final long serialVersionUID = CommonIO.SerialVersionUID;

    /* Peer public key used for peer identification (separate from coin public key) */
    public BigInteger PublicKey;

    /* Peer IP protocol */
    public CommonNet.Protocol Protocol;

    /* Addr to connect to peer */
    public String ConnectionAddr;

    /**
     * Initialize a new peer with a given connection address, generating a new key pair for the peer.
     * 
     * @param connectionAddr address used for connecting to node
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
     * 
     * @param connectionAddr address used for connecting to node
     * @param publicKey bigInteger address used for identifying node
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
     * 
     * @param connectionAddr address used for connecting to node
     * @param publicKey bigInteger address used for identifying node
     * @param protocol active IP protocol (v4 or v6)
     */
    public Peer(String connectionAddr, BigInteger publicKey, CommonNet.Protocol protocol) {
        this.PublicKey = publicKey; // Set public key
        this.Protocol = protocol; // Set protocol
        this.ConnectionAddr = connectionAddr; // Set connection address
    }

    /**
     * Deserialize a peer from given raw data, rawByte.
     * 
     * @param rawBytes raw data to deserialize
     */
    public Peer(byte[] rawBytes) {
        Peer peer = SerializationUtils.deserialize(rawBytes); // Deserialize

        this.PublicKey = peer.PublicKey; // Set public key
        this.Protocol = peer.Protocol; // Set protocol
        this.ConnectionAddr = peer.ConnectionAddr; // Set connection address
    }

    /**
     * Serialize given peer to byte array.
     * 
     * @return 
     */
    public byte[] Bytes() {
        return(SerializationUtils.serialize(this)); // Serialize
    }
}