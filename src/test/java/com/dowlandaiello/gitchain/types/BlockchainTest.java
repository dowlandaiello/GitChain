package com.dowlandaiello.gitchain.types;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.dowlandaiello.gitchain.common.CommonCoin;
import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.config.ChainConfig;
import com.dowlandaiello.gitchain.crypto.Sha;

import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.util.FileUtils;
import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * BlockchainTest is the main blockchain testing class.
 * 
 * @author Dowland Aiello
 * @since 18.02.2019
 */
public class BlockchainTest {
    /**
     * Test blockchain constructor.
     */
    @Test
    public void TestBlockchain() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Generate a new key pair
        } catch (Exception e) { // Catch
            fail(e.getLocalizedMessage()); // Panic
        }

        Map<BigInteger, Float> alloc = new HashMap<BigInteger, Float>(); // Init hash map

        alloc.put(keyPair.getPublicKey(), 1000000f); // Set alloc

        ChainConfig chainConfig = new ChainConfig(alloc, 0, "test_chain", 10, 1f); // Initialize chain config

        assertTrue("must remove test cache", FileUtils.deleteDirectoryContents(new java.io.File(CommonIO.DbPath + "/" + chainConfig.Chain))); // Delete test cache

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null

        Blockchain blockchain = new Blockchain(chainConfig); // Make new blockchain

        DBIterator iterator = blockchain.BlockDB.iterator(); // Get iterator

        iterator.seekToFirst(); // Seek to first element

        assertTrue("blockchain must not be null", blockchain != null); // Ensure chain not null
        assertTrue("genesis block must not be null", blockchain.GenesisBlock != null); // Ensure genesis not null
        assertTrue("genesis block in blocks must not be null", iterator.peekNext().getValue() != null); // Ensure genesis not null

        try {
            iterator.close(); // Close iterator
        } catch (IOException e) {
            fail(e.getLocalizedMessage()); // Panic
        }

        blockchain.CloseBlockDB(); // Close block db

        assertTrue("must remove test cache", FileUtils.deleteDirectoryContents(new java.io.File(CommonIO.DbPath + "/" + chainConfig.Chain))); // Delete test cache
    }

    /**
     * Test block generator.
     */
    @Test
    public void TestCreateNewBlock() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Generate a new key pair
        } catch (Exception e) { // Catch
            fail(e.getLocalizedMessage()); // Panic
        }

        Map<BigInteger, Float> alloc = new HashMap<BigInteger, Float>(); // Init hash map

        alloc.put(keyPair.getPublicKey(), 1000000f); // Set alloc

        ChainConfig chainConfig = new ChainConfig(alloc, 0, "test_chain", 13, 255.9f); // Initialize chain config

        assertTrue("must remove test cache", FileUtils.deleteDirectoryContents(new java.io.File(CommonIO.DbPath + "/" + chainConfig.Chain))); // Delete test cache

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null

        long genesisStartingTime = System.currentTimeMillis() / 1000; // Get current timestamp

        System.out.println(genesisStartingTime + ": making genesis block (this shouldn't take more than 25 seconds)...\n"); // Log gen time

        Blockchain blockchain = new Blockchain(chainConfig); // Make new blockchain

        System.out.println(System.currentTimeMillis() / 1000 + ": finished making genesis block with difficulty " + blockchain.GenesisBlock.Difficulty + ", nonce " + blockchain.GenesisBlock.Nonce + " and block time " + (blockchain.GenesisBlock.Timestamp - genesisStartingTime)); // Log start time

        DBIterator iterator = blockchain.BlockDB.iterator(); // Get iterator

        iterator.seekToFirst(); // Seek to first element

        assertTrue("blockchain must not be null", blockchain != null); // Ensure chain not null
        assertTrue("genesis block must not be null", blockchain.GenesisBlock != null); // Ensure genesis not null
        assertTrue("genesis block in blocks must not be null", iterator.peekNext().getValue() != null); // Ensure genesis not null

        try {
            iterator.close(); // Close iterator
        } catch (IOException e) {
            fail(e.getLocalizedMessage()); // Panic
        }

        int numBlocks = 3; // Number of blocks to make

        Block lastBlock = blockchain.GenesisBlock; // Set parent

        Float totalDifficulty = 0f; // Set difficulty buffer
        Long totalNonce = 0l; // Set nonce buffer
        Long totalBlockTime = 0l; // Set block time buffer

        for (int x = 1; x != numBlocks + 1; x++) { // Make all blocks
            Transaction[] transactions = new Transaction[1]; // Init tx arr
            transactions[0] = new Transaction(0, new byte[0], CommonCoin.MinerCoinbase, 50, 0, new byte[0]); // Set tx

            Block newBlock = blockchain.CreateNewBlock(lastBlock, transactions, 0); // Generate new block
            newBlock.Difficulty = Blockchain.CalculateDifficulty(lastBlock, newBlock.Timestamp); // Set difficulty
    
            assertTrue("block must not be null", newBlock != null); // Ensure block not null
    
            while (!Blockchain.VerifyBlockNonce(newBlock)) { // Check invalid hash
                newBlock.Nonce++; // Increment nonce
                newBlock.Timestamp = System.currentTimeMillis() / 1000; // Set timestamp
                newBlock.Difficulty = Blockchain.CalculateDifficulty(lastBlock, newBlock.Timestamp); // Set difficulty
            }
    
            newBlock.Hash = Sha.Sha3(newBlock.BytesHashSafe()); // Hash

            totalDifficulty += newBlock.Difficulty; // Add difficulty
            totalNonce += newBlock.Nonce; // Add nonce
            totalBlockTime += (newBlock.Timestamp - lastBlock.Timestamp); // Add timestamp
    
            System.out.println(System.currentTimeMillis() / 1000 + ": finished making block "+x+" with difficulty " + newBlock.Difficulty + ", nonce " + newBlock.Nonce + ", and block time " + (newBlock.Timestamp - lastBlock.Timestamp)); // Log finish time

            lastBlock = newBlock; // Set last block
        }

        blockchain.CloseBlockDB(); // Close block db

        assertTrue("must remove test cache", FileUtils.deleteDirectoryContents(new java.io.File(CommonIO.DbPath + "/" + chainConfig.Chain))); // Delete test cache

        System.out.println("\nfinished making " + numBlocks + " blocks with an average difficulty of " + totalDifficulty / numBlocks + ", an average nonce of "+ totalNonce / numBlocks +", and an average block time of "+ totalBlockTime / numBlocks +" in " + (System.currentTimeMillis() / 1000 - blockchain.GenesisBlock.Timestamp) +" seconds."); // Log test finished
    }
    
    /**
     * Test block append helper.
     */
    @Test
    public void TestAddBlock() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Generate a new key pair
        } catch (Exception e) { // Catch
            fail(e.getLocalizedMessage()); // Panic
        }

        Map<BigInteger, Float> alloc = new HashMap<BigInteger, Float>(); // Init hash map

        alloc.put(keyPair.getPublicKey(), 1000000f); // Set alloc

        ChainConfig chainConfig = new ChainConfig(alloc, 0, "test_chain", 13, 255.9f); // Initialize chain config

        assertTrue("must remove test cache", FileUtils.deleteDirectoryContents(new java.io.File(CommonIO.DbPath + "/" + chainConfig.Chain))); // Delete test cache

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null

        long genesisStartingTime = System.currentTimeMillis() / 1000; // Get current timestamp

        System.out.println(genesisStartingTime + ": making genesis block (this shouldn't take more than 25 seconds)...\n"); // Log gen time

        Blockchain blockchain = new Blockchain(chainConfig); // Make new blockchain

        System.out.println(System.currentTimeMillis() / 1000 + ": finished making genesis block with difficulty " + blockchain.GenesisBlock.Difficulty + ", nonce " + blockchain.GenesisBlock.Nonce + " and block time " + (blockchain.GenesisBlock.Timestamp - genesisStartingTime)); // Log start time

        DBIterator iterator = blockchain.BlockDB.iterator(); // Get iterator

        iterator.seekToFirst(); // Seek to first element

        assertTrue("blockchain must not be null", blockchain != null); // Ensure chain not null
        assertTrue("genesis block must not be null", blockchain.GenesisBlock != null); // Ensure genesis not null
        assertTrue("genesis block in blocks must not be null", iterator.peekNext().getValue() != null); // Ensure genesis not null

        try {
            iterator.close(); // Close iterator
        } catch (IOException e) {
            fail(e.getLocalizedMessage()); // Panic
        }

        int numBlocks = 3; // Number of blocks to make

        Block lastBlock = blockchain.GenesisBlock; // Set parent

        Float totalDifficulty = 0f; // Set difficulty buffer
        Long totalNonce = 0l; // Set nonce buffer
        Long totalBlockTime = 0l; // Set block time buffer

        for (int x = 1; x != numBlocks + 1; x++) { // Make all blocks
            Transaction[] transactions = new Transaction[1]; // Init tx arr
            transactions[0] = new Transaction(0, new byte[0], CommonCoin.MinerCoinbase, 50, 0, new byte[0]); // Set tx

            Block newBlock = blockchain.CreateNewBlock(lastBlock, transactions, 0); // Generate new block
            newBlock.Difficulty = Blockchain.CalculateDifficulty(lastBlock, newBlock.Timestamp); // Set difficulty
    
            assertTrue("block must not be null", newBlock != null); // Ensure block not null
    
            while (!Blockchain.VerifyBlockNonce(newBlock)) { // Check invalid hash
                newBlock.Nonce++; // Increment nonce
                newBlock.Timestamp = System.currentTimeMillis() / 1000; // Set timestamp
                newBlock.Difficulty = Blockchain.CalculateDifficulty(lastBlock, newBlock.Timestamp); // Set difficulty
            }
    
            newBlock.Hash = Sha.Sha3(newBlock.BytesHashSafe()); // Hash

            totalDifficulty += newBlock.Difficulty; // Add difficulty
            totalNonce += newBlock.Nonce; // Add nonce
            totalBlockTime += (newBlock.Timestamp - lastBlock.Timestamp); // Add timestamp

            assertTrue("must add to chain", blockchain.AddBlock(newBlock)); // Add new block to chain
    
            System.out.println(System.currentTimeMillis() / 1000 + ": finished making block "+x+" with difficulty " + newBlock.Difficulty + ", nonce " + newBlock.Nonce + ", and block time " + (newBlock.Timestamp - lastBlock.Timestamp)); // Log finish time

            lastBlock = newBlock; // Set last block
        }

        blockchain.CloseBlockDB(); // Close block db

        assertTrue("must remove test cache", FileUtils.deleteDirectoryContents(new java.io.File(CommonIO.DbPath + "/" + chainConfig.Chain))); // Delete test cache

        System.out.println("\nfinished making " + numBlocks + " blocks with an average difficulty of " + totalDifficulty / numBlocks + ", an average nonce of "+ totalNonce / numBlocks +", and an average block time of "+ totalBlockTime / numBlocks +" in " + (System.currentTimeMillis() / 1000 - blockchain.GenesisBlock.Timestamp) +" seconds."); // Log test finished
    }
}