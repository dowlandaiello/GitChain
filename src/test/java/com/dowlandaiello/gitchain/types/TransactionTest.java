package com.dowlandaiello.gitchain.types;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static com.dowlandaiello.gitchain.types.Transaction.SignTransaction;
import static com.dowlandaiello.gitchain.types.Transaction.VerifyTransactionSignature;

import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * TransactionTest is the main transaction testing file.
 * 
 * @author Dowland Aiello
 * @since 16.02.2019
 */
public class TransactionTest {
    /**
     * Test transaction constructor.
     */
    @Test
    public void TestTransaction() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            assertTrue("transaction cannot be nil", transaction != null); // Ensure tx is not nil
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }

    /**
     * Test transaction signing.
     */
    @Test
    public void TestSignTransaction() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            assertTrue("must sign successfully", SignTransaction(transaction, keyPair.getPrivateKey())); // Sign transaction
            assertTrue("signature cannot be null", transaction.Signature != null); // Ensure has signature
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }

    /**
     * Test transaction signature verification.
     */
    @Test
    public void TestVerifyTransactionSignature() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            assertTrue("must sign successfully", SignTransaction(transaction, keyPair.getPrivateKey())); // Sign transaction
            assertTrue("signature cannot be null", transaction.Signature != null); // Ensure has signature

            assertTrue("transaction signature must be valid", VerifyTransactionSignature(transaction)); // Validate
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }
    
    /**
     * Test transaction bytes serialization.
     */
    @Test
    public void TestBytes() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            assertTrue("transaction must not be null", transaction != null); // Ensure tx is not null
            assertTrue("transaction bytes must not be null", transaction.Bytes() != null); // Ensure tx bytes not null
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }

    /**
     * Test transaction string marshaling.
     */
    @Test
    public void TestString() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            assertTrue("transaction must not be null", transaction != null); // Ensure tx is not null
            assertTrue("transaction string must not be null", transaction.String() != null); // Ensure tx string not null
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }

    /**
     * Test transaction string marshaling.
     */
    @Test
    public void TestToString() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair(); // Generate a new key pair

            Transaction transaction = new Transaction(0, keyPair.getPublicKey().toByteArray(), keyPair.getPublicKey().toByteArray(), 0f, 0, keyPair.getPublicKey().toByteArray()); // Initialize transaction

            assertTrue("transaction must not be null", transaction != null); // Ensure tx is not null
            assertTrue("transaction string must not be null", transaction.String() != null); // Ensure tx string not null
        } catch (Exception e) {
            fail(e.getLocalizedMessage()); // Panic
        }
    }
}