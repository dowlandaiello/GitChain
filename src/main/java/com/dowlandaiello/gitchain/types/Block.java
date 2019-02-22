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
    public Transaction[] Transactions;

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
        this.Timestamp = System.currentTimeMillis() / 1000; // Get timestamp
    }

    /**
     * Deserialize a block from a given byte array, rawBytes.
     * @param rawBytes raw data to deserialize
     */
    public Block(byte[] rawBytes) {
        Block block = SerializationUtils.deserialize(rawBytes); // Deserialize

        this.Transactions = block.Transactions; // Set transactions
        this.MerkleRoot = block.MerkleRoot; // Set merkle root
        this.ParentHash = block.ParentHash; // Set parent hash
        this.Coinbase = block.Coinbase; // Set coinbase
        this.Difficulty = block.Difficulty; // Set difficulty
        this.Nonce = block.Nonce; // Set nonce
        this.Hash = block.Hash; // Set hash
        this.Timestamp = block.Timestamp; // Set timestamp
    }

    /**
     * Serialize block to byte array
     * 
     * @return byte serialized block
     */
    public byte[] Bytes() {
        return(SerializationUtils.serialize(this)); // Serialize
    }

    /**
     * Serialize block to byte array, omitting the block hash
     */
    public byte[] BytesHashSafe() {
        byte[] oldHash = this.Hash; // Store hash

        this.Hash = new byte[0]; // Set hash

        byte[] bytes = this.Bytes(); // Store safe bytes

        this.Hash = oldHash; // Reset hash

        return bytes; // Return safe bytes
    }

    /**
     * Serialize block to byte array, omitting the block nonce and block hash
     * @return
     */
    public byte[] BytesWithoutNonce() {
        byte[] oldHash = this.Hash; // Store hash
        long nonce = this.Nonce; // Store nonce

        this.Hash = new byte[0]; // Set hash
        this.Nonce = 0l; // Set nonce

        byte[] bytes = this.Bytes(); // Store safe bytes

        this.Hash = oldHash; // Reset hash
        this.Nonce = nonce; // Set nonce

        return bytes; // Return safe bytes
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