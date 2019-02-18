package com.dowlandaiello.gitchain.types;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.dowlandaiello.gitchain.common.CommonCoin;
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
    public float TotalDifficulty;

    /**
     * Initialize a blockchain with a given genesis block and chain config.
     * 
     * @param chainConfig chain configuration
     * @param genesisBlock chain genesis block
     */
    public Blockchain(ChainConfig chainConfig) {
        Block genesisBlock = MakeGenesisBlock(chainConfig); // Make genesis

        if (genesisBlock.Difficulty == 0) genesisBlock.Difficulty = 1f; // Set difficulty

        this.Config = chainConfig; // Set chain config
        this.ChainID = chainConfig.Chain; // Set chain name
        this.Network = chainConfig.Network; // Set network id
        this.GenesisBlock = genesisBlock; // Set genesis block
        this.TotalDifficulty = genesisBlock.Difficulty; // Set difficulty
        this.GenesisBlock = genesisBlock; // Set genesis
        this.Blocks = new ArrayList<Block>(); // Construct block list
        this.Blocks.add(genesisBlock); // Add genesis reeReeReeReeRee
    }

    /**
     * Generate a new block.
     * 
     * @param parent working block to generate from
     * @param transactions transactions to put in block (usually from mempool)
     * @return generated block
     */
    public Block CreateNewBlock(Block parent, Transaction[] transactions) {
        long time = System.currentTimeMillis() / 1000; // Get block time

        if (parent.Timestamp >= time) time = parent.Timestamp + 1; // Adjust time to parent block

        return new Block(
            transactions,
            parent.Hash,
            CommonCoin.MinerCoinbase,
            CalculateDifficulty(parent, time, this.Blocks.size(), Config.BlockInterval),
            this.Blocks.size()
        ); // Return initialized block
    }

    /**
     * Generate a genesis block from a given chain config.
     * 
     * @param chainConfig chain config to generate genesis block from
     */
    public static Block MakeGenesisBlock(ChainConfig chainConfig) {
        Transaction[] transactions = new Transaction[chainConfig.Alloc.size()]; // Init tx buffer

        java.util.Iterator<Map.Entry<BigInteger, Float>> i = chainConfig.Alloc.entrySet().iterator(); // Init iterator

        int x = 0; // Declare iterator

        while(i.hasNext()) { // Can iterate
            Entry<BigInteger, Float> pair = (Map.Entry<BigInteger, Float>)i.next(); // Get pair

            transactions[x] = new Transaction(x, new byte[0], pair.getKey().toByteArray(), pair.getValue(), -1, new byte[0]); // Add tx

            i.remove(); // Remove

            x++; // Iterate
        }

        return new Block(
            transactions,
            new byte[0],
            CommonCoin.MinerCoinbase,
            chainConfig.Difficulty,
            0
        ); // Return block
    }

    /**
     * Calculate the difficulty of a new block, given a parent block, parent.
     * 
     * @param parent working block to calculate from
     * @return calculated difficulty
     */
    public static float CalculateDifficulty(Block parent, long blockTime, long blockNonce, int blockInterval) {
        int multiplier = 1; // Init buffer

        if (blockTime - parent.Timestamp >= blockInterval) // Check insufficient
            multiplier = -1; // Set buffer

        return (float) (parent.Difficulty + parent.Difficulty / 2048 * multiplier + Math.pow(2, ((blockNonce / 100000) - 2))); // Return calculated
    }
}