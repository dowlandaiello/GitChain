package com.dowlandaiello.gitchain.p2p;

import java.io.Serializable;

import com.dowlandaiello.gitchain.common.CommonIO;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Connection event represents an atomic p2p event.
 */
public class ConnectionEvent implements Serializable {
    /* lol serialization */
    static final long serialVersionUID = CommonIO.SerialVersionUID;

    /* Event type */
    public ConnectionEventType Type;


    /* Event types */
    public static enum ConnectionEventType {
        Close;
    }

    /**
     * Initialize a new connection event with a given even type.
     * 
     * @param type connection even type
     */
    public ConnectionEvent(ConnectionEventType type) {
        this.Type = type; // Set type
    }

    /**
     * Deserialize connection even from given byte array.
     */
    public ConnectionEvent(byte[] rawBytes) {
        try {
            ConnectionEvent connectionEvent = SerializationUtils.deserialize(rawBytes); // Deserialize

            this.Type = connectionEvent.Type; // Set connection event type
        } catch (Exception e) { // Catch
            return; // Return
        }
    }

    /**
     * Serialize connection event to byte array.
     */
    public byte[] Bytes() {
        return(SerializationUtils.serialize(this)); // Serialize
    }
}