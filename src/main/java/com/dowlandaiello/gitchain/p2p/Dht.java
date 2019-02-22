package com.dowlandaiello.gitchain.p2p;

import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.common.CommonNet;
import com.dowlandaiello.gitchain.common.CommonNet.PeerAddress;
import com.dowlandaiello.gitchain.config.ChainConfig;
import com.dowlandaiello.gitchain.p2p.Connection.ConnectionType;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import static org.fusesource.leveldbjni.JniDBFactory.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * Dht is a synchronously managed distributed hash table used for the storage and retrieval of node lookup information.
 */
public class Dht {
    /* DHT Chain Config */
    public ChainConfig Config;

    /* Node DB */
    public DB NodeDB;

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
            CommonIO.MakeDirIfNotExist(CommonIO.DbPath); // Make db path

            this.NodeDB = factory.open(new File(CommonIO.DHTPath + "/" + config.Chain), options); // Construct DB

            this.NodeDB.put(bootstrapPeer.PublicKey.toByteArray(), bootstrapPeer.Bytes()); // Add bootstrap
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace
            }
        }
    }

    /**
     * Attempt to bootstrap a DHT with a given bootstrap peer address, bootstrapPeerAddress.
     * 
     * @param bootstrapPeerAddress bootstrap peer address represented as a string
     * @return bootstrapped DHT
     */
    public static Dht Bootstrap(String bootstrapPeerAddress) {
        PeerAddress parsedPeerAddress = CommonNet.ParseConnectionAddress(bootstrapPeerAddress); // Parse address

        Socket socket = null; // Init buffer

        DataOutputStream out = null; // Init buffer

        try {
            socket = new Socket(parsedPeerAddress.InetAddress, parsedPeerAddress.Port); // Connect

            out = new DataOutputStream(socket.getOutputStream()); // Init out writer

            out.write(ConnectionType.DHTRequest.name().getBytes()); // Write connection type
        } catch (Exception e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace

                return null; // Failed
            }
        }
    }
}