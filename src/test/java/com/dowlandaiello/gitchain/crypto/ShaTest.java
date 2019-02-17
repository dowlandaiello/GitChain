package com.dowlandaiello.gitchain.crypto;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * ShaTest is the main Sha testing file.
 * 
 * @author Dowland Aiello
 * @since 17.02.2019
 */
public class ShaTest {
    /**
     * Test sha3.
     */
    @Test
    public void TestSha3() {
        assertTrue("hash cannot be nil", Sha.Sha3("test".getBytes()) != null); // Check hash not nil
    }

    /**
     * Test sha3d.
     */
    @Test
    public void TestSha3d() {
        assertTrue("hash cannot be nil", Sha.Sha3d("test".getBytes()) != null); // Check hash not nil
    }
}