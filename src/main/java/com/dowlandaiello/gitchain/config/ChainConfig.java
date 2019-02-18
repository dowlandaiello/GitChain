package com.dowlandaiello.gitchain.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import com.dowlandaiello.gitchain.common.CommonIO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * ChainConfig is a data type containing the chain config (including allocation).
 * 
 * @author Dowland Aiello
 * @since 18.02.2019
 */
public class ChainConfig {
    /* Chain supply allocation */
    public Map<byte[], Float> Alloc;

    /* Network ID */
    public int Network;

    /* Blockchain ID / Version */
    public String Chain;

    /**
     * Initialize a new chain config.
     * 
     * @param alloc chain supply allocation
     * @param network chain network identifier
     * @param chain chain name / version
     */
    public ChainConfig(Map<byte[], Float> alloc, int network, String chain) {
        this.Alloc = alloc; // Set alloc
        this.Network = network; // Set network
        this.Chain = chain; // Set chain
    }

    /**
     * Initialize a chain config from a JSON file.
     * 
     * @param rawJSON raw JSON file to deserialize
     */
    public ChainConfig(byte[] rawJSON) {
        Gson gson = new Gson(); // Init gson

        ChainConfig chainConfig = gson.fromJson(new String(rawJSON), ChainConfig.class);

        this.Alloc = chainConfig.Alloc; // Set alloc
        this.Network = chainConfig.Network; // Set network
        this.Chain = chainConfig.Chain; // Set chain
    }

    /**
     * Initialize a chain config from a genesis.JSON file (assumes genesis.json path is commonIO.data/genesis.JSON).
     * 
     * @return the read ChainConfig
     */
    public static ChainConfig ReadChainConfig() {
        File genesisFile = new File(CommonIO.GenesisPath); // Init file
        byte[] rawJSON = null; // Declare buffer

        try {
            rawJSON = Files.readAllBytes(genesisFile.toPath());
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace
            }
        }

        ChainConfig chainConfig = new ChainConfig(rawJSON); // init chain config

        return chainConfig; // Return chain config
    }

    /**
     * Write chain config to persistent memory
     * 
     * @return whether the operation was successful
     */
    public boolean WriteChainConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeHierarchyAdapter(byte[].class, new CommonIO.ByteArrayToBase64TypeAdapter()).create(); // Init gson

        CommonIO.MakeDirIfNotExist(CommonIO.ConfigPath); // Make genesis path

        try {
            FileWriter writer = new FileWriter(CommonIO.GenesisPath); // Init writer

            gson.toJson(this, writer); // Write gson

            writer.close(); // Close writer
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace

                return false; // Failed
            }
        }

        return true; // Success
    }
}