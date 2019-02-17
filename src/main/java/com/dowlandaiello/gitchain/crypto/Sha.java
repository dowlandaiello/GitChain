package com.dowlandaiello.gitchain.crypto;

import org.bouncycastle.jcajce.provider.digest.SHA3;


/**
 * Sha3 is a wrapper of bouncyCastle's sha3 Java implementation.
 * 
 * @author Dowland Aiello
 * @since 17.02.2019
 */
public class Sha {
    /**
     * Hash a given byte array, b, via sha3.
     * 
     * @param b byte array to hash
     */
    public static byte[] Sha3(byte[] b) {
        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512(); // Init sha3 digest

        return digestSHA3.digest(b); // Return hash
    }

    /**
     * Hash a given byte array, b, via sha3d.
     * 
     * @param b byte array to hash
     */
    public static byte[] Sha3d(byte[] b) {
        return Sha3(Sha3(b)); // Return hash
    }
}