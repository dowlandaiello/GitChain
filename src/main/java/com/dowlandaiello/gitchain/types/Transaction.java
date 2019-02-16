package com.dowlandaiello.gitchain.types;

import java.math.BigInteger;

/**
 * Transaction is a data alteration represented by the transaction primitive (e.g. updating a file), that
 * of which can also concern the transfer of monetary value (just for fun lol).
 * 
 * @author Dowland Aiello
 * @since 15.02.2019
 */
public class Transaction {
    /* Transaction index in account tx list */
    public int AccountNonce;

    /* Sender of transaction expressed as a byte array */
    public byte[] Sender;

    /* Recipient of transaction expressed as a byte array */
    public byte[] Recipient;

    /* Transaction signature expressed as a signature type primitive */
    public byte[] Signature; // TODO: Add signature type primitive

    /* Transaction amount expressed as a float */
    public float Value;

    /* Transaction operation bytecode (i.e. 0 => add file, 1 => remove file, 2 => edit file) */
    public int Operation;

    /* Transaction payload (e.g. op => 0, payload => hash(file_name)) */
    public byte[] Payload;

    /**
     * Initialize a new transaction.
     * 
     * @param nonce
     * @param sender
     * @param recipient
     * @param value
     * @param operation
     * @param payload
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
     * Sign a given transaction.
     * 
     * @param privateKey the given private key to sign with
     */
    public void Sign(BigInteger privateKey) {
        // TODO: do this
    }
}