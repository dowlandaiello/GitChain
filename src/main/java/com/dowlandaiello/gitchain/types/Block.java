package com.dowlandaiello.gitchain.types;

import java.io.Serializable;
import java.util.ArrayList;

import com.dowlandaiello.gitchain.common.CommonIO;
import com.dowlandaiello.gitchain.crypto.Sha;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Block is a data type containing a list of transactions, as well as a merkle root.
 * 
 * @author Dowland Aiello
 * @since 17.02.2019
 */
public class Block implements Serializable {
    /* lol serialization */
    static final long serialVersionUID = CommonIO.SerialVersionUID;

    /* Transactions in block */
    Transaction[] Transactions;

    /* Exactly what it sounds like... */
    public byte[] MerkleRoot;

    /* Hash of preceding block */
    public byte[] ParentHash;

    /* Block publisher address */
    public byte[] Coinbase;

    /* Chain difficulty at block */
    public Float Difficulty;

    /* Block pow nonce */
    public long Nonce;

    /* Block hash */
    public byte[] Hash;

    /* Block timestamp */
    public long Timestamp;

    /**
     * Initialize a new block with a transaction set, transactions.
     * 
     * @param transactions transaction set to initialize block with
     * @param parentHash preceding block hash
     * @param coinbase fee address
     * @param difficulty network difficulty measurement at block
     * @param nonce block index in chain
     */
    public Block(Transaction[] transactions, byte[] parentHash, byte[] coinbase, Float difficulty, long nonce) {
        this.Transactions = transactions; // Set transactions
        this.MerkleRoot = HashTransactionSum(transactions); // Set merkle root
        this.ParentHash = parentHash; // Set parent hash
        this.Coinbase = coinbase; // Set coinbase
        this.Difficulty = difficulty; // Set difficulty
        this.Nonce = nonce; // Set nonce
        this.Hash = Sha.Sha3(this.Bytes()); // Set hash
        this.Timestamp = System.currentTimeMillis() / 1000; // Get timestamp
    }

    /**
     * Serialize block to byte array/
     * 
     * @return byte serialized block
     */
    public byte[] Bytes() {
        return(SerializationUtils.serialize(this)); // Serialize
    }

    /**
     * Calculate the hash sum of several transactions.
     * 
     * @param transactions transaction set to calculate hash sum
     * @return byte-array-represented hash sum
     */
    public static byte[] HashTransactionSum(Transaction[] transactions) {
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