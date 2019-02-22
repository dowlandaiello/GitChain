package com.dowlandaiello.gitchain.p2p;

import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.config.ChainConfig;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import static org.fusesource.leveldbjni.JniDBFactory.*;

import java.io.File;
import java.io.IOException;

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
}