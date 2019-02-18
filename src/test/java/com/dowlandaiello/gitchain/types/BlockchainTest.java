package com.dowlandaiello.gitchain.types;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.dowlandaiello.gitchain.config.ChainConfig;

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

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null

        Blockchain blockchain = new Blockchain(chainConfig); // Make new blockchain

        assertTrue("blockchain must not be null", blockchain != null); // Ensure chain not null
        assertTrue("genesis block must not be null", blockchain.GenesisBlock != null); // Ensure genesis not null
        assertTrue("genesis block in blocks must not be null", blockchain.Blocks.get(0) != null); // Ensure genesis not null
    }

    /**
     * Test block generator.
     */
    @Test
    public void TestCreateBlock() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Generate a new key pair
        } catch (Exception e) { // Catch
            fail(e.getLocalizedMessage()); // Panic
        }

        Map<BigInteger, Float> alloc = new HashMap<BigInteger, Float>(); // Init hash map

        alloc.put(keyPair.getPublicKey(), 1000000f); // Set alloc

        ChainConfig chainConfig = new ChainConfig(alloc, 0, "test_chain", 13, 1f); // Initialize chain config

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null

        Blockchain blockchain = new Blockchain(chainConfig); // Make new blockchain

        assertTrue("blockchain must not be null", blockchain != null); // Ensure chain not null
        assertTrue("genesis block must not be null", blockchain.GenesisBlock != null); // Ensure genesis not null
        assertTrue("genesis block in blocks must not be null", blockchain.Blocks.get(0) != null); // Ensure genesis not null

        Block newBlock = blockchain.CreateNewBlock(blockchain.GenesisBlock, new Transaction[0]); // Generate new block

        assertTrue("block must not be null", newBlock != null); // Ensure block not null
        assertTrue("block difficulty must be greater than genesis", newBlock.Difficulty > blockchain.GenesisBlock.Difficulty); // Ensure difficulty not null
    }
}