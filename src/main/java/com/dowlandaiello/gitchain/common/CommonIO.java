package com.dowlandaiello.gitchain.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import static com.google.common.base.Preconditions.checkArgument;

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
    public static String DataPath = new File("data").getAbsolutePath();

    /* Default db path */
    public static String DbPath = new File(DataPath + "/db").getAbsolutePath();

    /* Default config path */
    public static String ConfigPath = new File(DataPath + "/config").getAbsolutePath();

    /* Default genesis.json path */
    public static String GenesisPath = new File(ConfigPath + "/genesis.json").getAbsolutePath();

    /* Default keystore path */
    public static String KeystorePath = new File(DataPath + "/keystore").getAbsolutePath();

    /* Default p2p keystore path */
    public static String P2PKeystorePath = new File(KeystorePath + "/p2p").getAbsolutePath();

    /* Default DHT path */
    public static String DHTPath = new File(DbPath + "/dht").getAbsolutePath();

    /* Silence prints */
    public static boolean StdoutSilenced = false;

    /**
     * Make directory, dir, if doesn't already exist.
     * 
     * @param dir directory to create
     */
    public static void MakeDirIfNotExist(String dir) {
        File directory = new File(dir); // Init directory file

        if (!directory.exists()) { // Dir doesn't exist
            directory.mkdirs(); // Make directory
        }
    }

    public static boolean DeleteDirectoryContents(File directory) {
        checkArgument(directory.isDirectory(), "Not a directory: %s", directory);

        // Don't delete symbolic link directories
        if (IsSymbolicLink(directory)) {
            return false;
        }

        boolean success = true;
        for (File file : ListFiles(directory)) {
            success = DeleteRecursively(file) && success;
        }
        return success;
    }

    /**
     * Delete directory recursively.
     */
    public static boolean DeleteRecursively(File file) {
        boolean success = true;
        if (file.isDirectory()) {
            success = DeleteDirectoryContents(file);
        }

        return file.delete() && success;
    }

    /**
     * Check is symbolic link.
     */
    public static boolean IsSymbolicLink(File file) {
        try {
            File canonicalFile = file.getCanonicalFile();
            File absoluteFile = file.getAbsoluteFile();
            File parentFile = file.getParentFile();
            // a symbolic link has a different name between the canonical and absolute path
            return !canonicalFile.getName().equals(absoluteFile.getName()) ||
            // or the canonical parent path is not the same as the file's parent path,
            // provided the file has a parent path
                    parentFile != null && !parentFile.getCanonicalPath().equals(canonicalFile.getParent());
        } catch (IOException e) {
            // error on the side of caution
            return true;
        }
    }

    /**
     * List files in directory.
     */
    public static ImmutableList<File> ListFiles(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return ImmutableList.of();
        }
        return ImmutableList.copyOf(files);
    }

    /**
     * No clue
     */
    public static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return org.apache.commons.codec.binary.Base64.decodeBase64(json.getAsString()); // Decode
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(org.apache.commons.codec.binary.Base64.encodeBase64String(src)); // Encode
        }
    }
}