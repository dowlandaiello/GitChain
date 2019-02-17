package com.dowlandaiello.gitchain.types;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * SignatureTest is the main signature testing file.
 * 
 * @author Dowland Aiello
 * @since 16.02.2019
 */
public class SignatureTest {
    /**
     * Test signature constructor.
     */
    @Test
    public void TestSignature() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            Signature signature = new Signature(transaction, keyPair.getPrivateKey()); // Sign with private key

            System.out.println(signature.toString()); // Log signature
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }

    /**
     * Test signature string helper.
     */
    @Test
    public void TestString() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            Signature signature = new Signature(transaction, keyPair.getPrivateKey()); // Sign with private key

            System.out.println(signature.String()); // Log signature
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }

    /**
     * Test signature string helper.
     */
    @Test
    public void TestToString() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            Signature signature = new Signature(transaction, keyPair.getPrivateKey()); // Sign with private key

            System.out.println(signature.toString()); // Log signature
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }
}