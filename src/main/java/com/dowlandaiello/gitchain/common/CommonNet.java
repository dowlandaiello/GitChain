package com.dowlandaiello.gitchain.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;

/**
 * CommonNet outlines common networking definitions and parameters.
 */
public class CommonNet {
    /* Default node port */
    public static long NodePort = 3000;

    /* Default DHT port */
    public static int DhtPort = 3048;

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
     * 
     * @return found IP address
     */
    public static String GetPublicIPAddrWithoutUPnP() {
        if (!IsConnected()) { // Check no internet connection
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                return socket.getLocalAddress().getHostAddress(); // Return local IP
            } catch (Exception e) { // Catch
                return "127.0.0.1"; // ¯\_(ツ)_/¯
            }
        }

        for (String provider : IPProviders) { // Iterate through providers
            try {
                URL providerURL = new URL(provider); // Make provider URL

                BufferedReader req = new BufferedReader(new InputStreamReader(providerURL.openStream()));

                String ip = req.readLine(); // you get the IP as a String

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
     * Get free port.
     * 
     * @param port base port
     * @return found free port
     */
    public static int GetFreePort(int port) {
        for (int newPort = port; newPort < 4000; newPort++) { // Iterate until available
            if (Available(newPort)) { // Check port available
                return newPort; // Return port
            }
        }

        return port; // ¯\_(ツ)_/¯
    }

    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     */
    public static boolean Available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    /**
     * Check is connected to internet.
     * 
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

    /**
     * Parse a string peer connection address.
     * 
     * @return parsed address
     */
    public static PeerAddress ParseConnectionAddress(String peerConnectionAddress) {
        String[] params = peerConnectionAddress.split("/"); // Split params

        if (params.length < 4) { // Check invalid params
            return null; // Failed
        }

        Protocol protocol = null; // Init buffer

        switch (params[1]) {
        case "ipv4":
            protocol = Protocol.IPV4; // Set protocol
        case "ipv6":
            protocol = Protocol.IPV6; // Set protocol
        }

        return new PeerAddress(protocol, params[2], Integer.parseInt(params[params.length - 1])); // Return peer address
    }

    /**
     * Peer address is a data type representing a parsed peer address string
     * commonly in the form of "/ipv4/127.0.0.1/tcp/3000"
     */
    public static class PeerAddress {
        /* Address protocol */
        public Protocol Protocol;

        /* IP Address */
        public String InetAddress;

        /* Port */
        public int Port;

        /**
         * Initialize peer address with given params.
         * 
         * @param protocol    IP protocol
         * @param inetAddress IP address
         * @param port        port
         */
        public PeerAddress(Protocol protocol, String inetAddress, int port) {
            this.Protocol = protocol; // Set protocol
            this.InetAddress = inetAddress; // Set addr
            this.Port = port; // Set port
        }
    }
}