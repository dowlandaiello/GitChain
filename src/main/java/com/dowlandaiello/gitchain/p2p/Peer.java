package com.dowlandaiello.gitchain.p2p;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.Files;

import com.dowlandaiello.gitchain.account.Account;
import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.common.CommonNet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        Account account = null; // Init buffer

        File keystoreFile = new File(CommonIO.P2PKeystorePath + "/identity.json"); // Init file

        if (keystoreFile.exists()) { // Check already exists
            account = Account.ReadP2PIdentity(); // Read p2p identity
        } else {
            account = new Account(); // Initialize account
        }

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
     * Deserialize a peer from given raw JSON, rawJSON.
     * 
     * @param rawJSON raw JSON to deserialize
     */
    public Peer(byte[] rawJSON) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        Peer peer = gson.fromJson(new String(rawJSON), Peer.class); // Deserialize

        this.PublicKey = peer.PublicKey; // Set public key
        this.Protocol = peer.Protocol; // Set protocol
        this.ConnectionAddr = peer.ConnectionAddr; // Set connection address
    }

    /**
     * Attempt to construct a peer from a given connection address.
     * 
     * @param connectionAddr connection address
     * @return found peer info
     */
    public static Peer GetPeer(String connectionAddr) {
        return new Peer(connectionAddr, null); // Return peer
    }

    /**
     * Read peer config from persistent memory.
     * 
     * @return read peer
     */
    public static Peer ReadPeer() {
        File peerFile = new File(CommonIO.P2PKeystorePath + "/peer.json"); // Init file

        byte[] rawJSON = null; // Declare buffer

        try {
            rawJSON = Files.readAllBytes(peerFile.toPath()); // Read JSON
        } catch (IOException e) { // Catch
            return null; // Failed
        }

        Peer peer = new Peer(rawJSON); // init chain config

        return peer; // Return peer
    }

    /**
     * Write peer config to persistent memory.
     * 
     * @return whether the operation was successful
     */
    public boolean WriteToMemory() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        CommonIO.MakeDirIfNotExist(CommonIO.P2PKeystorePath); // Make p2p keystore path

        try {
            FileWriter writer = new FileWriter(new File(CommonIO.P2PKeystorePath + "/peer.json")); // Init writer

            gson.toJson(this, writer); // Write gson

            writer.close(); // Close writer
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace

                return false; // Failed
            }
        }

        return true; // Success
    }

    /**
     * Serialize given peer to byte array.
     * 
     * @return 
     */
    public byte[] Bytes() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        return(gson.toJson(this).getBytes()); // Serialize
    }
}