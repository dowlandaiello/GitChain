package com.dowlandaiello.gitchain.common;

import java.io.File;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

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

    /* Default config path */
    public static String ConfigPath = new File(DataPath+"/config").getAbsolutePath();

    /* Default genesis.json path */
    public static String GenesisPath = new File(ConfigPath+"/genesis.json").getAbsolutePath();

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

    /**
     * No clue
     */
    public static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return org.apache.commons.codec.binary.Base64.decodeBase64(json.getAsString()); // Decode
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(org.apache.commons.codec.binary.Base64.encodeBase64String(src)); // Encode
        }
    }
}