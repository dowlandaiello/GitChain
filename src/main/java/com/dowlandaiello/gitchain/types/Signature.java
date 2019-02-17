package com.dowlandaiello.gitchain.types;

import java.io.Serializable;
import java.math.BigInteger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;

/**
 * Signature holds a primitive secp256k1 signature.
 * 
 * @author Dowland Aiello
 * @since 16.02.2019
 */
public class Signature implements Serializable {
    /* lol serialization */
    static final long serialVersionUID = 0L;

    /* raw signature values */
    public final byte V;
    public final byte[] R;
    public final byte[] S;

    /**
     * Initialize a new transaction signature.
     * 
     * @param transaction The given transaction to sign
     * @param privateKey The working private key used for signing
     */
    public Signature(Transaction transaction, BigInteger privateKey) {
        BigInteger publicKey = Sign.publicKeyFromPrivate(privateKey); // Get public key

        ECKeyPair keyPair = new ECKeyPair(privateKey, publicKey); // Get key pair

        Sign.SignatureData signature = Sign.signMessage(transaction.Bytes(), keyPair, true); // Sign tx

        this.V = signature.getV(); // Set v
        this.R = signature.getR(); // Set r
        this.S = signature.getS(); // Set s
    }

    /**
     * Marshal the given transaction to a string.
     * 
     * @return the marshaled signature
     */
    public String String() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        return gson.toJson(this); // Return JSON marshaled
    }

    /**
     * For all the Java plebs out there...
     * 
     * @return the marshaled signature
     */
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        return gson.toJson(this); // Return JSON marshaled
    }
}