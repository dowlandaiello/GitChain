package com.dowlandaiello.gitchain.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import com.dowlandaiello.gitchain.common.CommonIO;

/**
 * Mempool is a list of pending transactions, that of which have not yet been included in a block.
 * 
 * @author Dowland Aiello
 * @since 16.02.2019
 */
public class Mempool implements Serializable {
    /* lol serialization */
    static final long serialVersionUID = CommonIO.SerialVersionUID;

    /* Pending transactions */
    public ArrayList<Transaction> TransactionList;

    /**
     * Initialize a new memPool with a single transaction, genesisTx (not necessarily the entire blockchain root).
     * 
     * @param genesisTransaction first transaction in the mempool
     */
    public Mempool(Transaction genesisTransaction) {
        this.TransactionList = new ArrayList<Transaction>(); // Set mempool
        this.TransactionList.add(genesisTransaction); // Set genesis
    }

    /**
     * Add a given transaction, transaction to the mempool.
     * 
     * @param transaction transaction to add to mempool
     * @return transaction was added successfully
     */
    public boolean AddTransaction(Transaction transaction) {
        return this.TransactionList.add(transaction); // Add tx
    }

    /**
     * Search through the working mempool, returning the index of a transaction w/ matching hash.
     * 
     * @param hash hash to query
     * @return index of found transaction
     */
    public int QueryTransaction(byte[] hash) {
        for (Transaction transaction: this.TransactionList) { // Iterate through transactions
            if (Arrays.equals(transaction.Hash, hash)) { // Check matching hash
                return this.TransactionList.indexOf(transaction); // Return index
            }
        }

        return -1; // No matching transaction
    }

    /**
     * Remove a given transaction, transaction from the mempool.
     * 
     * @param transaction transaction to remove from mempool
     * @return whether the operation was successful
     */
    public boolean RemoveTransaction(byte[] hash) {
        int txIndex = QueryTransaction(hash); // Get tx index

        if (txIndex == -1) { // Check non-existent
            return false; // Failed
        }

        this.TransactionList.remove(txIndex); // Remove transaction

        return true; // Success
    }
}