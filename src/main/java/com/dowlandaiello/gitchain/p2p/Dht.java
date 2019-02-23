package com.dowlandaiello.gitchain.p2p;

import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.common.CommonNet;
import com.dowlandaiello.gitchain.common.CommonNet.PeerAddress;
import com.dowlandaiello.gitchain.config.ChainConfig;
import com.dowlandaiello.gitchain.p2p.ConnectionEvent.ConnectionEventType;

import org.apache.commons.lang3.SerializationUtils;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import static org.fusesource.leveldbjni.JniDBFactory.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.nio.file.Files;

/**
 * Dht is a synchronously managed distributed hash table used for the storage
 * and retrieval of node lookup information.
 */
public class Dht implements Serializable {
    /* lol serialization */
    static final long serialVersionUID = CommonIO.SerialVersionUID;

    /* DHT Chain Config */
    public ChainConfig Config;

    /* Node DB */
    public static transient DB WorkingNodeDB = null;

    /* Dht server instance */
    private transient DhtServer Server;

    /**
     * Initialize a new DHT with a given chain config.
     * 
     * @param bootstrapPeer endpoint to serve bootstrapped DB
     */
    public Dht(ChainConfig config, Peer bootstrapPeer) {
        Options options = new Options(); // Make DB options
        options.createIfMissing(true); // Set options

        this.Config = config; // Set config

        try {
            CommonIO.MakeDirIfNotExist(CommonIO.DHTPath); // Make db path

            WorkingNodeDB = factory.open(new File(CommonIO.DHTPath + "/" + config.Chain), options); // Construct DB

            WorkingNodeDB.put(bootstrapPeer.PublicKey.toByteArray(), bootstrapPeer.Bytes()); // Add bootstrap
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace
            }
        }
    }

    /**
     * Deserialize a DHT from a given byte array.
     */
    public Dht(byte[] rawBytes) {
        try {
            Dht dht = SerializationUtils.deserialize(rawBytes); // Deserialize

            this.Config = dht.Config; // Set chain config
        } catch (Exception e) { // Catch
            return; // Return
        }
    }

    /**
     * Serialize DHT to byte array.
     * 
     * @return serialized DHT
     */
    public byte[] Bytes() {
        boolean lolNothingToReopenDumbDumb = true; // :(

        try {
            if (WorkingNodeDB != null) { // Check node db already opened
                WorkingNodeDB.close(); // Close node db

                WorkingNodeDB = null; // Reset node db

                lolNothingToReopenDumbDumb = false; // Set to false
            }
        } catch (IOException e) {
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace
            }
        }

        byte[] bytes = SerializationUtils.serialize(this); // Serialize

        if (!lolNothingToReopenDumbDumb) {
            this.OpenNodeDB(); // Re-open node db
        }

        return bytes; // Return serialized
    }

    /**
     * Read DHT header from persistent memory.
     * 
     * @return read DHT
     */
    public static Dht ReadFromMemory(String chain) {
        File dbHeaderFile = new File(CommonIO.DHTPath + "/" + chain + "/db_header.db"); // Init file

        byte[] rawData = null; // Declare buffer

        try {
            rawData = Files.readAllBytes(dbHeaderFile.toPath());
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace
            }
        }

        return new Dht(rawData); // init dht
    }

    /**
     * Write DHT header to persistent memory.
     * 
     * @return whether the operation was successful
     */
    public boolean WriteToMemory() {
        try {
            WorkingNodeDB.close(); // Close node db

            WorkingNodeDB = null; // Reset node db
        } catch (IOException e) {
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace
            }
        }

        CommonIO.MakeDirIfNotExist(CommonIO.DHTPath + "/" + this.Config.Chain); // Make db header path

        try {
            File dbHeaderFile = new File(CommonIO.DHTPath + "/" + this.Config.Chain + "/db_header.db"); // Initialize
                                                                                                        // file

            FileOutputStream writer = new FileOutputStream(dbHeaderFile); // Init writer

            writer.write(this.Bytes()); // Write header

            writer.close(); // Close writer
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace

                return false; // Failed
            }
        }

        return false; // Re-open node db
    }

    /**
     * Open dht node database.
     */
    public boolean OpenNodeDB() {
        try {
            Options options = new Options(); // Make DB options
            options.createIfMissing(true); // Set options

            WorkingNodeDB = factory.open(new File(CommonIO.DbPath + "/" + this.Config.Chain), options); // Open DB
        } catch (IOException e) {
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace
            }

            return false; // Failed
        }

        return true; // Success
    }

    /**
     * Close dht node database.
     */
    public boolean CloseNodeDB() {
        try {
            WorkingNodeDB.close(); // Close db

            WorkingNodeDB = null; // Reset node db
        } catch (IOException e) {
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace
            }

            return false; // Failed
        }

        return true; // Success
    }

    /**
     * Start dht server.
     */
    public void StartServing() {
        Peer workingPeerIdentity = Peer.ReadPeer(); // Read local peer

        if (workingPeerIdentity == null) { // Invalid identity
            workingPeerIdentity = new Peer("/ipv4/" + CommonNet.GetPublicIPAddrWithoutUPnP() + "/tcp/"
                    + CommonNet.GetFreePort(CommonNet.DhtPort)); // Set working
            // peer identity
        }

        DhtServer server = new DhtServer(workingPeerIdentity); // Initialize DHT server

        this.Server = server; // Set dht server

        Thread thread = new Thread(this.Server); // Initialize thread

        thread.start(); // Start serving DHT
    }

    /**
     * Stop dht server.
     */
    public void StopServing() {
        this.Server.StopServing(); // Stop serving
    }

    /**
     * Attempt to bootstrap a DHT with a given bootstrap peer address,
     * bootstrapPeerAddress.
     * 
     * @param bootstrapPeerAddress bootstrap peer address represented as a string
     * @return bootstrapped DHT
     */
    public static Dht Bootstrap(String bootstrapPeerAddress, String chain) {
        PeerAddress parsedPeerAddress = CommonNet.ParseConnectionAddress(bootstrapPeerAddress); // Parse address

        Dht dht = null; // Init buffer

        Peer workingPeerIdentity = Peer.ReadPeer(); // Read local peer

        if (workingPeerIdentity == null) { // Invalid identity
            workingPeerIdentity = new Peer(
                    "/ipv4/" + CommonNet.GetPublicIPAddrWithoutUPnP() + "/tcp/" + CommonNet.NodePort); // Set working
                                                                                                       // peer identity
        }

        Socket socket = null; // Init buffer

        DataOutputStream out = null; // Init buffer

        DataInputStream in = null; // Init buffer

        byte[] buffer = new byte[880]; // Init buffer

        Connection connection = new Connection(Connection.ConnectionType.DHTBootstrapRequest,
                new byte[][] { chain.getBytes() }, workingPeerIdentity, Peer.GetPeer(bootstrapPeerAddress)); // Construct
                                                                                                             // connection

        try {
            socket = new Socket(parsedPeerAddress.InetAddress, parsedPeerAddress.Port); // Connect

            out = new DataOutputStream(socket.getOutputStream()); // Init out writer

            in = new DataInputStream(socket.getInputStream()); // Init in reader

            out.write(connection.Bytes()); // Write connection type

            in.read(buffer); // Read into buffer

            dht = new Dht(buffer); // Deserialize DHT

            in.read(buffer); // Read into buffer

            Options options = new Options(); // Make DB options
            options.createIfMissing(true); // Set options

            byte[] lastReadBuffer = null; // Set last read buffer

            for (ConnectionEvent connectionEvent = new ConnectionEvent(buffer); connectionEvent != null;) { // Check not
                                                                                                            // closed
                if (lastReadBuffer != buffer && connectionEvent.Type == ConnectionEventType.Response) { // Check is
                                                                                                        // response
                    WorkingNodeDB.put(connectionEvent.Meta[0], connectionEvent.Meta[1]); // Put node
                }

                lastReadBuffer = buffer; // Set last read buffer

                buffer = new byte[880]; // Reset buffer

                if (in.read(buffer) == -1) { // Check finished
                    break; // Break
                }
            }

            socket.close(); // Close socket
            in.close(); // Close input
            out.close(); // Close output
        } catch (Exception e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace

                return null; // Failed
            }
        }

        return dht; // Return dht
    }
}