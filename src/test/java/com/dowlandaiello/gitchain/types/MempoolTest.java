package com.dowlandaiello.gitchain.types;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * MempoolTest is the main mempool testing file.
 */
public class MempoolTest {
    /**
     * Test mempool constructor.
     */
    @Test
    public void TestMempool() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            Mempool mempool = new Mempool(transaction); // Initialize mempool

            assertTrue("mempool must not be nil", mempool != null); // Ensure signature exists
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }

    /**
     * Test mempool append helper.
     */
    @Test
    public void TestAddTransaction() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            Mempool mempool = new Mempool(transaction); // Initialize mempool

            mempool.AddTransaction(transaction); // Add tx

            assertTrue("mempool must not be nil", mempool != null); // Ensure signature exists
            assertTrue("mempool length must be 2", mempool.TransactionList.size() == 2); // Ensure 2 txs in mempool
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }
}