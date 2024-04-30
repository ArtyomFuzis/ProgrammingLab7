package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class ClientMain {
    static ClientLogger logger = ClientLogger.getInstance();
    public static void main(String[] args) {
        try {
            try {
                if (args.length >= 1) AppData.ADDRESS = InetAddress.getByName(args[0]);
                else AppData.ADDRESS = InetAddress.getLocalHost();
            } catch (UnknownHostException ex) {
                logger.error("Host not found, redirect to localhost");
                AppData.ADDRESS = InetAddress.getLocalHost();
            }
        }
        catch (UnknownHostException ex)
        {
            logger.error("Net permissions error");
        }
        try {
            if (args.length >= 2) AppData.PORT = Integer.parseInt(args[1]);
            else AppData.PORT = 4352;
        }
        catch (NumberFormatException ex)
        {
            logger.error("Port not found, redirect to 4352");
            AppData.PORT = 4352;
        }
        ClientConnectionModule.connect();
        ClientExecutionModule.start();
    }

}