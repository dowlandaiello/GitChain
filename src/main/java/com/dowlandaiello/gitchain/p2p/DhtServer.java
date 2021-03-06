package com.dowlandaiello.gitchain.p2p;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.common.CommonNet;
import com.dowlandaiello.gitchain.common.CommonNet.PeerAddress;

import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import static org.fusesource.leveldbjni.JniDBFactory.*;

/**
 * Dht server is a simple socket-based DHT server.
 */
public class DhtServer implements Runnable {
    /* Server peer identity */
    public Peer PeerIdentity;

    /* Is serving */
    public boolean IsServing;

    /**
     * Initialize a new DHT server with a given peer identity.
     * 
     * @param identity working peer identity
     */
    public DhtServer(Peer identity) {
        this.PeerIdentity = identity; // Set identity
    }

    /**
     * Threading stuff.
     */
    public void run() {
        this.StartServing(); // Start serving
    }

    /**
     * Start serving.
     */
    public void StartServing() {
        this.IsServing = true; // Set is serving

        ServerSocket serverSocket = null; // Initialize server

        try {
            serverSocket = new ServerSocket(CommonNet.ParseConnectionAddress(PeerIdentity.ConnectionAddr).Port); // Set
                                                                                                                 // server
                                                                                                                 // socket
        } catch (IOException e) { // Catch
            return; // Failed
        }

        while (this.IsServing) { // Check should be serving
            Socket socket = null; // Init buffer

            DataInputStream in = null; // Init buffer

            DataOutputStream out = null; // Init buffer

            byte[] buffer = new byte[896]; // Init buffer

            try {
                socket = serverSocket.accept(); // Accept connection

                in = new DataInputStream(socket.getInputStream()); // Set input stream

                out = new DataOutputStream(socket.getOutputStream()); // Set output stream

                in.read(buffer); // Read into buffer
            } catch (IOException e) { // Catch
                if (!CommonIO.StdoutSilenced) { // Check stdout silenced
                    e.printStackTrace(); // Print stack trace
                }

                continue; // Continue
            }

            Connection connection = new Connection(buffer, socket); // Deserialize connection

            this.handleConnection(connection, socket, in, out); // handle
        }

        try {
            serverSocket.close(); // Close
        } catch (IOException e) { // Catch
            return; // Failed
        }
    }

    /**
     * Stop serving DHT.
     */
    public void StopServing() {
        this.IsServing = false; // Stop serving
    }

    /**
     * Handle received connection.
     * 
     * @param connection connection to handle
     * @param socket     socket
     * @param in         input data stream
     * @param out        output data stream
     */
    private void handleConnection(Connection connection, Socket socket, DataInputStream in, DataOutputStream out) {
        switch (connection.Type) { // Handle connection types
        case DHTBootstrapRequest: // Handle bootstrap request
            handleBootstrapRequest(in, out, connection); // Handle bootstrap request

            break; // Break
        case PeerJoinRequest: // Handle peer join request
            handlePeerJoinRequest(connection); // handle join request

            break; // Break
        }

        try {
            in.close(); // Close input stream
            out.close(); // Close output stream
            socket.close(); // Close socket
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace
            }

            return; // Failed
        }
    }

    /**
     * Handle received db bootstrap request.
     * 
     * @param in  input stream
     * @param out output stream
     */
    private void handleBootstrapRequest(DataInputStream in, DataOutputStream out, Connection connection) {
        Options options = new Options(); // Make DB options
        options.createIfMissing(true); // Set options

        Dht dht = null; // Init buffer

        try {
            dht = Dht.ReadFromMemory(new String(connection.Meta[0])); // Read dht

            out.write(dht.Bytes()); // Write DB

            if (Dht.WorkingNodeDB == null) { // Check no db
                Dht.WorkingNodeDB = factory.open(new File(CommonIO.DHTPath + "/" + new String(connection.Meta[0])),
                        options); // Construct db
            }
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace

                return; // Return
            }
        }

        DBIterator iterator = Dht.WorkingNodeDB.iterator(); // Get iterator

        try {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                byte[][] data = new byte[2][]; // Initialize data

                data[0] = iterator.peekNext().getKey(); // Set key
                data[1] = iterator.peekNext().getValue(); // Set value

                ConnectionEvent event = new ConnectionEvent(ConnectionEvent.ConnectionEventType.Response, data); // Init
                                                                                                                 // event

                out.write(event.Bytes()); // Write connection event
            }

            iterator.close(); // Close iterator
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace

                return; // Return
            }
        }

        return; // Return
    }

    /**
     * Handle incoming peer join request.
     * 
     * @param connection
     */
    private void handlePeerJoinRequest(Connection connection) {
        DBIterator iterator = Dht.WorkingNodeDB.iterator(); // Get iterator

        if (Dht.WorkingNodeDB.get(connection.Meta[0]) != null) { // Check already exists
            return; // Return
        }

        Dht.WorkingNodeDB.put(new Peer(connection.Meta[0]).PublicKey.toByteArray(),
                new Peer(connection.Meta[0]).Bytes()); // Put new peer

        try {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                if (iterator.peekNext().getKey() != this.PeerIdentity.PublicKey.toByteArray()
                        && iterator.peekNext().getKey() != new Peer(connection.Meta[0]).PublicKey.toByteArray()) { // Check
                                                                                                                   // isn't
                                                                                                                   // same
                                                                                                                   // peer

                    Peer destinationPeer = new Peer(iterator.peekNext().getValue()); // Deserialize peer

                    PeerAddress parsedPeerAddress = CommonNet.ParseConnectionAddress(destinationPeer.ConnectionAddr); // Parse

                    Connection newConnection = new Connection(Connection.ConnectionType.PeerJoinRequest,
                            connection.Meta, this.PeerIdentity, destinationPeer); // Construct connection

                    try {
                        Socket socket = new Socket(parsedPeerAddress.InetAddress, parsedPeerAddress.Port); // Connect

                        DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // Init out writer

                        out.write(newConnection.Bytes()); // Write connection

                        socket.close(); // Close socket
                        out.close(); // Close out writer
                    } catch (IOException e) { // Catch
                        continue; // Continue
                    }
                }
            }

            iterator.close(); // Close iterator
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace

                return; // Return
            }
        }
    }
}