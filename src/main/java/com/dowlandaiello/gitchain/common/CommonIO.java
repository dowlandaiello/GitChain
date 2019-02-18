package com.dowlandaiello.gitchain.common;

import java.io.File;

/**
 * CommonIO is an IO helper class.
 * 
 * @author Dowland Aiello
 * @since 18.02.2019
 */
public class CommonIO {
    /* Serialization UID constant */
    public static final long SerialVersionUID = 0L;

    /* Default data path */
    public static String DataPath = new File("../data").getAbsolutePath();

    /* Default genesis.json path */
    public static String GenesisPath = new File(DataPath+"/genesis.json").getAbsolutePath();

    /* Silence prints */
    public static boolean StdoutSilenced = false;
}