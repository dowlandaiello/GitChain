package com.dowlandaiello.gitchain.types;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * BlockTest is the main Block testing file.
 * 
 * @author Dowland Aiello
 * @since 17.02.2019
 */
public class BlockTest {
    /**
     * Test block constructor.
     */
    @Test
    public void TestBlock() {
        Transaction[] emptyTxArr = new Transaction[0]; // Lol no
        Transaction[] txArr = new Transaction[1]; // Has a tx

        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            txArr[0] = transaction; // Set tx

            assertTrue("transaction cannot be nil", transaction != null); // Ensure tx is not nil
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }

        Block emptyBlock = new Block(emptyTxArr, new byte[0], new byte[0], 0f, 0l); // Initialize block
        Block block = new Block(txArr, new byte[0], new byte[0], 0f, 0l); // Initialize block

        assertTrue("block must not be null", emptyBlock != null); // Ensure not null
        assertTrue("block must not be null", block != null); // Ensure not null
    }

    /**
     * Test block bytes serialization.
     */
    @Test
    public void TestBytes() {
        Transaction[] txArr = new Transaction[1]; // Has a tx

        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            txArr[0] = transaction; // Set tx

            assertTrue("transaction cannot be nil", transaction != null); // Ensure tx is not nil
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }

        Block block = new Block(txArr, new byte[0], new byte[0], 0f, 0l); // Initialize block

        assertTrue("block must not be null", block != null); // Ensure not null
        assertTrue("block bytes must not be null", block.Bytes() != null); // Ensure not null
    }

    /**
     * Test hash sum.
     */
    @Test
    public void TestHashSum() {
        Transaction[] txArr = new Transaction[1]; // Has a tx

        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            txArr[0] = transaction; // Set tx

            assertTrue("transaction cannot be nil", transaction != null); // Ensure tx is not nil
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }

        Block block = new Block(txArr, new byte[0], new byte[0], 0f, 0l); // Initialize block

        assertTrue("block must not be null", block != null); // Ensure not null
        assertTrue("hash sum most not be null", Block.HashTransactionSum(txArr) != null); // Ensure not null
        assertTrue("merkle root must not be null", block.MerkleRoot != null); // Ensure not null
    }
}