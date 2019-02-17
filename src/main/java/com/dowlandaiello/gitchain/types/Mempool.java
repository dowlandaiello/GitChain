package com.dowlandaiello.gitchain.types;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Mempool is a list of pending transactions, that of which have not yet been included in a block.
 * 
 * @author Dowland Aiello
 * @since 16.02.2019
 */
public class Mempool implements Serializable {
    /* lol serialization */
    static final long serialVersionUID = 0L;

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
     * Remove a given transaction, transaction from the mempool.
     * 
     * @param transaction transaction to remove from mempool
     */
    public boolean RemoveTransaction(Transaction transaction) {

    }
}