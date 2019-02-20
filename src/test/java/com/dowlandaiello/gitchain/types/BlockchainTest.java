package com.dowlandaiello.gitchain.types;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.dowlandaiello.gitchain.config.ChainConfig;
import com.dowlandaiello.gitchain.crypto.Sha;

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
    public void TestCreateNewBlock() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Generate a new key pair
        } catch (Exception e) { // Catch
            fail(e.getLocalizedMessage()); // Panic
        }

        Map<BigInteger, Float> alloc = new HashMap<BigInteger, Float>(); // Init hash map

        alloc.put(keyPair.getPublicKey(), 1000000f); // Set alloc

        ChainConfig chainConfig = new ChainConfig(alloc, 0, "test_chain", 13, 256f); // Initialize chain config

        assertTrue("chain config must not be null", chainConfig != null); // Ensure config not null

        System.out.println(System.currentTimeMillis() / 1000 + ": making genesis block (this shouldn't take more than 20 seconds)...\n"); // Log gen time

        Blockchain blockchain = new Blockchain(chainConfig); // Make new blockchain

        System.out.println(System.currentTimeMillis() / 1000 + ": finished making genesis block with difficulty " + blockchain.GenesisBlock.Difficulty + " and nonce " + blockchain.GenesisBlock.Nonce); // Log start time

        assertTrue("blockchain must not be null", blockchain != null); // Ensure chain not null
        assertTrue("genesis block must not be null", blockchain.GenesisBlock != null); // Ensure genesis not null
        assertTrue("genesis block in blocks must not be null", blockchain.Blocks.get(0) != null); // Ensure genesis not null

        int numBlocks = 50; // Number of blocks to make

        Block lastBlock = blockchain.GenesisBlock; // Set parent

        Float totalDifficulty = 0f; // Set difficulty buffer
        Long totalNonce = 0l; // Set nonce buffer
        Long totalBlockTime = 0l; // Set block time buffer

        for (int x = 1; x != numBlocks + 1; x++) { // Make all blocks
            Block newBlock = blockchain.CreateNewBlock(lastBlock, new Transaction[0], 0); // Generate new block
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

        System.out.println("\nfinished making " + numBlocks + " blocks with an average difficulty of " + totalDifficulty / numBlocks + ", an average nonce of "+ totalNonce / numBlocks +", and an average block time of "+ totalBlockTime / numBlocks +" in " + (System.currentTimeMillis() / 1000 - blockchain.GenesisBlock.Timestamp) +" seconds."); // Log test finished
    }
}