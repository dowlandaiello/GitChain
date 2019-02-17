package com.dowlandaiello.gitchain.types;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Transaction is a data alteration represented by the transaction primitive (e.g. updating a file), that
 * of which can also concern the transfer of monetary value (just for fun lol).
 * 
 * @author Dowland Aiello
 * @since 15.02.2019
 */
public class Transaction implements Serializable {
    /* lol serialization */
    static final long serialVersionUID = 0L;

    /* Transaction index in account tx list */
    public int AccountNonce;

    /* Sender of transaction expressed as a byte array */
    public byte[] Sender;

    /* Recipient of transaction expressed as a byte array */
    public byte[] Recipient;

    /* Transaction signature expressed as a signature type primitive */
    public Signature Signature;

    /* Transaction amount expressed as a float */
    public float Value;

    /* Transaction operation bytecode (i.e. 0 => add file, 1 => remove file, 2 => edit file) */
    public int Operation;

    /* Transaction payload (e.g. op => 0, payload => hash(file_name)) */
    public byte[] Payload;

    /**
     * Initialize a new transaction.
     * 
     * @param nonce transaction index in account tx set
     * @param sender transaction sender expressed as byte array
     * @param recipient transaction recipient expressed as byte array
     * @param value transaction value
     * @param operation given tx operation
     * @param payload transaction asm bytecodes
     */
    public Transaction(int nonce, byte[] sender, byte[] recipient, float value, int operation, byte[] payload) {
        this.AccountNonce = nonce; // Set nonce
        this.Sender = sender; // Set sender
        this.Recipient = recipient; // Set recipient
        this.Value = value; // Set value
        this.Operation = operation; // Set operation
        this.Payload = payload; // Set payload
    }

    /**
     * Decode a transaction from a given raw data set, rawData.
     * 
     * @param rawData data set to decode
     */
    public Transaction(byte[] rawData) {
        Transaction decodedTx = SerializationUtils.deserialize(rawData); // Deserialize

        this.AccountNonce = decodedTx.AccountNonce; // Set nonce
        this.Sender = decodedTx.Sender; // Set sender
        this.Recipient = decodedTx.Recipient; // Set recipient
        this.Signature = decodedTx.Signature; // Set signature
        this.Value = decodedTx.Value; // Set value
        this.Operation = decodedTx.Operation; // Set operation
        this.Payload = decodedTx.Payload; // Set payload
    }

    /**
     * Sign a given transaction.
     * 
     * @param privateKey the given private key to sign with
     * @return whether the transaction was signed successfully
     */
    public boolean Sign(BigInteger privateKey) {
        if (this.Bytes() == null) {
            return false; // Nothing to sign ¯\_(ツ)_/¯
        }
        this.Signature = new Signature(this, privateKey); // Sign transaction

        return true; // Return success
    }

    /**
     * Serialize the working transaction to a byte array.
     * 
     * @return the serialized transaction
     */
    public byte[] Bytes() {
        return(SerializationUtils.serialize(this)); // Serialize
    }
}