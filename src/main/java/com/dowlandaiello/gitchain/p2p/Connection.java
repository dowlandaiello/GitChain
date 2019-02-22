package com.dowlandaiello.gitchain.p2p;

/**
 * Connection represents a given connection's metadata and routing information.
 */
public class Connection {
    /* Connection type */
    public ConnectionType ConnectionType;

    /* Common connection types */
    public enum ConnectionType {
        DHTRequest; // Connection type defs
    }
}