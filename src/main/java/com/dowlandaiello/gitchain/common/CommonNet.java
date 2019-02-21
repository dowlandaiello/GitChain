package com.dowlandaiello.gitchain.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;

/**
 * CommonNet outlines common networking definitions and parameters.
 */
public class CommonNet {
    /* Default node port */
    public static long NodePort = 3000;

    /* Default IP lookup providers */
    public static String[] IPProviders = { "http://checkip.amazonaws.com/", "http://icanhazip.com/",
            "http://www.trackip.net/ip", "http://bot.whatismyipaddress.com/", "https://ipecho.net/plain",
            "http://myexternalip.com/raw", };

    /* Common IP protocols */
    public enum Protocol {
        IPV4, IPV6; // Protocol defs
    }

    /**
     * Get public IP address without UPnP.
     * @return found IP address
     */
    public static String GetPublicIPAddrWithoutUPnP() {
        if (!IsConnected()) { // Check no internet connection
            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                return socket.getLocalAddress().getHostAddress(); // Return local IP
            } catch (Exception e) { // Catch
                return "127.0.0.1"; // ¯\_(ツ)_/¯
            }
        }

        for (String provider: IPProviders) { // Iterate through providers
            try {
                URL providerURL = new URL(provider); // Make provider URL

                BufferedReader req = new BufferedReader(new InputStreamReader(providerURL.openStream()));

                String ip = req.readLine(); //you get the IP as a String

                if (ip != "") { // Check found IP
                    return ip; // Return found IP
                }
            } catch (Exception e) { // Catch
                continue; // Continue
            }
        }

        return "127.0.0.1"; // ¯\_(ツ)_/¯
    }

    /**
     * Check is connected to internet.
     * @return is connected
     */
    public static boolean IsConnected() {
        try {
            final URL url = new URL("http://www.google.com"); // Open url
            final URLConnection conn = url.openConnection(); // Connect

            conn.connect(); // Connect
            conn.getInputStream().close(); // Close

            return true; // Connected!
        } catch (MalformedInputException e) {
            throw new RuntimeException(e); // Panic
        } catch (IOException e) {
            return false; // Not connected
        }
    }
}