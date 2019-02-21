package com.dowlandaiello.gitchain.types;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import com.dowlandaiello.gitchain.common.CommonByteCmp;
import com.dowlandaiello.gitchain.common.CommonCoin;
import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.config.ChainConfig;
import com.dowlandaiello.gitchain.crypto.Sha;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

import org.iq80.leveldb.*;

import static org.fusesource.leveldbjni.JniDBFactory.*;

/**
 * Blockchain is a data type containing a list of blocks, a genesis block, and consensus metadata.
 * 
 * @author Dowland Aiello
 * @since 18.02.2019
 */
public class Blockchain {
    /* Blocks in blockchain */
    public DB BlockDB;

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

        if (genesisBlock.Difficulty == 0) { // Check invalid difficulty
            genesisBlock.Difficulty = 1f; // Set difficulty
        }

        Options options = new Options(); // Make DB options
        options.createIfMissing(true); // Set options

        this.Config = chainConfig; // Set chain config
        this.ChainID = chainConfig.Chain; // Set chain name
        this.Network = chainConfig.Network; // Set network id
        this.GenesisBlock = genesisBlock; // Set genesis block
        this.TotalDifficulty = genesisBlock.Difficulty; // Set difficulty
        this.GenesisBlock = genesisBlock; // Set genesis

        try {
            CommonIO.MakeDirIfNotExist(CommonIO.DataPath); // Make data path

            this.BlockDB = factory.open(new File(CommonIO.DbPath + "/" + chainConfig.Chain), options); // Construct DB

            this.BlockDB.put(genesisBlock.Hash, genesisBlock.Bytes()); // Add genesis reeReeReeReeRee
        } catch (IOException e) {
            throw new RuntimeException(e); // Panic
        }
    }

    /**
     * Deserialize blockchain from given byte array, rawJSON.
     * @param rawJSON json to deserialize
     */
    public Blockchain(byte[] rawJSON) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        Blockchain blockchain = gson.fromJson(new String(rawJSON), Blockchain.class);

        this.GenesisBlock = blockchain.GenesisBlock; // Set genesis block
        this.Config = blockchain.Config; // Set config
        this.ChainID = blockchain.ChainID; // Set chain ID
        this.Network = blockchain.Network; // Set network
        this.TotalDifficulty = blockchain.TotalDifficulty; // Set total difficulty
    }

    /**
     * Add specified block to blockchain.
     * @param block block to add
     * @return whether the block was added successfully
     */
    public boolean AddBlock(Block block) {
        if (this.BlockDB == null || !VerifyBlockNonce(block) || !java.util.Arrays.equals(block.ParentHash, this.GetLastBlock().Hash)) { // Check no block db, block nonce invalid
            return false; // ¯\_(ツ)_/¯
        }

        try {
            this.BlockDB.put(block.Hash, block.Bytes()); // Add block
        } catch (DBException e) { // Catch
            return false; // Failed
        }

        return true; // Success
    }

    /**
     * Get working block.
     * @return last block
     */
    public Block GetLastBlock() {
        DBIterator iterator = this.BlockDB.iterator(); // Get iterator

        iterator.seekToLast(); // Seek to start of db

        Block lastBlock = new Block(iterator.peekNext().getValue()); // Get block

        try {
            iterator.close(); // Close iterator
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace
            }

            return null; // Failed
        }

        return lastBlock; // Return block
    }

    /**
     * Open blockchain block database.
     */
    public boolean OpenBlockDB() {
        try {
            Options options = new Options(); // Make DB options
            options.createIfMissing(true); // Set options

            this.BlockDB = factory.open(new File(CommonIO.DbPath + "/" + this.Config.Chain), options); // Open DB
        } catch (IOException e) {
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace
            }

            return false; // Failed
        }

        return true; // Success
    }

    /**
     * Close blockchain block database.
     */
    public boolean CloseBlockDB() {
        try {
            this.BlockDB.close(); // Close db
        } catch (IOException e) {
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace
            }

            return false; // Failed
        }

        return true; // Success
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

        if (parent.Timestamp >= time) { // Check invalid time
            time = parent.Timestamp + 1; // Adjust time to parent block
        }

        return new Block(
            transactions,
            parent.Hash,
            CommonCoin.MinerCoinbase,
            CalculateDifficulty(parent, time),
            nonce
        ); // Return initialized block
    }

    /**
     * Read blockchain from persistent memory.
     * 
     * @return read blockchain
     */
    public static Blockchain ReadFromMemory(String chain) {
        File dbHeaderFile = new File(CommonIO.DbPath + "/" + chain + "/db_header.json"); // Init file

        byte[] rawJSON = null; // Declare buffer

        try {
            rawJSON = Files.readAllBytes(dbHeaderFile.toPath());
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace
            }
        }

        return new Blockchain(rawJSON); // init blockchain
    }

    /**
     * Write blockchain db header to persistent memory.
     * 
     * @return whether the operation was successful
     */
    public boolean WriteToMemory() {
        try {
            this.BlockDB.close(); // Close block db

            this.BlockDB = null; // Reset block db
        } catch (IOException e) {
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        CommonIO.MakeDirIfNotExist(CommonIO.DbPath + "/" + this.Config.Chain); // Make db header path

        try {
            FileWriter writer = new FileWriter(CommonIO.DbPath + "/" + this.Config.Chain + "/db_header.json"); // Init writer

            gson.toJson(this, writer); // Write gson

            writer.close(); // Close writer
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace

                return false; // Failed
            }
        }

        this.OpenBlockDB(); // Open block db

        return true; // Success
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

        Block genesisBlock = new Block(transactions, new byte[0], CommonCoin.MinerCoinbase, chainConfig.Difficulty, 0); // Create block

        Block difficultySpawnBlock = new Block(new Transaction[0], new byte[0], new byte[0], chainConfig.Difficulty, 0); // Create chain difficulty spawn block

        while (!Blockchain.VerifyBlockNonce(genesisBlock)) { // Calculate nonce
            genesisBlock.Nonce++; // Increment nonce
            genesisBlock.Timestamp = System.currentTimeMillis() / 1000; // Set timestamp
            genesisBlock.Difficulty = Blockchain.CalculateDifficulty(difficultySpawnBlock, genesisBlock.Timestamp); // Set difficulty
        }

        genesisBlock.Hash = Sha.Sha3(genesisBlock.Bytes()); // Hash

        return genesisBlock; // Return initialized genesis block
    }

    /**
     * Verify block difficulty matches current block nonce.
     * 
     * @param block block to check
     * @return validity of block hash
     */
    public static boolean VerifyBlockNonce(Block block) {
        if (Float.isInfinite(block.Difficulty)) { // Check invalid difficulty
            throw new RuntimeException("block difficulty overflow--block difficulty is infinite"); // Panic
        }

        BigInteger max = BigInteger.valueOf(2).pow(255); // Get max difficulty
        byte[] target = BigIntegers.asUnsignedByteArray(32, max.divide(new BigInteger(1, Long.toHexString(block.Difficulty.longValue()).getBytes()))); // Calculate target difficulty hash

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
        BigDecimal y = BigDecimal.valueOf(0l); // Init y

        x = BigInteger.valueOf(Math.round(blockTime - parent.Timestamp)); // Calculate block time difference
        x = x.divide(BigInteger.valueOf(10l)); // Weird mem allocations
        x = BigInteger.valueOf(1l).subtract(x); // More weird mem allocations

        if (x.compareTo(BigInteger.valueOf(-99l)) < 0) {
            x = BigInteger.valueOf(-99l); // max(1 - (block_timestamp - parent_timestamp) // 10, -99)
        }

        y = BigDecimal.valueOf(parent.Difficulty / 2048); // parent_diff // 2048
        
        BigDecimal finalX = y.multiply(BigDecimal.valueOf(x.floatValue())); // Multiply
        finalX = finalX.add(BigDecimal.valueOf(parent.Difficulty)); // Add parent_diff

        return finalX.floatValue(); // Return calculated difficulty
    }
}