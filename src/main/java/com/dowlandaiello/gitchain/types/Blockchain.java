package com.dowlandaiello.gitchain.types;

import java.math.BigInteger;
import java.util.ArrayList;

import com.dowlandaiello.gitchain.config.ChainConfig;

/**
 * Blockchain is a data type containing a list of blocks, a genesis block, and consensus metadata.
 * 
 * @author Dowland Aiello
 * @since 18.02.2019
 */
public class Blockchain {
    /* Blocks in blockchain */
    public ArrayList<Block> Blocks;

    /* Blockchain genesis block */
    public Block GenesisBlock;

    /* Chain alloc, configuration */
    public ChainConfig Config;

    /* Blockchain ID / version */
    public String ChainID;

    /* Network ID */
    public int Network;

    /* Network difficulty */
    public BigInteger TotalDifficulty;

    public Blockchain(ChainConfig chainConfig, Block genesisBlock) {
        this.Config = chainConfig; // Set chain config
        this.ChainID = chainConfig.Chain; // Set chain name
        this.Network = chainConfig.Network; // Set network id
        this.GenesisBlock = genesisBlock; // Set genesis block
        this.TotalDifficulty = BigInteger.ZERO; // Set difficulty
    }
}