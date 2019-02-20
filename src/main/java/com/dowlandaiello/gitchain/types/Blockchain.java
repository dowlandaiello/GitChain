package com.dowlandaiello.gitchain.types;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.dowlandaiello.gitchain.common.CommonByteCmp;
import com.dowlandaiello.gitchain.common.CommonCoin;
import com.dowlandaiello.gitchain.config.ChainConfig;
import com.dowlandaiello.gitchain.crypto.Sha;

import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

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
    public Block CreateNewBlock(Block parent, Transaction[] transactions, long nonce) {
        long time = System.currentTimeMillis() / 1000; // Get block time

        if (parent.Timestamp >= time) time = parent.Timestamp + 1; // Adjust time to parent block

        return new Block(
            transactions,
            parent.Hash,
            CommonCoin.MinerCoinbase,
            CalculateDifficulty(parent, time),
            nonce
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
     * Verify block difficulty matches current block nonce.
     * 
     * @param block block to check
     * @return validity of block hash
     */
    public static boolean VerifyBlockNonce(Block block) {
        BigInteger max = BigInteger.valueOf(2).pow(255); // Get max difficulty
        byte[] target = BigIntegers.asUnsignedByteArray(32, max.divide(new BigInteger(1, Integer.toHexString((int) Math.floor(block.Difficulty)).getBytes()))); // Calculate target difficulty hash

        byte[] blockHash = Sha.Sha3(block.BytesWithoutNonce()); // Get block hash

        byte[] concatenated = Arrays.concatenate(blockHash, ByteBuffer.allocate(4).putFloat(block.Nonce).array()); // Get concatenated

        byte[] hash = Sha.Sha3(concatenated); // Calculate hash

        return (CommonByteCmp.compareTo(hash, 0, 32, target, 0, 32) < 0);
    }

    /**
     * Calculate the difficulty of a new block, given a parent block, parent.
     * 
     * @param parent working block to calculate from
     * @return calculated difficulty
     */
    public static float CalculateDifficulty(Block parent, long blockTime) {
        BigInteger x = BigInteger.valueOf(0l); // Init x
        BigInteger y = BigInteger.valueOf(0l); // Init y

        x = BigInteger.valueOf(Math.round(blockTime - parent.Timestamp)); // Calculate block time difference
        x = x.divide(BigInteger.valueOf(10l)); // Weird mem allocations
        x = BigInteger.valueOf(1l).subtract(x); // More weird mem allocations

        if (x.compareTo(BigInteger.valueOf(-99l)) < 0) x = BigInteger.valueOf(-99l); // max(1 - (block_timestamp - parent_timestamp) // 10, -99)

        y = BigInteger.valueOf((long) (parent.Difficulty / 2048)); // parent_diff // 2048
        x = y.multiply(x); // Multiply
        x = x.add(BigInteger.valueOf(Math.round(parent.Difficulty))); // Add parent_diff

        return x.floatValue(); // Return calculated difficulty
    }
}