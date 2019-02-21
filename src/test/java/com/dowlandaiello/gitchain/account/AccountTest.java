package com.dowlandaiello.gitchain.account;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * AccountTest is the main account testing file.
 */
public class AccountTest {
    /**
     * Test account constructor.
     */
    @Test
    public void TestAccount() {
        Account account = new Account(); // Initialize account

        assertTrue("account must not be null", account != null); // Ensure account is not null
    }

    /**
     * Test inbound account I/O.
     */
    @Test
    public void TestReadAccount() {
        Account account = new Account(); // Initialize account

        account.WriteToMemory(); // Write account to memory

        assertTrue("read account must be equivalent to mem account", Account.ReadAccount(account.PublicKey).PrivateKey.equals(account.PrivateKey)); // Ensure private key equivalent
    }

    /**
     * Test inbound p2p account I/O.
     */
    @Test
    public void TestReadP2PAccount() {
        Account account = new Account(); // Initialize account

        account.WriteToMemory("test_p2p_chain"); // Write account to memory

        assertTrue("read account must be equivalent to mem account", Account.ReadAccount("test_p2p_chain").PrivateKey.equals(account.PrivateKey)); // Ensure private key equivalent
    }

    /**
     * Test outbound account I/O.
     */
    @Test
    public void TestWriteToMemory() {
        Account account = new Account(); // Initialize account

        assertTrue("must write successfully", account.WriteToMemory()); // Write account to memory
    }

    /**
     * Test outbound P2P account I/O.
     */
    public void TestWriteToMemoryP2P() {

        Account account = new Account(); // Initialize account

        assertTrue("must write successfully", account.WriteToMemory("test_p2p_chain")); // Write account to memory
    }
}