package com.dowlandaiello.gitchain.p2p;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * ConnectionEventTest is the main ConnectionEvent testing file.
 */
public class ConnectionEventTest {
    /**
     * Test connection event constructor.
     */
    @Test
    public void TestConnectionEvent() {
        ConnectionEvent connectionEvent = new ConnectionEvent(ConnectionEvent.ConnectionEventType.Close, new byte[0][0]); // Initialize connection event

        assertTrue("connection event must not be null", connectionEvent != null); // Ensure event is not null
    }

    /**
     * Test connection event constructor from raw data.
     */
    @Test
    public void TestConnectionEventFromBytes() {
        ConnectionEvent connectionEvent = new ConnectionEvent(ConnectionEvent.ConnectionEventType.Close, new byte[0][0]); // Initialize connection event

        ConnectionEvent deserializedEvent = new ConnectionEvent(connectionEvent.Bytes()); // Deserialize

        ConnectionEvent testDeserializedEvent = new ConnectionEvent(new byte[0]); // Deserialize

        assertTrue("invalid deserialized connection event must be null", testDeserializedEvent.Type == null); // Ensure null

        assertTrue("deserialized connection event must be equivalent to raw connection event", Arrays.equals(connectionEvent.Bytes(), deserializedEvent.Bytes())); // Ensure equivalent
    }

    /**
     * Test connection byte array serialization.
     */
    @Test
    public void TestBytes() {
        ConnectionEvent connectionEvent = new ConnectionEvent(ConnectionEvent.ConnectionEventType.Close, new byte[0][0]); // Initialize connection event

        assertTrue("connection event bytes must not be null", connectionEvent.Bytes() != null); // Ensure event is not null
    }
}