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
    public ConnectionType Type;

    /* Meta params */
    public byte[][] Meta;

    /* Sender public key */
    public BigInteger SenderPublicKey;

    /* Recipient address */
    public String RecipientAddress;

    /* Common connection types */
    public static enum ConnectionType {
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
        this.Type = connectionType; // Set connection type
        this.Meta = meta; // Set meta
        this.SenderPublicKey = sender.PublicKey; // Set sender
        this.RecipientAddress = recipient.ConnectionAddr; // Set recipient
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
        this.Type = connectionType; // Set connection type
        this.SenderPublicKey = sender.PublicKey; // Set sender
        this.RecipientAddress = recipient.ConnectionAddr; // Set recipient
    }

    /**
     * Deserialize connection from given byte array.
     * 
     * @param rawBytes raw data to deserialize
     */
    public Connection(byte[] rawBytes) {
        Connection connection = SerializationUtils.deserialize(rawBytes); // Deserialize

        this.Type = connection.Type; // Set connection type
        this.Meta = connection.Meta; // Set meta
        this.SenderPublicKey = connection.SenderPublicKey; // Set sender
        this.RecipientAddress = connection.RecipientAddress; // Set recipient
    }

    /**
     * Serialize connection to byte array.
     */
    public byte[] Bytes() {
        return(SerializationUtils.serialize(this)); // Serialize
    }
}