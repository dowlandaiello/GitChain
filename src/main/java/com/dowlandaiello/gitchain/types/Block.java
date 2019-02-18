package com.dowlandaiello.gitchain.types;

import java.util.ArrayList;

import com.dowlandaiello.gitchain.crypto.Sha;

/**
 * Block is a data type containing a list of transactions, as well as a merkle root.
 * 
 * @author Dowland Aiello
 * @since 17.02.2019
 */
public class Block {
    /* Transactions in block */
    Transaction[] Transactions;

    /* Exactly what it sounds like... */
    public byte[] MerkleRoot;

    /* Hash of preceding block */
    public byte[] ParentHash;

    /* Block publisher address */
    public byte[] Coinbase;

    /* Chain difficulty at block */
    public long Difficulty;

    /* Block index in chain */
    public long Nonce;

    /**
     * Initialize a new block with a transaction set, transactions.
     * 
     * @param transactions transaction set to initialize block with
     * @param parentHash preceding block hash
     * @param coinbase fee address
     * @param difficulty network difficulty measurement at block
     * @param nonce block index in chain
     */
    public Block(Transaction[] transactions, byte[] parentHash, byte[] coinbase, long difficulty, long nonce) {
        this.Transactions = transactions; // Set transactions
        this.MerkleRoot = HashSum(transactions); // Set merkle root
        this.ParentHash = parentHash; // Set parent hash
        this.Coinbase = coinbase; // Set coinbase
        this.Difficulty = difficulty; // Set difficulty
        this.Nonce = nonce; // Set nonce
    }

    /**
     * Calculate the hash sum of several transactions.
     * 
     * @param transactions transaction set to calculate hash sum
     */
    public static byte[] HashSum(Transaction[] transactions) {
        ArrayList<Byte> byteSum = new ArrayList<Byte>(); // Declare buffer

        for (Transaction transaction: transactions) { // Iterate through transactions
            for (Byte hashByte: transaction.Hash) { // Append all bytes
                byteSum.add(hashByte); // Append hash byte
            }
        }

        byte[] hashSum = new byte[byteSum.size()]; // Declare buffer

        for (int i = 0; i < hashSum.length; i++) { // Iterate through bytes
            hashSum[i] = byteSum.get(i); // Get byte at index
        }

        return Sha.Sha3(hashSum); // Return hash
    }
}