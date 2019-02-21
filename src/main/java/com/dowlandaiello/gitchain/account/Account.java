package com.dowlandaiello.gitchain.account;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;

import com.dowlandaiello.gitchain.common.CommonIO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.codec.binary.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * Account represents a serializable ECKeyPair.
 */
public class Account {
    /* Account public key */
    public BigInteger PublicKey;

    /* Account private key */
    public BigInteger PrivateKey;

    /**
     * Generate a new account.
     */
    public Account() {
        ECKeyPair keyPair = null; // Init buffer

        try {
            keyPair = Keys.createEcKeyPair(); // Create key pair
        } catch (Exception e) {
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Print stack trace
            }
        }

        this.PublicKey = keyPair.getPublicKey(); // Set account public key
        this.PrivateKey = keyPair.getPrivateKey(); // Set account private key
    }

    /**
     * Deserialize account from raw json.
     * @param rawJSON json to deserialize
     */
    public Account(byte[] rawJSON) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        Account account = gson.fromJson(new String(rawJSON), Account.class);

        this.PublicKey = account.PublicKey; // Set public key
        this.PrivateKey = account.PrivateKey; // Set private key
    }

    /**
     * Read account with public key, publicKey, from persistent memory.
     */
    public static Account ReadAccount(BigInteger publicKey) {
        File keystoreFile = new File(CommonIO.KeystorePath + "/account_" + Hex.encodeHexString(publicKey.toByteArray()) + ".json"); // Init file

        byte[] rawJSON = null; // Declare buffer

        try {
            rawJSON = Files.readAllBytes(keystoreFile.toPath());
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace
            }

            return null; // Failed
        }

        Account account = new Account(rawJSON); // init account

        return account; // Return account
    }

    /**
     * Read p2p identity from persistent memory.
     */
    public static Account ReadP2PIdentity() {
        File keystoreFile = new File(CommonIO.P2PKeystorePath + "/identity.json"); // Init file

        byte[] rawJSON = null; // Declare buffer

        try {
            rawJSON = Files.readAllBytes(keystoreFile.toPath());
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace
            }
        }

        Account account = new Account(rawJSON); // init account

        return account; // Return account
    }

    /**
     * Write account to persistent memory.
     * @return whether the operation was successful
     */
    public boolean WriteToMemory() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        CommonIO.MakeDirIfNotExist(CommonIO.KeystorePath); // Make keystore dir

        try {
            FileWriter writer = new FileWriter(CommonIO.KeystorePath + "/account_" + Hex.encodeHexString(this.PublicKey.toByteArray()) + ".json"); // Init writer

            gson.toJson(this, writer); // Write gson

            writer.close(); // Close writer
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace

                return false; // Failed
            }
        }

        return true; // Success
    }

    /**
     * Write p2p identity to persistent memory.
     * @return whether the operation was successful
     */
    public boolean WriteP2PIdentityToMemory() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Init gson

        CommonIO.MakeDirIfNotExist(CommonIO.P2PKeystorePath); // Make keystore dir

        try {
            FileWriter writer = new FileWriter(CommonIO.P2PKeystorePath + "/identity.json"); // Init writer

            gson.toJson(this, writer); // Write gson

            writer.close(); // Close writer
        } catch (IOException e) { // Catch
            if (!CommonIO.StdoutSilenced) { // Check can print
                e.printStackTrace(); // Log stack trace

                return false; // Failed
            }
        }

        return true; // Success
    }
}