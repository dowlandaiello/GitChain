package com.dowlandaiello.gitchain.p2p;

import java.io.Serializable;
import java.math.BigInteger;

import com.dowlandaiello.gitchain.common.CommonIO;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Connection represents a given connection's metadata and routing information.
 */
public class Connection implements Serializable {
    /* lol serialization */
    static final long serialVersionUID = CommonIO.SerialVersionUID;

    /* Connection type */
    public ConnectionType ConnectionType;

    /* Meta params */
    public byte[][] Meta;

    /* Sender public key */
    public BigInteger SenderPublicKey;

    /* Recipient public key */
    public BigInteger RecipientPublicKey;

    /* Common connection types */
    public enum ConnectionType {
        DHTRequest; // Connection type defs
    }

    /**
     * Initialize a new connection metadata primitive with given connection type and meta.
     * 
     * @param connectionType connection type
     * @param meta miscellaneous metadata
     * @param sender sender public key
     * @param recipient recipient public key
     */
    public Connection(ConnectionType connectionType, byte[][] meta, Peer sender, Peer recipient) {
        this.ConnectionType = connectionType; // Set connection type
        this.Meta = meta; // Set meta
        this.SenderPublicKey = sender.PublicKey; // Set sender
        this.RecipientPublicKey = recipient.PublicKey; // Set recipient
    }

    /**
     * Initialize a new connection metadata primitive with given connection type.
     * 
     * @param connectionType connection type
     * @param meta miscellaneous metadata
     * @param sender sender public key
     * @param recipient recipient public key
     */
    public Connection(ConnectionType connectionType, Peer sender, Peer recipient) {
        this.ConnectionType = connectionType; // Set connection type
        this.SenderPublicKey = sender.PublicKey; // Set sender
        this.RecipientPublicKey = recipient.PublicKey; // Set recipient
    }

    /**
     * Deserialize connection from given byte array.
     * 
     * @param rawBytes raw data to deserialize
     */
    public Connection(byte[] rawBytes) {
        Connection connection = SerializationUtils.deserialize(rawBytes); // Deserialize

        this.ConnectionType = connection.ConnectionType; // Set connection type
        this.Meta = connection.Meta; // Set meta
        this.SenderPublicKey = connection.SenderPublicKey; // Set sender
        this.RecipientPublicKey = connection.RecipientPublicKey; // Set recipient
    }

    /**
     * Serialize connection to byte array.
     */
    public byte[] Bytes() {
        return(SerializationUtils.serialize(this)); // Serialize
    }
}