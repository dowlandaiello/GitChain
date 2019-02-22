package com.dowlandaiello.gitchain.p2p;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.common.CommonNet;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import static org.fusesource.leveldbjni.JniDBFactory.*;

/**
 * Dht server is a simple socket-based DHT server.
 */
public class DhtServer {
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

            byte[] buffer = null; // Init buffer

            try {
                socket = serverSocket.accept(); // Accept connection

                in = new DataInputStream(socket.getInputStream()); // Set input stream

                out = new DataOutputStream(socket.getOutputStream()); // Set output stream

                in.readFully(buffer); // Read into buffer
            } catch (IOException e) { // Catch
                if (!CommonIO.StdoutSilenced) { // Check stdout silenced
                    e.printStackTrace(); // Print stack trace
                }

                continue; // Continue
            }

            Connection connection = new Connection(buffer); // Deserialize connection

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
        }

        connection.CloseFromReceiver(out); // Close connection

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
    public void handleBootstrapRequest(DataInputStream in, DataOutputStream out, Connection connection) {
        Options options = new Options(); // Make DB options
        options.createIfMissing(true); // Set options

        DB nodeDb = null; // Init buffer

        try {
            nodeDb = factory.open(new File(CommonIO.DHTPath + "/" + new String(connection.Meta[0])), options); // Construct
                                                                                                               // DB
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace

                return; // Return
            }
        }

        DBIterator iterator = nodeDb.iterator(); // Get iterator

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
            nodeDb.close(); // Close db
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace

                return; // Return
            }
        }

        return; // Return
    }
}