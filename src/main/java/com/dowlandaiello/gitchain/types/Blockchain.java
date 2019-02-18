package com.dowlandaiello.gitchain.types;

import java.util.ArrayList;

/**
 * Blockchain is a data type containing a list of blocks, a genesis block, and consensus metadata.
 * 
 * @author Dowland Aiello
 * @since 18.02.2019
 */
public class Blockchain {
    /* Blocks in blockchain */
    ArrayList<Block> blocks;

    public Blockchain() {
        
    }
}